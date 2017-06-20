package it.uniroma2.isssr.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import it.uniroma2.isssr.Exception.ActivitiEntityAlreadyExistsException;
import it.uniroma2.isssr.Exception.BusinessWorkflowNotCreatedException;
import it.uniroma2.isssr.Exception.IllegalCharacterRequestException;
import it.uniroma2.isssr.Exception.JsonRequestException;
import it.uniroma2.isssr.Exception.MetaWorkflowNotDeployedException;
import it.uniroma2.isssr.Exception.MetaWorkflowNotStartedException;
import it.uniroma2.isssr.Exception.ModelXmlNotFoundException;
import it.uniroma2.isssr.Exception.ProcessDefinitionNotFoundException;
import it.uniroma2.isssr.Exception.StrategyDataException;
import it.uniroma2.isssr.dto.post.PostCreateWorkflow;
import it.uniroma2.isssr.dto.post.PostDeploy;
import it.uniroma2.isssr.dto.post.PostStartProcessInstance;


public interface WorkflowController {

	@RequestMapping(value = "/workflows/create", method = RequestMethod.POST)
	ResponseEntity<?> createWorkflow(HttpServletResponse response,
			@RequestBody PostCreateWorkflow createWorkflowBody) throws UnsupportedEncodingException,
			IOException, IllegalCharacterRequestException, ActivitiEntityAlreadyExistsException, JsonRequestException,
			MetaWorkflowNotDeployedException, MetaWorkflowNotStartedException, BusinessWorkflowNotCreatedException,
			ModelXmlNotFoundException, ProcessDefinitionNotFoundException;

	@RequestMapping(value = "/workflows", method = RequestMethod.GET)
	ResponseEntity<?> getWorkflows() throws JsonRequestException;

	@RequestMapping(value = "/workflows/deployments", method = RequestMethod.POST)
	ResponseEntity<?> deployWorkflowModel(@RequestBody PostDeploy deployBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			StrategyDataException, IOException, ProcessDefinitionNotFoundException;

	@RequestMapping(value = "/workflows/processinstances", method = RequestMethod.POST)
	ResponseEntity<?> startWorkflowProcessInstance(@RequestBody PostStartProcessInstance startProcessInstanceBody)
			throws JsonRequestException, MetaWorkflowNotDeployedException, ModelXmlNotFoundException,
			MetaWorkflowNotStartedException, StrategyDataException;
}
