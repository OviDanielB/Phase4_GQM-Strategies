package it.uniroma2.isssr.repository;

import it.uniroma2.isssr.model.MeasureTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeasureTaskRepository extends MongoRepository<MeasureTask, String> {

	public List<MeasureTask> findByTaskId(String taskId);



}
