package it.uniroma2.isssr.services.phase42.implementation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormVariableProperty;
import it.uniroma2.isssr.services.phase42.ActivitiFormService;
import it.uniroma2.isssr.exception.ActivitiGetException;
import it.uniroma2.isssr.exception.ActivitiPostException;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormProperty;
import it.uniroma2.isssr.model.phase42.rest.DTO;
import it.uniroma2.isssr.model.phase42.rest.response.activiti.DTOResponseActivitiFormProperty;
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
