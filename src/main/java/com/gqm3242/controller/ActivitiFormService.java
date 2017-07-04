package com.gqm3242.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gqm3242.exception.ActivitiGetException;
import com.gqm3242.exception.ActivitiPostException;
import com.gqm3242.model.activiti.form.ActivitiFormVariableProperty;
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
