package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.CollectedData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CollectedDataRepository extends MongoRepository<CollectedData, String> {
	public List<CollectedData> findByTaskId(String taskId);
}
