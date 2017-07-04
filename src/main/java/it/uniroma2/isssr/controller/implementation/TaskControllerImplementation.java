package it.uniroma2.isssr.controller.implementation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.Exception.BackEnd3242Exception;
import it.uniroma2.isssr.Exception.JsonRequestConflictException;
import it.uniroma2.isssr.Exception.JsonRequestException;
import it.uniroma2.isssr.controller.TaskController;
import it.uniroma2.isssr.dto.ErrorResponse;
import it.uniroma2.isssr.dto.activiti.entity.*;
import it.uniroma2.isssr.dto.activiti.entitylist.GroupActivitiList;
import it.uniroma2.isssr.dto.activiti.entitylist.TaskList;
import it.uniroma2.isssr.dto.post.PostClaimTask;
import it.uniroma2.isssr.dto.post.PostQueryTask;
import it.uniroma2.isssr.dto.response.ResponseDescription;
import it.uniroma2.isssr.repository.WorkflowDataRepository;
import it.uniroma2.isssr.tools.Costants;
import it.uniroma2.isssr.tools.JsonRequestActiviti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@Api(value = "Task", description = "Task API")
public class TaskControllerImplementation implements TaskController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TaskControllerImplementation.class);

	@Autowired
	private HostSettings hostSettings;

	@Autowired
	private WorkflowDataRepository workflowDataRepository;

	@Autowired
    RestTemplate restTemplate;

	private static final String ACTION_CLAIM = "claim";

	@RequestMapping(value = "/tasks/unassigned", method = RequestMethod.GET)
	@ApiOperation(value = "Get all unassigned tasks", notes = "This endpoint returns all unassigned tasks of groups of a user(assignee).")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<TaskList> getUnassignedTasks(@RequestParam(value = "assignee") String assignee)
			throws JsonRequestException, JsonRequestConflictException, UnsupportedEncodingException {

		JsonRequestActiviti jsonRequest = new JsonRequestActiviti(hostSettings);

		TaskList taskList = new TaskList();

		// Get all tasks unassigned to assignee
		PostQueryTask postQueryTask = new PostQueryTask();
		postQueryTask.setUnassigned(true);
		postQueryTask.setCandidateUser(assignee);
		postQueryTask.setIncludeTaskLocalVariables(true);

		ResponseEntity<TaskList> tasks = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(),
				postQueryTask, TaskList.class);

		// Filter result: get all tasks without state for every group
		for (Iterator<Task> it = tasks.getBody().getData().iterator(); it.hasNext();) {
			Task task = it.next();
			if (!task.getVariables().isEmpty())
				for (Variable variable : task.getVariables())
					if (variable.getName().equals(hostSettings.getActivitiTaskVariableState()))
						it.remove();
		}

		taskList.getData().addAll(tasks.getBody().getData());

		// Get all groups for assignee
		Map<String, String> queryParams = new LinkedHashMap<String, String>();
		queryParams.put("member", assignee);

		@SuppressWarnings("unchecked")
		List<GroupActiviti> groupActivitiList = (List<GroupActiviti>) jsonRequest
				.getList(hostSettings.getActivitiRestEndpointIdentityGroups(), GroupActivitiList.class, queryParams);

		// Get all tasks unassigned that have responsible all group that
		// assignee is a member and state 1 (to measure)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);

		List<TaskVariable> taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_ONE,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), "",
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		ResponseEntity<TaskList> taskListEntity = null;

		// for every group of this assignee return tasks unassigned
		for (GroupActiviti groupActiviti : groupActivitiList) {

			taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableResponsible(),
					groupActiviti.getName(), Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
			postQueryTask.setTaskVariables(taskVariables);

			taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(), postQueryTask,
					TaskList.class);

			// change documentation tasks to measure
			for (Task task : taskListEntity.getBody().getData()) {
				task.setDescription(Costants.MEASURE_TASK_DOCUMENTATION_1 + task.getName()
						+ Costants.MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.DATA_COLLECTOR_PAGE + task.getTaskDefinitionKey() + Costants.DATA_COLLECTOR_PAGE_1
						+ workflowDataRepository.findByBusinessWorkflowProcessInstanceId(task.getProcessInstanceId())
								.get(0).getBusinessWorkflowName()
						+ Costants.DATA_COLLECTOR_PAGE_2 + task.getId() + Costants.MEASURE_TASK_DOCUMENTATION_3);
			}

			taskList.getData().addAll(taskListEntity.getBody().getData());

			taskVariables.remove(2);

		}

		// Get all tasks unassigned that have responsible all group that
		// assignee is a member and state 2 (to validate)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);
		taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_TWO,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), "",
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		taskListEntity = null;

		// for every group of this assignee return tasks unassigned
		for (GroupActiviti groupActiviti : groupActivitiList) {

			taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableResponsible(),
					groupActiviti.getName(), Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
			postQueryTask.setTaskVariables(taskVariables);

			taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(), postQueryTask,
					TaskList.class);

			// Change documentation validation tasks
			for (Task task : taskListEntity.getBody().getData()) {
				task.setDescription(Costants.VALIDATION_TASK_DOCUMENTATION_1 + task.getName()
						+ Costants.VALIDATION_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.VALIDATOR_PAGE + task.getTaskDefinitionKey() + Costants.VALIDATOR_PAGE_1
						+ task.getId() + Costants.VALIDATION_TASK_DOCUMENTATION_3);
			}

			taskList.getData().addAll(taskListEntity.getBody().getData());

			taskVariables.remove(2);

		}

		// Get all tasks unassigned that have responsible all group that
		// assignee is a member and state 3 (to validate)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);
		taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_THREE,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), "",
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		taskListEntity = null;

		// for every group of this assignee return tasks unassigned
		for (GroupActiviti groupActiviti : groupActivitiList) {

			taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableResponsible(),
					groupActiviti.getName(), Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
			postQueryTask.setTaskVariables(taskVariables);

			taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(), postQueryTask,
					TaskList.class);

			ResponseEntity<Variable> errorMessage = null;
			ResponseEntity<Variable> collectedDataId = null;
			// change documentation tasks to repeat
			for (Task task : taskListEntity.getBody().getData()) {

				// get errorMessage and collectedDataId
				errorMessage = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + task.getId()
						+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
						+ hostSettings.getActivitiTaskVariableErrormessage(), Variable.class);

				collectedDataId = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + task.getId()
						+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
						+ hostSettings.getActivitiTaskVariableIdCollectedData(), Variable.class);

				task.setDescription(Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_1 + task.getName()
						+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.REPEAT_MEASURE_TASK_PAGE + task.getTaskDefinitionKey()
						+ Costants.REPEAT_MEASURE_TASK_PAGE_1
						+ workflowDataRepository.findByBusinessWorkflowProcessInstanceId(task.getProcessInstanceId())
								.get(0).getBusinessWorkflowName()
						+ Costants.REPEAT_MEASURE_TASK_PAGE_2
						+ URLEncoder.encode(errorMessage.getBody().getValue(), "UTF-8")
						+ Costants.REPEAT_MEASURE_TASK_PAGE_3 + collectedDataId.getBody().getValue()
						+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_3);

			}

			taskList.getData().addAll(taskListEntity.getBody().getData());

			taskVariables.remove(2);

		}

		return new ResponseEntity<TaskList>(taskList, HttpStatus.OK);

	}

	@RequestMapping(value = "/tasks/assigned", method = RequestMethod.GET)
	@ApiOperation(value = "Get all assigned tasks", notes = "This endpoint returns all assigned tasks a user(assignee).")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<TaskList> getAssignedTasks(@RequestParam(value = "assignee") String assignee)
			throws JsonRequestException, JsonRequestConflictException, UnsupportedEncodingException {

		JsonRequestActiviti jsonRequest = new JsonRequestActiviti(hostSettings);

		TaskList taskList = new TaskList();

		// Get all tasks assigned to assignee
		PostQueryTask postQueryTask = new PostQueryTask();
		postQueryTask.setAssignee(assignee);
		postQueryTask.setIncludeTaskLocalVariables(true);

		ResponseEntity<TaskList> tasks = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(),
				postQueryTask, TaskList.class);

		// Filter result: get all tasks without state for every group
		for (Iterator<Task> it = tasks.getBody().getData().iterator(); it.hasNext();) {
			Task task = it.next();
			if (!task.getVariables().isEmpty())
				for (Variable variable : task.getVariables())
					if (variable.getName().equals(hostSettings.getActivitiTaskVariableState()))
						it.remove();
		}

		taskList.getData().addAll(tasks.getBody().getData());

		// Get all tasks assigned that have responsible all group that assignee
		// is a member and state 1 (to measure)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);
		List<TaskVariable> taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_ONE,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), assignee,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		postQueryTask.setTaskVariables(taskVariables);

		ResponseEntity<TaskList> taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(),
				postQueryTask, TaskList.class);

		// change documentation tasks to measure
		for (Task task : taskListEntity.getBody().getData()) {
			task.setDescription(Costants.MEASURE_TASK_DOCUMENTATION_1 + task.getName()
					+ Costants.MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
					+ Costants.DATA_COLLECTOR_PAGE + task.getTaskDefinitionKey() + Costants.DATA_COLLECTOR_PAGE_1
					+ workflowDataRepository.findByBusinessWorkflowProcessInstanceId(task.getProcessInstanceId()).get(0)
							.getBusinessWorkflowName()
					+ Costants.DATA_COLLECTOR_PAGE_2 + task.getId() + Costants.MEASURE_TASK_DOCUMENTATION_3);
		}

		taskList.getData().addAll(taskListEntity.getBody().getData());

		// Get all tasks assigned that have responsible all group that assignee
		// is a member and state 2 (to validate)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);
		taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_TWO,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), assignee,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		postQueryTask.setTaskVariables(taskVariables);

		taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(), postQueryTask,
				TaskList.class);

		// Change documentation validation tasks
		for (Task task : taskListEntity.getBody().getData()) {
			task.setDescription(Costants.VALIDATION_TASK_DOCUMENTATION_1 + task.getName()
					+ Costants.VALIDATION_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
					+ Costants.VALIDATOR_PAGE + task.getId() + Costants.VALIDATOR_PAGE_1 + task.getTaskDefinitionKey()
					+ Costants.VALIDATION_TASK_DOCUMENTATION_3);
		}

		taskList.getData().addAll(taskListEntity.getBody().getData());

		// Get all tasks assigned that have responsible all group that assignee
		// is a member and state 3 (to repeate)
		postQueryTask = new PostQueryTask();
		postQueryTask.setIncludeTaskLocalVariables(true);
		taskVariables = new ArrayList<TaskVariable>();
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableState(), Costants.STATE_THREE,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));
		taskVariables.add(new TaskVariable(hostSettings.getActivitiTaskVariableAssignee(), assignee,
				Costants.OPERATION_EQUALS, Costants.STRING_TYPE));

		postQueryTask.setTaskVariables(taskVariables);

		taskListEntity = jsonRequest.post(hostSettings.getActivitiRestEndpointQueryTasks(), postQueryTask,
				TaskList.class);

		// change documentation tasks to repeat
		ResponseEntity<Variable> errorMessage = null;
		ResponseEntity<Variable> collectedDataId = null;
		// change documentation tasks to repeat
		for (Task task : taskListEntity.getBody().getData()) {

			// get all variable for a task
			errorMessage = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + task.getId()
					+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableErrormessage(), Variable.class);

			collectedDataId = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + task.getId()
					+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableIdCollectedData(), Variable.class);

			task.setDescription(Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_1 + task.getName()
					+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
					+ Costants.REPEAT_MEASURE_TASK_PAGE + task.getTaskDefinitionKey()
					+ Costants.REPEAT_MEASURE_TASK_PAGE_1
					+ workflowDataRepository.findByBusinessWorkflowProcessInstanceId(task.getProcessInstanceId()).get(0)
							.getBusinessWorkflowName()
					+ Costants.REPEAT_MEASURE_TASK_PAGE_2
					+ URLEncoder.encode(errorMessage.getBody().getValue(), "UTF-8")
					+ Costants.REPEAT_MEASURE_TASK_PAGE_3 + collectedDataId.getBody().getValue()
					+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_3);

		}

		taskList.getData().addAll(taskListEntity.getBody().getData());

		return new ResponseEntity<TaskList>(taskList, HttpStatus.OK);

	}

	@RequestMapping(value = "/tasks/{taskId}/claim", method = RequestMethod.POST)
	@ApiOperation(value = "Claim a task", notes = "This endpoint claims a task for a user(assignee).")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> claimTask(@PathVariable(value = "taskId") String taskId,
                                       @RequestBody PostClaimTask postClaimTask) throws JsonRequestException, JsonRequestConflictException {

		String assignee = postClaimTask.getAssignee();

		JsonRequestActiviti jsonRequest = new JsonRequestActiviti(hostSettings);

		try {

			// Variable can be not found so catch exception
			ResponseEntity<Variable> variable = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/"
					+ taskId + hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableState(), Variable.class);

			// if exception hasn't been raised
			variable = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
					+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableAssignee(), Variable.class);

			variable.getBody().setValue(assignee);

			variable = jsonRequest.put(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
					+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableAssignee(), variable.getBody(), Variable.class);

			return ResponseEntity.status(variable.getStatusCode()).body(variable.getBody());
		} catch (HttpClientErrorException e) {

			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				TaskAction taskAction = new TaskAction();

				taskAction.setAction(ACTION_CLAIM);
				taskAction.setAssignee(assignee);

				return jsonRequest.post(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId, taskAction,
						String.class);
			} else
				throw e;
		}

	}

	@RequestMapping(value = "/tasks/{taskId}/complete", method = RequestMethod.POST)
	@ApiOperation(value = "Complete a task", notes = "This endpoint completes a task for a user(assignee).")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> completeTask(@PathVariable(value = "taskId") String taskId,
                                          @RequestBody List<ActivitiFormVariableProperty> variables)
			throws JsonRequestException, JsonRequestConflictException, URISyntaxException, BackEnd3242Exception {

		JsonRequestActiviti jsonRequest = new JsonRequestActiviti(hostSettings);

		try {

			ResponseEntity<Variable> variable = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/"
					+ taskId + hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableState(), Variable.class);

			if (variable.getStatusCode().equals(HttpStatus.OK)) {

				// DEBUG
				// Retrive all task variables from task
				@SuppressWarnings("unchecked")
                ResponseEntity<List<Variable>> variableListResponse = (ResponseEntity<List<Variable>>) jsonRequest.get(
						hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
								+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix(),
						new ParameterizedTypeReference<List<Variable>>() {
						});

				List<Variable> variableList = variableListResponse.getBody();
				// if state == 1 or state == 3 change status to 2, responsible
				// in validator, assignee empty. In case the state is 3 delete
				// variables error message and idCollectedData
				if (variable.getBody().getValue().equals("1") || variable.getBody().getValue().equals("3")) {

					for (Variable var : variableList) {
						// Update state
						if (var.getName().equals(hostSettings.getActivitiTaskVariableState())) {

							var.setValue(Costants.STATE_TWO);

							variable = jsonRequest.put(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
									+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
									+ hostSettings.getActivitiTaskVariableState(), var, Variable.class);

							if (!variable.getStatusCode().equals(HttpStatus.OK))
								return ResponseEntity.status(variable.getStatusCode()).body(variable.getBody());
						}
						// Update responsible
						if (var.getName().equals(hostSettings.getActivitiTaskVariableResponsible())) {

							var.setValue(Costants.VALIDATOR);
							variable = jsonRequest.put(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
									+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
									+ hostSettings.getActivitiTaskVariableResponsible(), var, Variable.class);

							if (!variable.getStatusCode().equals(HttpStatus.OK))
								return ResponseEntity.status(variable.getStatusCode()).body(variable.getBody());

						}
						// Update assignee
						if (var.getName().equals(hostSettings.getActivitiTaskVariableAssignee())) {

							var.setValue("");
							variable = jsonRequest.put(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
									+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
									+ hostSettings.getActivitiTaskVariableAssignee(), var, Variable.class);

							if (!variable.getStatusCode().equals(HttpStatus.OK))
								return ResponseEntity.status(variable.getStatusCode()).body(variable.getBody());
						}
						// Delete Error Message
						if (var.getName().equals(hostSettings.getActivitiTaskVariableErrormessage())) {
							jsonRequest.delete(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
									+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
									+ hostSettings.getActivitiTaskVariableErrormessage());
						}
						// Delete idCollectedData
						if (var.getName().equals(hostSettings.getActivitiTaskVariableIdCollectedData())) {
							jsonRequest.delete(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
									+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
									+ hostSettings.getActivitiTaskVariableIdCollectedData());
						}
					}

				} else if (variable.getBody().getValue().equals("2")) {

					@SuppressWarnings("rawtypes")
                    RequestEntity request = RequestEntity
							.post(new URI(hostSettings.getWebapp3242EndpointTestBackend()
									+ hostSettings.getWebapp3242EndpointCompleteValidationTask() + taskId))
							.body(variables);
					ResponseEntity<String> response = restTemplate.exchange(request, String.class);

					if (response.getStatusCode().equals(HttpStatus.OK)
							|| response.getStatusCode().equals(HttpStatus.CREATED))
						return ResponseEntity.status(HttpStatus.OK).body("Task completed successfully!");
					else
						throw new BackEnd3242Exception();
				}
			} else
				return ResponseEntity.status(variable.getStatusCode()).body(variable.getBody());
		} catch (HttpClientErrorException e) {

			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				// Call endpoint backend 3.2 4.2 to complete or change status of
				// a
				// task that requires measure

				@SuppressWarnings("rawtypes")
                RequestEntity request = RequestEntity.post(new URI(hostSettings.getWebapp3242EndpointTestBackend()
						+ hostSettings.getWebapp3242EndpointCompleteTask() + taskId)).body(variables);
				ResponseEntity<String> response = restTemplate.exchange(request, String.class);

				if (response.getStatusCode().equals(HttpStatus.OK)
						|| response.getStatusCode().equals(HttpStatus.CREATED))
					return ResponseEntity.status(HttpStatus.OK).body("Task completed successfully!");
				else
					throw new BackEnd3242Exception();

			} else
				throw e;
		}
		return null;
	}

	@RequestMapping(value = "/tasks/{taskId}/description", method = RequestMethod.GET)
	@ApiOperation(value = "Get all assigned tasks", notes = "This endpoint returns all assigned tasks a user(assignee).")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<ResponseDescription> getDescriptionTask(@PathVariable(value = "taskId") String taskId)
			throws JsonRequestException, UnsupportedEncodingException {

		JsonRequestActiviti jsonRequest = new JsonRequestActiviti(hostSettings);

		ResponseEntity<Task> task = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId,
				Task.class);

		try {

			// Variable can be not found so catch exception
			ResponseEntity<Variable> variable = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/"
					+ taskId + hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
					+ hostSettings.getActivitiTaskVariableState(), Variable.class);

			String description = "";

			if (variable.getBody().getValue().equals(Costants.STATE_ONE)) {

				description = Costants.MEASURE_TASK_DOCUMENTATION_1 + task.getBody().getName()
						+ Costants.MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.DATA_COLLECTOR_PAGE + task.getBody().getTaskDefinitionKey()
						+ Costants.DATA_COLLECTOR_PAGE_1
						+ workflowDataRepository
								.findByBusinessWorkflowProcessInstanceId(task.getBody().getProcessInstanceId()).get(0)
								.getBusinessWorkflowName()
						+ Costants.DATA_COLLECTOR_PAGE_2 + taskId + Costants.MEASURE_TASK_DOCUMENTATION_3;

			} else if (variable.getBody().getValue().equals(Costants.STATE_TWO)) {

				description = Costants.VALIDATION_TASK_DOCUMENTATION_1 + task.getBody().getName()
						+ Costants.VALIDATION_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.VALIDATOR_PAGE + taskId + Costants.VALIDATOR_PAGE_1
						+ task.getBody().getTaskDefinitionKey() + Costants.VALIDATION_TASK_DOCUMENTATION_3;

			} else if (variable.getBody().getValue().equals(Costants.STATE_THREE)) {

				ResponseEntity<Variable> errorMessage = null;
				ResponseEntity<Variable> collectedDataId = null;

				// get errorMessage and collectedDataId
				errorMessage = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
						+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
						+ hostSettings.getActivitiTaskVariableErrormessage(), Variable.class);

				collectedDataId = jsonRequest.get(hostSettings.getActivitiRestEndpointTasks() + "/" + taskId
						+ hostSettings.getActivitiRestEndpointTasksVariablesSuffix()
						+ hostSettings.getActivitiTaskVariableIdCollectedData(), Variable.class);

				description = Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_1 + task.getBody().getName()
						+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_2 + hostSettings.getWebappFrontendUri()
						+ Costants.REPEAT_MEASURE_TASK_PAGE + task.getBody().getTaskDefinitionKey()
						+ Costants.REPEAT_MEASURE_TASK_PAGE_1
						+ workflowDataRepository
								.findByBusinessWorkflowProcessInstanceId(task.getBody().getProcessInstanceId()).get(0)
								.getBusinessWorkflowName()
						+ Costants.REPEAT_MEASURE_TASK_PAGE_2
						+ URLEncoder.encode(errorMessage.getBody().getValue(), "UTF-8")
						+ Costants.REPEAT_MEASURE_TASK_PAGE_3 + collectedDataId.getBody().getValue()
						+ Costants.REPEAT_MEASURE_TASK_DOCUMENTATION_3;
			}

			ResponseDescription des = new ResponseDescription();
			if (description.equals(""))
				des.setDescription(task.getBody().getDescription());
			else
				des.setDescription(description);

			return new ResponseEntity<ResponseDescription>(des, HttpStatus.OK);

		} catch (HttpClientErrorException e) {

			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {

				ResponseDescription des = new ResponseDescription();
				des.setDescription(task.getBody().getDescription());

				return new ResponseEntity<ResponseDescription>(des, HttpStatus.OK);

			} else
				throw e;
		}
	}
}
