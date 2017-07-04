package it.uniroma2.isssr.dto.activiti.entitylist;

import it.uniroma2.isssr.dto.activiti.entity.ActivitiEntity;

import java.util.List;


public interface ActivitiEntityList {

	public List<? extends ActivitiEntity> getData();
	
	public Integer getTotal();
	
	public Integer getStart();
	
	public Integer getSize();
	
	public String getOrder();
	
	public String getSort();
}
