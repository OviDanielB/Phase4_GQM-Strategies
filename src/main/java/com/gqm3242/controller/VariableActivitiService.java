package com.gqm3242.controller;

import com.gqm3242.exception.*;
import com.gqm3242.model.rest.DTOVariableActiviti;
import com.gqm3242.model.rest.response.DTOResponseVariableActiviti;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VariableActivitiService {
	
	public ResponseEntity<?> getVariableActiviti(String id) throws EntityNotFoundException, AnomalySystemException;
	public ResponseEntity<?> createVariableActiviti(DTOVariableActiviti dtoVariableActiviti) throws BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException;
	public ResponseEntity<?> updateVariableActiviti(String taskId);
	public ResponseEntity<?> deleteVariableActiviti(String taskId) throws EntityNotFoundException, AnomalySystemException;
	public ResponseEntity<List<DTOResponseVariableActiviti>> getAllVariablesActiviti();
	
}
