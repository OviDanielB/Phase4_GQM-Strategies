package it.uniroma2.isssr.model;


import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.dto.activiti.entity.Variable;
import it.uniroma2.isssr.dto.activiti.entitylist.ProcessDefinitionList;
import it.uniroma2.isssr.dto.post.PostProcessInstance;
import it.uniroma2.isssr.dto.response.ResponseDeployment;
import it.uniroma2.isssr.dto.response.ResponseProcessInstance;
import it.uniroma2.isssr.tools.JsonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Workflow {

	private String name;
	private String modelId;
	private String processDefinitionId;
	private String processInstanceId;

	protected HostSettings hostSettings;

	protected JsonRequest jsonRequest;

	public Workflow(HostSettings hostSettings, String name) {
		super();
		this.name = name;
		this.hostSettings = hostSettings;
		this.jsonRequest = new JsonRequest(hostSettings);
	}

	public Workflow(HostSettings hostSettings) {
		this.hostSettings = hostSettings;
		this.jsonRequest = new JsonRequest(hostSettings);
	}

	public void start() throws MetaWorkflowNotStartedException, JsonRequestException {

		PostProcessInstance processInstanceBody = buildStartRequestBody();

		ResponseEntity<ResponseProcessInstance> postProcessinstanceResponse = this.jsonRequest.post(
				hostSettings.getActivitiRestEndpointProcessInstances(), processInstanceBody,
				ResponseProcessInstance.class);

		if (postProcessinstanceResponse.getBody() != null) {
			setProcessInstanceId(postProcessinstanceResponse.getBody().getId());
		}
		if (getProcessInstanceId() == null || getProcessInstanceId().isEmpty()) {
			throw new MetaWorkflowNotStartedException(getProcessDefinitionId());
		} else {
			return;
		}
	}

	abstract protected PostProcessInstance buildStartRequestBody();

	public void deploy() throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			IOException, ProcessDefinitionNotFoundException {

		String metaworkflowString = buildWorkflowXmlString();

		ResponseEntity<ResponseDeployment> postDeploymentResponse = jsonRequest.postMetaWorkflowMultiPart(
				hostSettings.getActivitiRestEndpointDeployments(), ResponseDeployment.class, getName(),
				metaworkflowString);

		String deploymentId = postDeploymentResponse.getBody().getId();

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(hostSettings.getActivitiRestEndpointProcessDefinitions())
				.queryParam("deploymentId", deploymentId);

		ResponseEntity<ProcessDefinitionList> processDefinitionList = jsonRequest
				.get(builder.build().encode().toUri().toString(), ProcessDefinitionList.class);

		if (processDefinitionList.getBody().getData() != null
				&& processDefinitionList.getBody().getData().get(0) != null) {
			setProcessDefinitionId(processDefinitionList.getBody().getData().get(0).getId());
		} else {
			throw new ProcessDefinitionNotFoundException(deploymentId);
		}

		if (getProcessDefinitionId() == null || getProcessDefinitionId().isEmpty()) {
			throw new MetaWorkflowNotDeployedException(deploymentId);

		} else {

			return;
		}

	}

	abstract protected String buildWorkflowXmlString()
			throws JsonRequestException, ModelXmlNotFoundException, IOException;

	public void updateVariable(String key, String value) throws JsonRequestException {

		Variable modelIdVariable = new Variable();
		modelIdVariable.setName(key);
		modelIdVariable.setValue(value);

		ArrayList<Variable> updateVariableBody = new ArrayList<Variable>();
		updateVariableBody.add(modelIdVariable);

		String restAddress = hostSettings.getActivitiRestEndpointProcessInstances() + "/" + this.getProcessInstanceId()
				+ "/variables";

		jsonRequest.put(restAddress, updateVariableBody, String.class);

	}

	public void updateVariables(ArrayList<Variable> variables) throws JsonRequestException {

		String restAddress = hostSettings.getActivitiRestEndpointProcessInstances() + "/" + this.getProcessInstanceId()
				+ "/variables";

		jsonRequest.put(restAddress, variables, String.class);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
