
package it.uniroma2.isssr.dto.bus;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "businessWorkflowInstanceId", "issueMessage", "issueMessageResources" })
public class BusIssueMessage {

	@JsonProperty("businessWorkflowInstanceId")
	private String businessWorkflowInstanceId;
	@JsonProperty("messageContent")
	private String messageContent;
	@JsonProperty("messageType")
	private String messageType;
	@JsonProperty("issueMessageResources")
	private String issueMessageResources;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The businessWorkflowInstanceId
	 */
	@JsonProperty("businessWorkflowInstanceId")
	public String getBusinessWorkflowInstanceId() {
		return businessWorkflowInstanceId;
	}

	/**
	 * 
	 * @param businessWorkflowInstanceId
	 *            The businessWorkflowInstanceId
	 */
	@JsonProperty("businessWorkflowInstanceId")
	public void setBusinessWorkflowInstanceId(String businessWorkflowInstanceId) {
		this.businessWorkflowInstanceId = businessWorkflowInstanceId;
	}

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
	 * @return The issueMessageResources
	 */
	@JsonProperty("issueMessageResources")
	public String getIssueMessageResources() {
		return issueMessageResources;
	}

	/**
	 * 
	 * @param issueMessageResources
	 *            The issueMessageResources
	 */
	@JsonProperty("issueMessageResources")
	public void setIssueMessageResources(String issueMessageResources) {
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
