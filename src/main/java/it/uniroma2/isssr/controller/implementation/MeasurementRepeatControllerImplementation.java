package it.uniroma2.isssr.controller.implementation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.uniroma2.isssr.Exception.CollectedDataNotFoundException;
import it.uniroma2.isssr.Exception.CollectedDataNotValidException;
import it.uniroma2.isssr.Exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.Exception.MetricNotFoundException;
import it.uniroma2.isssr.controller.MeasurementRepeatController;
import it.uniroma2.isssr.dto.ErrorResponse;
import it.uniroma2.isssr.dto.response.ResponseMeasurementRepeat;
import it.uniroma2.isssr.model.CollectedData;
import it.uniroma2.isssr.model.MeasureTask;
import it.uniroma2.isssr.model.Metric;
import it.uniroma2.isssr.repository.CollectedDataRepository;
import it.uniroma2.isssr.repository.MeasureTaskRepository;
import it.uniroma2.isssr.tools.BasicValidation;
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
@Api(value = "Measurement Repeat", description = "Measurement Repeat API")
public class MeasurementRepeatControllerImplementation implements MeasurementRepeatController {

	@Autowired
	private MeasureTaskRepository measureTaskRespository;

	@Autowired
	private CollectedDataRepository collectedDataRepository;

	@RequestMapping(value = "/measurement-repeat", method = RequestMethod.GET)
	@ApiOperation(value = "Get a measurement data collected and its relative measure task", notes = "This endpoint returns a collected data "+
	"to repeat and its relative measure task.")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> getRepeatMeasurementData(
			@RequestParam(value = "collectedDataId", required = true) String collectedDataId,
			@RequestParam(value = "taskId", required = true) String taskId)
			throws MeasureTaskNotFoundException, CollectedDataNotFoundException {

		List<MeasureTask> measureTasks = measureTaskRespository.findByTaskId(taskId);
		MeasureTask measureTask = measureTasks.get(0);

		if (measureTask != null) {

			CollectedData collectedData = (CollectedData) collectedDataRepository.findOne(collectedDataId);

			if (collectedData != null)
				return new ResponseEntity<ResponseMeasurementRepeat>(
						new ResponseMeasurementRepeat(measureTask, collectedData), HttpStatus.OK);
			else
				throw new CollectedDataNotFoundException(collectedDataId);

		} else
			throw new MeasureTaskNotFoundException(taskId);
	}

	@RequestMapping(value = "/measurement-repeat", method = RequestMethod.POST)
	@ApiOperation(value = "Save a repeated collected data", notes = "This endpoint saves a repeated collected data if it passes basic validation.")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "See error code and message", response = ErrorResponse.class) })
	public ResponseEntity<?> saveRepeatMeasurementData(@RequestBody CollectedData collectedData)
			throws MeasureTaskNotFoundException, CollectedDataNotValidException, MetricNotFoundException, ParseException {

		Metric metric = null;
		for (MeasureTask measureTask : collectedData.getWorkflowData().getMeasureTasksList())
			if (collectedData.getTaskId().equals(measureTask.getTaskId())) {
				metric = measureTask.getMetric();
				break;
			}

		if (metric != null) {
			if (BasicValidation.isValid(metric, collectedData.getValue())) {
				// set date
				DateFormat format =new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
				collectedData.setDate(format.format(Calendar.getInstance().getTime()));
				
				// save collected data
				collectedDataRepository.save(collectedData);
				return ResponseEntity.status(HttpStatus.OK).body("The collected data has been successfully saved");
			} else 
				throw new CollectedDataNotValidException(collectedData.getValue(), metric.getName());
		} else
			throw new MetricNotFoundException();
	}

}
