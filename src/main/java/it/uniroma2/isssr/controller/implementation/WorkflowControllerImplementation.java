package it.uniroma2.isssr.controller.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletResponse;

import it.uniroma2.isssr.HostSettings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma2.isssr.Exception.ActivitiEntityAlreadyExistsException;
import it.uniroma2.isssr.Exception.BusinessWorkflowNotCreatedException;
import it.uniroma2.isssr.Exception.IllegalCharacterRequestException;
import it.uniroma2.isssr.Exception.JsonRequestException;
import it.uniroma2.isssr.Exception.MetaWorkflowNotDeployedException;
import it.uniroma2.isssr.Exception.MetaWorkflowNotStartedException;
import it.uniroma2.isssr.Exception.ModelXmlNotFoundException;
import it.uniroma2.isssr.Exception.ProcessDefinitionNotFoundException;
import it.uniroma2.isssr.Exception.StrategyDataException;
import it.uniroma2.isssr.controller.WorkflowController;
import it.uniroma2.isssr.dto.activiti.entity.Model;
import it.uniroma2.isssr.dto.activiti.entitylist.ModelList;
import it.uniroma2.isssr.dto.post.PostCreateWorkflow;
import it.uniroma2.isssr.dto.post.PostDeploy;
import it.uniroma2.isssr.dto.post.PostStartProcessInstance;
import it.uniroma2.isssr.model.BusinessWorkflow;
import it.uniroma2.isssr.model.MetaWorkflow;
import it.uniroma2.isssr.model.WorkflowData;
import it.uniroma2.isssr.repository.WorkflowDataRepository;
import it.uniroma2.isssr.tools.JsonRequest;

@RestController
public class WorkflowControllerImplementation implements WorkflowController {

