package it.uniroma2.isssr.model.phase42.rest.response;

import it.uniroma2.isssr.model.phase41.MetricTask;
import it.uniroma2.isssr.model.phase42.rest.DTO;

import java.util.List;

public class DTOResponseMetricTask extends DTO {

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
