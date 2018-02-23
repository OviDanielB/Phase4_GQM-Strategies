package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.XmlWorkflow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * MongoRepository for WorkflowData
 *
 */
public interface XmlWorkflowRepository extends MongoRepository<XmlWorkflow, String> {

    public List<XmlWorkflow> findByName(String name);

}
