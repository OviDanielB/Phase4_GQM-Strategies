
package it.uniroma2.isssr.dto;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "businessWorkflowProcessInstanceId", "issueMessage", "issueMessageResources" })
public class IssueMessage {

	@JsonProperty("businessWorkflowProcessInstanceId")
	private String businessWorkflowProcessInstanceId;
	@JsonProperty("taskId")
	private String taskId;
	@JsonProperty("messageContent")
	private String messageContent;
	@JsonProperty("messageType")
	private String messageType;
	@JsonProperty("issueMessageResources")
	private List<IssueMessageResource> issueMessageResources = new ArrayList<IssueMessageResource>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**

	 *
	 * @return The businessWorkflowProcessInstanceId
	 */
	@JsonProperty("businessWorkflowProcessInstanceId")
	public String getBusinessWorkflowProcessInstanceId() {
		return businessWorkflowProcessInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 *
	 * @param businessWorkflowProcessInstanceId
	 *            The businessWorkflowProcessInstanceId
	 */
	@JsonProperty("businessWorkflowProcessInstanceId")
	public void setBusinessWorkflowProcessInstanceId(String businessWorkflowProcessInstanceId) {
		this.businessWorkflowProcessInstanceId = businessWorkflowProcessInstanceId;
	}


	/**
	 * 
	 * @return The issueMessageResources
	 */
	@JsonProperty("issueMessageResources")
	public List<IssueMessageResource> getIssueMessageResources() {
		return issueMessageResources;
	}

	/**
	 * 
	 * @param issueMessageResources
	 *            The issueMessageResources
	 */
	@JsonProperty("issueMessageResources")
	public void setIssueMessageResources(List<IssueMessageResource> issueMessageResources) {
		this.issueMessageResources = issueMessageResources;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
