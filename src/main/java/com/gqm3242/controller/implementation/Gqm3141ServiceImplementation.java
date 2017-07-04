package com.gqm3242.controller.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.activiti.rest.ActivitiInterationImplementation;
import com.gqm3242.controller.Gqm3141Service;
import com.gqm3242.controller.VariableActivitiService;
import com.gqm3242.exception.*;
import com.gqm3242.model.MeasureTask;
import com.gqm3242.model.VariableActiviti;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
import com.gqm3242.model.activiti.task.ActivitiTask;
import com.gqm3242.model.activiti.task.ActivitiTaskVariable;
import com.gqm3242.model.rest.DTO;
import com.gqm3242.model.rest.DTOVariableActiviti;
import com.gqm3242.repositories.VariableActivitiRepository;
import com.gqm3242.repositories.gqm3141.MeasureTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: gqm3141ServiceImplementation</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Implementazione dei metodi dell'interfaccia gqm3141Service.
 * Questa classe è pensata per implementare i servizi Rest offerti dalla 
 * applicazione gqm32 e che vengono esposti ed utilizzati per interagire con la applicazione
 * gqm3141, anche attraverso il DB condiviso in cui è possibile l'interazione reciproca
 * attraverso documenti salvati nelle stesse collezioni, sia per accedere attraverso servizi
 * Rest, agli endpoint messi a disposizione dall'altra applicazione.
 * </p> 
 * 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

/* Per ora non viene offerto un servizio di validazione del form, nel senso 
 * che quando arriva e è di misura viene salvato, dopo di che nel completamento
 * diventano tutte variabili globali (nel caso di semplice completamento si completa
 * e via)
 * 
 * Qualora uno aggiungesse variabili nel properties del json, queste verrebbero semplicemente
 * considerate come nuove variabili.. per il momento ci si fida dell'utente
 * 
 * */
@Service("Gqm3141Service")
public class Gqm3141ServiceImplementation implements Gqm3141Service{
	
	private static final Logger log = LoggerFactory.getLogger(Gqm3141ServiceImplementation.class);
	 
	@Autowired
	MeasureTaskRepository measureTaskRepository;
	
	@Autowired
	VariableActivitiRepository variableActivitiRepository;
	
	@Autowired
	ActivitiInterationImplementation activitiInterationImplementation;
	
	@Autowired
	VariableActivitiService variableActivitiService;
	
	@Override
	public ResponseEntity<?> suspendTaskAndSaveFormVariablesInDB
	(String taskIdRun, List<ActivitiFormVariableProperty> activitiFormVariableProperties) 
			throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException, BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException{
		
		/* passo 0. Quel task che appare è generico oppure è un measure Task? (lo vedo confrontando
		   taskId di modello con quello salvato nel DB)=> se non è Measure Task lo completo, altrimenti:
		*/
		//passo taskId che è l'id di runtime.. da quello ricavo taskDefinitionKey
		ActivitiTask activitiTask = activitiInterationImplementation.getUserTaskByTaskId(taskIdRun);
		
		if(!activitiFormVariableProperties.isEmpty()){
			/*activitiInterationImplementation.submitFormDataAndCompleteTask(taskIdRun, 
					activitiFormVariableProperties);*/
			DTOVariableActiviti dtoVariableActiviti = new DTOVariableActiviti();
			dtoVariableActiviti.setTaskId(taskIdRun);
			dtoVariableActiviti.setTaskDefinitionKey(activitiTask.getTaskDefinitionKey());
			dtoVariableActiviti.setProperties(activitiFormVariableProperties);
					
			List<VariableActiviti> variableActivitiList = 
					variableActivitiRepository.findByTaskId(taskIdRun);
			
			if(variableActivitiList == null||variableActivitiList.isEmpty()){
				variableActivitiService.createVariableActiviti(dtoVariableActiviti);
				ResponseEntity<List<ActivitiFormVariableProperty>> responseEntity =
						new ResponseEntity<List<ActivitiFormVariableProperty>>(
								activitiFormVariableProperties, HttpStatus.OK);
						return responseEntity;
			}
			else{
				ResponseEntity<List<ActivitiFormVariableProperty>> responseEntity =
						new ResponseEntity<List<ActivitiFormVariableProperty>>(
								activitiFormVariableProperties, HttpStatus.OK);
						return responseEntity;
				}
			
		}
		else {
			/*activitiInterationImplementation.updateActivitiTask(taskIdRun,
					"complete");
		*/
		ResponseEntity<List<ActivitiFormVariableProperty>> responseEntity =
				new ResponseEntity<List<ActivitiFormVariableProperty>>(
						activitiFormVariableProperties, HttpStatus.OK);
				return responseEntity;
		}
		
	}

