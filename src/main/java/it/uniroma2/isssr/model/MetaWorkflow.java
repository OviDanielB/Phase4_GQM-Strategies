package it.uniroma2.isssr.model;


import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.Exception.ActivitiEntityAlreadyExistsException;
import it.uniroma2.isssr.Exception.JsonRequestException;
import it.uniroma2.isssr.Exception.ModelXmlNotFoundException;
import it.uniroma2.isssr.dto.activiti.entity.Variable;
import it.uniroma2.isssr.dto.activiti.entitylist.ActivitiEntityList;
import it.uniroma2.isssr.dto.activiti.entitylist.DeploymentList;
import it.uniroma2.isssr.dto.activiti.entitylist.ProcessDefinitionList;
import it.uniroma2.isssr.dto.post.PostProcessInstance;
import it.uniroma2.isssr.tools.JsonRequest;
import it.uniroma2.isssr.tools.XmlTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaWorkflow extends Workflow {

	private String businessWorkflowName;

	private static final String MODEL_XML_EXT = ".bpmn20.xml";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public MetaWorkflow(HostSettings hostSettings, String name, String businessWorkflowName) {
		super(hostSettings, name);
		this.businessWorkflowName = businessWorkflowName;
	}

	public MetaWorkflow(HostSettings hostSettings, String metaWorkflowProcessInstanceId) {
		super(hostSettings);
		super.setProcessInstanceId(metaWorkflowProcessInstanceId);
	}

	@Override
	protected PostProcessInstance buildStartRequestBody() {
		PostProcessInstance processInstanceBody = new PostProcessInstance();
		processInstanceBody.setProcessDefinitionId(super.getProcessDefinitionId());
		processInstanceBody.setVariables(initializeMetaWorkflowVariables());
		return processInstanceBody;
	}

	@Override
	protected String buildWorkflowXmlString() throws ModelXmlNotFoundException, IOException {

		String metaworkflowString;
		try {
			log.info(hostSettings.getMetaworkflowPath(), hostSettings.getMetaworkflowProcessIdentifierName(),
					hostSettings.getMetaworkflowName());
			String n = super.getName();
			metaworkflowString = XmlTools.workflowModelXmlToString(hostSettings.getMetaworkflowPath(),
					hostSettings.getMetaworkflowProcessIdentifierName(), hostSettings.getMetaworkflowName(), n, n);
		} catch (FileNotFoundException e) {
			throw new ModelXmlNotFoundException();
		}
		return metaworkflowString;
	}

	/**
	 * Initialize variables of MetaWorkflow Process-instance.
	 * 
	 * @return List of variables initialized.
	 */
	private ArrayList<Variable> initializeMetaWorkflowVariables() {

		ArrayList<Variable> variables = new ArrayList<>();

		ArrayList<String> variablesName = new ArrayList<>(Arrays.asList("businessWorkflowModelId",
				"businessWorkflowProcessDefinitionId", "businessWorkflowProcessInstanceId", "deployEndpoint",
				"webappUrl", "startEndpoint", "issueMessage", "exportEndpoint", "measurePlanningEndpoint",
				"validationPlanningEndpoint", "strategicPlannerNote", "strategicPlannerReport", "workflowDeveloperNote",
				"workflowDeveloperReport", "measurementPlannerNote", "measurementPlannerReport",
				"validationPlannerNote", "validationPlannerReport", "gqmExpertNote", "gqmExpertReport"));
		ArrayList<String> addressVariablesName = new ArrayList<>(Arrays.asList("webappUrl", "deployEndpoint",
				"startEndpoint", "exportEndpoint", "measurePlanningEndpoint", "validationPlanningEndpoint",
				"workflowDataPlansUri", "activitiExplorerUri", "activitiModelerUri"));
		ArrayList<String> addressVariablesValue = new ArrayList<>(Arrays.asList(hostSettings.getWebappUrl(),
				hostSettings.getDeployEndpoint(), hostSettings.getStartEndpoint(), hostSettings.getExportEndpoint(),
				hostSettings.getMeasurePlanningEndpoint(), hostSettings.getValidationPlanningEndpoint(),
				hostSettings.getStrategyPlansUri(), hostSettings.getActivitiExplorerConnectionUrl(),
				hostSettings.getActivitiModelerUri()));

		Variable workflowNameVariable = new Variable();
		workflowNameVariable.setName("workflowName");
		workflowNameVariable.setValue(getBusinessWorkflowName());
		variables.add(workflowNameVariable);

		for (String variableName : variablesName) {

			Variable variable = new Variable();
			variable.setName(variableName);
			variable.setValue("");
			variables.add(variable);
		}
		for (int i = 0; i < addressVariablesName.size(); i++) {

			Variable variable = new Variable();
			variable.setName(addressVariablesName.get(i));
			variable.setValue(addressVariablesValue.get(i));
			variables.add(variable);
		}

		return variables;
	}

	public static void checkAlreadyExists(HostSettings hostSettings, String name)
			throws ActivitiEntityAlreadyExistsException, JsonRequestException {

		String metaWorkflowName = name + MODEL_XML_EXT;

		List<String> restAddresses = Arrays.asList(hostSettings.getActivitiRestEndpointDeployments(),
				hostSettings.getActivitiRestEndpointProcessDefinitions());

		List<Class<? extends ActivitiEntityList>> entitylists = Arrays.asList(DeploymentList.class,
				ProcessDefinitionList.class);

		JsonRequest jsonRequest = new JsonRequest(hostSettings);

		for (int i = 0; i < restAddresses.size(); i++) {

			String restAddress = restAddresses.get(i);
			Class<? extends ActivitiEntityList> T = entitylists.get(i);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(restAddress).queryParam("name",
					metaWorkflowName);
			ResponseEntity<? extends ActivitiEntityList> entityList = jsonRequest
					.get(builder.build().encode().toUri().toString(), T);

			if (entityList.getBody().getData() != null && !entityList.getBody().getData().isEmpty()) {
				throw new ActivitiEntityAlreadyExistsException(metaWorkflowName, T);
			}
		}
	}

	public String getBusinessWorkflowName() {
		return businessWorkflowName;
	}

	public void setBusinessWorkflowName(String businessWorkflowName) {
		this.businessWorkflowName = businessWorkflowName;
	}

}
