package it.uniroma2.isssr.services.phase42.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.bus.BusPhase4Interaction;
import it.uniroma2.isssr.exception.*;
import it.uniroma2.isssr.model.phase41.*;
import it.uniroma2.isssr.model.phase42.activiti.FlowElement;
import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;
import it.uniroma2.isssr.services.phase42.ValidationOpService;
import it.uniroma2.isssr.model.phase42.rest.DTO;
import it.uniroma2.isssr.model.phase42.rest.DTOActivitiTaskVariable;
import it.uniroma2.isssr.model.phase42.rest.DTOValidationOp;
import it.uniroma2.isssr.model.phase42.rest.response.*;
import it.uniroma2.isssr.repositories.phase41.CollectedDataRepository;
import it.uniroma2.isssr.repositories.phase42.ValidationOpRepository;
import it.uniroma2.isssr.model.phase42.ValidationOp;
import it.uniroma2.isssr.repositories.phase41.MeasureTaskRepository;
import it.uniroma2.isssr.repositories.phase41.WorkflowDataRepository;
import it.uniroma2.isssr.utils.phase42.ValidationPhase4;
import it.uniroma2.isssr.validation.Phase;
import org.json.HTTP;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: ValidationOpServiceImpl</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * <p>Class description:
 * Classe che implementa la relativa interfaccia, ed è annotata con @Service per
 * indicare a Spring che è un service bean. La dependency injection di
 * strategyRepository è operata attraverso l'annotazione @Autowired. In
 * particolare questa classe si occupa di gestire tutto ciò che riguarda le
 * interazioni con la classe ValidationOp da parte di altri. E infatti richiama il
 * repository relativo a ValidationOp.
 * 
 * In più offre i servizi relativi alla validazione da operare nella fase 4.
 * 
 * @author Fabio Alberto Coira, Gabriele Belli
 * @version 1.0
 */

@Service("ValidationOpService")
public class ValidationOpServiceImpl implements ValidationOpService {
	
	// Define the logger object for this class
	private static final Logger log = LoggerFactory.getLogger(ValidationOpServiceImpl.class);
		
	@Autowired
	ValidationOpRepository validationOpRepository;
	
	@Autowired
	MeasureTaskRepository measureTaskRepository;
	
	@Autowired
	WorkflowDataRepository workflowDataRepository;
	
	@Autowired
	ActivitiInterationImplementation activitiInterationImplementation;
	
	@Autowired
	CollectedDataRepository collectedDataRepository;
	
	@Autowired
	ValidationPhase4 validationPhase4;
	
	@Autowired
	BusPhase4Interaction busPhase4InteractionImplementation;


    @Override
    public ResponseEntity<?> saveValidatedDataOnBus(String taskId) {

        busPhase4InteractionImplementation.saveValitatedDataOnBus(taskId);
        return new ResponseEntity<>("Data saved on bus successfully",HttpStatus.OK);
    }

