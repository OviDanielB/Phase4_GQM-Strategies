package it.uniroma2.isssr.controller;

import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.dto.post.PostCreateWorkflow;
import it.uniroma2.isssr.dto.post.PostDeploy;
import it.uniroma2.isssr.dto.post.PostStartProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public interface WorkflowController {

//	/**
//	 * This is the endpoint to do the creation of a workflow, with the related meta-workflow.
//	 * @param response The HttpServletResponse
//	 * @param createWorkflowBody The info about the workflow to create
//	 * @return 201 CREATED if workflow created
//	 * @throws UnsupportedEncodingException
//	 * @throws IOException
//	 * @throws IllegalCharacterRequestException
//	 * @throws ActivitiEntityAlreadyExistsException
//	 * @throws JsonRequestException
//	 * @throws MetaWorkflowNotDeployedException
//	 * @throws MetaWorkflowNotStartedException
//	 * @throws BusinessWorkflowNotCreatedException
//	 * @throws ModelXmlNotFoundException
//	 * @throws ProcessDefinitionNotFoundException
//	 * @throws JsonRequestConflictException
//	 */
//	@RequestMapping(value = "/workflows/create", method = RequestMethod.POST)
//    ResponseEntity<?> createWorkflow(HttpServletResponse response,
//                                     @RequestBody PostCreateWorkflow createWorkflowBody) throws UnsupportedEncodingException,
//			IOException, IllegalCharacterRequestException, ActivitiEntityAlreadyExistsException, JsonRequestException,
//			MetaWorkflowNotDeployedException, MetaWorkflowNotStartedException, BusinessWorkflowNotCreatedException,
//			ModelXmlNotFoundException, ProcessDefinitionNotFoundException, JsonRequestConflictException;

	/**
	 * This is the endpoint to get all workflows in Activiti
	 * @return 200 OK 
	 * @throws JsonRequestException
	 */
	@RequestMapping(value = "/workflows", method = RequestMethod.GET)
    ResponseEntity<?> getWorkflows() throws JsonRequestException;

	/**
	 * This is the endpoint to deploy a workflow model42 in Activiti if it does not exist
	 * @param deployBody The info about the workflow to deploy
	 * @return 200 OK if model42 deployed
	 * @throws JsonRequestException
	 * @throws MetaWorkflowNotDeployedException
	 * @throws ModelXmlNotFoundException
	 * @throws WorkflowDataException
	 * @throws IOException
	 * @throws ProcessDefinitionNotFoundException
	 * @throws ActivitiEntityAlreadyExistsException
	 */
	@RequestMapping(value = "/workflows/deployments", method = RequestMethod.POST)
    ResponseEntity<?> deployWorkflowModel(@RequestBody PostDeploy deployBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			WorkflowDataException, IOException, ProcessDefinitionNotFoundException, ActivitiEntityAlreadyExistsException;

	/**
	 * This is the endpoint to obtain information about the status of deployment of a processDefinition
	 * @param modelId The id of the Process Definition
	 * @return 200 OK if workflow already deployed 204 NO CONTENT if workflow not deployed yet
	 * @throws JsonRequestException
	 * @throws ActivitiEntityAlreadyExistsException
	 * @throws WorkflowDataException
	 * @throws ProcessDefinitionNotFoundException
	 */
	@RequestMapping(value = "/workflows/{modelId}/process-definition-id", method = RequestMethod.GET)
    ResponseEntity<?> getWorkflowProcessDefinitionId(@PathVariable(value = "modelId") String modelId)
			throws JsonRequestException, ActivitiEntityAlreadyExistsException, WorkflowDataException, ProcessDefinitionNotFoundException;
	
	/**
	 * This is the endpoint to start a workflow process instance
	 * @param startProcessInstanceBody The info about the process instance to start
	 * @return 200 OK if started
	 * @throws JsonRequestException
	 * @throws MetaWorkflowNotDeployedException
	 * @throws ModelXmlNotFoundException
	 * @throws MetaWorkflowNotStartedException
	 * @throws WorkflowDataException
	 * @throws JsonRequestConflictException
	 */
	@RequestMapping(value = "/workflows/processinstances", method = RequestMethod.POST)
    ResponseEntity<?> startWorkflowProcessInstance(@RequestBody PostStartProcessInstance startProcessInstanceBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			MetaWorkflowNotStartedException, WorkflowDataException, JsonRequestConflictException;

	/**
	 * This is the endpoint to check if a process definition already has a process instances, i.e. to maintain a one-to-one relation between process definitions and process instances
	 * @param businessWorkflowProcessDefinitionId
	 * @return
	 * @throws JsonRequestException
	 * @throws ActivitiEntityAlreadyExistsException
	 * @throws WorkflowDataException
	 * @throws ProcessDefinitionNotFoundException
	 */
	@RequestMapping(value = "/workflows/{businessWorkflowProcessDefinitionId}/process-instance", method = RequestMethod.GET)
    ResponseEntity<?> getWorkflowProcessInstanceId(@PathVariable(value = "businessWorkflowProcessDefinitionId") String businessWorkflowProcessDefinitionId) throws JsonRequestException,
	        ActivitiEntityAlreadyExistsException, WorkflowDataException, ProcessDefinitionNotFoundException;
	
	}
