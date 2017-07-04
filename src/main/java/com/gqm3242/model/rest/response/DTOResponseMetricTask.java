package com.gqm3242.model.rest.response;

import com.gqm3242.model.MetricTask;
import com.gqm3242.model.rest.DTO;

import java.util.List;

public class DTOResponseMetricTask extends DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private List<MetricTask> metricTask;

	public List<MetricTask> getMetricTask() {
		return metricTask;
	}

	public void setMetricTask(List<MetricTask> metricTask) {
		this.metricTask = metricTask;
	}
	
	

}
