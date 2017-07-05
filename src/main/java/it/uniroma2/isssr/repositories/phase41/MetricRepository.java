package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.Metric;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * MongoRepository for Metric
 *
 */
public interface MetricRepository extends MongoRepository<Metric, String> {
	
}