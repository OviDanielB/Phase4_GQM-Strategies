package it.uniroma2.isssr.repositories.phase42;

import it.uniroma2.isssr.model.phase42.VariableActiviti;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VariableActivitiRepository extends MongoRepository<VariableActiviti, String> {
	 public List<VariableActiviti> findByTaskId(String taskId);
	 public List <VariableActiviti> deleteByTaskId(String taskId);
}
