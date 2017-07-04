package it.uniroma2.isssr.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.controller42.MeasureTaskService;
import it.uniroma2.isssr.controller42.ValidationOpService;
import it.uniroma2.isssr.model42.rest.DTOMeasureTask;
import it.uniroma2.isssr.model42.rest.DTOValidationOp;
import it.uniroma2.isssr.model42.rest.response.DTOResponseMeasureTask;
import it.uniroma2.isssr.validation.Phase;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>Title: RestVariableActivitiPresentation</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * Classe che espone una interfaccia pensata per un utente di della fase 3 e 4 
 * di GQM+Strategies che desidera effettuare i piani di validazione
 * ed eseguire le operazioni di validazione sui dati raccolti dagli utenti della 
 * fase 3.1/4.1. 
 * Si da una descrizione del contesto in cui essa opera:
 *  - la pianificazione della validazione su una certa misura che sarà 
 *  effettuata nella fase 4 di GQM è qualcosa che viene operato da un Validator
 *  a cui è assegnato uno User Task all'interno di un metamodello di Activiti
 *  per gestire questa operazione. In questa fase viene attrezzato un MeasureTask
 *  di ValidationOp. 
 *  - gestione della raccolta dei dati e della validazione sono pensati come uno UserTask
 *  riassegnato tra data Collector e validator per la gestione della raccolta di dati
 *  e della loro validazione eseguendo una misura secondo una certa metrica.
 *  Ad ogni MeasureTask è associata una unica metrica e una unica tipologia di misura, 
 *  con un conseguente unico dato da dover validare, e da dover raccogliere.
 *  Quindi un MeasureTask, indicizzato da un MeasureTaskId, per cui esisteranno
 *  una o pià ValidationOp, con cui il MeasureTask è stato attrezzato da un 
 *  validator nella fase 3. Infine per ogni dato raccolto in fase 4, specificato in
 *  un CollectedData, viene eseguita la lista di operazioni di validazione definite 
 *  in fase 3 e associate al MeasureTask. E' evidente che per ogni coppia
 *  raccolta dato e validazione dato esiste ed è unico il CollectedData con un certo
 *  MeasureTaskId, associato a quindi ad un MeasureTask, e ancora non validato.
 * 
 * </p> 
 * 
 * @author Fabio Alberto Coira, Gabriele Belli
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/validation/")
public class RestValidationOpPresentation {

	@Autowired
	ValidationOpService validationOpService;
	
	@Autowired
	MeasureTaskService measureTaskService;

	/**
	 * Metodo pensato come endpoint per offrire un servizio Rest per un
	 * Validator che in fase 3.2 vuole la lista di tutte le ValidationOp 
	 * associate ad un certo measureTaskId, mentre in fase 4.2 vuole 
	 * la stessa lista filtrata rispetto alle ValidationOp che hanno già 
	 * ottenuto un superamento della ValidationOp effettuata.
	 * Come è buona pratica in ingegneria si è riutilizzato lo stesso codice
	 * per entrambi i casi, e la discriminazione è operata attraverso il parametro
	 * phase.
	 * 
	 * @throws EntityNotFoundException 
	 * @throws AnomalySystemException
	 */
	@RequestMapping(value = "/validationOpList", method = RequestMethod.GET)
	public ResponseEntity<?> getValidationOpListByMeasureTaskId(
			@RequestParam("measureTaskId") String measureTaskId,
			@RequestParam("phase") Phase phase)
					throws EntityNotFoundException, AnomalySystemException{
		return validationOpService.getValidationOpListByMeasureTaskId(
				measureTaskId, phase);
		
	}
	
	/**
	 * Metodo pensato come endpoint per offrire un servizio Rest per un Validator
	 * che desidera ottenere tutte le informazioni inerenti uno specifico 
	 * ValidationOp salvato nella relativa collezione in MongoDB
	 * 
	 * @throws EntityNotFoundException 
	 */
	
	@RequestMapping(value = "/validationOp", method = RequestMethod.GET)
	public ResponseEntity<?> getValidationOp(
			@RequestParam("id") String id) throws EntityNotFoundException{
		return validationOpService.getValidationOp(id);
	}
	
