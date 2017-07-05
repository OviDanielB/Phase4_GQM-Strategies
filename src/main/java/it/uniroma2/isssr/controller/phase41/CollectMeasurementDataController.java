package it.uniroma2.isssr.controller.phase41;

import it.uniroma2.isssr.exception.CollectedDataNotValidException;
import it.uniroma2.isssr.exception.JsonRequestException;
import it.uniroma2.isssr.exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.exception.TaskNotFoundException;
import it.uniroma2.isssr.model.phase41.CollectedData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

public interface CollectMeasurementDataController {

	/**
	 * @param taskId
	 *            task id at runtime
	 * @return a MeasureTask by taskId
	 * @throws MeasureTaskNotFoundException
	 */
	@RequestMapping(value = "/measurement-collection", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectMeasurementData(@RequestParam(value = "taskId", required = true) String taskId)
			throws MeasureTaskNotFoundException;

	/**
	 * Save a CollectedDate only if basic validation was successful
	 * 
	 * @param collectedData
	 *            contains measure value to be saved
	 * @return 200OK if the @RequestMapping(value = "/measurement-collection",
	 *         method = RequestMethod.POST) public ResponseEntity<?>
	 *         saveCollectMeasurementData(
	 * @RequestParam(value = "runtimeTaskId", required = true) String
	 *                     runtimeTaskId,
	 * @RequestBody CollectedData collectedData) throws
	 *              MeasureTaskNotFoundException,
	 *              CollectedDataNotValidException, ParseException,
	 *              JsonRequestException, TaskNotFoundException
	 *              measure value is
	 *              saved successfully
	 * @throws MeasureTaskNotFoundException
	 * @throws CollectedDataNotValidException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/measurement-collection", method = RequestMethod.POST)
	public ResponseEntity<?> saveCollectMeasurementData(
            @RequestParam(value = "runtimeTaskId", required = true) String runtimeTaskId,
            @RequestBody CollectedData collectedData) throws MeasureTaskNotFoundException,
			CollectedDataNotValidException, ParseException, JsonRequestException, TaskNotFoundException;

}
