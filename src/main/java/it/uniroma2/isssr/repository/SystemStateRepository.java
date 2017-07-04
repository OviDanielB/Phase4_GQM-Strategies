package it.uniroma2.isssr.repository;

import it.uniroma2.isssr.model.SystemState;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoRepository for SystemState
 *
 */
public interface SystemStateRepository extends MongoRepository<SystemState, String>
{
	
	
}