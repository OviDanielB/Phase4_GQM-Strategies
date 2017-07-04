package com.gqm3242.repositories;

import com.gqm3242.model.Metric;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MetricRepository extends MongoRepository<Metric, String> {
	
}