package com.gqm3242.repositories;

import com.gqm3242.model.CollectedData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CollectedDataRepository extends MongoRepository<CollectedData, String> {
	public List<CollectedData> findByTaskId(String taskId);
}
