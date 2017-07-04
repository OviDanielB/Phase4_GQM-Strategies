package com.gqm3242.controller.implementation;

import com.gqm3242.controller.StrategicPlanService;
import com.gqm3242.model.*;
import com.gqm3242.model.rest.response.DTOResponseAttribute;
import com.gqm3242.model.rest.response.DTOResponseMetaWorkflow;
import com.gqm3242.model.rest.response.DTOResponseSWRelation;
import com.gqm3242.model.rest.response.DTOResponseStrategicPlan;
import com.gqm3242.repositories.*;
import com.gqm3242.utils.GlobalConst;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: StrategicPlanServiceImpl
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * <p>
 * Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di
 * Roma Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, Federico
 * Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli, Luca
 * Della Gatta
 * </p>
 * 
 * <p>
 * Class description: Implementazione dello Strategic Plan Service ...
 * 
 * 
 * 
 * @author Daniele Capri
 * @version 1.0
 *
 */

@Service("StrategicPlanService")
public class StrategicPlanServiceImpl implements StrategicPlanService {

	/** The strategy repository. */
	@Autowired
	StrategyRepository strategyRepository;

	/** The strategy repository. */
	@Autowired
	StrategicPlanRepository strategicPlanRepository;

	/** The attribute repository. */
	@Autowired
	AttributeRepository attributeRepository;

	/** The s wrr repository. */
	@Autowired
	SWRRepository sWRRRepository;

	/** The metaworkflow repository. */

	/** The bus interation implementation. */
//	@Autowired
//	BusInterationImplementation busInterationImplementation;

	/** The strategy repository. */
	@Autowired
	MetaWorkflowRepository mwRepository;
	
	@Autowired
	WorkflowDataRepository3141 wdRepository;

	/** The mongo template. */
	@Autowired
    MongoTemplate mongoTemplate;
	
	@Autowired
	GlobalConst globalConst;
	

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@SuppressWarnings("unused")

