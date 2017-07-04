package com.gqm3242.repositories;

import com.gqm3242.model.VariableActiviti;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VariableActivitiRepository extends MongoRepository<VariableActiviti, String> {
	 public List<VariableActiviti> findByTaskId(String taskId);
	 public List <VariableActiviti> deleteByTaskId(String taskId);
}
