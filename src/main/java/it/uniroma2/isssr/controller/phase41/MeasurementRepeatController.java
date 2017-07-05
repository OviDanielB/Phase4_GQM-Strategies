package it.uniroma2.isssr.controller.phase41;

import it.uniroma2.isssr.exception.CollectedDataNotFoundException;
import it.uniroma2.isssr.exception.CollectedDataNotValidException;
import it.uniroma2.isssr.exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.exception.MetricNotFoundException;
import it.uniroma2.isssr.model.phase41.CollectedData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;

public interface MeasurementRepeatController {

	/**
	 * 
	 * @param collectedDataId collected data id
	 * @param taskId task id at runtime
	 * @return MeasureTask and DataCollected
	 * @throws MeasureTaskNotFoundException
	 * @throws CollectedDataNotFoundException
	 */
	@RequestMapping(value = "/measurement-repeat/", method = RequestMethod.GET)
	public ResponseEntity<?> getRepeatMeasurementData(
            @RequestParam(value = "collectedDataId", required = true) String collectedDataId,
            @RequestParam(value = "taskId", required = true) String taskId)
			throws MeasureTaskNotFoundException, CollectedDataNotFoundException;

	/**
	 * Save CollectedData only if basic validation was successful
	 * For the basic validation is necessary retrieve the metric associated to collectedData passed
	 * 
	 * @param collectedData collected data
	 * @return 200OK if collected data is valided correctly and is saved successfully 
	 * @throws MeasureTaskNotFoundException
	 * @throws CollectedDataNotValidException
	 * @throws MetricNotFoundException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/measurement-collection/", method = RequestMethod.POST)
	public ResponseEntity<?> saveRepeatMeasurementData(@RequestBody CollectedData collectedData)
			throws MeasureTaskNotFoundException, CollectedDataNotValidException, MetricNotFoundException, ParseException;
}
