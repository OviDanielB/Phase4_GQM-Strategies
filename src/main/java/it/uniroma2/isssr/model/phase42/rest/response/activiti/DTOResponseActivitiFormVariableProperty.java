package it.uniroma2.isssr.model.phase42.rest.response.activiti;

import it.uniroma2.isssr.model.phase42.rest.DTO;

public class DTOResponseActivitiFormVariableProperty extends DTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String  id;
	private String  value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
