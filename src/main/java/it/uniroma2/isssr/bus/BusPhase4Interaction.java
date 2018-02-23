package it.uniroma2.isssr.bus;

import it.uniroma2.isssr.model.phase42.Strategy;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ovidiudanielbarba on 07/07/2017.
 */
@Service("BusPhase4Interaction")
public interface BusPhase4Interaction {

    ResponseEntity<DTOResponseStrategy> getStrategies();

    List<Strategy> getStrategiesList();

    ResponseEntity<DTOResponseStrategy> getStrategiesFree();

    ResponseEntity<DTOResponseStrategy> getValidationOps();

    String saveValitatedDataOnBus(String taskId);


}
