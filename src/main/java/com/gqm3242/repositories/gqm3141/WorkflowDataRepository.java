package com.gqm3242.repositories.gqm3141;

import com.gqm3242.model.WorkflowData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkflowDataRepository extends MongoRepository<WorkflowData, String>
{
	public List<WorkflowData> findByBusinessWorkflowModelId(String businessWorkflowModelId);
	
	public List<WorkflowData> findByBusinessWorkflowProcessDefinitionId(String businessWorkflowProcessDefinitionId);
	
	public List<WorkflowData> findByBusinessWorkflowProcessInstanceId(String businessWorkflowProcessInstanceId);

	public List<WorkflowData> findByMetaWorkflowProcessInstanceId(String metaWorkflowProcessInstanceId);

	/*@Query(value= "{'taskId':{ $elemMatch: ?0}}")
	public List<Strategy> findByTaskIdElementInMeasureTask(String _id);*/

	public List<WorkflowData> findByMeasureTasksListTaskId(String taskId);
}