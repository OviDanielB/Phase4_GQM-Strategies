package it.uniroma2.isssr.controller.phase41.implementation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.exception.CollectedDataNotValidException;
import it.uniroma2.isssr.exception.JsonRequestException;
import it.uniroma2.isssr.exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.exception.TaskNotFoundException;
import it.uniroma2.isssr.controller.phase41.CollectMeasurementDataController;
import it.uniroma2.isssr.dto.ErrorResponse;
import it.uniroma2.isssr.dto.activiti.entity.Task;
import it.uniroma2.isssr.model.phase41.CollectedData;
import it.uniroma2.isssr.model.phase41.MeasureTask;
import it.uniroma2.isssr.model.phase41.Metric;
import it.uniroma2.isssr.model.phase41.WorkflowData;
import it.uniroma2.isssr.repositories.phase41.CollectedDataRepository;
import it.uniroma2.isssr.repositories.phase41.MeasureTaskRepository;
import it.uniroma2.isssr.repositories.phase41.WorkflowDataRepository;
import it.uniroma2.isssr.utils.phase41.BasicValidation;
import it.uniroma2.isssr.utils.phase41.JsonRequestActiviti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Collect Measurement Data", description = "Collect Measurement Data API")
public class CollectMeasurementDataControllerImplementation implements CollectMeasurementDataController {

	@Autowired
	private HostSettings hostSettings;

	@Autowired
	private CollectedDataRepository collectedDataRepository;

	@Autowired
	private MeasureTaskRepository measureTaskRespository;

	@Autowired
	private WorkflowDataRepository workflowDataRepository;

	@RequestMapping(value = "/measurement-collection", method = RequestMethod.GET)
	@ApiOperation(value = "Get a measurement data collect", notes = "This endpoint returns a measure task that needs to measure.")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> getCollectMeasurementData(@RequestParam(value = "taskId", required = true) String taskId)
			throws MeasureTaskNotFoundException {

		List<MeasureTask> measureTasks = measureTaskRespository.findByTaskId(taskId);
		MeasureTask measureTask = measureTasks.get(0);

		if (measureTask != null)
			return new ResponseEntity<MeasureTask>(measureTask, HttpStatus.OK);
		else
			throw new MeasureTaskNotFoundException(taskId);
	}

	@RequestMapping(value = "/measurement-collection", method = RequestMethod.POST)
	@ApiOperation(value = "Save a collected data", notes = "This endpoint saves a collected data if it passes basic validation.")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> saveCollectMeasurementData(
			@RequestParam(value = "runtimeTaskId", required = true) String runtimeTaskId,
			@RequestBody CollectedData collectedData) throws MeasureTaskNotFoundException,
			CollectedDataNotValidException, ParseException, JsonRequestException, TaskNotFoundException {

		JsonRequestActiviti jsonRequestActiviti = new JsonRequestActiviti(hostSettings);
		ResponseEntity<Task> taskResponseEntity = jsonRequestActiviti
				.get(hostSettings.getActivitiRestEndpointTasks() + "/" + runtimeTaskId, Task.class);
		if (taskResponseEntity.getBody() == null)
			throw new TaskNotFoundException("" + runtimeTaskId);
		Task task = taskResponseEntity.getBody();

		// retrieve workflowData for collectedData
		List<WorkflowData> workflowDatas = workflowDataRepository
				.findByBusinessWorkflowProcessInstanceId(task.getProcessInstanceId());

		if (workflowDatas.get(0) != null) {

			List<MeasureTask> measureTaskList = workflowDatas.get(0).getMeasureTasksList();

			for (MeasureTask measureTask : measureTaskList) {
				if (measureTask.getTaskId().equals(collectedData.getTaskId())) {

					Metric metric = measureTask.getMetric();

					System.out.println(metric.toString());
					// basic validation
					if (BasicValidation.isValid(metric, collectedData.getValue())) {

						// set workflowData
						collectedData.setWorkflowData(workflowDatas.get(0));

						// set date
						DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
						collectedData.setDate(format.format(Calendar.getInstance().getTime()));

						// save collected data
						collectedDataRepository.save(collectedData);

						return ResponseEntity.status(HttpStatus.OK)
								.body("The collected data has been successfully saved");
					} else
						throw new CollectedDataNotValidException(collectedData.getValue(), metric.getName());
				}
			}
		} else {
			throw new MeasureTaskNotFoundException(collectedData.getTaskId());
		}

		return null;

	}

}
