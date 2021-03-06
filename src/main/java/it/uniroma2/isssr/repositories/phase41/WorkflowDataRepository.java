package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.WorkflowData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * MongoRepository for WorkflowData
 *
 */
public interface WorkflowDataRepository extends MongoRepository<WorkflowData, String> {
	public List<WorkflowData> findByBusinessWorkflowModelId(String businessWorkflowModelId);

	public List<WorkflowData> findByBusinessWorkflowProcessDefinitionId(String businessWorkflowProcessDefinitionId);

	public List<WorkflowData> findByBusinessWorkflowProcessInstanceId(String businessWorkflowProcessInstanceId);

	public List<WorkflowData> findByMetaWorkflowProcessInstanceId(String metaWorkflowProcessInstanceId);

	public List<WorkflowData> findByEnded(Boolean ended);

}