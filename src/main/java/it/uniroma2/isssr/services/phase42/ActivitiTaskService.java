package it.uniroma2.isssr.services.phase42;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.exception.ActivitiGetException;
import it.uniroma2.isssr.exception.ActivitiPutException;
import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;
import it.uniroma2.isssr.model.phase42.rest.DTOActivitiTaskVariable;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseActivitiTaskVariable;
import it.uniroma2.isssr.model.phase42.rest.response.activiti.DTOResponseActivitiTask;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * <p>Title: TaskService</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * 
 * <p>Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * Interfaccia per la dichiarazione di metodi che verranno invocati nella richiesta 
 * del servizio REST e restituiranno la relativa risposta.
 * 
 * Questa interfaccia espone dei metodi per interagire con i task di Activiti</p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */
public interface ActivitiTaskService {
	
	/**
	 * Metodo per richiedere la lista di tutti i tasks di Activiti
	 * 
	 * @return ResponseEntity generica (la sua implementazione avrà per body la lista degli UserTasks di Activiti e HttpStatus.OK
	 * 		in caso di successo)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<?> getTasks()
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
	/**
	 * Metodo per richiedere la lista di tutti gli UserTasks assegnati allo user di Activiti
	 * con username {username}
	 * 
	 * @param username : nome dello user di cui si vuole l'elenco dei tasks che lo hanno come assignee 
	 * @return ResponseEntity generica (la sua implementazione avrà per body la lista degli UserTasks di Activiti 
	 * con assignee = username e HttpStatus.OK in caso di successo)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<?> getUserTasks(String username)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
			
	/**
	 * Metodo per richiedere le informazioni ad uno User Task con taskId come id di runtime.
	 * 
	 * @param taskId : id di runtime dello User Task
	 * @return ResponseEntity che ha per body un oggetto DTOResponseActivitiTask e HttpStatus.OK
	 * 		in caso di successo)
	 * 		
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<DTOResponseActivitiTask> getUserTaskByTaskId(String taskId)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
	
	/**
	 * Metodo esposto per effettuare una update di un task di Activiti. La sua implementazione
	 * offre il servizio di completamento di uno UserTask con id di runtime id, 
	 * ma non è l'unica implementazione possibile per questo metodo.
	 * 
	 * @param id : id di runtime dello User Task
	 * @param action : azione da intraprendere sullo UserTask: nel caso specifico sarà "action": "complete"
	 * @return ResponseEntity che ha per body ...
	 * 		in caso di successo)
	 */
	public ResponseEntity<DTOResponseActivitiTask> updateActivitiTask(String id,
                                                                      String action);
	/**
	 * Metodo che consente di ottenere la lista in Activiti di tutte le variabili 
	 * di uno UserTask a partire dall'id di runtime {taskId}
	 *  
	 * @param taskId : id di runtime dello User Task
	 * @param scope : scope delle variabili, può essere "scope":"local" o "scope": "global"
	 * @return ResponseEntity generica
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<?> getTaskVariablesFromRuntimeTaskId(String taskId,
                                                               String scope) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;
	/**
	 * Metodo che consente di creare delle variabili e di associarle
	 * ad un task di Activiti a partite da una lista di variabili e dall'id di runtime {taskId}
	 * Si sottolinea come si sia osservato che activiti non consente di creare variabili
	 * sia con scope "local" che "global"
	 * 
	 * @param taskId : id di runtime dello User Task
	 * @param taskVariables : lista di TaskVariable che
	 * 		si vuole creare a associarle allo UserTask
	 * 
	 * @return ResponseEntity generica
	 */
	public ResponseEntity<?> createTaskVariableFromRuntimeTaskId(String taskId,
                                                                 List<TaskVariable> taskVariables);
	/**
	 * 
	 * @param taskId : id di runtime dello User Task
	 * @param action : 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<?> completeUserTasksWithStoredVariables(
            String taskId, String action) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;
	/**
	 * 
	 * @param candidateGroup : 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<DTOResponseActivitiTask> getUserTasksByCandidateGroup(
            String candidateGroup) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param dtoUpdateActivitiTask
	 * @return
	 * @throws ActivitiPutException
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public ResponseEntity<DTOResponseActivitiTaskVariable> updateActivitiTaskVariable(
            String id, String name,
            DTOActivitiTaskVariable dtoActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException;

}
