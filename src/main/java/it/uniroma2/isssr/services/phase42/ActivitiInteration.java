package it.uniroma2.isssr.services.phase42;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.model.phase42.activiti.FlowElement;
import it.uniroma2.isssr.exception.ActivitiGetException;
import it.uniroma2.isssr.exception.ActivitiPostException;
import it.uniroma2.isssr.exception.ActivitiPutException;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormProperty;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormVariableProperty;
import it.uniroma2.isssr.model.phase42.activiti.task.ActivitiTask;
import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;
import it.uniroma2.isssr.model.phase42.activiti.user.ActivitiUser;
import it.uniroma2.isssr.model.phase42.rest.DTOActivitiTaskVariable;
import it.uniroma2.isssr.model.phase42.rest.response.activiti.DTOResponseActivitiProcess;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * <p>Title: ActivitiInterationImplementation</p>
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
 * Interfaccia che espone dei metodi per i servizi richiesti ad 
 * Activiti-Rest, attore della nostra applicazione. Questi metodi
 * sono utilizzati per interfacciare la nostra applicazione alla
 * risposta di Activiti-Rest</p> 
 * 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */
public interface ActivitiInteration {

	public List<ActivitiUser> getUsers() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;

	public List<ActivitiTask> getTasks() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
	
	public List<ActivitiTask> getUserTasks(String username) 
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;
	
	public byte[] getProcessInstanceState(String id) throws IOException;
	
	public boolean updateActivitiTask(String id, String action);

	public ResponseEntity<DTOResponseActivitiProcess> getProcess(String username, String password)
			throws JsonParseException, JsonMappingException, IOException;

	public List<ActivitiFormProperty> getFormPropertiesTaskById(String taskId) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;

	public List<TaskVariable> getTaskVariablesFromRuntimeTaskId(String taskId, String scope) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;

	public void createTaskVariableFromRuntimeTaskId(String taskId,
                                                    List<TaskVariable> taskVariables);

	public boolean completeUserTasksWithStoredVariables(String taskId,
                                                        String action) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;
	
	public List<ActivitiTask> getUserTasksByCandidateGroup(
            String candidateGroup) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;

	public ActivitiTask getUserTaskByTaskId(String taskId) 
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;

	public List<FlowElement> getFlowElementsList(String username, String password, String businessWorkflowProcessDefinitionId)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;

	public void submitFormDataAndCompleteTask(String taskId,
                                              List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws ActivitiPostException;

	public TaskVariable updateActivitiTaskVariable(String id, String name,
                                                   DTOActivitiTaskVariable dtoActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException;

	
}