package it.uniroma2.isssr.services.phase42;

import it.uniroma2.isssr.exception.*;
import it.uniroma2.isssr.model.phase42.rest.DTOVariableActiviti;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseVariableActiviti;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VariableActivitiService {
	
	public ResponseEntity<?> getVariableActiviti(String id) throws EntityNotFoundException, AnomalySystemException;
	public ResponseEntity<?> createVariableActiviti(DTOVariableActiviti dtoVariableActiviti) throws BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException;
	public ResponseEntity<?> updateVariableActiviti(String taskId);
	public ResponseEntity<?> deleteVariableActiviti(String taskId) throws EntityNotFoundException, AnomalySystemException;
	public ResponseEntity<List<DTOResponseVariableActiviti>> getAllVariablesActiviti();
	
}
