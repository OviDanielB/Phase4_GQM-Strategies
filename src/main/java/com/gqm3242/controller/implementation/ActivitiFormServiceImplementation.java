package com.gqm3242.controller.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.activiti.rest.ActivitiInterationImplementation;
import com.gqm3242.controller.ActivitiFormService;
import com.gqm3242.exception.ActivitiGetException;
import com.gqm3242.exception.ActivitiPostException;
import com.gqm3242.model.activiti.form.ActivitiFormProperty;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
import com.gqm3242.model.rest.DTO;
import com.gqm3242.model.rest.response.activiti.DTOResponseActivitiFormProperty;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service("ActivitiFormService")
public class ActivitiFormServiceImplementation implements ActivitiFormService {
	
	@Autowired
	ActivitiInterationImplementation actitiviInterationImplementation;
	
	/**
	 * Metodo che restituisce la lista dei properties di un form task dato un taskId
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws ActivitiGetException 
	 * @throws JSONException
	 * 
	 */
	
	@Override
	public ResponseEntity<?> getActiviFormTaskById(
			String taskId) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		// TODO Auto-generated method stub
			List<ActivitiFormProperty>  activitiFormProperties = 
					actitiviInterationImplementation.getFormPropertiesTaskById(
					taskId);
			
			List<DTOResponseActivitiFormProperty> dtoResponseActivitiFormProperties = 
					new ArrayList<DTOResponseActivitiFormProperty>();
			for(ActivitiFormProperty activitiFormProperty :activitiFormProperties){
				DTOResponseActivitiFormProperty dtoResponseActivitiFormProperty =
						new DTOResponseActivitiFormProperty();
				
				dtoResponseActivitiFormProperty.setId( activitiFormProperty.getId());
				dtoResponseActivitiFormProperty.setName( activitiFormProperty.getName());
				dtoResponseActivitiFormProperty.setType( activitiFormProperty.getType());
				dtoResponseActivitiFormProperty.setValue( activitiFormProperty.getValue());
				dtoResponseActivitiFormProperty.setReadable( activitiFormProperty.isReadable());
				dtoResponseActivitiFormProperty.setWritable( activitiFormProperty.isWritable());
				dtoResponseActivitiFormProperty.setRequired( activitiFormProperty.isRequired());
				dtoResponseActivitiFormProperty.setDatePattern( activitiFormProperty.getDatePattern());
				dtoResponseActivitiFormProperty.setEnumValues(activitiFormProperty.getEnumValues());
				
				dtoResponseActivitiFormProperties.add(dtoResponseActivitiFormProperty);
			}
			ResponseEntity<List<DTOResponseActivitiFormProperty>> responseEntity = new ResponseEntity<List<DTOResponseActivitiFormProperty>>
			(dtoResponseActivitiFormProperties, HttpStatus.OK);
			return responseEntity;
		
	}

	@Override
	public ResponseEntity<?> submitFormDataAndCompleteTask(String taskId,
                                                           List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws ActivitiPostException {
		// TODO Auto-generated method stub
		    actitiviInterationImplementation.submitFormDataAndCompleteTask(taskId, activitiFormVariableProperties);
		  	
			DTO dtoResponse = new DTO();
			//Activti non ritorna un body per questa post
			ResponseEntity<DTO> responseEntity = new ResponseEntity<DTO>
			( dtoResponse, HttpStatus.OK);
			return responseEntity;
		
			
	}
}
