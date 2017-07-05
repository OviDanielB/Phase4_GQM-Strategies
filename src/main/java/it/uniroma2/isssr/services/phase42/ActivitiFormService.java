package it.uniroma2.isssr.services.phase42;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.exception.ActivitiGetException;
import it.uniroma2.isssr.exception.ActivitiPostException;
import it.uniroma2.isssr.model.phase42.activiti.form.ActivitiFormVariableProperty;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ActivitiFormService {
	
	/**
	 * Metodo che restituisce tutte le properties di un task a partire dal suo TaskId
	 * @param username
	 * @param taskId
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ActivitiGetException
	 */
	public ResponseEntity<?> getActiviFormTaskById(String taskId)
			throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException;

	public ResponseEntity<?> submitFormDataAndCompleteTask(String taskId,
                                                           List<ActivitiFormVariableProperty> activitiFormVariableProperties) throws ActivitiPostException;

}
