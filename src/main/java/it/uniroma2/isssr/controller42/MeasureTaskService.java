package it.uniroma2.isssr.controller42;

import it.uniroma2.isssr.model42.rest.DTOMeasureTask;
import it.uniroma2.isssr.model42.rest.response.DTOResponseMeasureTask;
import org.springframework.http.ResponseEntity;

public interface MeasureTaskService {

	public ResponseEntity<DTOResponseMeasureTask> createMeasureTask(
            DTOMeasureTask dtoMeasureTask);

}
