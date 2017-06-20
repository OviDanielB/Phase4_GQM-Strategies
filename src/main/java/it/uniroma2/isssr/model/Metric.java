package it.uniroma2.isssr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "name", "description", "metricatorId", "hasMax", "max", "hasMin", "min", "scaleType", "set",
		"hasUserDefinedList", "userDefinedList", "unit", "ordered", "id",
"version",
	    "tags",
	    "creationDate",
	    "lastVersionDate",
	    "state",
	    "releaseNote"})

@Document
public class Metric {

	@Id
    private String _id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> tags;
    
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private double min;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean isOrdered;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean hasUserDefinedList;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private double max;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean hasMax;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String set;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private boolean hasMin;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String state;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String elementType;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String version;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String id;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String creationDate;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String unit;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String scaleType;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String releaseNote;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> userDefinedList;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastVersionDate;
	
	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public boolean getIsOrdered() {
		return isOrdered;
	}

	public void setIsOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public boolean getHasUserDefinedList() {
		return hasUserDefinedList;
	}

	public void setHasUserDefinedList(boolean hasUserDefinedList) {
		this.hasUserDefinedList = hasUserDefinedList;
	}

	public double getMax() {
		return max;
	}
	
	public void setMax(double max) {
		this.max = max;
	}

	public boolean getHasMax() {
		return hasMax;
	}

	public void setHasMax(boolean hasMax) {
		this.hasMax = hasMax;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public boolean getHasMin() {
		return hasMin;
	}

	public void setHasMin(boolean hasMin) {
		this.hasMin = hasMin;
	}

	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getScaleType() {
		return scaleType;
	}

	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReleaseNote() {
		return releaseNote;
	}
	
	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public List<String> getUserDefinedList() {
		return userDefinedList;
	}

	public void setUserDefinedList(List<String> userDefinedList) {
		this.userDefinedList = userDefinedList;
	}

	public String getLastVersionDate() {
		return lastVersionDate;
	}

	public void setLastVersionDate(String lastVersionDate) {
		this.lastVersionDate = lastVersionDate;
	}


}
