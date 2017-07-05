package it.uniroma2.isssr.services.phase42;

import it.uniroma2.isssr.model.phase42.rest.DTOMeasureTask;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseMeasureTask;
import org.springframework.http.ResponseEntity;

public interface MeasureTaskService {

	public ResponseEntity<DTOResponseMeasureTask> createMeasureTask(
            DTOMeasureTask dtoMeasureTask);

}
