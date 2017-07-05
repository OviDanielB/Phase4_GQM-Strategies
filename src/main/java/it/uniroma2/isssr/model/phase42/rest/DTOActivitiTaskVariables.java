package it.uniroma2.isssr.model.phase42.rest;

import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;

import java.util.List;

public class DTOActivitiTaskVariables extends DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	List<TaskVariable> taskVariables;


	public List<TaskVariable> getTaskVariables() {
		return taskVariables;
	}


	public void setTaskVariables(List<TaskVariable> taskVariables) {
		this.taskVariables = taskVariables;
	}
	
	
}
