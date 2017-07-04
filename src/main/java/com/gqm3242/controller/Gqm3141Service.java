package com.gqm3242.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.exception.*;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface Gqm3141Service {
	
	public ResponseEntity<?> suspendTaskAndSaveFormVariablesInDB(String taskId, List<ActivitiFormVariableProperty> activitiFormVariableProperties)
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException, BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException;
	
	public ResponseEntity<?> completeTaskWithFormVariablesinDBByTaskId(
            String taskId) throws EntityNotFoundException, AnomalySystemException, ActivitiPostException;

	public ResponseEntity<?> completeUserTask(String taskId,
                                              List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws EntityNotFoundException, AnomalySystemException, JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException;
}
