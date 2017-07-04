package com.gqm3242.model.activiti.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author luca
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ActivitiProcessState {
	
	private byte[] image;
	
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
}
