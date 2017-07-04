package com.gqm3242.model.rest.response;

import com.gqm3242.model.WorkflowData;
import com.gqm3242.model.rest.DTO;

public class DTOResponseCollectedData extends DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String _id;
	
	private WorkflowData workflowData;
	
	private String taskId;
	
	private String value;
	private boolean validated;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public WorkflowData getWorkflowData() {
		return workflowData;
	}
	public void setWorkflowData(WorkflowData workflowData) {
		this.workflowData = workflowData;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	
}
