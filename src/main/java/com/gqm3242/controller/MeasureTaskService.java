package com.gqm3242.controller;

import com.gqm3242.model.rest.DTOMeasureTask;
import com.gqm3242.model.rest.response.DTOResponseMeasureTask;
import org.springframework.http.ResponseEntity;

public interface MeasureTaskService {

	public ResponseEntity<DTOResponseMeasureTask> createMeasureTask(
            DTOMeasureTask dtoMeasureTask);

}
