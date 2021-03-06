package it.uniroma2.isssr.model.phase41;

import it.uniroma2.isssr.exception.IllegalReceiveMessageRequestBodyException;
import it.uniroma2.isssr.dto.EndingMessage;
import it.uniroma2.isssr.dto.IssueMessage;
import it.uniroma2.isssr.dto.IssueMessageResource;

import java.util.List;

public class WorkflowMessage
{
	public static final String MESSAGE_EVENT_SUBSCRIPTION_NAME_ISSUE_MESSAGE = "workflowIssueMessage";

	public static final String MESSAGE_EVENT_SUBSCRIPTION_NAME_ENDING_MESSAGE = "workflowEndingMessage";

	private String businessWorkflowProcessInstanceId;
	private String activitiMessageType;
	private String messageType;
	private String messageContent;
	private String messageBody;
	
	
	public WorkflowMessage(IssueMessage issueMessage) throws IllegalReceiveMessageRequestBodyException
	{
		
		if (issueMessage.getBusinessWorkflowProcessInstanceId() == null
				|| issueMessage.getBusinessWorkflowProcessInstanceId().isEmpty()
				|| issueMessage.getMessageType() == null || issueMessage.getMessageType().isEmpty()) {
			throw new IllegalReceiveMessageRequestBodyException();
		}
		
		this.businessWorkflowProcessInstanceId = issueMessage.getBusinessWorkflowProcessInstanceId();
		this.activitiMessageType = MESSAGE_EVENT_SUBSCRIPTION_NAME_ISSUE_MESSAGE;
		this.messageType = issueMessage.getMessageType();
		this.messageContent = issueMessage.getMessageContent();
		
		List<IssueMessageResource> issueMessageResources = issueMessage.getIssueMessageResources();

		for ( IssueMessageResource issueMessageResource : issueMessageResources){
			
			String link = "\n";
			link += issueMessageResource.getName() + "\n";
			link += issueMessageResource.getUrl() + "\n";
			link += issueMessageResource.getDescription() + "\n";
			
			this.messageBody += link;
		}
	}
	
	public WorkflowMessage(EndingMessage endingMessage) throws IllegalReceiveMessageRequestBodyException
	{
		if (endingMessage.getBusinessWorkflowProcessInstanceId() == null
				|| endingMessage.getBusinessWorkflowProcessInstanceId().isEmpty()) {
			throw new IllegalReceiveMessageRequestBodyException();
		}
		
		this.businessWorkflowProcessInstanceId = endingMessage.getBusinessWorkflowProcessInstanceId();		
		this.activitiMessageType = MESSAGE_EVENT_SUBSCRIPTION_NAME_ENDING_MESSAGE;
		this.messageBody = null;
	}
	
	
	
	public String getBusinessWorkflowProcessInstanceId()
	{
		return businessWorkflowProcessInstanceId;
	}
	
	
	public String getMessageType()
	{
		return messageType;
	}
	
	
	public String getMessageBody()
	{
		return messageBody;
	}


	public String getActivitiMessageType() {
		return activitiMessageType;
	}

	public void setActivitiMessageType(String activitiMessageType) {
		this.activitiMessageType = activitiMessageType;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
}
