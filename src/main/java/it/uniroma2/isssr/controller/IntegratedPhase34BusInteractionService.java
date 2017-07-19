package it.uniroma2.isssr.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.dto.DTOStrategyFrom2;
import it.uniroma2.isssr.dto.bus.BusReadResponse;
import it.uniroma2.isssr.exception.BusRequestException;
import it.uniroma2.isssr.integrazione.BusException;
import it.uniroma2.isssr.integrazione.BusMessage;
import it.uniroma2.isssr.model.phase2.DTOOntologyResponsePhase2;
import it.uniroma2.isssr.model.phase2.Ontology;
import it.uniroma2.isssr.model.phase41.MeasureTask;
import it.uniroma2.isssr.model.phase41.WorkflowData;
import it.uniroma2.isssr.model.phase41.XmlWorkflow;
import it.uniroma2.isssr.model.phase42.StrategicPlan;
import it.uniroma2.isssr.model.phase42.Strategy;
import it.uniroma2.isssr.model.phase42.ValidationOp;
import it.uniroma2.isssr.repositories.OntologyRepository;
import it.uniroma2.isssr.repositories.phase41.MeasureTaskRepository;
import it.uniroma2.isssr.repositories.phase41.WorkflowDataRepository;
import it.uniroma2.isssr.repositories.phase41.XmlWorkflowRepository;
import it.uniroma2.isssr.repositories.phase42.StrategicPlanRepository;
import it.uniroma2.isssr.repositories.phase42.StrategyRepository;
import it.uniroma2.isssr.repositories.phase42.ValidationOpRepository;
import it.uniroma2.isssr.utils.BusObjectTypes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by ovidiudanielbarba on 06/07/2017.
 */

@Service
public class IntegratedPhase34BusInteractionService {

    @Autowired
    private HostSettings hostSettings;

    @Autowired
    private WorkflowDataRepository workflowDataRepository;

    @Autowired
    private MeasureTaskRepository measureTaskRepository;

    @Autowired
    private StrategicPlanRepository strategicPlanRepository;

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private XmlWorkflowRepository xmlWorkflowRepository;

    @Autowired
    private ValidationOpRepository validationOpRepository;