    /**
	 * Metodo che restituisce una ResponseEntity, che contiene nel body un
	 * JSON array contentente la lista di tutti i ValidationOp associati 
	 * ad un MeasureTask
	 * @throws EntityNotFoundException
	 * @throws AnomalySystemException 
	 */
	//va testato
	@Override
	public ResponseEntity<?> getValidationOpListByMeasureTaskId(String measureTaskId, Phase phase) throws EntityNotFoundException, AnomalySystemException {
		// TODO Auto-generated method stub
		/**
		 * passo 1: trovo il measure task
		 */
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(measureTaskId);
		if( measureTaskList.isEmpty()){
			//ritorna HttpStatus.NOT_FOUND
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente alcun Measure Task con "
					+ "'taskId' nel DB");
		}
		if( measureTaskList.size()>1){
			//ritorna HttpStatus.NOT_FOUND
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
					+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
					+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
					+ "Si sospetta un serio errore interno del sistema");
		}
		
		MeasureTask measureTask = measureTaskList.get(0);

		
		List<ValidationOp> validationOpList = measureTask.getValidationIdList();
		if(validationOpList==null){
			validationOpList = new ArrayList<ValidationOp>();
			measureTaskList.get(0).setValidationIdList(validationOpList);
			measureTaskRepository.save(measureTaskList.get(0));
		}
		if(phase.equals(Phase.PHASE_4)){
			//filtro quelle da mostrare rispetto a quelle che hanno un flag
			//settato a true
			List<ValidationOp> validationOpFilterList = new ArrayList<ValidationOp>();
			
			if(!validationOpList.isEmpty()){
				for(ValidationOp validationOp: validationOpList){
					if(!validationOp.isContromeasureDone()){
						validationOpFilterList.add(validationOp);
					}
				}
					
			}
			validationOpList = validationOpFilterList;
		}
		List<DTOResponseValidationOp> dtoResponseValidationOpList =
				new ArrayList<DTOResponseValidationOp>();
		
		for(ValidationOp validationOp: validationOpList ){
			DTOResponseValidationOp dtoResponseValidationOp = new DTOResponseValidationOp();
			dtoResponseValidationOp.setId(validationOp.getId());
			dtoResponseValidationOp.setName(validationOp.getName());
			dtoResponseValidationOp.setType(validationOp.getType());
			dtoResponseValidationOp.setDescription(validationOp.getDescription());
			dtoResponseValidationOp.setCardinality(validationOp.getCardinality());
			dtoResponseValidationOp.setCompType(validationOp.getCompType());
			dtoResponseValidationOp.setMeasureTaskId(validationOp.getMeasureTaskId());
			dtoResponseValidationOp.setRefMeasureTaskId(validationOp.getRefMeasureTaskId());
			dtoResponseValidationOp.setReferenceParams(validationOp.getReferenceParams());
			dtoResponseValidationOp.setCountermeasures(validationOp.getCountermeasures());
			dtoResponseValidationOp.setThisOp(validationOp.getThisOp());
			dtoResponseValidationOp.setRefOp(validationOp.getRefOp());
			dtoResponseValidationOp.setUserDefined(validationOp.getUserDefined());
			dtoResponseValidationOp.setContromeasureDone(validationOp.isContromeasureDone());
			
			dtoResponseValidationOpList.add(dtoResponseValidationOp);
		}
		
		//le devo ritornare in modo che siano una lista di oggetti con HttpStatus.Ok 
		
		ResponseEntity<List<DTOResponseValidationOp>> responseEntity =
				new ResponseEntity<List<DTOResponseValidationOp>>(
				dtoResponseValidationOpList, HttpStatus.OK);
		return responseEntity;
		
		
	}


	/**
	 * Per ottenere una validationOp a partire dall'id del DB
	 * @throws EntityNotFoundException 
	 */
	@Override
	public ResponseEntity<?> getValidationOp(String id) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		ValidationOp validationOp = validationOpRepository.findOne(id);
		if(validationOp == null){
			//ritorno HttpStatus.NOT_FOUND
			//solleva eccezione HttpStatus.NOT_FOUND
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente alcun Measure Task con questo"
					+ "'id' nel DB");
		
		}
		
		DTOResponseValidationOp dtoResponseValidationOp =
				new DTOResponseValidationOp();
		dtoResponseValidationOp.setId(validationOp.getId());
		dtoResponseValidationOp.setName(validationOp.getName()); 
		dtoResponseValidationOp.setType(validationOp.getType());
		dtoResponseValidationOp.setDescription(validationOp.getDescription());
		dtoResponseValidationOp.setCardinality(validationOp.getCardinality());
		dtoResponseValidationOp.setCompType(validationOp.getCompType());
		dtoResponseValidationOp.setMeasureTaskId(validationOp.getMeasureTaskId());
		dtoResponseValidationOp.setRefMeasureTaskId(validationOp.getRefMeasureTaskId());
		dtoResponseValidationOp.setReferenceParams(validationOp.getReferenceParams());
		dtoResponseValidationOp.setCountermeasures(validationOp.getCountermeasures());
		
		dtoResponseValidationOp.setThisOp(validationOp.getThisOp());
		dtoResponseValidationOp.setRefOp(validationOp.getRefOp());
		dtoResponseValidationOp.setUserDefined(validationOp.getUserDefined());
		dtoResponseValidationOp.setContromeasureDone(validationOp.isContromeasureDone());
		
		ResponseEntity<DTOResponseValidationOp> responseEntity =
				new ResponseEntity<DTOResponseValidationOp>(
						dtoResponseValidationOp, HttpStatus.OK);
		return responseEntity;
	}
	
	//codice di Gabriele da rivedere 
	@Override
	public ResponseEntity<?> getMeasureTask(
			String businessWorkflowProcessDefinitionId) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		List<WorkflowData> workflowDataList = workflowDataRepository.findByBusinessWorkflowProcessDefinitionId(businessWorkflowProcessDefinitionId);
		if(workflowDataList.isEmpty())
		{
			log.warn("1");
			//return httpstatus.notfound()
		}
		if(workflowDataList.size()>1)
		{
			log.warn("2");
			//return httpstatus.notfound()
		}
		WorkflowData workflowData = workflowDataList.get(0);
		log.warn("3");
		
		List<MeasureTask> measureTasksList = workflowData.getMeasureTasksList();
		log.warn("4");
		log.warn("measureTask", measureTasksList.get(0).getTaskId());
		
		//dentro ogni measureTask c'è un taskId ed una metrica
		List<FlowElement> flowElementList = activitiInterationImplementation.getFlowElementsList(
				"kermit", "kermit", businessWorkflowProcessDefinitionId);
		
		List<MetricTask> metricTasks = new ArrayList<MetricTask>();
		for(int i=0; i< measureTasksList.size(); i++){
			for(int j=0; j< flowElementList.size(); j++){
				if(measureTasksList.get(i).getTaskId().equals(flowElementList.get(j).getId())){
				metricTasks.add(new MetricTask(
						measureTasksList.get(i).getMetric().getName(),
						flowElementList.get(j).getName().toString(),
						//"",
						measureTasksList.get(i).getMetric().getDescription(),
						measureTasksList.get(i).getTaskId()));	
				}
				
			}
		}
		DTOResponseMetricTask dtoResponseMetricTask = new DTOResponseMetricTask();
		dtoResponseMetricTask.setMetricTask(metricTasks);
		
		ResponseEntity<DTOResponseMetricTask > responseEntity =
				new ResponseEntity<DTOResponseMetricTask>(dtoResponseMetricTask, HttpStatus.OK);
		return responseEntity;
	}
	/**
	 * 
	 * Metodo pensato per essere invocato da un endpoint di un validator nella
	 * fase 4 di gqm+strategies per effettuare l'operazione di 
	 * validazione in un MeasureTask sul DataCollected appena raccolto. 
	 * Se dovesse superare il test, allora quella specifica operazione di validazione
	 * risulterà oltrepassata. 
	 * 
	 * Il test è funzione di ciò che specifica il Validator nei piani di validazione in
	 * fase 3.
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException 
	 */
	
	@Override
	public ResponseEntity<?> validateDataCollected(String id) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		ValidationOp validationOp = validationOpRepository.findOne(id);
		if(validationOp ==null){
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente alcun ValidationOp con questo"
					+ "'id' nel DB");
		}
		//questo fa la validazione ma per ora non fa nulla
		Boolean resultValidation = validationPhase4.validate(validationOp);

		log.warn("Data is Valid from Phase 4: " + resultValidation.toString());
		
		if(resultValidation){
			DTO dto = new DTO();
			dto.setMessage("L'operazione di validazione è stata superata con successo");
			validationOp.setContromeasureDone(true);
			validationOpRepository.save(validationOp);
			return new ResponseEntity<DTO>(dto, HttpStatus.OK);
		}
		else{
			DTOResponseCountermeasures dtoResponseCountermeasures =
					new DTOResponseCountermeasures();
			dtoResponseCountermeasures.setCountermeasures(
					validationOp.getCountermeasures());
			dtoResponseCountermeasures.setMessage("L'operazione di validazione non è stata superata");
			return new ResponseEntity<DTOResponseCountermeasures>(
                    dtoResponseCountermeasures, HttpStatus.OK);
		}
		
		
	}
	/**
	 * 
	 * Metodo pensato per essere invocato da un endpoint di un validator nella
	 * fase 4 di gqm+strategies per segnalare ad un utente della fase 3.1/4.1, 
	 * di ignorare il risultato della validazione, nel senso che lo
	 * si ammette tra i valori buoni anche se non ha superato quella operazione di 
	 * validazione
	 * 
	 * E' una azione che intraprendiamo come contromisura in conseguenza al risultato
	 * di una certa validazione
	 *
	 * @param id
	 * @return
	 * @throws BodyEmptyException
	 * @throws EntityNotFoundException
	 * @throws IdKeyNullException
	 * @throws AnomalySystemException
	 */
	@Override
	public ResponseEntity<?> ignoreValidation(String id) throws BodyEmptyException,
	EntityNotFoundException, IdKeyNullException, AnomalySystemException {
		// TODO Auto-generated method stub
		DTOValidationOp dtoValidationOp = new DTOValidationOp();
		ValidationOp validationOp = validationOpRepository.findOne(id);
		if(validationOp==null){
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente alcun ValidationOp con questo"
					+ "'id' nel DB");
		}
		/**
		 * L'azione principale di questo metodo è quello di fare una
		 * update di una ValidationOp, con lo scopo di settare un 
		 * flag per indicare che l'operazione di Validazione non è necessaria
		 * sia andata a buon fine per quel dato. Ovvero viene probabilmente commesso
		 * un errore di primo tipo, ovvero viene consapevolemente considerato
		 * come falso positivo.
		 * 
		 */
		dtoValidationOp.setName(validationOp.getName()); 
		dtoValidationOp.setType(validationOp.getType());
		dtoValidationOp.setDescription(validationOp.getDescription());
		dtoValidationOp.setCardinality(validationOp.getCardinality());
		dtoValidationOp.setCompType(validationOp.getCompType());
		dtoValidationOp.setMeasureTaskId(validationOp.getMeasureTaskId());
		dtoValidationOp.setRefMeasureTaskId(validationOp.getRefMeasureTaskId());
		dtoValidationOp.setReferenceParams(validationOp.getReferenceParams());
		dtoValidationOp.setCountermeasures(validationOp.getCountermeasures());
	
		dtoValidationOp.setThisOp(validationOp.getThisOp());
		dtoValidationOp.setRefOp(validationOp.getRefOp());
		dtoValidationOp.setUserDefined(validationOp.getUserDefined());
		//
		dtoValidationOp.setContromeasureDone(true);


		ResponseEntity<?> response = updateValidationOp(id,
				dtoValidationOp);
		
		return response;
	}
	/**
	 *
	 * Metodo pensato per essere invocato da un endpoint di un validator nella
	 * fase 4 di gqm+strategies per segnalare ad un utente della fase 3.1/4.1, 
	 * di ripetere l'operazione di misura
	 * 
	 * E' una azione che intraprendiamo come contromisura in conseguenza al risultato
	 * di una certa validazione.
	 * 
	 * @param idRuntimeTask
	 * @param idValidationOp
	 * @param errorMessage
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JSONException
	 * @throws IOException
	 * @throws ActivitiGetException
	 * @throws ActivitiPutException
	 * @throws AnomalySystemException 
	 * @throws EntityNotFoundException 
	 */
	@Override
	public ResponseEntity<?> repeateMeasure(
			String idRuntimeTask,
			String idValidationOp,
			String errorMessage) throws JsonParseException, JsonMappingException, JSONException, IOException, ActivitiGetException, ActivitiPutException, AnomalySystemException, EntityNotFoundException
			{
		
		ValidationOp validationOp = validationOpRepository.findOne(idValidationOp);
		if(validationOp==null){
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente alcun ValidationOp con questo"
					+ "'id' nel DB");		}
		String measureTaskId = validationOp.getMeasureTaskId();
		if(measureTaskId==null){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"La ValidationOp in questione presenta una MeasureTaskId"
					+ "pari a null. E' impossibile una situazione del genere, perché"
					+ "in fase di creazione una ValidationOp è sempre associata ad un "
					+ "MeasureTask. Si dovrebbe indagare al riguardo.");		
		}
		 List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(
				 measureTaskId );
			if(measureTaskList.isEmpty()){
				//return eccezione, measureTask non esiste, httpstatus Conflict
				throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Il riferimento al MeasureTask presente nel ValidationOp non"
						+ "è più un riferimento valido. Evidentemente non"
						+ "si è tenuto traccia di un aggiornamento del sistema da qualche"
						+ "parte. Si dovrebbe indagare al riguardo.");
			}
			if( measureTaskList.size()>1){
				//ritorna c'è un problema, 
				//non possono esistere due measure task con stesso taskId
				throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
						+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
						+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
						+ "Si sospetta un serio errore interno del sistema");
			}
		MeasureTask measureTask = measureTaskList.get(0);
		List<ValidationOp> validationOpList = measureTask.getValidationIdList();
		if( validationOpList.isEmpty()){
			//return eccezione, measureTask non esiste, httpstatus Conflict
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"E' impossibile che la lista di ValidationOp presente"
					+ "all'interno del MeasureTask sia vuota. Questo perché deve"
					+ "essere quanto meno presente la ValidationOp corrente, su cui"
					+ "si sta operando questa contromisura di validazione.");
		}
		/**
		 * Resetto lo status del ValidationOp per indicare che anche se era stata 
		 * superata una operazione di validazione, nonostante ciò è tutto
		 * da rivalidare nel prossimo step, ovvero rioperando la misura 
		 */
		for(ValidationOp v: validationOpList){
			v.setContromeasureDone(false);
			validationOpRepository.save(v);
		}
		// TODO Auto-generated method stub
		//aggiornare la variabile state passando da 2 a 3
		//aggiungere una nuova variabile che sarebbe il messaggio dell'errore
		//aggiungere l'id del data collected che si sta validando
		
		/**
		 * Poiché si gestisce lo status di un task attraverso il suo riassegnamento, 
		 * quello che si fa è pertanto quello di fare l'update di una variabile
		 * già presente, e di impostarla al valore 2, quello previsto per 
		 * la raccolta di un dato
		 */
		
		DTOActivitiTaskVariable state = new DTOActivitiTaskVariable();
		state.setName("state");
		state.setScope("local");
		state.setType("string");
		state.setValue("3");
		
		@SuppressWarnings("unused")
        TaskVariable taskVariable =
				activitiInterationImplementation.updateActivitiTaskVariable(
						idRuntimeTask, "state", 
						state);
		
		DTOActivitiTaskVariable assignee = new DTOActivitiTaskVariable();
		assignee.setName("assignee");
		assignee.setScope("local");
		assignee.setType( "string");
		assignee.setValue("");
		
		@SuppressWarnings("unused")
        TaskVariable taskVariable1 =
				activitiInterationImplementation.updateActivitiTaskVariable(
						idRuntimeTask, "assignee", 
						assignee);
		
		DTOActivitiTaskVariable responsible = new DTOActivitiTaskVariable();
		responsible.setName("responsible");
		responsible.setScope("local");
		responsible.setType("string");
		responsible.setValue(measureTask.getResponsible());
		
		@SuppressWarnings("unused")
        TaskVariable taskVariable2 =
				activitiInterationImplementation.updateActivitiTaskVariable(
						idRuntimeTask, "responsible", 
						responsible);
		
		/**
		 * Poiché deve essere fornito anche un riscontro 
		 * nel ripetere la misura, quello che si fa è salvare in una variabile
		 * in Activiti l'eventuale messaggio restituito da un Validator,
		 * in cui si motivano esaustivamente le sue scelte e una 
		 * seconda variabile, quella che indica 
		 * l'id del data collected inputato dal Validator.
		 */
		
		List<CollectedData> collectedDataList = collectedDataRepository.findByTaskId(
				measureTaskId);
		
		if(collectedDataList.isEmpty()){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"La lista di collected Data per quel taskId è vuota. Ma "
					+ "ciò non è possibile. Almeno il CollectedData oggetto"
					+ " della validazione deve essere presente");
		}
		int i=0;
		/**
		 * Si cerca il CollectedData da validare. Nel DB deve essercene almeno
		 * uno ancora con il campo validated a false. Altrimenti sono già stati 
		 * tutti validati, quindi non ha senso chiamare questo metodo
		 */
		CollectedData collectedDataToBeValidated = new CollectedData();
		for(CollectedData collectedData: collectedDataList){
			if(!collectedData.isValidated()){
				collectedDataToBeValidated = collectedData;
				++i;
			}
		}
