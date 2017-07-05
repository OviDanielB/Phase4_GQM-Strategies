package it.uniroma2.isssr.services.phase42.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.model.phase42.activiti.FlowElement;
import it.uniroma2.isssr.exception.ActivitiGetException;
import it.uniroma2.isssr.exception.ActivitiPostException;
import it.uniroma2.isssr.exception.ActivitiPutException;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormProperty;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormVariableProperty;
import it.uniroma2.isssr.model.phase42.activiti.process.ActivitiProcessDef;
import it.uniroma2.isssr.model.phase42.activiti.task.ActivitiTask;
import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;
import it.uniroma2.isssr.model.phase42.activiti.user.ActivitiUser;
import it.uniroma2.isssr.model.phase42.rest.DTOActivitiTaskVariable;
import it.uniroma2.isssr.model.phase42.rest.response.activiti.DTOResponseActivitiProcess;
import it.uniroma2.isssr.services.phase42.ActivitiInteration;
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
public class ActivitiInterationImplementation implements ActivitiInteration {
	
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
	public List<TaskVariable> getTaskVariablesFromRuntimeTaskId(
			String taskId, String scope) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		// TODO Auto-generated method stub
			return activiti2fase32Implementation.getTaskVariablesFromRuntimeTaskId(
							taskId, scope);
	}
	
	@Override
	public void createTaskVariableFromRuntimeTaskId(
			String taskId, List<TaskVariable> taskVariables) {
		// TODO Auto-generated method stub
		activiti2fase32Implementation.createTaskVariables
				(taskId, taskVariables);
			
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
	public TaskVariable updateActivitiTaskVariable(String id, String name,
                                                   DTOActivitiTaskVariable dtoActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException {
		// TODO Auto-generated method stub
		return activiti2fase32Implementation.updateActivitiTaskVariable(id,
				name, dtoActivitiTaskVariable);
	}

	

}
	