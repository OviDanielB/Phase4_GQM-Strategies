package it.uniroma2.isssr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.uniroma2.isssr.model42.ValidationOp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class MeasureTask {
	@Id
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String _id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String taskId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Metric metric;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	// automatic or manual
	private String means;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	// who is responsible to measure
	private String responsible;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String source;
	@DBRef
	private List<ValidationOp> validationIdList;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public String getMeans() {
		return means;
	}

	public void setMeans(String means) {
		this.means = means;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public List<ValidationOp> getValidationIdList() {
		return validationIdList;
	}

	public void setValidationIdList(List<ValidationOp> validationIdList) {
		this.validationIdList = validationIdList;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}