package com.gqm3242.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gqm3242.model.Metric;
import com.gqm3242.model.ValidationOp;

import java.util.List;

public class DTOMeasureTask  extends DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String taskId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Metric metric;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	// automatic or manual
	private String means;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	// who is responsible to measure
	private String responsible;
	
	private List<ValidationOp> validationIdList;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public String getMeans() {
		return means;
	}

	public void setMeans(String means) {
		this.means = means;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public List<ValidationOp> getValidationIdList() {
		return validationIdList;
	}

	public void setValidationIdList(List<ValidationOp> validationIdList) {
		this.validationIdList = validationIdList;
	}
	
	
}
