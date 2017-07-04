package com.gqm3242.activiti.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.activiti.entity.FlowElement;
import com.gqm3242.exception.ActivitiGetException;
import com.gqm3242.exception.ActivitiPostException;
import com.gqm3242.exception.ActivitiPutException;
import com.gqm3242.model.activiti.form.ActivitiFormProperty;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
import com.gqm3242.model.activiti.task.ActivitiTask;
import com.gqm3242.model.activiti.task.ActivitiTaskVariable;
import com.gqm3242.model.activiti.user.ActivitiUser;
import com.gqm3242.model.rest.DTOActivitiTaskVariable;
import com.gqm3242.model.rest.response.activiti.DTOResponseActivitiProcess;
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

	public List<ActivitiTaskVariable> getTaskVariablesFromRuntimeTaskId(String taskId, String scope) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;

	public void createTaskVariableFromRuntimeTaskId(String taskId,
                                                    List<ActivitiTaskVariable> activitiTaskVariables);

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

	public ActivitiTaskVariable updateActivitiTaskVariable(String id, String name,
                                                           DTOActivitiTaskVariable dtoActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException;

	
}