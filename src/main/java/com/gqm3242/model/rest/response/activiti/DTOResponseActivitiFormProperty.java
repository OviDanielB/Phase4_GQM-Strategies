package com.gqm3242.model.rest.response.activiti;

import com.gqm3242.model.activiti.form.ActivitiFormPropertyEnumValue;
import com.gqm3242.model.rest.DTO;

import java.util.List;

public class DTOResponseActivitiFormProperty extends DTO{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	  private String name;
	  private String type;
	  private Object value;
	  private boolean readable;
	  private boolean writable;
	  private boolean required;
	  private String datePattern;
	  private List<ActivitiFormPropertyEnumValue> enumValues;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public boolean isReadable() {
		return readable;
	}
	public void setReadable(boolean readable) {
		this.readable = readable;
	}
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getDatePattern() {
		return datePattern;
	}
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	public List<ActivitiFormPropertyEnumValue> getEnumValues() {
		return enumValues;
	}
	public void setEnumValues(List<ActivitiFormPropertyEnumValue> enumValues) {
		this.enumValues = enumValues;
	}
	  
	  

}