	@Override
	public ResponseEntity<?> completeTaskWithFormVariablesinDBByTaskId(
			String taskId) throws EntityNotFoundException, AnomalySystemException, ActivitiPostException {
		// TODO Auto-generated method stub
		/* passo 0. faccio una query nel db, cerco tutte le variabili salvate e associate ad un MeasureTask con
		 * taskId (non so se usare il taskId del runtime task o quello del deploy... ma mi sa quello del runtime)
		 * passo 1. setto un body JSON da inviare con una POST
		 * passo 2. faccio la delete sul DB
		 * 
		 *
		 */
		//Faccio una query sul db, se non c'è una variabileActiviti associata a 
		//quel taskId di run (ce ne potrebbe essere al pi)
		
		List<VariableActiviti> variableActivitiList = 
				variableActivitiRepository.findByTaskId(taskId);
		
		if(variableActivitiList==null ||variableActivitiList.isEmpty()){
			activitiInterationImplementation.updateActivitiTask(taskId, "complete");
			DTO response = new DTO();
			ResponseEntity<DTO> responseEntity =
				new ResponseEntity<DTO>(
						response, HttpStatus.OK);
				return responseEntity;
		}
		if(variableActivitiList.size()>1){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"Sono state trovate più VariableActiviti, non dovrebbe essere possibile! "
			+ "Serio errore interno del sistema");
		}
			
		VariableActiviti variableActiviti = variableActivitiList.get(0);
		
		//controllo per body vuoto
		
		List<ActivitiFormVariableProperty> activitiFormVariableProperties = 
				variableActiviti.getProperties();
		activitiInterationImplementation.submitFormDataAndCompleteTask(taskId,activitiFormVariableProperties );
		variableActivitiService.deleteVariableActiviti(taskId);
		DTO response = new DTO();
		ResponseEntity<DTO> responseEntity =
			new ResponseEntity<DTO>(
					response, HttpStatus.OK);
			return responseEntity;
	}
	
	//patch
	@Override
	public ResponseEntity<?> completeUserTask(String taskIdRun,
                                              List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws EntityNotFoundException, AnomalySystemException, JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException {
		// TODO Auto-generated method stub
		ActivitiTask activitiTask = activitiInterationImplementation.getUserTaskByTaskId(taskIdRun);
		
		List<MeasureTask> measureTaskList =  measureTaskRepository.findAll();
		for(MeasureTask measureTask : measureTaskList){
			
			if(activitiTask.getTaskDefinitionKey().equals(measureTask.getTaskId())){
				log.warn(activitiTask.getTaskDefinitionKey() + " "+ measureTask.getTaskId());
				/**
				 * E' UN MEASURE TASK
				 */
				
				List<ActivitiTaskVariable> activitiTaskVariables = 
			  			new ArrayList<ActivitiTaskVariable>();
			  ActivitiTaskVariable state = new ActivitiTaskVariable();
			  ActivitiTaskVariable responsible= new ActivitiTaskVariable();
			  ActivitiTaskVariable assignee = new ActivitiTaskVariable();
			  state.setName("state");
			  state.setScope("local");
			  state.setType("string");
			  state.setValue("1");
			 
			  responsible.setName("responsible");
			  responsible.setScope("local");
			  responsible.setType( "string");
			  responsible.setValue(measureTask.getResponsible());
			  
			  assignee.setName("assignee");
			  assignee.setScope("local");
			  assignee.setType( "string");
			  assignee.setValue("");
			  
			  activitiTaskVariables.add(0,state);
			  activitiTaskVariables.add(1,responsible);
			  activitiTaskVariables.add(2,assignee);
				
			  activitiInterationImplementation.createTaskVariableFromRuntimeTaskId(
				  		taskIdRun, activitiTaskVariables);
			  
				DTO dto = new DTO();
				ResponseEntity<DTO> responseEntity =
						new ResponseEntity<DTO>(dto, HttpStatus.OK);
				return responseEntity;
			}
		}
		
		List<VariableActiviti> variableActivitiList = 
				variableActivitiRepository.findByTaskId(taskIdRun);
		
		if(variableActivitiList == null || variableActivitiList.isEmpty()){
			activitiInterationImplementation.updateActivitiTask(taskIdRun, "complete");
			DTO response = new DTO();
			ResponseEntity<DTO> responseEntity =
				new ResponseEntity<DTO>(
						response, HttpStatus.OK);
				return responseEntity;
		
		}
		if(variableActivitiList.size()>1){
			throw new AnomalySystemException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"Sono state trovate più VariableActiviti, non dovrebbe essere possibile! "
			+ "Serio errore interno del sistema");
		}
		
			
		VariableActiviti variableActiviti = variableActivitiList.get(0);
		
		//controllo per body vuoto
		
		List<ActivitiFormVariableProperty> actformVarProperties = 
				variableActiviti.getProperties();
		activitiInterationImplementation.submitFormDataAndCompleteTask(taskIdRun,actformVarProperties );
		variableActivitiService.deleteVariableActiviti(taskIdRun);
		DTO response = new DTO();
		ResponseEntity<DTO> responseEntity =
			new ResponseEntity<DTO>(
					response, HttpStatus.OK);
			return responseEntity;
		
	}
	
	
}

