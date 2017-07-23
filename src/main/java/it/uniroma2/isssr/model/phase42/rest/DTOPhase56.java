package it.uniroma2.isssr.model.phase42.rest;

public class DTOPhase56  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String dataId;
	
	String data;
	
	String businessWorkFlowInstanceId;
	
	String strategyReference;
	
	String ontologyReference;

	String attributeMeasured;

	public String getAttributeMeasured() {
		return attributeMeasured;
	}

	public void setAttributeMeasured(String attributeMeasured) {
		this.attributeMeasured = attributeMeasured;
	}

	public String getDataId() {
		return dataId;
	}
	
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String string) {
		this.data = string;
	}
	
	public String getBusinessWorkFlowInstanceId() {
		return businessWorkFlowInstanceId;
	}
	
	public void setBusinessWorkFlowInstanceId(String businessWorkFlowInstanceId) {
		this.businessWorkFlowInstanceId = businessWorkFlowInstanceId;
	}
	
	public String getStrategyReference() {
		return strategyReference;
	}
	
	public void setStrategyReference(String strategyReference) {
		this.strategyReference = strategyReference;
	}
	
	public String getOntologyReference() {
		return ontologyReference;
	}
	
	public void setOntologyReference(String ontologyReference) {
		this.ontologyReference = ontologyReference;
	}
	
	

}
