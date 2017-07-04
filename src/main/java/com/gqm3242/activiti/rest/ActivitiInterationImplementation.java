package com.gqm3242.activiti.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.activiti.entity.FlowElement;
import com.gqm3242.exception.ActivitiGetException;
import com.gqm3242.exception.ActivitiPostException;
import com.gqm3242.exception.ActivitiPutException;
import com.gqm3242.model.activiti.form.ActivitiFormProperty;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
import com.gqm3242.model.activiti.process.ActivitiProcessDef;
import com.gqm3242.model.activiti.task.ActivitiTask;
import com.gqm3242.model.activiti.task.ActivitiTaskVariable;
import com.gqm3242.model.activiti.user.ActivitiUser;
import com.gqm3242.model.rest.DTOActivitiTaskVariable;
import com.gqm3242.model.rest.response.activiti.DTOResponseActivitiProcess;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
 * Implementazione dei servizi richiesta ad Activiti Rest, attore
 * della nostra applicazione. Questa classe è pensata per essere
 * una sorta di traduttore delle nostre richieste (quello
 * che realmente occorre alla nostra applicazione) rispetto
 * a quello che effettivamente arriva da Activiti Rest.
 * E' per quello che in questo Service viene iniettata la dipendenza
 * da un'altra classe, Activiti2fase32Implementation,
 * che è quella che si occupa di effettuare le richieste ad Activiti
 * Rest e di esporre alla nostra applicazione le sue risposte.</p> 
 * 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@Service("ActivitiInteration")
public class ActivitiInterationImplementation implements ActivitiInteration{
	
	@Autowired
	Activiti2fase32Implementation activiti2fase32Implementation;
	
	@Override
	public List<ActivitiUser> getUsers() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
			return  activiti2fase32Implementation.getUsers();
	}
	
	@Override
	public List<ActivitiTask> getTasks() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
			return activiti2fase32Implementation.getTasks();
	}

	@Override
	public List<ActivitiTask> getUserTasks(String username) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
			return activiti2fase32Implementation.getUserTasks(username);
			
	}
	
	@Override
	public byte[] getProcessInstanceState(String id) throws IOException {
		// TODO Auto-generated method stub
			
		return activiti2fase32Implementation.getProcessInstanceState(id);
	}
	
	@Override
	public boolean updateActivitiTask(String id, String action) {
			// TODO Auto-generated method stub
			return activiti2fase32Implementation.updateTask(id, action);
			
	}

	@Override
	public ResponseEntity<DTOResponseActivitiProcess> getProcess(String username, String password) throws JsonParseException, JsonMappingException, IOException {
			List<ActivitiProcessDef> process = activiti2fase32Implementation.getProcess(username, password);
			
			DTOResponseActivitiProcess dtoResponseActivitiProcess = new DTOResponseActivitiProcess();
			dtoResponseActivitiProcess.setActivitiProcess(process);
			ResponseEntity<DTOResponseActivitiProcess> responseEntity = new ResponseEntity<DTOResponseActivitiProcess>(dtoResponseActivitiProcess, HttpStatus.OK);
			return responseEntity;
	}
	
	@Override
	public List<ActivitiFormProperty> getFormPropertiesTaskById(
			String taskId) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		// TODO Auto-generated method stub
			
			return activiti2fase32Implementation.getFormPropertiesTaskById(taskId);
		
	}
	
	@Override
	public List<ActivitiTaskVariable> getTaskVariablesFromRuntimeTaskId(
			String taskId, String scope) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		// TODO Auto-generated method stub
			return activiti2fase32Implementation.getTaskVariablesFromRuntimeTaskId(
							taskId, scope);
	}
	
	@Override
	public void createTaskVariableFromRuntimeTaskId(
			String taskId, List<ActivitiTaskVariable> activitiTaskVariables) {
		// TODO Auto-generated method stub
		activiti2fase32Implementation.createTaskVariables
				(taskId, activitiTaskVariables);
			
	}
	
	@Override
	public boolean completeUserTasksWithStoredVariables(
			String taskId, String action) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		// TODO Auto-generated method stub
		return activiti2fase32Implementation.completeUserTasksWithStoredVariables
				(taskId, action);
	}
	@Override
	public List<ActivitiTask> getUserTasksByCandidateGroup(
			String candidateGroup) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
		return activiti2fase32Implementation.getUserTasksByCandidateGroup(candidateGroup);
	}
	@Override
	public ActivitiTask getUserTaskByTaskId(
			String taskId) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		// TODO Auto-generated method stub
			return activiti2fase32Implementation.getUserTaskByTaskId(
					taskId);			
	}
	
	@Override
	public List<FlowElement> getFlowElementsList(String username, String password,
			String businessWorkflowProcessDefinitionId) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException {
		return activiti2fase32Implementation.getFlowElementsList(username, password, businessWorkflowProcessDefinitionId);
}
	@Override
	public void submitFormDataAndCompleteTask(String taskId, List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws ActivitiPostException {
		// TODO Auto-generated method stub

		activiti2fase32Implementation.submitFormDataAndCompleteTask(taskId, activitiFormVariableProperties);
	
	}
	@Override
	public ActivitiTaskVariable updateActivitiTaskVariable(String id, String name,
			DTOActivitiTaskVariable dtoActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException {
		// TODO Auto-generated method stub
		return activiti2fase32Implementation.updateActivitiTaskVariable(id,
				name, dtoActivitiTaskVariable);
	}

	

}
	