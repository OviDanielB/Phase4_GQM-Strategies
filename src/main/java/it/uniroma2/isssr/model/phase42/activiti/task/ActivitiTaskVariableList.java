package it.uniroma2.isssr.model.phase42.activiti.task;

import it.uniroma2.isssr.dto.activiti.entity.TaskVariable;

import java.util.ArrayList;
import java.util.List;

public class ActivitiTaskVariableList {
	private List<TaskVariable> taskVariableList = new ArrayList<TaskVariable>();

	public List<TaskVariable> getTaskVariableList() {
		return taskVariableList;
	}

	public void setTaskVariableList(List<TaskVariable> taskVariableList) {
		this.taskVariableList = taskVariableList;
	}
	
	public void addActivitiTaskVariable(TaskVariable taskVariable){
		taskVariableList.add(taskVariable);
	}
}