/*		if(i!=1){
			//errore, il data collected da validare deve esserci nel DB e ce ne è solo 1
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"La lista di collected Data per quel taskId non è vuota. Ma "
					+ "o nessun dato è da validare, o ce ne sono più di uno."
					+ " Questa cosa non è possibile.");
		}*/
		List<TaskVariable> taskVarList =
				activitiInterationImplementation.getTaskVariablesFromRuntimeTaskId( idRuntimeTask, "local");
		/**
		 * Si fa un controllo per verificare che la variabile 
		 * errorMessage non sia presente. Se non lo è la si crea, 
		 * altrimenti la si aggiorna. Questo perché in Activiti 
		 * ricreare la stessa variabile comporta un conflitto
		 */
		
		
		List<TaskVariable> taskVariables =
					new ArrayList<TaskVariable>();
		TaskVariable error = new TaskVariable();
		error.setName("errorMessage");
		error.setScope("local");
		error.setType("string");
		error.setValue("La validazione del dato oggetto della misura e' fallita.");
		taskVariables.add(error);
		
		TaskVariable idData = new TaskVariable();
		idData.setName("idCollectedData");
		idData.setScope("local");
		idData.setType("string");
		idData.setValue(collectedDataToBeValidated.get_id());
			
		taskVariables.add(idData);
		activitiInterationImplementation.createTaskVariableFromRuntimeTaskId(
					idRuntimeTask, taskVariables);
		
		
		List<DTOResponseActivitiTaskVariable> dtoResponseActivitiTaskVariables
			= new ArrayList<DTOResponseActivitiTaskVariable>();
		List<TaskVariable> taskVariableList =
				activitiInterationImplementation.getTaskVariablesFromRuntimeTaskId(
						idRuntimeTask, "local");
		for(TaskVariable variable :
                taskVariableList){
			DTOResponseActivitiTaskVariable dtoResponseactivitiTaskVariable = new 
					DTOResponseActivitiTaskVariable();
			dtoResponseactivitiTaskVariable.setName(variable.getName());
			dtoResponseactivitiTaskVariable.setScope(variable.getScope());
			dtoResponseactivitiTaskVariable.setType(variable.getType());
			dtoResponseactivitiTaskVariable.setValue(variable.getValue());
		}

		ResponseEntity<List<DTOResponseActivitiTaskVariable>> responseEntity =
				new ResponseEntity<List<DTOResponseActivitiTaskVariable>>(
						dtoResponseActivitiTaskVariables, HttpStatus.OK);
		return responseEntity;
		
	}
	/**
	 * Metodo pensato per essere invocato da un endpoint di un validator nella
	 * fase 4 di gqm+strategies per completare la validazione di un task di misura
	 * 
	 * 
	 * @param idDataCollected
	 * @return
	 * @throws AnomalySystemException 
	 */
	
	@Override
	public ResponseEntity<?> completeValidation(String taskId,String validationOpId) throws AnomalySystemException{
		/**
		 * Per completare infine una operazione di Validazione, ciò che viene fatto è 
		 * quello di settare il flag in CollectedData a true che indica proprio
		 * che il Dato resterà presente all'interno del DB, perché è giusto che sia 
		 * così. A quel punto il dato resterà disponibile per future 
		 * validazioni.
		 */
		
		 List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(
				 taskId );
			if(measureTaskList.isEmpty()){
				//return eccezione, measureTask non esiste, httpstatus Conflict
				throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Il riferimento al MeasureTask presente nel ValidationOp non"
						+ "è più un riferimento valido. Evidentemente non"
						+ "si è tenuto traccia di un aggiornamento del sistema da qualche"
						+ "parte. Si dovrebbe indagare al riguardo.");
			}
			if( measureTaskList.size()>1){
				//ritorna c'è un problema, 
				//non possono esistere due measure task con stesso taskId
				throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
						+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
						+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
						+ "Si sospetta un serio errore interno del sistema");
			}
		
		List<CollectedData> collectedDataList = collectedDataRepository.findByTaskId(
				taskId);
		
		if(collectedDataList.isEmpty()){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"La lista di collected Data per quel taskId è vuota. Ma "
					+ "ciò non è possibile. Almeno il CollectedData oggetto"
					+ " della validazione deve essere presente");
		}

		List<Integer> validOpIndexInCollectedData = new ArrayList<>();



		collectedDataList.stream().forEach(e -> {

            if(e.getValidationOpList() != null){
                e.getValidationOpList().stream().forEach(v -> {
                    log.info(v.getId());
                    if(v.getId().equals(validationOpId));
                    int index = e.getValidationOpList().indexOf(v);
                    validOpIndexInCollectedData.add(index);
                });
            }
        });

		for(int j = 0; j < collectedDataList.size(); j++){
		    CollectedData collectedData = collectedDataList.get(j);
		    collectedData.getValidatedList().remove((int) validOpIndexInCollectedData.get(j));
		    collectedData.getValidatedList().add(validOpIndexInCollectedData.get(j), true);



		    boolean allValidated = false;
		    int count = 0;
		    for(int k = 0; k < collectedData.getValidatedList().size(); k++){
		        if(collectedData.getValidatedList().get(k).equals(true)){
		            count++;
                }
            }
            if(count == collectedData.getValidatedList().size()){
		        allValidated = true;
            }

            collectedData.setValidated(allValidated);
            collectedDataRepository.save(collectedData);

        }

