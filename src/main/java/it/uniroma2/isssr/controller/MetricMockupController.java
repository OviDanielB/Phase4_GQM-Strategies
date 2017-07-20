package it.uniroma2.isssr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.dto.bus.BusReadResponse;
import it.uniroma2.isssr.integrazione.BusException;
import it.uniroma2.isssr.integrazione.BusMessage;
import it.uniroma2.isssr.model.phase2.DTOOntologyResponsePhase2;
import it.uniroma2.isssr.model.phase2.Ontology;
import it.uniroma2.isssr.gqm3.model.ontologyPhase2.measurementModel.DirectPredictive;
import it.uniroma2.isssr.gqm3.model.ontologyPhase2.measurementModel.DirectPrescriptive;
import it.uniroma2.isssr.gqm3.model.ontologyPhase2.scale.Interval;
import it.uniroma2.isssr.model.phase41.Metric;
import it.uniroma2.isssr.dto.DTOMetric;
import it.uniroma2.isssr.dto.DTOResponseMetric;
import it.uniroma2.isssr.repositories.OntologyRepository;
import it.uniroma2.isssr.repositories.phase41.MetricRepository;
import it.uniroma2.isssr.utils.BusObjectTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// TODO mockup for metrics. Remove after along with DTOMetric and DTOResponseMetric

@RestController
@CrossOrigin(origins = "*")
public class MetricMockupController {

    @Autowired
    MetricRepository metricRepository;

    @Autowired
    HostSettings hostSettings;

    @Autowired
    OntologyRepository ontologyRepository;

    @RequestMapping(value = "/createMetric", method = RequestMethod.POST)
    public ResponseEntity<DTOResponseMetric> createMetric(@RequestBody DTOMetric metricDto) {

        Metric metric = new Metric();
        metric.setOrdered(metricDto.getOrdered());
        metric.setName(metricDto.getName());
        metric.setVersion(metricDto.getVersion());
        metric.setReleaseNote(metricDto.getReleaseNote());
        metric.setUnit(metricDto.getUnit());
        metric.setScaleType(metricDto.getScaleType());
        metric.setId(metricDto.getId());
        metric.setDescription(metricDto.getDescription());

        /* save on local mongodb */
        metricRepository.save(metric);

        DTOResponseMetric dtoResponse = new DTOResponseMetric();
        dtoResponse.setName(metric.getName());
        dtoResponse.setDescription(metric.getDescription());
        dtoResponse.setId(metric.getId());

        ResponseEntity<DTOResponseMetric> responseEntity = new ResponseEntity<>(dtoResponse,
                HttpStatus.OK);

        return responseEntity;
    }

    @RequestMapping(value = "/getOntologiesPhase2",method = RequestMethod.GET)
    public ResponseEntity<DTOOntologyResponsePhase2> getOntologiesPhase2(){



        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.ONTOLOGY);
        jo.put("instance", "");
        jo.put("busVersion", "");
        jo.put("tags", "[]");

        BusMessage message = null;
         Ontology ontology = null;
        try {
            message = new BusMessage(BusMessage.OPERATION_READ,"phase2", jo.toString());
            String busResponse = message.send(hostSettings.getBusUri());
            System.out.println(busResponse);

            ObjectMapper mapper = new ObjectMapper();
            List<BusReadResponse> readResponseList = mapper.readValue(
                    busResponse,
                    mapper.getTypeFactory().constructCollectionType(List.class,
                            BusReadResponse.class));

            List<Ontology> ontologyList = new ArrayList<>();
            for(BusReadResponse e: readResponseList) {
                try {
                    ontology = mapper.readValue(e.getPayload().toString(), Ontology.class);
                    System.out.println(ontology.toString());

                    ontologyRepository.save(ontology);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }



        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DTOOntologyResponsePhase2 dtoResponse = new DTOOntologyResponsePhase2();
        if(ontology != null) {
            dtoResponse.setName("Ontology");
            dtoResponse.setId(ontology.getId());
            ResponseEntity<DTOOntologyResponsePhase2> responseEntity = new ResponseEntity<>(dtoResponse,
                    HttpStatus.OK);

            return responseEntity;
        } else {
            dtoResponse.setId("NOT FOUND");
            return new ResponseEntity<>(dtoResponse,HttpStatus.NOT_FOUND);
        }
    }
}
