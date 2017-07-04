package com.gqm3242.repositories.gqm3141;

import com.gqm3242.model.MeasureTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MeasureTaskRepository extends MongoRepository<MeasureTask, String> {

	public List<MeasureTask> findByTaskId(String taskId);


}