/*		int i = 0;
		*//**
		 * Si cerca il CollectedData da validare. Nel DB deve essercene almeno
		 * uno ancora con il campo validated a false. Altrimenti sono già stati 
		 * tutti validati, quindi non ha senso chiamare questo metodo
		 *//*
		List<CollectedData> toBeValidated = new ArrayList<>();
		CollectedData collectedDataToBeValidated = new CollectedData();
		for(CollectedData collectedData: collectedDataList){
			if(!collectedData.isValidated()){
				collectedDataToBeValidated = collectedData;
				//++i;

				toBeValidated.add(collectedData);
			}
		}
*//*		if(i!=1){
			//errore, il data collected da validare deve esserci nel DB e ce ne è solo 1
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"La lista di collected Data per quel taskId non è vuota. Ma "
					+ "o nessun dato è da validare, o ce ne sono più di uno."
					+ " Questa cosa non è possibile.");
		}*//*

		for(int h = 0; h < collectedDataList.size();h++) {

		    collectedDataToBeValidated = collectedDataList.get(h);
			collectedDataToBeValidated.setValidated(true);
			collectedDataRepository.save(collectedDataToBeValidated);
			System.out.println(collectedDataToBeValidated.toString());
		}
		*/
		MeasureTask measureTask = measureTaskList.get(0);
		List<ValidationOp> validationOpList = measureTask.getValidationIdList();
		if( validationOpList.isEmpty()){
			//return eccezione, measureTask non esiste, httpstatus Conflict
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"E' impossibile che la lista di ValidationOp presente"
					+ "all'interno del MeasureTask sia vuota. Questo perché deve"
					+ "essere quanto meno presente la ValidationOp corrente, su cui"
					+ "si sta operando questa contromisura di validazione.");
		}
		/**
		 * Resetto lo status del ValidationOp per indicare che anche se era stata 
		 * superata una operazione di validazione, nonostante ciò è tutto
		 * da rivalidare nel prossimo step, ovvero rioperando la misura 
		 */
		for(ValidationOp v: validationOpList){
			v.setContromeasureDone(false);
			validationOpRepository.save(v);
		}
		//c'è una chiamata da effettuare al bus
		System.out.println("sto per eseguire la chiamata al bus");



		//String s = busPhase4InteractionImplementation.saveValitatedDataOnBus(taskId);
		
	/*	System.out.println(s);
		DTOResponseCollectedData dtoResponseCollectedData =
				new DTOResponseCollectedData();
		dtoResponseCollectedData.set_id(collectedDataToBeValidated.get_id());
		dtoResponseCollectedData.setWorkflowData(collectedDataToBeValidated.getWorkflowData());
		dtoResponseCollectedData.setTaskId(collectedDataToBeValidated.getTaskId());
		dtoResponseCollectedData.setValue(collectedDataToBeValidated.getValue());
		dtoResponseCollectedData.setValidated(collectedDataToBeValidated.isValidated());
		
		
		ResponseEntity<DTOResponseCollectedData> responseEntity =
				new ResponseEntity<DTOResponseCollectedData>(
						dtoResponseCollectedData, HttpStatus.OK);
		return responseEntity;*/

	    return new ResponseEntity<String>("Data Validated correctly.", HttpStatus.OK);
	}



	/*	    TODO REMOVE PHASE3
	*//**
     * Metodo che restituisce una ResponseEntity, che contiene nel body un
     * JSON object del ValidationOp creato nel DB
     *
     * L'ipotesi è che sia presente un MeasureTask associato a quel TaskId. Se
     * così non fosse viene restituito un errore. E' contemplato
     * il caso in cui un RefMeasureTask non sia settato in ValidationOp (measureTaskid
     * è null) Infatti è ammessa la possibilità di aggiornarlo in seguito
     * In ogni caso se il taskId è corretto, automaticamente viene aggiunto
     * il ValidationOp alla lista di MeasureTask
     *
     * @throws BodyEmptyException
     * @throws IdKeyNullException
     * @throws EntityNotFoundException
     * @throws AnomalySystemException
     *
     *//*
	@Override
	public ResponseEntity<?> createValidationOp(DTOValidationOp dtoValidationOp) throws BodyEmptyException, IdKeyNullException, EntityNotFoundException, AnomalySystemException {
		// TODO Auto-generated method stub

		*//**
     *  Leggo il DTO del body e setto ValidationOp
     *//*
		if(dtoValidationOp==null){
			//return eccezione requestBody, httpstatus BadRequest
			throw new BodyEmptyException(HttpStatus.BAD_REQUEST.value(),
					"Il body della post non è corretto e la deserializzazione"
					+ "ha generato una istanza null");
		}
		if(dtoValidationOp.getMeasureTaskId()==null){
			//return eccezione, dovevi settare TaskId , httpstatus BadRequest
			//ovvero, non puoi creare un ValidationOp se non passi
			//un MeasureTask con un taskId non null (che quindi esiste)
			throw new IdKeyNullException(HttpStatus.FORBIDDEN.value(),
					"Dovevi settare TaskId,"+
					" non puoi creare un ValidationOp se non passi"+
					" un MeasureTask con un taskId non null (che quindi esiste)");

		}

		*//**
     * passo 1: trovo il measure task
     *//*
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(dtoValidationOp.getMeasureTaskId());
		if(measureTaskList.isEmpty()){
			//return eccezione, measureTask non esiste, httpstatus Conflict
			throw new EntityNotFoundException(HttpStatus.BAD_REQUEST.value(),
					"Non è presente alcun MeasureTask con "
					+ "questo 'taskId' nel DB");
		}
		if( measureTaskList.size()>1){
			//ritorna c'è un problema,
			//non possono esistere due measure task con stesso taskId
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
					+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
					+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
					+ "Si sospetta un serio errore interno del sistema");
		}
		List<MeasureTask> refMeasureTaskList;
		if(dtoValidationOp.getRefMeasureTaskId()!=null){
				refMeasureTaskList = measureTaskRepository.findByTaskId(dtoValidationOp.getRefMeasureTaskId());

			if( refMeasureTaskList.size()>1){
				//ritorna c'è un problema,
				//non possono esistere due measure task con stesso taskId
				throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
						+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
						+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
						+ "Si sospetta un serio errore interno del sistema");
			}
		}

		MeasureTask measureTask = measureTaskList.get(0);
		*//**
     * passo 2: lo devo poter aggiornare:
     * farò la seguente operazione:
     * -salvo in validationOp il taskId (non è necessario perché me lo passa così
     * già lui nel body)
     * -salvo validationOp nel db
     * -ora che ho generato l'id, faccio l'update di MeasureTask
     *//*

		ValidationOp validationOp = new ValidationOp();
		validationOp.setName(dtoValidationOp.getName());
		validationOp.setType(dtoValidationOp.getType());
		validationOp.setDescription(dtoValidationOp.getDescription());
		validationOp.setCardinality(dtoValidationOp.getCardinality());
		validationOp.setCompType(dtoValidationOp.getCompType());
		validationOp.setMeasureTaskId(dtoValidationOp.getMeasureTaskId());
		validationOp.setRefMeasureTaskId(dtoValidationOp.getRefMeasureTaskId());
		validationOp.setReferenceParams(dtoValidationOp.getReferenceParams());
		validationOp.setCountermeasures(dtoValidationOp.getCountermeasures());

		validationOp.setThisOp(dtoValidationOp.getThisOp());
		validationOp.setRefOp(dtoValidationOp.getRefOp());
		validationOp.setUserDefined(dtoValidationOp.isUserDefined());
		//potrei fare una analisi per vedere se il campo è false o true, ma potrei
		//anche ignorarlo
		validationOp.setContromeasureDone(false);
		*//**
     * Salvo ValidationOp nel DB
     * *//*
		//qui potrei fare un eventuale controllo per vedere se già è presente
		//un validatioOp uguale per qualche ragione, nel caso restituisco Httpstatus.CONFLICT
		validationOpRepository.save(validationOp);

		*//*******************************************************************************
     * FASE 2: devo aggiornare MeasureTask
     *
     * Qui curo la relazione tra ValidationOp e MeasureTask
     *//*

		//qui forse dovrei fare un controllo sul fatto che MeasureTask
		//è stato misteriosamente rimosso
		List<ValidationOp> validationOpList =
				measureTaskList.get(0).getValidationIdList();
		//do per scontato che non è null, perché chi la crea la setta come []
		if(validationOpList == null){
			validationOpList = new ArrayList<ValidationOp>();
		}
		validationOpList.add(validationOp);
		measureTaskList.get(0).setValidationIdList(validationOpList);
		measureTaskRepository.save(measureTaskList.get(0));

		*//**
     * Qui setto il valore della risposta di ritorno
     *//*
		*//*
		DTOResponseValidationOp dtoResponseValidationOp = new DTOResponseValidationOp();
		dtoResponseValidationOp.setId(validationOp.getId());
		dtoResponseValidationOp.setName(validationOp.getName());
		dtoResponseValidationOp.setType(validationOp.getType());
		dtoResponseValidationOp.setDescription(validationOp.getDescription());
		dtoResponseValidationOp.setCardinality(validationOp.getCardinality());
		dtoResponseValidationOp.setCompType(validationOp.getCompType());
		dtoResponseValidationOp.setIdMeasureTask(validationOp.getMeasureTask().getTaskId());
		dtoResponseValidationOp.setIdRefMeasureTask(validationOp.getRefMeasureTask().getTaskId());
		dtoResponseValidationOp.setReferenceParams(validationOp.getReferenceParams());
		dtoResponseValidationOp.setCountermeasures(validationOp.getCountermeasures());
		dtoResponseValidationOp.setSelectedCountermeasures(
				validationOp.getSelectedCountermeasures());
		dtoResponseValidationOp.setThisOp(validationOp.getThisOp());
		dtoResponseValidationOp.setRefOp(validationOp.getRefOp());
		dtoResponseValidationOp.setUserDefined(validationOp.getUserDefined());

		ResponseEntity<DTOResponseValidationOp> responseEntity = new ResponseEntity<DTOResponseValidationOp>(
				dtoResponseValidationOp,HttpStatus.CREATED);
		return responseEntity;
		*//*
		//cosa restituisco come valore di ritorno? Forse è più significativo
		//restituire il MeasureTask aggiornato (non è un DTO.. rivedere questa cosa )
		ResponseEntity<MeasureTask> responseEntity = new ResponseEntity<MeasureTask>(
			measureTask, HttpStatus.OK);
		return responseEntity;
	}


	*//* Esempio body put (basta mandare il body corrente come se fosse una nuova create,
	 * ma l'id resta uguale). Il taskId deve essere uguale, perché il sistema
	 * è fatto che da errore se si cambia taskId ad un documento che ha già quell'id!
	 *
	 *
	{
	    "name": "post1",
		"type": "ABNORMAL_DATA",
		"description": "a description",
	    "cardinality": "COMPARE_MANY_TO_MANY",
	    "compType": "GREATER_THAN",
	    "measureTaskId":"userTaskA",
		"refMeasureTask": null,
		"referenceParams": null,
		"countermeasures": null,
		"selectedCountermeasures": null,
		"thisOp": "SUM",
	    "refOp": "AVERAGE",
		"userDefined": false
	}
	*/


	/*
     * Metodo che restituisce una ResponseEntity, che contiene nel body un
     * JSON object del ValidationOp aggiornato nel DB
     * @throws BodyEmptyException
     * @throws EntityNotFoundException
     * @throws IdKeyNullException
     * @throws AnomalySystemException
     *
     */

	@Override
	public ResponseEntity<?> updateValidationOp(String id,
                                                DTOValidationOp dtoValidationOp) throws BodyEmptyException, EntityNotFoundException, IdKeyNullException, AnomalySystemException {
		// TODO Auto-generated method stub
		ValidationOp validationOp = validationOpRepository.findOne(id);

		if (dtoValidationOp ==null) {
            /*//*gestire l'eccezione trovando una soluzione definitiva
            return new ResponseEntity<User>(HttpStatus.NOT_CONTENT);*/
			throw new BodyEmptyException(HttpStatus.BAD_REQUEST.value(),
					"Il body della post non è corretto e la deserializzazione"
					+ "ha generato una istanza null");
        }
		if(dtoValidationOp.getMeasureTaskId()==null){
			//return eccezione, dovevi settare TaskId , httpstatus BadRequest
			//ovvero, non puoi creare un ValidationOp se non passi
			//un MeasureTask con un taskId non null (che quindi esiste)
			throw new IdKeyNullException(HttpStatus.FORBIDDEN.value(),
					"Dovevi settare TaskId,"+
					" non puoi fare l'update di un ValidationOp se non passi"+
					" un MeasureTaskId con un valore diverso (che quindi esiste)");

		}
        if (validationOp ==null) {
            /*//*gestire l'eccezione trovando una soluzione definitiva
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);*/

        	throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente nessun documento con questo 'id'"
					+ "' nel DB");
        }

        List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(dtoValidationOp.getMeasureTaskId());
		if(measureTaskList.isEmpty()){
			//return eccezione, measureTask non esiste, httpstatus Conflict
			throw new EntityNotFoundException(HttpStatus.BAD_REQUEST.value(),
					"Non è presente alcun Measure Task con "
					+ "'taskId' nel DB");
		}
		if( measureTaskList.size()>1){
			//ritorna c'è un problema,
			//non possono esistere due measure task con stesso taskId
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
					+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
					+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
					+ "Si sospetta un serio errore interno del sistema");
		}

		List<MeasureTask> refMeasureTaskList = measureTaskRepository.findByTaskId(dtoValidationOp.getRefMeasureTaskId());

		if( refMeasureTaskList.size()>1){
			//ritorna c'è un problema,
			//non possono esistere due measure task con stesso taskId
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
					+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
					+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
					+ "Si sospetta un serio errore interno del sistema");
		}

		 if (!dtoValidationOp.getMeasureTaskId().equals(validationOp.getMeasureTaskId())) {
	            /*//*gestire l'eccezione trovando una soluzione definitiva
	            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);*/
			 throw new IdKeyNullException(HttpStatus.FORBIDDEN.value(),
						"Non puoi cambiare il TaskId di un ValidationOp."+
						" Devi inserire nel body lo stesso taskId");

	        }
		//verificare se va bene la save di mongorepository per fare l'update
		//perché potrebbe cambiare l'id, dovrei aggiornare Measure Task, e
		// non mi piace questa cosa, oppure devo ricorredere alle update
		// di MongoTemplate
		//https://www.mkyong.com/mongodb/spring-data-mongodb-update-document/

		//measureTask deve essere aggiornato in ogni caso credo, forse l'aggiornamento
		//dei campi nuovi non viene fatto in automatico anche mantenendo il riferimento

		//Verificato => va bene così, non serve neanche la save, l'id resta lo stesso,
		//quindi punta al record aggiornato, quindi una get su MeasureTask resituisce

		validationOp.setName(dtoValidationOp.getName());
		validationOp.setType(dtoValidationOp.getType());
		validationOp.setDescription(dtoValidationOp.getDescription());
		validationOp.setCardinality(dtoValidationOp.getCardinality());
		validationOp.setCompType(dtoValidationOp.getCompType());
		validationOp.setMeasureTaskId(dtoValidationOp.getMeasureTaskId()); //ovvero lo stesso
		validationOp.setRefMeasureTaskId(dtoValidationOp.getRefMeasureTaskId());
		validationOp.setReferenceParams(dtoValidationOp.getReferenceParams());
		validationOp.setCountermeasures(dtoValidationOp.getCountermeasures());

		validationOp.setThisOp(dtoValidationOp.getThisOp());
		validationOp.setRefOp(dtoValidationOp.getRefOp());
		validationOp.setUserDefined(dtoValidationOp.isUserDefined());
		validationOp.setContromeasureDone(dtoValidationOp.isContromeasureDone());

		//l'id resta uguale?
		validationOpRepository.save(validationOp);  //this will overwrite the document in database

		/*//*******************************************************************************
     * FASE 2: devo aggiornare MeasureTask
     *
     * Qui curo la relazione tra ValidationOp e MeasureTask
     */

		DTOResponseValidationOp dtoResponseValidationOp = new DTOResponseValidationOp();

		dtoResponseValidationOp.setId(validationOp.getId());
		dtoResponseValidationOp.setName(validationOp.getName());
		dtoResponseValidationOp.setType(validationOp.getType());
		dtoResponseValidationOp.setDescription(validationOp.getDescription());
		dtoResponseValidationOp.setCardinality(validationOp.getCardinality());
		dtoResponseValidationOp.setCompType(validationOp.getCompType());
		dtoResponseValidationOp.setMeasureTaskId(validationOp.getMeasureTaskId());
		dtoResponseValidationOp.setRefMeasureTaskId(validationOp.getRefMeasureTaskId());
		dtoResponseValidationOp.setReferenceParams(validationOp.getReferenceParams());
		dtoResponseValidationOp.setCountermeasures(validationOp.getCountermeasures());

		dtoResponseValidationOp.setThisOp(validationOp.getThisOp());
		dtoResponseValidationOp.setRefOp(validationOp.getRefOp());
		dtoResponseValidationOp.setUserDefined(validationOp.getUserDefined());
		dtoResponseValidationOp.setContromeasureDone(validationOp.isContromeasureDone());

		ResponseEntity<DTOResponseValidationOp> responseEntity = new ResponseEntity<DTOResponseValidationOp>(
				dtoResponseValidationOp, HttpStatus.OK);
		return responseEntity;

	}