	//TODO lettere accentate
	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>',
			'|', '\"', ':' };

	@Autowired
	private HostSettings hostSettings;

	
	
	@Autowired
	private WorkflowDataRepository workflowDataRepository;

	
	
	@RequestMapping(value = "/workflows/create", method = RequestMethod.POST)
	public ResponseEntity<?> createWorkflow(HttpServletResponse httpServletResponse,
			@RequestBody PostCreateWorkflow createWorkflowBody)
			throws IllegalCharacterRequestException, ActivitiEntityAlreadyExistsException, JsonRequestException,
			BusinessWorkflowNotCreatedException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			MetaWorkflowNotStartedException, IOException, ProcessDefinitionNotFoundException {

		String workflowName = createWorkflowBody.getName();
		
		for (char character : ILLEGAL_CHARACTERS) {
			if (workflowName.indexOf(character) >= 0) {
				throw new IllegalCharacterRequestException(workflowName, character);
			}
		}

		String metaWorkflowName = hostSettings.getMetaworkflowPrefix() + workflowName
				+ hostSettings.getMetaworkflowSuffix();

		MetaWorkflow.checkAlreadyExists(hostSettings, metaWorkflowName);
		BusinessWorkflow.checkAlreadyExists(hostSettings, workflowName);

		MetaWorkflow metaWorkflow = new MetaWorkflow(hostSettings, metaWorkflowName, workflowName);
		metaWorkflow.deploy();
		metaWorkflow.start();

		BusinessWorkflow businessWorkflow = new BusinessWorkflow(hostSettings, workflowName,
				metaWorkflow.getProcessInstanceId());
		businessWorkflow.createModel();
		metaWorkflow.updateVariable("modelId", businessWorkflow.getModelId());

		WorkflowData workflowData = new WorkflowData();
		workflowData.setBusinessWorkflowName(businessWorkflow.getName());
		workflowData.setBusinessWorkflowModelId(businessWorkflow.getModelId());
		workflowData.setMetaWorkflowName(metaWorkflow.getName());
		workflowData.setMetaWorkflowProcessInstanceId(metaWorkflow.getProcessInstanceId());
		workflowDataRepository.save(workflowData);

		JSONObject response = new JSONObject();
		response.put("metaWorkflowProcessInstanceId", metaWorkflow.getProcessInstanceId());
		response.put("businessWorkflowModelId", businessWorkflow.getModelId());

		return new ResponseEntity<String>(response.toString(), HttpStatus.CREATED);
	}

	
	
	
	
	
	@RequestMapping(value = "/workflows", method = RequestMethod.GET)
	public ResponseEntity<?> getWorkflows() throws JsonRequestException {

		List<String> workflowNames = new ArrayList<String>();
		List<String> workflowIDs = new ArrayList<String>();

		JsonRequest jsonRequest = new JsonRequest(hostSettings);

		ResponseEntity<ModelList> modelList = jsonRequest.get(hostSettings.getActivitiRestEndpointModels(),
				ModelList.class);

		if (modelList.getBody().getData() != null) {
			for (Model model : modelList.getBody().getData()) {
				workflowNames.add(model.getName());
				workflowIDs.add(model.getId());
			}
		}

		JSONObject response = new JSONObject();
		response.put("workflowNames", workflowNames);
		response.put("workflowIDs", workflowIDs);

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	
	
	
	
	@RequestMapping(value = "/workflows/deployments", method = RequestMethod.POST)
	public ResponseEntity<?> deployWorkflowModel(@RequestBody PostDeploy deployBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			StrategyDataException, IOException, ProcessDefinitionNotFoundException {

		String modelId = deployBody.getModelId();
		List<WorkflowData> workflowDatas = workflowDataRepository.findByBusinessWorkflowModelId(modelId);
		if (workflowDatas.size() != 1) {
			throw new StrategyDataException();
		}
		WorkflowData workflowData = workflowDatas.get(0);
		String metaWorkflowProcessInstanceId = workflowData.getMetaWorkflowProcessInstanceId();
		String businessWorkflowName = workflowData.getBusinessWorkflowName();

		BusinessWorkflow businessWorkflow = new BusinessWorkflow(hostSettings);
		businessWorkflow.setModelId(modelId);
		businessWorkflow.setName(businessWorkflowName);

		businessWorkflow.deploy();

		MetaWorkflow metaWorkflow = new MetaWorkflow(hostSettings, metaWorkflowProcessInstanceId);

		String businessWorkflowProcessDefinitionId = businessWorkflow.getProcessDefinitionId();
		metaWorkflow.updateVariable("businessWorkflowProcessDefinitionId", businessWorkflowProcessDefinitionId);
		workflowData.setBusinessWorkflowProcessDefinitionId(businessWorkflowProcessDefinitionId);
		workflowDataRepository.save(workflowData);

		JSONObject response = new JSONObject();
		response.put("businessWorkflowProcessDefinitionId", businessWorkflowProcessDefinitionId);

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	
	
	
	
	
	@RequestMapping(value = "/workflows/processinstances", method = RequestMethod.POST)
	public ResponseEntity<?> startWorkflowProcessInstance(
			@RequestBody PostStartProcessInstance startProcessInstanceBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			MetaWorkflowNotStartedException, StrategyDataException {
		String processDefinitionId = startProcessInstanceBody.getProcessDefinitionId();
		List<WorkflowData> workflowDatas = workflowDataRepository.findByBusinessWorkflowProcessDefinitionId(processDefinitionId);
		if (workflowDatas.size() != 1) {
			throw new StrategyDataException();
		}
		WorkflowData workflowData = workflowDatas.get(0);
		String businessWorkflowName = workflowData.getBusinessWorkflowName();

		BusinessWorkflow businessWorkflow = new BusinessWorkflow(hostSettings);
		businessWorkflow.setProcessDefinitionId(processDefinitionId);
		businessWorkflow.setName(businessWorkflowName);

		businessWorkflow.start();
		String metaWorkflowProcessInstanceId = workflowData.getMetaWorkflowProcessInstanceId();
		String businessWorkflowProcessInstanceId = businessWorkflow.getProcessInstanceId();
		MetaWorkflow metaWorkflow = new MetaWorkflow(hostSettings, metaWorkflowProcessInstanceId);
		metaWorkflow.updateVariable("businessWorkflowProcessInstanceId", businessWorkflowProcessInstanceId);
		workflowData.setBusinessWorkflowProcessInstanceId(businessWorkflowProcessInstanceId);
		workflowDataRepository.save(workflowData);

		JSONObject response = new JSONObject();
		response.put("businessWorkflowProcessInstanceId", businessWorkflow.getProcessInstanceId());

		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);

	}

}