    @Autowired
    private OntologyRepository ontologyRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    public Boolean deleteAllAndUpdateLocalOntologies(){

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

            /* delete all ontologies */
            ontologyRepository.deleteAll();

            List<Ontology> ontologyList = new ArrayList<>();
            for(BusReadResponse e: readResponseList) {
                try {
                    ontology = mapper.readValue(e.getPayload().toString(), Ontology.class);
                    System.out.println(ontology.toString());

                    ontologyRepository.save(ontology);

                } catch (IOException e1) {
                    e1.printStackTrace();
                    return false;
                }
            }



        } catch (BusException | IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public Boolean updateLocalStrategiesBase64() {
        ArrayList<DTOStrategyFrom2> upToDateStr = new ArrayList<>();
        String response;

        org.json.JSONArray jsonResponse;
        JSONObject jsonobj;
        try {
            JSONObject jsonRead = new JSONObject();
            jsonRead.put("objIdLocalToPhase", "");
            jsonRead.put("typeObj", "base64-TerminalStrategy");
            jsonRead.put("instance", "");
            jsonRead.put("busVersion", "");
            jsonRead.put("tags", "[]");

            BusMessage busMessage = new BusMessage(BusMessage.OPERATION_READ, "phase2", jsonRead.toString());
            response = busMessage.send(hostSettings.getBusUri());
            jsonResponse = new org.json.JSONArray(response);

        } catch (IOException | JSONException | BusException e) {
            e.printStackTrace();
            return false;
        }

        for (int i = 0; i < jsonResponse.length(); i++) {

            try {

                jsonobj = jsonResponse.getJSONObject(i);

                String payloads = jsonobj.getString("payload");
                DTOStrategyFrom2 ob = new DTOStrategyFrom2();

                jsonobj = new JSONObject(payloads);
                ob.setId(jsonobj.getString("id"));
                ob.setName(jsonobj.getString("name"));
                ob.setDescription(jsonobj.getString("description"));
                ob.setOrganizationalUnit(jsonobj.getString("organizationalUnit"));
                ob.setOrganizationalUnitId(jsonobj.getString("organizationalUnitId"));
                ob.setRevisited(jsonobj.getInt("status"));
                ob.setVersion(jsonobj.getInt("version"));

                upToDateStr.add(ob);

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        // strategia attualmente salvate so mongodb
        List<Strategy> actualStrategies = strategyRepository.findAll();

        // elimino le strategy presenti su mongodb ma che non sono più presenti sul bus
        for (Strategy strategy : actualStrategies) {

            Boolean notFound = true;
            for (DTOStrategyFrom2 dtoStrategyFrom2 : upToDateStr)
                if (dtoStrategyFrom2.getId().equals(strategy.getIdF2())) {
                    notFound = false;
                }

            if (notFound)
                strategyRepository.delete(strategy);

        }

        if (upToDateStr == null) {
            strategyRepository.deleteAll();
            return true;
        }

        // aggiorno i valori nuovi
        for (DTOStrategyFrom2 dtoSF2 : upToDateStr) {

            if (dtoSF2.getRevisited() == HostSettings.state.NEW.getValue() ||
                    dtoSF2.getRevisited() == HostSettings.state.MODIFIED.getValue()) {

                Query query = new Query();
                query.addCriteria(Criteria.where("idF2").is(dtoSF2.getId()));
                List<Strategy> mongoStrategy = mongoTemplate.find(query, Strategy.class);

                // strategy is marked as MODIFIED but it doesn't exist, so we create it
                if (mongoStrategy.isEmpty()) {
                    Strategy newStrategy = new Strategy(dtoSF2.getName(), dtoSF2.getDescription(),
                            dtoSF2.getOrganizationalUnit(), dtoSF2.getOrganizationalUnitId(), dtoSF2.getRevisited(),
                            dtoSF2.getVersion(), 0);
                    newStrategy.setIdF2(dtoSF2.getId());
                    strategyRepository.save(newStrategy);

                } else {

                    Strategy toUpdate = mongoStrategy.get(0);

                    if (mongoStrategy.size() > 1) {
                        //TODO: resolve unexpected state
                        System.out.println("On the bus there are more than one strategy with the same idF2: " + toUpdate.getIdF2());
                    }

                    if (toUpdate.getVersion() < dtoSF2.getVersion()) {
                        toUpdate.setIdF2(dtoSF2.getId());
                        toUpdate.setName(dtoSF2.getName());
                        toUpdate.setDescription(dtoSF2.getDescription());
                        toUpdate.setOrganizationalunit(dtoSF2.getOrganizationalUnit());
                        toUpdate.setOrganizationalunitId(dtoSF2.getOrganizationalUnitId());
                        toUpdate.setStatus(dtoSF2.getRevisited());
                        toUpdate.setVersion(dtoSF2.getVersion());

                        strategyRepository.save(toUpdate);
                    }
                }

            }
        }

        return true;


    }

    /** TODO PHASE4 integrate with phase2
     * Get Strategies from Bus (saved by Phase2) and saves
     * them on local DB after deleting
     * all previous data
     * This method is more useful to phase3
     * @return
     */
    public Boolean deleteAllAndUpdateLocalStrategies(){
        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.STRATEGY);
        jo.put("instance", "");
        jo.put("busVersion", "");
        jo.put("tags", "[]");


        try {

            BusMessage message = new BusMessage(BusMessage.OPERATION_READ,"phase2", jo.toString());
            String busResponse = message.send(hostSettings.getBusUri());
            System.out.println(busResponse);

            ObjectMapper mapper = new ObjectMapper();

            List<BusReadResponse> readResponseList ;
            try {
                readResponseList = mapper.readValue(
                        busResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class,
                                BusReadResponse.class));
            } catch (JsonMappingException e){
                return false;
            }

            // empty old measure tasks
            strategyRepository.deleteAll();

            for(BusReadResponse response : readResponseList ){
                Strategy measureTask = mapper.treeToValue(response.getPayload(), Strategy.class);

                strategyRepository.save(measureTask);
            }


        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return true;

    }


    /**
     * Get Measurement Task from Bus (saved by Phase3) and saves
     * them on local DB after deleting
     * all previous data
     * @return
     */
    public Boolean deleteAllAndUpdateLocalMeasureTasks(){
        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.MEASURE_TASK);
        jo.put("instance", "");
        jo.put("busVersion", "");
        jo.put("tags", "[]");

        try {

            BusMessage message = new BusMessage(BusMessage.OPERATION_READ,"phase3", jo.toString());
            String busResponse = message.send(hostSettings.getBusUri());
            System.out.println(busResponse);

            ObjectMapper mapper = new ObjectMapper();

            List<BusReadResponse> readResponseList ;
            try {
                 readResponseList = mapper.readValue(
                        busResponse,
                        mapper.getTypeFactory().constructCollectionType(List.class,
                                BusReadResponse.class));
            } catch (JsonMappingException e){
                return false;
            }

            // empty old measure tasks
            measureTaskRepository.deleteAll();

            for(BusReadResponse response : readResponseList ){
                MeasureTask measureTask = mapper.treeToValue(response.getPayload(), MeasureTask.class);

                measureTaskRepository.save(measureTask);
            }


        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;

    }


    /**
     *  gets workflow data from bus sent from phase 3
     *  and saves them on local DB after deleting
     *  all previous data
     * @return
     */
    public Boolean deleteAllAndUpdateLocalWorkflowData(ResponseEntity<ArrayList<WorkflowData>> workflowDatas){

        // empty old workflowdatas and old related measure tasks
        workflowDataRepository.deleteAll();
        measureTaskRepository.deleteAll();

        for (WorkflowData workflowData : workflowDatas.getBody() ){
            workflowDataRepository.save(workflowData);
            for ( MeasureTask m :workflowData.getMeasureTasksList() ) {
                measureTaskRepository.save(m);
                if(m.getValidationIdList() != null){
                    for (ValidationOp vo : m.getValidationIdList()) {
                        validationOpRepository.save(vo);
                    }
                }
            }
        }
        return true;
    }


    /**
     *  gets strategic plans from bus sent from phase 3
     *  and saves them on local DB after deleting
     *  all previous data
     * @return
     */
    public boolean deleteAllAndUpdateLocalStrategicPlans() {
        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.STRATEGIC_PLAN);
        jo.put("instance", "");
        jo.put("busVersion", "");
        jo.put("encode","base64");
        jo.put("tags", "[]");

        try {

            BusMessage message = new BusMessage(BusMessage.OPERATION_READ,"phase3", jo.toString());
            String busResponse = message.send(hostSettings.getBusUri());
            System.out.println(busResponse);

            ObjectMapper mapper = new ObjectMapper();

            JSONArray busResponsePayloadObject = new JSONArray(busResponse);


            strategicPlanRepository.deleteAll();

            for(int i = 0; i < busResponsePayloadObject.length(); i ++) {
                JSONObject json = busResponsePayloadObject.getJSONObject(i);
                JSONObject object = json.getJSONObject("payload");

                String encoded = object.getString("object");
                System.out.println("ENCODED " + encoded);
                byte[] decodedBArray = Base64.getDecoder().decode(encoded);
                String decoded = new String(decodedBArray, "UTF-8");

                System.out.println("DECODED " + decoded);


                try {
                    StrategicPlan strategicPlan = mapper.readValue(decoded, StrategicPlan.class);
                    System.out.println(strategicPlan.toString());


                    strategicPlanRepository.save(strategicPlan);

 /*               readResponseList = mapper.readValue(
                        decoded,
                        mapper.getTypeFactory().constructCollectionType(List.class,
                                BusReadResponse.class));*/
                } catch (JsonMappingException e) {
                    return false;
                }

            }
/*            // empty old measure tasks
            strategicPlanRepository.deleteAll();

            for(BusReadResponse response : readResponseList ){
                StrategicPlan strategicPlan = mapper.treeToValue(response.getPayload(), StrategicPlan.class);

                strategicPlanRepository.save(strategicPlan);
            }*/


        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return true;
    }

    private void sendMockStrategiesOnBus(){

        Strategy strategy = new Strategy();
        strategy.setId("3");
        strategy.setName("ciao");
        strategy.setDescription("booo");
        strategy.setIdF2("5");
        strategy.setOrganizational_Unit("unit");
        strategy.setOrganizationalunitId("6");
        strategy.setOrganizationalunit("unit");
        strategy.setRelease(54);
        strategy.setStatus(4);
        strategy.setVersion(2);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        String strategy1 = "";
        try {
            strategy1 = mapper.writeValueAsString(strategy);
            System.out.println(strategy1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "3");
        jo.put("typeObj", BusObjectTypes.STRATEGY);
        jo.put("instance", "ciao" );
        jo.put("tags", "[ ]");
        jo.put("payload",strategy1);





        BusMessage busMessage = null;
        try {
            busMessage = new BusMessage(BusMessage.OPERATION_CREATE, "phase2", jo.toString());
        } catch (BusException e) {
            e.printStackTrace();
        }
        String busResponse = null;
        try {
            busResponse = busMessage.send(hostSettings.getBusUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BusReadResponse busResponseParsed = null;

        ObjectMapper responseMapper = new ObjectMapper();
        try {
            busResponseParsed = responseMapper.readValue(busResponse, BusReadResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!busResponseParsed.getErr().equals("0")) {
            try {
                busMessage = new BusMessage(BusMessage.OPERATION_CREATE, "phase2", jo.toString());
            } catch (BusException e) {
                e.printStackTrace();
            }
            try {
                busResponse = busMessage.send(hostSettings.getBusUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                busResponseParsed = responseMapper.readValue(busResponse,
                        BusReadResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!busResponseParsed.getErr().equals("0")) {
                try {
                    throw new BusRequestException(busResponseParsed.getErr());
                } catch (BusRequestException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    // TODO REMOVE FAKE
    private void fakeWorkFlowData() {

        WorkflowData workflowData = new WorkflowData();
        workflowData.set_id("143");
        workflowData.setBusinessWorkflowModelId("2");
        workflowData.setBusinessWorkflowName("name");
        workflowData.setBusinessWorkflowProcessDefinitionId("65464");
        workflowData.setBusinessWorkflowProcessInstanceId("523");
        //workflowData.setEnded(false);
        workflowData.setMeasureTasksList(new ArrayList<>());
        workflowData.setMetaWorkflowName("meta");
        workflowData.setMetaWorkflowProcessInstanceId("131412312");

        JSONObject pl = new JSONObject();
        pl.put("_id", workflowData.get_id());
        pl.put("businessWorkflowName", workflowData.getBusinessWorkflowName());
        pl.put("metaWorkflowName", workflowData.getMetaWorkflowName());
        pl.put("metaWorkflowProcessInstanceId", workflowData.getMetaWorkflowProcessInstanceId());
        pl.put("businessWorkflowModelId", workflowData.getBusinessWorkflowModelId());
        pl.put("businessWorkflowProcessDefinitionId", workflowData.getBusinessWorkflowProcessDefinitionId());
        pl.put("businessWorkflowProcessInstanceId", workflowData.getBusinessWorkflowProcessInstanceId());
        pl.put("measureTasksList", new ArrayList().toString());
        pl.put("ended", workflowData.isEnded().toString());


        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", workflowData.get_id());
        jo.put("typeObj", "WorkflowData");
        jo.put("instance", workflowData.getMetaWorkflowName());
        jo.put("tags", "[]");
        jo.put("payload", pl.toString());

        BusMessage busMessage = null;
        try {
            busMessage = new BusMessage(BusMessage.OPERATION_UPDATE, "phase3", jo.toString());
        } catch (BusException e) {
            e.printStackTrace();
        }
        String busResponse = null;
        try {
            busResponse = busMessage.send(hostSettings.getBusUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BusReadResponse busResponseParsed = null;

        ObjectMapper responseMapper = new ObjectMapper();
        try {
            busResponseParsed = responseMapper.readValue(busResponse, BusReadResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!busResponseParsed.getErr().equals("0")) {
            try {
                busMessage = new BusMessage(BusMessage.OPERATION_CREATE, "phase3", jo.toString());
            } catch (BusException e) {
                e.printStackTrace();
            }
            try {
                busResponse = busMessage.send(hostSettings.getBusUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                busResponseParsed = responseMapper.readValue(busResponse,
                        BusReadResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!busResponseParsed.getErr().equals("0")) {
                try {
                    throw new BusRequestException(busResponseParsed.getErr());
                } catch (BusRequestException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Boolean updateLocalWorkflowXML(ResponseEntity<ArrayList<XmlWorkflow>> xmlWorkflows ) {

        xmlWorkflowRepository.deleteAll();

        for (XmlWorkflow xmlWorkflow : xmlWorkflows.getBody()) {
            xmlWorkflowRepository.save(xmlWorkflow);
        }
        return true;
    }

}