/*
	*//**
     * delete ValidationOp
     * @throws AnomalySystemException
     * @throws EntityNotFoundException
     *//*
	@Override
	public ResponseEntity<?> deleteValidationOp(String id) throws AnomalySystemException, EntityNotFoundException {
		// TODO Auto-generated method stub

		ValidationOp validationOp = validationOpRepository.findOne(id);
		if(validationOp	==null){
			//solleva eccezione HttpStatus.NOT_FOUND
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					"Non è presente nessun documento con questo 'id'"
					+ "' nel DB");
		}
		if(validationOp.getMeasureTaskId()==null){
			//solleva eccezione HttpStatus.INTERNAL_SERVER_ERROR
			//questo perché in teoria i measuretask sono pensati per non essere
			//cancellati, devono poter mantenere la storia..
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Non è stato trovato nessun MeasureTask associato a questa ValidationOp,"
					+ " ma non dovrebbe essere possibile in"
					+ " quanto si è fatta l'ipotesi di rendere il ValidationOp"
					+ " legato ad un MeasureTask, perciò non può esistere una ValidationOp"
					+ " se non è associata ad un MeasureTask. Non ha senso");
		}

		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(
				validationOp.getMeasureTaskId());

		if(measureTaskList.isEmpty()){
			//solleva eccezione HttpStatus.INTERNAL_SERVER_ERROR
			//questo perché in teoria i measuretask sono pensati per non essere
			//cancellati, devono poter mantenere la storia, e inoltre un
			//validationOp esiste solo se esiste un MeasureTask a cui è associato
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"E' stato trovato un MeasureTaskId in ValidationOp ma "
					+ "non ha una corrispondenza in un MeasureTask salvato nel DB."
					+ " Non dovrebbe essere possibile questa cosa in quanto una ValidationOp"
					+ " una volta assegnata ad un MeasureTask non può essere assegnata ad un altro"
					+ " MeasureTask, tanto meno essere posta a null (esiste solo se esiste un Measure Task a lui associato). Inoltre"
					+ " legato ad un MeasureTask, perciò non può esistere una ValidationOp"
					+ " su Activiti non dovrebbe essere possibile cancellare i corrispondenti UserTask dei MeasureTask,"
					+ " questo perché comunque si deve mantenere traccia della storia passata");
		}
		if(measureTaskList.size()>1){
			//solleva eccezione HttpStatus.INTERNAL_SERVER_ERROR
			//questo perché in teoria i measuretask sono pensati per non essere
			//cancellati, devono poter mantenere la storia..
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Sono stati trovati più MeasureTask, non dovrebbe essere possibile in"
					+ "quanto in activiti i taskDefinitionKey dei task sono univoci"
					+ " e i MeasureTask non vengono cancellati per mantenerne la storia passata! "
					+ "Si sospetta un serio errore interno del sistema");
		}


		MeasureTask measureTask = measureTaskList.get(0);

		List<ValidationOp> validationOpList = measureTask.getValidationIdList();
		if(validationOpList.isEmpty()){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Nel MeasureTask associato a 'taskId' di ValidationOp non è stata "
					+ "alcuna ValidationOp, ma questa cosa non dovrebbe essere possibile, perché"
					+ " non dovrebbe esistere una ValidationOp associata ad un MeasureTask"
					+ " che esiste ma non è presente al suo interno. Evidentemente"
					+ " non è stata cancellata a dovere. Si sospetta un serio errore interno del sistema. Si consiglia"
					+ " di indagare");
		}
		int validationOpListLength = validationOpList.size();

		//è strano perché dovrei cancellare il base all'id della validationOp
		for(ValidationOp validationOpInMeasureTask: validationOpList ){
			if((validationOpInMeasureTask.getId()).equals(
					validationOp.getId())){
				validationOpList.remove(validationOpInMeasureTask);
				measureTask.setValidationIdList(validationOpList);
				break;
			}

		}
		if(validationOpListLength == validationOpList.size()){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Nella lista di ValidationOp del MeasureTask non è stato possibile cancellare"
					+ " la ValidationOp perché non è presente, ma questa cosa non dovrebbe essere possibile, perché"
					+ " non dovrebbe esistere una ValidationOp associata ad un MeasureTask"
					+ " che esiste ma non è presente al suo interno. Evidentemente"
					+ " non è stata cancellata a dovere. Si sospetta un serio errore interno del sistema. Si consiglia"
					+ " di indagare");
		}
		measureTaskRepository.save(measureTask);
		validationOpRepository.delete(validationOp);

		DTO dto= new DTO();
		ResponseEntity<DTO> responseEntity =
				new ResponseEntity<DTO>(
				dto, HttpStatus.NO_CONTENT);
		return responseEntity;
	}
	*/

}
