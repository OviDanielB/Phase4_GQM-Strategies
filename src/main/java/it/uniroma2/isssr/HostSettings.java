package it.uniroma2.isssr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HostSettings {

	@Value("${host.address}")
	private String address;

	@Value("${host.port}")
	private String port;

	@Value("${host.activiti.address}")
	private String activitiAddress;

	@Value("${host.activiti.port}")
	private String activitiPort;

	@Value("${host.activiti.username}")
	private String activitiUsername;

	@Value("${host.activiti.password}")
	private String activitiPassword;

	@Value("${host.activiti.modeler.uri}")
	private String activitiModelerUri;

	@Value("${host.activiti.explorer.endpoint}")
	private String activitiExplorerEndpoint;

	@Value("${host.activiti.rest.endpoint.models}")
	private String activitiRestEndpointModels;

	@Value("${host.activiti.rest.endpoint.deployments}")
	private String activitiRestEndpointDeployments;

	@Value("${host.activiti.rest.endpoint.processdefinitions}")
	private String activitiRestEndpointProcessDefinitions;

	@Value("${host.activiti.rest.endpoint.processistances.model.suffix}")
	private String activitiRestEndpointProcessDefinitionsModelSuffix;

	@Value("${host.activiti.rest.endpoint.processistances.image.suffix}")
	private String activitiRestEndpointProcessDefinitionsImageSuffix;

	@Value("${host.activiti.rest.endpoint.processistances}")
	private String activitiRestEndpointProcessInstances;

	@Value("${host.activiti.rest.endpoint.executions}")
	private String activitiRestEndpointExecutions;

	@Value("${host.activiti.rest.endpoint.query.executions}")
	private String activitiRestEndpointQueryExecutions;

	@Value("${host.activiti.rest.endpoint.tasks}")
	private String activitiRestEndpointTasks;

	@Value("${host.activiti.rest.endpoint.tasks.variables.suffix}")
	private String activitiRestEndpointTasksVariablesSuffix;

	@Value("${host.activiti.rest.endpoint.query.tasks}")
	private String activitiRestEndpointQueryTasks;

	@Value("${host.activiti.rest.endpoint.history.historictaskinstances}")
	private String activitiRestEndpointHistoryHistoricTaskInstances;

	@Value("${host.activiti.rest.endpoint.identity.groups}")
	private String activitiRestEndpointIdentityGroups;

	@Value("${host.activiti.rest.endpoint.identity.users}")
	private String activitiRestEndpointIdentityUsers;

	@Value("${host.activiti.rest.endpoint}")
	private String activitiRestEndpoint;

	@Value("${host.activiti.task.variable.state}")
	private String activitiTaskVariableState;

	@Value("${host.activiti.task.variable.responsible}")
	private String activitiTaskVariableResponsible;

	@Value("${host.activiti.task.variable.assignee}")
	private String activitiTaskVariableAssignee;

	@Value("${host.metaworkflow.prefix}")
	private String metaworkflowPrefix;

	@Value("${host.metaworkflow.suffix}")
	private String metaworkflowSuffix;

	@Value("${host.metaworkflow.path}")
	private String metaworkflowPath;

	@Value("${host.metaworkflow.processidentifiername}")
	private String metaworkflowProcessIdentifierName;

	@Value("${host.metaworkflow.name}")
	private String metaworkflowName;

	@Value("${host.debug}")
	private boolean debug;

	@Value("${host.mongodb.username}")
	private String mongodbUsername;

	@Value("${host.mongodb.password}")
	private String mongodbPassword;

	@Value("${host.mongodb.database}")
	private String mongodbDatabase;

	@Value("${host.mongodb.host}")
	private String mongodbHost;

	@Value("${host.mongodb.port}")
	private Integer mongodbPort;

	@Value("${host.webapp.url}")
	private String webappUrl;

	@Value("${host.webapp.endpoint.deploy}")
	private String deployEndpoint;

	@Value("${host.webapp.endpoint.start}")
	private String startEndpoint;

	@Value("${host.webapp.endpoint.export}")
	private String exportEndpoint;

	@Value("${host.webapp.endpoint.measure-planning}")
	private String measurePlanningEndpoint;

	@Value("${host.webapp.endpoint.validation-planning}")
	private String validationPlanningEndpoint;

	@Value("${host.webapp.endpoint.workflowData-plans-uri}")
	private String workflowDataPlansUri;

	@Value("${host.bus}")
	private Boolean bus;

	@Value("${host.bus.address}")
	private String busAddress;

	public String getWebappUrl() {
		return webappUrl;
	}

	public String getDeployEndpoint() {
		return deployEndpoint;
	}

	public String getStartEndpoint() {
		return startEndpoint;
	}

	public String getExportEndpoint() {
		return exportEndpoint;
	}

	public String getMeasurePlanningEndpoint() {
		return measurePlanningEndpoint;
	}

	public String getValidationPlanningEndpoint() {
		return validationPlanningEndpoint;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getAddress() {
		return address;
	}

	public String getPort() {
		return port;
	}

	public String getConnectionUrl() {
		return address + ":" + port;
	}

	public String getActivitiRestConnectionUrl() {
		return activitiAddress + ":" + activitiPort + activitiRestEndpoint;
	}

	public String getActivitiExplorerConnectionUrl() {
		return activitiAddress + ":" + activitiPort + activitiExplorerEndpoint;
	}

	public String getActivitiExplorerEndpoint() {
		return activitiExplorerEndpoint;
	}

	public String getActivitiRestEndpoint() {
		return activitiRestEndpoint;
	}

	public String getActivitiRestEndpointModels() {
		return activitiRestEndpointModels;
	}

	public String getActivitiRestEndpointDeployments() {
		return activitiRestEndpointDeployments;
	}

	public String getActivitiRestEndpointProcessDefinitions() {
		return activitiRestEndpointProcessDefinitions;
	}

	public String getActivitiRestEndpointProcessInstances() {
		return activitiRestEndpointProcessInstances;
	}

	public String getActivitiRestEndpointQueryExecutions() {
		return activitiRestEndpointQueryExecutions;
	}

	public String getActivitiRestEndpointExecutions() {
		return activitiRestEndpointExecutions;
	}

	public String getMetaworkflowPrefix() {
		return metaworkflowPrefix;
	}

	public String getMetaworkflowSuffix() {
		return metaworkflowSuffix;
	}

	public String getMetaworkflowPath() {
		return metaworkflowPath;
	}

	public String getMetaworkflowProcessIdentifierName() {
		return metaworkflowProcessIdentifierName;
	}

	public String getMetaworkflowName() {
		return metaworkflowName;
	}

	public String getActivitiUsername() {
		return activitiUsername;
	}

	public String getActivitiPassword() {
		return activitiPassword;
	}

	public String getMongodbUsername() {
		return mongodbUsername;
	}

	public String getMongodbPassword() {
		return mongodbPassword;
	}

	public String getMongodbDatabase() {
		return mongodbDatabase;
	}

	public String getMongodbHost() {
		return mongodbHost;
	}

	public Integer getMongodbPort() {
		return mongodbPort;
	}

	public String getStrategyPlansUri() {
		return workflowDataPlansUri;
	}

	public String getActivitiModelerUri() {
		return activitiModelerUri;
	}

	public String getActivitiAddress() {
		return activitiAddress;
	}

	public String getActivitiPort() {
		return activitiPort;
	}

	public String getActivitiRestEndpointProcessDefinitionsModelSuffix() {
		return activitiRestEndpointProcessDefinitionsModelSuffix;
	}

	public String getActivitiRestEndpointProcessDefinitionsImageSuffix() {
		return activitiRestEndpointProcessDefinitionsImageSuffix;
	}

	public String getActivitiRestEndpointTasks() {
		return activitiRestEndpointTasks;
	}

	public String getActivitiRestEndpointTasksVariablesSuffix() {
		return activitiRestEndpointTasksVariablesSuffix;
	}

	public String getActivitiRestEndpointQueryTasks() {
		return activitiRestEndpointQueryTasks;
	}

	public String getActivitiRestEndpointHistoryHistoricTaskInstances() {
		return activitiRestEndpointHistoryHistoricTaskInstances;
	}

	public String getActivitiRestEndpointIdentityGroups() {
		return activitiRestEndpointIdentityGroups;
	}

	public String getActivitiRestEndpointIdentityUsers() {
		return activitiRestEndpointIdentityUsers;
	}

	public Boolean getBus() {
		return bus;
	}

	public String getActivitiTaskVariableState() {
		return activitiTaskVariableState;
	}

	public String getActivitiTaskVariableResponsible() {
		return activitiTaskVariableResponsible;
	}

	public String getActivitiTaskVariableAssignee() {
		return activitiTaskVariableAssignee;
	}

	public Boolean isBus() {
		return bus;
	}

	public String getBusAddress() {
		return busAddress;
	}

}