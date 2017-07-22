package it.uniroma2.isssr.bus;

import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.integrazione.BusException;
import it.uniroma2.isssr.integrazione.BusMessage;
import it.uniroma2.isssr.model.phase41.CollectedData;
import it.uniroma2.isssr.model.phase41.MeasureTask;
import it.uniroma2.isssr.model.phase42.StrategicPlan;
import it.uniroma2.isssr.model.phase42.Strategy;
import it.uniroma2.isssr.model.phase42.rest.DTOPhase56;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseStrategy;
import it.uniroma2.isssr.repositories.phase41.CollectedDataRepository;
import it.uniroma2.isssr.repositories.phase42.StrategicPlanRepository;
import it.uniroma2.isssr.repositories.phase42.StrategyRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovidiudanielbarba on 07/07/2017.
 */
@Component
public class BusPhase4InteractionImplementation implements BusPhase4Interaction {

    /** The local strategy repository  */
    @Autowired
    StrategyRepository strategyRepository;

    /** The local strategic plan repository. */
    @Autowired
    StrategicPlanRepository strategicPlanRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    HostSettings hostSettings;

    @Autowired
    CollectedDataRepository collectedDataRepository;

    // TODO PHASE4 maybe not used
    @Override
    public ResponseEntity<DTOResponseStrategy> getStrategies() {

        List<Strategy> strategies = getStrategiesList();

        DTOResponseStrategy dtoResponse = new DTOResponseStrategy();
        dtoResponse.setStrategies(strategies);
        ResponseEntity<DTOResponseStrategy> responseEntity = new ResponseEntity<DTOResponseStrategy>(dtoResponse, HttpStatus.OK);
        return responseEntity;
    }





    @Override
    public List<Strategy> getStrategiesList() {
        List<Strategy> strategies = strategyRepository.findAll();
        return strategies;
    }

    @Override
    public ResponseEntity<DTOResponseStrategy> getStrategiesFree() {

        List<Strategy> strategies = getStrategiesList();

        List<StrategicPlan> strategicPlans = strategicPlanRepository.findAll();
        List<String> organizationalunitSP = new ArrayList<String>();

        for(int i = 0; i < strategicPlans.size(); i++) {
            organizationalunitSP.add(strategicPlans.get(i).getOrganizationalUnit()); //Lista delle unità organizzative utilizzate in Strategic Plan esistenti
        }

        //Rimozione delle strategie con unità organizzativa già utlizzata
        for(int k = 0; k < strategies.size(); k++){
            for(int j = 0; j < organizationalunitSP.size(); j++) {
                if(strategies.get(k).getOrganizationalunit().equals(organizationalunitSP.get(j))){
                    strategies.remove(k);
                    k--;
                    break;
                }
            }
        }

        DTOResponseStrategy dtoResponse = new DTOResponseStrategy();
        dtoResponse.setStrategies(strategies);
        ResponseEntity<DTOResponseStrategy> responseEntity = new ResponseEntity<DTOResponseStrategy>(dtoResponse, HttpStatus.OK);
        return responseEntity;
    }

    @Override
    public ResponseEntity<DTOResponseStrategy> getValidationOps() {
        // TODO PHASE4
        return null;
    }

    @Override
    public String saveValitatedDataOnBus(String taskId) {

        // TODO PHASE 4 change to validate only 1 collected data

        List<CollectedData> collectedDataList = collectedDataRepository.findByTaskId(taskId);


        String processInstanceId = "", workflowData = "", metric = "";
        ArrayList<String> data = new ArrayList<String>();
        Query query = new Query();
        query.addCriteria(Criteria.where("taskId").is(taskId));
        query.addCriteria(Criteria.where("validated").is(true));
        List<CollectedData> collectedData = mongoTemplate.find(query, CollectedData.class);
        if (collectedData.isEmpty())
            return "Can't find collected data";

        processInstanceId = collectedData.get(0).getWorkflowData().getBusinessWorkflowProcessInstanceId();
        if (processInstanceId == null)
            return "ProcessId is corrupted.";
        workflowData = collectedData.get(0).getWorkflowData().getBusinessWorkflowModelId();
        if (workflowData == null)
            return "can't find data regarding the chosen workflow ";

        query = new Query();
        query.addCriteria(Criteria.where("taskId").is(taskId));
        List<MeasureTask> mt = mongoTemplate.find(query, MeasureTask.class);
        if (!mt.isEmpty()) {
            metric = mt.get(0).getMetric().getId();
        } else {
            return "can't find measure task!";
        }
        for (CollectedData cd : collectedData) {
            data.add(cd.getValue());
        }
        DTOPhase56 dtoPhase56 = new DTOPhase56();
        dtoPhase56.setBusinessWorkFlowInstanceId(processInstanceId);
        dtoPhase56.setData(new JSONArray(data.toArray(new String[0])).toString());
        dtoPhase56.setDataId(taskId);
        dtoPhase56.setMetricRef(metric);
        dtoPhase56.setStrategyRef(workflowData);


        try {

            JSONObject obj = new JSONObject();
            obj.put("objIdLocalToPhase", dtoPhase56.getDataId());
            obj.put("typeObj", "validateData");
            obj.put("instance", dtoPhase56.getDataId());
            obj.put("tags", "[]" /*"[{\"key\": \"TaskId\", \"value\": \""+dtoPhase56.getDataId()+"\"}]"*/);
            JSONObject dataJSON = new JSONObject(dtoPhase56);
            obj.put("payload", dataJSON.toString());
            String s = obj.toString();

            BusMessage message = new BusMessage(BusMessage.OPERATION_CREATE,hostSettings.getPhaseName(), obj.toString());
            String response = message.send(hostSettings.getBusUri());
            System.out.println(response);
            String err;
            try {
                err = new JSONObject(response).getString("err");
            } catch (JSONException e) {
                //il messaggio non ha dato errori
                return response;
            }
            if (err.equals("ERR4")) {
                //"The object seems to be already created" provo la update
                message = new BusMessage(BusMessage.OPERATION_UPDATE, hostSettings.getPhaseName(), obj.toString());
                response = message.send(hostSettings.getBusUri());
                System.out.println(response);

            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "It seems bus is not longer avaiable";
        } catch (BusException e) {
            e.printStackTrace();
            return "Error on bus message creation";
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error while creating a json object";
        }

    }
}
