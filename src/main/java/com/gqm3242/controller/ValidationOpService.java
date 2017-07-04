package com.gqm3242.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.exception.*;
import com.gqm3242.model.rest.DTOValidationOp;
import com.gqm3242.validation.Phase;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


public interface ValidationOpService {

	public ResponseEntity<?> getValidationOpListByMeasureTaskId(String measureTaskId, Phase phase) throws EntityNotFoundException, AnomalySystemException;

	public ResponseEntity<?> createValidationOp(DTOValidationOp dtoValidationOp) throws BodyEmptyException, IdKeyNullException, EntityNotFoundException, AnomalySystemException;
	
	public ResponseEntity<?> updateValidationOp(String id,
                                                DTOValidationOp dtoValidationOp) throws BodyEmptyException, EntityNotFoundException, IdKeyNullException, AnomalySystemException;

	public ResponseEntity<?> deleteValidationOp(String id) throws AnomalySystemException, EntityNotFoundException;

	
	public ResponseEntity<?> getValidationOp(String id) throws EntityNotFoundException;

	public ResponseEntity<?> getMeasureTask(String businessWorkflowProcessDefinitionId)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException;

	public ResponseEntity<?> validateDataCollected(String id) throws EntityNotFoundException;

	public ResponseEntity<?> ignoreValidation(String id) throws BodyEmptyException, EntityNotFoundException, IdKeyNullException, AnomalySystemException;

	public ResponseEntity<?> repeateMeasure(String idRuntimeTask, String idValidationOp, String errorMessage) throws JsonParseException, JsonMappingException, JSONException, IOException, ActivitiGetException, ActivitiPutException, AnomalySystemException, EntityNotFoundException;

	public ResponseEntity<?> completeValidation(String idDataCollected) throws AnomalySystemException;
	
}