	/**
	 * CHIAMATA 3 (Gabriele):
	 * dato un businessWorkflowProcessInstanceId get MeasureTasks
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws ActivitiGetException
	 */
	@RequestMapping(value = "/measureTasks", method = RequestMethod.GET)
	public ResponseEntity<?> getMeasureTasks(
			@RequestParam("businessWorkflowProcessDefinitionId") String businessWorkflowProcessDefinitionId) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
		return validationOpService.getMeasureTask(businessWorkflowProcessDefinitionId);
	}
	
	/**
	 * Metodo pensato come endpoint per offrire un servizio Rest per un
	 * Validator che desideri creare una ValidationOp da associare ad un
	 * MeasureTask. Il Validator passerà nel body le informazioni richieste.
	 * Una osservazione da effettuare è che è evidente che nel database
	 * sarà presente solo un MeasureTask con quel taskId. Inoltre l'esistenza
	 * di un ValidationOp è fortemente legata ad un MeasureTask, e difatti non
	 * può esistere un ValidationOp che non è associato al MeasureTask.
	 * 
	 * La lista di ValidationOp presente nel relativo MeasureTask viene aggiornata
	 * perché deve essere possibile poter passare da uno agli altri, cioè la loro
	 * relazione deve essere tracciata.
	 * 
	 * @throws BodyEmptyException
	 * @throws IdKeyNullException
	 * @throws EntityNotFoundException 
	 * @throws AnomalySystemException 
	 */
	
	@RequestMapping(value = "/validationOp", method = RequestMethod.POST)
	public ResponseEntity<?> createValidationOp(
			@RequestBody DTOValidationOp dtoValidationOp) throws BodyEmptyException, IdKeyNullException, EntityNotFoundException, AnomalySystemException{
		return validationOpService.createValidationOp(dtoValidationOp);
	}
	
	/**
	 * Metodo pensato come endpoint per offrire un servizio Rest per un
	 * aggiornamento di una ValidationOp. Qui è da evidenziare come
	 * non si possa nell'aggiornamento modificare il riferimento al MeasureTask
	 * in virtù del fatto che non ha senso il riassegnamento di una specifica
	 * ValidationOp ad un altro MeasureTask differente da quello per cui è
	 * stata creata.
	 * 
	 * @throws BodyEmptyException 
	 * @throws EntityNotFoundException 
	 * @throws IdKeyNullException 
	 * @throws AnomalySystemException 
	 */
	@RequestMapping(value = "/validationOp", method = RequestMethod.PUT)
	public ResponseEntity<?> updateValidationOp(
            @RequestParam("id") String id, @RequestBody DTOValidationOp dtoValidationOp) throws BodyEmptyException, EntityNotFoundException, IdKeyNullException, AnomalySystemException{
		return validationOpService.updateValidationOp(id,dtoValidationOp);
	}
	
	 /** 
	 * Metodo pensato come endpoint per offrire un servizio Rest per un
	 * validator che voglia rimuovere una ValidationOp dal database. Ovviamente
	 * non è pensabile che un MeasureTask continui a puntare ad una ValidationOp
	 * non più esistente, e pertanto dapprima si aggiorna il MeasureTask
	 * rimuovendo il riferimento, e poi si cancella il ValidationOp
	 * 
	 * @throws EntityNotFoundException 
	 * @throws AnomalySystemException 
	 * 
	 */
	 
	@RequestMapping(value = "/validationOp", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteValidationOp(
			@RequestParam("id") String id) throws AnomalySystemException, EntityNotFoundException{
		return validationOpService.deleteValidationOp(id);
	}
	/**
	 * creo un measureTask (per debug)
	 * @param dtoMeasureTask
	 * @return
	 */
	@RequestMapping(value = "/measureTask",
			method = RequestMethod.POST)
	public ResponseEntity<DTOResponseMeasureTask> createMeasureTask(
			@RequestBody DTOMeasureTask dtoMeasureTask){
		return measureTaskService.createMeasureTask(dtoMeasureTask);
	}
	
	/*
	
	 * 
	 * Eseguo validazione:
	 * metodo che esegue validate e devi ritornare l'errore
	 * parametri input: validationOpId , idCollectedData
	 *
	 * @param id: id del validationOp
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 * @throws EntityNotFoundException
	 * @throws AnomalySystemException
	 */
	@RequestMapping(value = "/validateData", method = RequestMethod.GET)
	public ResponseEntity<?> validate(@RequestParam("id") String id) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, EntityNotFoundException, AnomalySystemException{
		return validationOpService.validateDataCollected(id);
				
	}
	/**
	 * Servizio 
	 * @param idValidationOp: id del validationOp
	 * @param idDataCollected: id del validationOp
	 * @param errorMessage: messaggio dell'errore
	 * @throws ActivitiPutException
	 * @throws JSONException
	 * 
	 */
	@RequestMapping(value = "/repeateMeasure", method = RequestMethod.GET)
	public ResponseEntity<?> repeateValidation(
			@RequestParam(value = "idRuntimeTask", required = true) String idRuntimeTask,
			@RequestParam(value = "idValidationOp", required = true) String idValidationOp,
			@RequestParam(value = "errorMessage") String errorMessage)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, EntityNotFoundException, AnomalySystemException, BodyEmptyException, IdKeyNullException, JSONException, ActivitiPutException{
		return validationOpService.repeateMeasure(idRuntimeTask,idValidationOp, errorMessage);
				
	}
	/**
	 * Scarta misura
	 * @throws IdKeyNullException 
	 * @throws BodyEmptyException 
	 */
	@RequestMapping(value = "/ignoreValidation", method = RequestMethod.GET)
	public ResponseEntity<?> ignoreValidation(@RequestParam("id") String id) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, EntityNotFoundException, AnomalySystemException, BodyEmptyException, IdKeyNullException{
		return validationOpService.ignoreValidation(id);
				
	}
	/**
	 * Completa validazione
	 */
	@RequestMapping(value = "/completeValidation", method = RequestMethod.GET)
	public ResponseEntity<?> completeValidation(@RequestParam("taskId") String taskId)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, EntityNotFoundException, AnomalySystemException, BodyEmptyException, IdKeyNullException{
		return validationOpService.completeValidation(taskId);
				
	}

}
