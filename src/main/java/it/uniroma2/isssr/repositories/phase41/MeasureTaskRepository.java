package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.MeasureTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MeasureTaskRepository extends MongoRepository<MeasureTask, String> {

	public List<MeasureTask> findByTaskId(String taskId);


}
