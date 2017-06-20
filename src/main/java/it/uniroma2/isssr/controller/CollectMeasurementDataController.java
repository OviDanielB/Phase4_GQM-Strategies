package it.uniroma2.isssr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma2.isssr.Exception.CollectedDataNotValidException;
import it.uniroma2.isssr.Exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.model.CollectedData;

public interface CollectMeasurementDataController {
	
	@RequestMapping(value = "/measurement-collection", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectMeasurementData(@RequestParam(value = "taskId", required = true) String taskId) throws MeasureTaskNotFoundException ;
	
	@RequestMapping(value = "/measurement-collection", method = RequestMethod.POST)
	public ResponseEntity<?> saveCollectMeasurementData(@RequestBody CollectedData collectedData) throws MeasureTaskNotFoundException, CollectedDataNotValidException;

}
