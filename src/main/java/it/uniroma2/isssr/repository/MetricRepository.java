package it.uniroma2.isssr.repository;

import it.uniroma2.isssr.model.Metric;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * MongoRepository for Metric
 *
 */
public interface MetricRepository extends MongoRepository<Metric, String> {
	
}