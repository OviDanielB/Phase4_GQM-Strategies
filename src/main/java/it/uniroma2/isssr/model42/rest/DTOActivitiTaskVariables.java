package it.uniroma2.isssr.model42.rest;

import it.uniroma2.isssr.model42.activiti.task.ActivitiTaskVariable;

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
