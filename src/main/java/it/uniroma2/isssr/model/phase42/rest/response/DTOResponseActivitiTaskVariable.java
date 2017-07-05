package it.uniroma2.isssr.model.phase42.rest.response;

import it.uniroma2.isssr.model.phase42.rest.DTO;

public class DTOResponseActivitiTaskVariable extends DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String scope;
	private String type;
	private Object value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
