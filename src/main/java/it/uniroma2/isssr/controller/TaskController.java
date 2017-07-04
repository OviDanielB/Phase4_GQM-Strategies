package it.uniroma2.isssr.controller;


import it.uniroma2.isssr.Exception.BackEnd3242Exception;
import it.uniroma2.isssr.Exception.JsonRequestConflictException;
import it.uniroma2.isssr.Exception.JsonRequestException;
import it.uniroma2.isssr.dto.activiti.entity.ActivitiFormVariableProperty;
import it.uniroma2.isssr.dto.activiti.entitylist.TaskList;
import it.uniroma2.isssr.dto.post.PostClaimTask;
import it.uniroma2.isssr.dto.response.ResponseDescription;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;


public interface TaskController {
	
	/**
	 * 
	 * @param role
	 * @return
	 * @throws JsonRequestException
	 * @throws JsonRequestConflictException
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/tasks/unassigned", method = RequestMethod.GET)
	public ResponseEntity<TaskList> getUnassignedTasks(@RequestParam(value = "role") String role)
			throws JsonRequestException, JsonRequestConflictException, UnsupportedEncodingException;
	
	/**
	 * 
	 * @param assignee
	 * @return
	 * @throws JsonRequestException
	 * @throws JsonRequestConflictException
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/tasks/assigned", method = RequestMethod.GET)
	public ResponseEntity<TaskList> getAssignedTasks(
            @RequestParam(value = "assignee") String assignee) throws JsonRequestException, JsonRequestConflictException, UnsupportedEncodingException;

	/**
	 *
	 * @param taskId
	 * @return
	 * @throws JsonRequestException
	 * @throws JsonRequestConflictException
	 */
	@RequestMapping(value = "/tasks/{taskId}/claim", method = RequestMethod.POST)
	public ResponseEntity<?> claimTask(@PathVariable(value = "taskId") String taskId,
                                       @RequestBody PostClaimTask postClaimTask) throws JsonRequestException, JsonRequestConflictException;

	/**
	 *
	 * @param taskId
	 * @param variables
	 * @return
	 * @throws JsonRequestException
	 * @throws JsonRequestConflictException
	 */
	@RequestMapping(value = "/tasks/{taskId}/complete", method = RequestMethod.POST)
	public ResponseEntity<?> completeTask(@PathVariable(value = "taskId") String taskId,
                                          @RequestBody List<ActivitiFormVariableProperty> variables)
			throws JsonRequestException, JsonRequestConflictException, URISyntaxException, BackEnd3242Exception;
	
	@RequestMapping(value = "/tasks/{taskId}/description", method = RequestMethod.GET)
	public ResponseEntity<ResponseDescription> getDescriptionTask(@PathVariable(value = "taskId") String taskId)
			throws JsonRequestException, UnsupportedEncodingException;
}
