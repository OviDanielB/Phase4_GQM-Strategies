package com.gqm3242.model.rest;

import com.gqm3242.model.activiti.task.ActivitiTaskVariable;

import java.util.List;

public class DTOActivitiTaskVariables extends DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	List<ActivitiTaskVariable> activitiTaskVariables;


	public List<ActivitiTaskVariable> getActivitiTaskVariables() {
		return activitiTaskVariables;
	}


	public void setActivitiTaskVariables(List<ActivitiTaskVariable> activitiTaskVariables) {
		this.activitiTaskVariables = activitiTaskVariables;
	}
	
	
}
