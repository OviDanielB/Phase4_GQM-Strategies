package it.uniroma2.isssr.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.uniroma2.isssr.HostSettings;
import it.uniroma2.isssr.dto.bus.BusReadResponse;
import it.uniroma2.isssr.exception.BusRequestException;
import it.uniroma2.isssr.integrazione.BusException;
import it.uniroma2.isssr.integrazione.BusMessage;
import it.uniroma2.isssr.model.phase41.MeasureTask;
import it.uniroma2.isssr.model.phase41.WorkflowData;
import it.uniroma2.isssr.model.phase42.StrategicPlan;
import it.uniroma2.isssr.repositories.phase41.MeasureTaskRepository;
import it.uniroma2.isssr.repositories.phase41.SystemStateRepository;
import it.uniroma2.isssr.repositories.phase41.WorkflowDataRepository;
import it.uniroma2.isssr.repositories.phase42.StrategicPlanRepository;
import it.uniroma2.isssr.utils.BusObjectTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovidiudanielbarba on 06/07/2017.
 */

@Service
public class IntegratedPhase34BusInteractionService {

    @Autowired
    private HostSettings hostSettings;

    @Autowired
    private SystemStateRepository systemStateRepository;

    @Autowired
    private WorkflowDataRepository workflowDataRepository;

    @Autowired
    private MeasureTaskRepository measureTaskRepository;

    @Autowired
    private StrategicPlanRepository strategicPlanRepository;


    /**
     * Get Measurement Task from Bus (saved by Phase3) and saves
     * them on local DB after deleting
     * all previous data
     * @return
     */
    public Boolean updateLocalMeasureTasks(){
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
    public Boolean updateLocalWorkflowData(){

        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.WORKFLOW_DATA);
        jo.put("instance", "");
        jo.put("busVersion", "");
        jo.put("tags", "[]");

        try {

            // TODO remove fake
            //fakeWorkFlowData();

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
            } catch (JsonMappingException e ){
                return false;
            }

            // empty old workflowdatas
            workflowDataRepository.deleteAll();

            for(BusReadResponse response : readResponseList ){
                WorkflowData workflowData = mapper.treeToValue(response.getPayload(), WorkflowData.class);

                workflowDataRepository.save(workflowData);
            }


        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
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

    public boolean updateLocalStrategicPlans() {
        JSONObject jo = new JSONObject();
        jo.put("objIdLocalToPhase", "");
        jo.put("typeObj", BusObjectTypes.STRATEGIC_PLAN);
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
            strategicPlanRepository.deleteAll();

            for(BusReadResponse response : readResponseList ){
                StrategicPlan strategicPlan = mapper.treeToValue(response.getPayload(), StrategicPlan.class);

                strategicPlanRepository.save(strategicPlan);
            }


        } catch (BusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }
}