package it.uniroma2.isssr.controller.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma2.isssr.Exception.CollectedDataNotValidException;
import it.uniroma2.isssr.Exception.MeasureTaskNotFoundException;
import it.uniroma2.isssr.controller.CollectMeasurementDataController;
import it.uniroma2.isssr.model.CollectedData;
import it.uniroma2.isssr.model.MeasureTask;
import it.uniroma2.isssr.model.Metric;
import it.uniroma2.isssr.model.WorkflowData;
import it.uniroma2.isssr.repository.CollectedDataRepository;
import it.uniroma2.isssr.repository.MeasureTaskRepository;
import it.uniroma2.isssr.repository.WorkflowDataRepository;
import it.uniroma2.isssr.tools.Costants;

@RestController
public class CollectMeasurementDataControllerImplementation implements CollectMeasurementDataController{

	@Autowired
	private CollectedDataRepository collectedDataRepository;
	
	@Autowired
	private MeasureTaskRepository measureTaskRespository;
	
	@Autowired
	private WorkflowDataRepository workflowDataRepository;
	


	@RequestMapping(value = "/measurement-collection", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectMeasurementData(@RequestParam(value = "taskId", required = true) String taskId) throws MeasureTaskNotFoundException {

		MeasureTask measureTask = (MeasureTask) measureTaskRespository.findByTaskId(taskId);
		
		if(measureTask != null)
			return new ResponseEntity<MeasureTask>(measureTask, HttpStatus.OK);
		else
			throw new MeasureTaskNotFoundException(taskId);
	}
	
	
	@RequestMapping(value = "/measurement-collection", method = RequestMethod.POST)
	public ResponseEntity<?> saveCollectMeasurementData(@RequestBody CollectedData collectedData) throws MeasureTaskNotFoundException, CollectedDataNotValidException {
		
	
		//retrieve workflowData for collectedData
		List<WorkflowData> workflowDatas = workflowDataRepository.findByMeasureTasksListTaskId(collectedData.getTaskId());
		
		if(workflowDatas.get(0) != null){
			
			List<MeasureTask> measureTaskList = workflowDatas.get(0).getMeasureTasksList();
			
			for(MeasureTask measureTask : measureTaskList){
				if(measureTask.getTaskId().equals(collectedData.getTaskId())){
					
					Metric metric = measureTask.getMetric();
					//basic validation
					if(basicValidation(metric, collectedData.getValue())){
						
						//set workflowData
						collectedData.setStrategy(workflowDatas.get(0));
						
						//save collected data
						collectedDataRepository.save(collectedData);
						
						return ResponseEntity.status(HttpStatus.OK).body("The collected data has been successfully saved");
					}
					else{
						throw new CollectedDataNotValidException(collectedData.getValue(), metric.getName());

					}
				}
			}
		}
		else{
			throw new MeasureTaskNotFoundException(collectedData.getTaskId());
		}
		
		return null;
		
	}
	
	
	private boolean basicValidation(Metric metric, String value){
		
		double valueDouble;
		int valueInt;
		
		if(metric.getHasUserDefinedList()){
			for(String userDefined : metric.getUserDefinedList()){
				if(userDefined.equals(value))
					return true;
			}
			return false;
		}
		else if(metric.getSet().equals(Costants.REALS)){
			
			valueDouble = Double.parseDouble(value);
			
			if(metric.getHasMax() && valueDouble > metric.getMax())
				return false;
			else if(metric.getHasMin() && valueDouble < metric.getMin())
				return false;
			else
				return true;
				
		}
		else if(metric.getSet().equals(Costants.INTEGERS)){
			
			valueInt = Integer.parseInt(value);
			
			if(metric.getHasMax() && valueInt > metric.getMax())
				return false;
			else if(metric.getHasMin() && valueInt < metric.getMin())
				return false;
			else
				return true;
		} 
		
		return false;
	}
	

}