	@Override
	public ResponseEntity<DTOResponseStrategicPlan> createStrategicPlan(ArrayList<String> strategyId, String name,
                                                                        String description, float version, String release) {
		String tempOU = null, tempOU2 = null;
		ArrayList<StrategyWorkflowRelation> strategyResult = new ArrayList<StrategyWorkflowRelation>();
		// controllo che tutte le strategie abbino lo stessa unità
		// organizzativa, altrimenti pulisco la lista e ritorno bad request
		for (int i = 0; i < strategyId.size(); i++) {
			Strategy s = strategyRepository.findOne(strategyId.get(i));
			if (i == 0) {
				tempOU = s.getOrganizational_Unit();
				if (tempOU == null) {
					strategyResult.clear();
					break;
				}
			} else {
				tempOU2 = s.getOrganizational_Unit();
				if (!tempOU.equals(tempOU2)) {
					strategyResult.clear();
					break;
				}
			}
			if (s != null) {
				StrategyWorkflowRelation swr = new StrategyWorkflowRelation(s);
				// sWRRRepository.save(swr);
				strategyResult.add(swr);
			}
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("organizationalunit").is(tempOU));
		List<StrategicPlan> mongoStrategicPlans = mongoTemplate.find(query, StrategicPlan.class);
		if (!mongoStrategicPlans.isEmpty()) {
			strategyResult.clear();
		}
		if (!strategyResult.isEmpty()) {

			StrategicPlan strategicP = new StrategicPlan(strategyResult, name, description, tempOU, version, release);
			strategicPlanRepository.save(strategicP);
			DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
			dtoResponse.setId(strategicP.getId());
			dtoResponse.setName(strategicP.getName());
			dtoResponse.setDescription(strategicP.getDescription());
			dtoResponse.setOrganizzationalUnit(tempOU);
			dtoResponse.setStrategyToWorkflowId(strategyResult);
			dtoResponse.setVersion(strategicP.getVersion());
			dtoResponse.setRelease(strategicP.getRelease());
			ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
					dtoResponse, HttpStatus.OK);
			return responseEntity;
		} else {
			DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
			ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
					dtoResponse, HttpStatus.BAD_REQUEST);
			return responseEntity;
		}
	}

	@Override

	public ResponseEntity<DTOResponseStrategicPlan> deleteStrategicPlan(String id) {
		ResponseEntity<DTOResponseStrategicPlan> responseEntity;
		StrategicPlan strategicP = strategicPlanRepository.findOne(id);
		if (strategicP != null) {
			strategicPlanRepository.delete(strategicP);
			DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
			dtoResponse.setId(strategicP.getId());
			dtoResponse.setName(strategicP.getName());
			dtoResponse.setDescription(strategicP.getDescription());
			dtoResponse.setAttributes(strategicP.getAttributes());
			responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(dtoResponse, HttpStatus.OK);
		} else {
			DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
			responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(dtoResponse, HttpStatus.BAD_REQUEST);
		}
		return responseEntity;
	}

	/**
	 * Restituisce la lista dei strategic plan 
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseStrategicPlan> getStrategicPlans() {
		List<StrategicPlan> strategicPlans = strategicPlanRepository.findAll();
		DTOResponseStrategicPlan dtoresponse = new DTOResponseStrategicPlan();
		dtoresponse.setStrategicPlans(strategicPlans);
		ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
				dtoresponse, HttpStatus.OK);

		return responseEntity;

	}
	
	/**
	 * Restituisce la lista dei strategic plan 
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */
	
	@Override
	public List<StrategicPlan> getStrategicPlansList() {
		List<StrategicPlan> strategicPlans = strategicPlanRepository.findAll();
		return strategicPlans;
	}
	

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseStrategicPlan> getStrategicPlan(String strategicPlanId) {

		StrategicPlan strategicP = strategicPlanRepository.findOne(strategicPlanId);
		DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
		dtoResponse.setId(strategicP.getId());
		dtoResponse.setName(strategicP.getName());
		dtoResponse.setDescription(strategicP.getDescription());
		dtoResponse.setOrganizzationalUnit(strategicP.getOrganizationalUnit());
		dtoResponse.setVersion(strategicP.getVersion());
		dtoResponse.setRelease(strategicP.getRelease());
		dtoResponse.setStrategyToWorkflowId(strategicP.getStrategyWorkflowIds());
		dtoResponse.setAttributes(strategicP.getAttributes());

		ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;

	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseAttribute> createAttribute(String name, String type, String value) {

		Attribute attr = new Attribute(name, type, value);

		attributeRepository.save(attr);
		DTOResponseAttribute dtoResponse = new DTOResponseAttribute();
		dtoResponse.setId(attr.getId());
		dtoResponse.setName(attr.getName());
		dtoResponse.setType(attr.getType());
		dtoResponse.setValue(attr.getValue());
		ResponseEntity<DTOResponseAttribute> responseEntity = new ResponseEntity<DTOResponseAttribute>(dtoResponse,
				HttpStatus.OK);
		return responseEntity;

	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseStrategicPlan> updateStrategicPlan(String id,
                                                                        String name, String description, ArrayList<Attribute> attributes, float version, String release) {

		StrategicPlan p = null;

		p = strategicPlanRepository.findOne(id);

		p.setName(name);
		p.setDescription(description);
		p.setAttributes(attributes);
		p.setVersion(version);
		p.setRelease(release);

		strategicPlanRepository.save(p);
		DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
		dtoResponse.setId(p.getId());
		dtoResponse.setStrategyToWorkflowId(p.getStrategyWorkflowIds());
		dtoResponse.setName(p.getName());
		dtoResponse.setDescription(p.getDescription());
		dtoResponse.setOrganizzationalUnit(p.getOrganizationalUnit());
		dtoResponse.setAttributes(p.getAttributes());
		ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseSWRelation> getMetaWorkflows(String strategicPlanId) {
		StrategicPlan strategicP = strategicPlanRepository.findOne(strategicPlanId);
		DTOResponseSWRelation dtoResponse = new DTOResponseSWRelation();
		ArrayList<StrategyWorkflowRelation> relations = strategicP.getStrategyWorkflowIds();
		for (StrategyWorkflowRelation swr : relations) {
			Strategy strategy = swr.getStrategy();
			WorkflowData metaWorkflow = null;
			if (swr.getWorkflow() != null)
				metaWorkflow = swr.getWorkflow();
			dtoResponse.push(strategy, metaWorkflow);
		}
		ResponseEntity<DTOResponseSWRelation> responseEntity = new ResponseEntity<DTOResponseSWRelation>(dtoResponse,
				HttpStatus.OK);
		return responseEntity;

	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getStrategiesOfStrategicPlan(String id) {
		// TODO Auto-generated method stub

		StrategicPlan strategicPlan = strategicPlanRepository.findOne(id);

		DTOResponseMetaWorkflow dtoResponse = new DTOResponseMetaWorkflow();

		List<StrategyWorkflowRelation> strategyWorkflowRelationList = strategicPlan.getStrategyWorkflowIds();
		List<Strategy> strategiesList = new ArrayList<Strategy>();
		for (StrategyWorkflowRelation strategyWorkflowRelation : strategyWorkflowRelationList) {
			if (strategyWorkflowRelation.getStrategy() != null) {
				strategiesList.add(strategyWorkflowRelation.getStrategy());
			}

		}

		dtoResponse.setStrategies(strategiesList);
		ResponseEntity<DTOResponseMetaWorkflow> responseEntity = new ResponseEntity<DTOResponseMetaWorkflow>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getMetaworkflowOfStrategicPlan(String id) {
		StrategicPlan strategicPlan = strategicPlanRepository.findOne(id);
		DTOResponseMetaWorkflow dtoResponse = new DTOResponseMetaWorkflow();

		List<StrategyWorkflowRelation> strategyWorkflowRelationList = strategicPlan.getStrategyWorkflowIds();

		List<WorkflowData> metaWorkflowList = new ArrayList<WorkflowData>();
		for (StrategyWorkflowRelation strategyWorkflowRelation : strategyWorkflowRelationList) {

			if (strategyWorkflowRelation.getWorkflow() != null) {
				metaWorkflowList.add(strategyWorkflowRelation.getWorkflow());
			}
		}

		dtoResponse.setMetaWorkflowList(metaWorkflowList);
		ResponseEntity<DTOResponseMetaWorkflow> responseEntity = new ResponseEntity<DTOResponseMetaWorkflow>(
				dtoResponse, HttpStatus.OK);

		return responseEntity;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getStrategyWorkflowRelationOfStrategicPlan(String id) {
		StrategicPlan strategicPlan = strategicPlanRepository.findOne(id);
		DTOResponseMetaWorkflow dtoResponse = new DTOResponseMetaWorkflow();
		List<StrategyWorkflowRelation> strategyWorkflowRelationList = strategicPlan.getStrategyWorkflowIds();
		dtoResponse.setStrategyWorkflowRelationList(strategyWorkflowRelationList);

		ResponseEntity<DTOResponseMetaWorkflow> responseEntity = new ResponseEntity<DTOResponseMetaWorkflow>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getWorkflowBusinessIdOfStrategicPlan(String id) {

		return null;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */
	@Override
	public ResponseEntity<DTOResponseSWRelation> setMetaWorkflow(String strategicPlanId, String strategyId,
                                                                 String name) {
		ResponseEntity<DTOResponseSWRelation> responseEntity;
		StrategicPlan strategicP = strategicPlanRepository.findOne(strategicPlanId);
		Strategy strategy = strategyRepository.findOne(strategyId);
		// se ho trovato strategia e strategic plan avvio il metaworkflow poi lo
		// associo alla strategia
		// TODO fare il check se l'unità organizzativa è la stessa
		// _____________________________________________________________________________
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("name", name);
		RestTemplate template = new RestTemplate();
		String URL = GlobalConst.GQM3141_BASEURL + "/workflows/create";
		HttpEntity<String> request = new HttpEntity<String>(obj.toString(), headers);
		ResponseEntity<String> response = template.exchange(URL, HttpMethod.POST, request, String.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			JSONObject jsnobject;
			String metaId = "", businessModelId = "";
			try {
				jsnobject = new JSONObject(response.getBody());

				metaId = jsnobject.getString("metaWorkflowProcessInstanceId");
				businessModelId = jsnobject.getString("businessWorkflowModelId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// jsnobject.getString(key);
			// trattare poi la risposta
			// ____________________________________________________________________________

			WorkflowData wd= wdRepository.findByBusinessWorkflowModelId(businessModelId).get(0);
			strategicP.setMetaId(strategy, wd);
			strategicPlanRepository.save(strategicP);
			DTOResponseSWRelation dtoResponse = new DTOResponseSWRelation();
			dtoResponse.push(strategy, wd);
			responseEntity = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.OK);
		} else {
			DTOResponseSWRelation dtoResponse = new DTOResponseSWRelation();
			responseEntity = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.BAD_REQUEST);
		}
		return responseEntity;

	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */
	@Override
	public ResponseEntity<DTOResponseStrategicPlan> getStrategyToWorkflow(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */
	@Override
	public ResponseEntity<DTOResponseStrategicPlan> updateStrategicPlan(ArrayList<String> strategyId, String name,
                                                                        String description, ArrayList<Attribute> attributes, float version, String release) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ResponseEntity<DTOResponseSWRelation> getStrategyWorkflowData(String strategicPlanId, String strategyId){
		DTOResponseSWRelation dtoResponse= new DTOResponseSWRelation();
		ResponseEntity<DTOResponseSWRelation> response=null;
		StrategicPlan strategicPlan = strategicPlanRepository.findOne(strategicPlanId);
		if(strategicPlan!=null){
		Strategy strategy = strategyRepository.findOne(strategyId);
		if(strategy!=null){
		WorkflowData workflowData = strategicPlan.getSWFromStrategy(strategy);
		if(workflowData!=null){
		dtoResponse.setStrategy(strategy);
		dtoResponse.setWorkflow(workflowData);
		response = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.OK);
		return response;
		}
		else{
			dtoResponse.setStrategy(strategy);
			dtoResponse.setWorkflow(null);
			dtoResponse.setError("Can't find workflowData associated with the selectionated strategy. Maybe it doesn't exist");
			response = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.NOT_FOUND);
			return response;
		}
		}
		else{
			dtoResponse.setError("Can't find strategy inside the strategic plan");
			response = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.NOT_FOUND);
			return response;
		}
		}
		else{
			dtoResponse.setError("Strategic Plan not fount");
			response = new ResponseEntity<DTOResponseSWRelation>(dtoResponse, HttpStatus.NOT_FOUND);
			return response;
		}
	}

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getStrategyWithWorkflow(String strategicPlanId) {
		ArrayList<Strategy> result= new ArrayList<Strategy>();
		DTOResponseMetaWorkflow dtoResponseswRelation = new DTOResponseMetaWorkflow();
		ResponseEntity<DTOResponseMetaWorkflow> response=null;
		StrategicPlan strategicPlan = strategicPlanRepository.findOne(strategicPlanId);
		if(strategicPlan!=null){
			ArrayList<StrategyWorkflowRelation> swr = strategicPlan.getStrategyWorkflowIds();
			for (StrategyWorkflowRelation strategyWorkflowRelation : swr) {
				if(strategyWorkflowRelation.getWorkflow()!=null){
					WorkflowData wd= strategyWorkflowRelation.getWorkflow();
					result.add(strategyWorkflowRelation.getStrategy());
				}
			}
			dtoResponseswRelation.setStrategies(result);
			response= new ResponseEntity<DTOResponseMetaWorkflow>(dtoResponseswRelation, HttpStatus.OK);
			return response;
		}
		else{
			dtoResponseswRelation.setError("strategicPlan not found");
			response= new ResponseEntity<DTOResponseMetaWorkflow>(dtoResponseswRelation, HttpStatus.NOT_FOUND);
			return response;
		}
		
		
	}
	
	
	

	/**
	 * Restituisce tutte le strategie con Unità Organizzativa dello Strategic Plan  
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseMetaWorkflow> getStrategiesWithOrganizationalUnitOfStrategicPlan(String id) {
		StrategicPlan strategicPlan = strategicPlanRepository.findOne(id);
		DTOResponseMetaWorkflow dtoResponse = new DTOResponseMetaWorkflow();
		List<StrategyWorkflowRelation> strategyWorkflowRelationList = strategicPlan.getStrategyWorkflowIds();

		String orgUnit = strategicPlan.getOrganizationalUnit();
		
		List<Strategy> strategies = strategyRepository.findAll();

		StrategyWorkflowRelation strategyWorkflowRelation = new StrategyWorkflowRelation();//strategia da costruire e inserire nella lista
		
		/*elimino dalla lista le strategie con unità organizzativa differente da quella dello strategic plan*/
		
		
		for(int i = 0; i < strategies.size(); i++){
			if(strategies.get(i).getOrganizationalunit().equals(orgUnit) ){	
			}
			else{
				strategies.remove(i);
				i--;
			}			
		}
		
		for(int x = 0; x < strategies.size(); x++ ){
			
			for(int j = 0; j < strategyWorkflowRelationList.size(); j++){
				
				if(strategies.get(x).getId().equals(strategyWorkflowRelationList.get(j).getStrategy().getId())){
				strategies.remove(x);
				x--;
				break;
				}	
			}
		}
		
		for(int y = 0; y < strategies.size(); y++){
			strategyWorkflowRelation.set_id(null);
			strategyWorkflowRelation.setWorkflow(null);
			strategyWorkflowRelation.setStrategy(strategies.get(y));
			strategyWorkflowRelationList.add(strategyWorkflowRelation);
			
		}
		dtoResponse.setStrategyWorkflowRelationList(strategyWorkflowRelationList);
		ResponseEntity<DTOResponseMetaWorkflow> responseEntity = new ResponseEntity<DTOResponseMetaWorkflow>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;
	}
	
	
	

	/**
	 * Descrizione del metodo
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @throws
	 */

	@Override
	public ResponseEntity<DTOResponseStrategicPlan> updateStrategiesOfStrategicPlan(String id,
                                                                                    ArrayList<StrategyWorkflowRelation> strategyToWorkflowId) {

		StrategicPlan p = null;

		p = strategicPlanRepository.findOne(id);

		p.setStrategyWorkflowIds(strategyToWorkflowId);

		strategicPlanRepository.save(p);
		DTOResponseStrategicPlan dtoResponse = new DTOResponseStrategicPlan();
		dtoResponse.setId(p.getId());
		dtoResponse.setStrategyToWorkflowId(p.getStrategyWorkflowIds());
		dtoResponse.setName(p.getName());
		dtoResponse.setDescription(p.getDescription());
		dtoResponse.setOrganizzationalUnit(p.getOrganizationalUnit());
		dtoResponse.setAttributes(p.getAttributes());
		ResponseEntity<DTOResponseStrategicPlan> responseEntity = new ResponseEntity<DTOResponseStrategicPlan>(
				dtoResponse, HttpStatus.OK);
		return responseEntity;
	}

}
