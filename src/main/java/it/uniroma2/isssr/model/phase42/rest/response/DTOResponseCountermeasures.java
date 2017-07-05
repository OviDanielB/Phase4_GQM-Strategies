package it.uniroma2.isssr.model.phase42.rest.response;

import it.uniroma2.isssr.model.phase42.rest.DTO;
import it.uniroma2.isssr.validation.Countermeasure;

import java.util.List;

public class DTOResponseCountermeasures extends DTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Countermeasure> countermeasures;

	public List<Countermeasure> getCountermeasures() {
		return countermeasures;
	}

	public void setCountermeasures(List<Countermeasure> countermeasures) {
		this.countermeasures = countermeasures;
	}
	
	
}
