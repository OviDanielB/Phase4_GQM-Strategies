package com.gqm3242.model.rest.response;

import com.gqm3242.model.rest.DTO;
import com.gqm3242.validation.Countermeasure;

import java.util.List;

public class DTOResponseCountermeasures extends DTO{

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
