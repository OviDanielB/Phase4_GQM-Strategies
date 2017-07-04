package it.uniroma2.isssr.controller;

import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.dto.EndingMessage;
import it.uniroma2.isssr.dto.IssueMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

public interface FeedbackController {

	/**
	 * This endpoint is not utilized because the issue message is read from the bus
	 * @param issueMessage
	 * @param response The HttpServletResponse
	 * @return 200 OK if the issue was correctly managed
	 * @throws WorkflowDataException
	 * @throws IssueMessageCatcherNotFoundException
	 * @throws IllegalReceiveMessageRequestBodyException
	 * @throws JsonRequestException
	 * @throws JsonRequestConflictException
	 */
	@RequestMapping(value = "/BusinessIssueMessages", method = RequestMethod.POST)
    ResponseEntity<?> receiveIssueMessage(@RequestBody IssueMessage issueMessage, HttpServletResponse response)
			throws WorkflowDataException, IssueMessageCatcherNotFoundException, IllegalReceiveMessageRequestBodyException, JsonRequestException, JsonRequestConflictException;
	
	/**
	 * This is the endpoint that receive an ending workflow message and terminate the specified workflow
	 * @param issueMessage Contains the info about the workflow wanted for terminate
	 * @param response The HttpServletResponse
	 * @return 200 OK if successfully terminated
	 * @throws WorkflowDataException
	 * @throws IssueMessageCatcherNotFoundException
	 * @throws JsonRequestException
	 * @throws IllegalReceiveMessageRequestBodyException
	 * @throws JsonRequestConflictException
	 */
	@RequestMapping(value = "/BusinessEndingMessages", method = RequestMethod.POST)
    ResponseEntity<?> receiveEndingMessage(@RequestBody EndingMessage endingMessage, HttpServletResponse response)
			throws WorkflowDataException, IssueMessageCatcherNotFoundException, JsonRequestException, IllegalReceiveMessageRequestBodyException, JsonRequestConflictException;

}
