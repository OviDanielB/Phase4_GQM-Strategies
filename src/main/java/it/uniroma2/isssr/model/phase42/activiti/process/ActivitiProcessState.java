package it.uniroma2.isssr.model.phase42.activiti.process;

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
