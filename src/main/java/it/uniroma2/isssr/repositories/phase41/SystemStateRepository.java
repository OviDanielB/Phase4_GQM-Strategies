package it.uniroma2.isssr.repositories.phase41;

import it.uniroma2.isssr.model.phase41.SystemState;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoRepository for SystemState
 *
 */
public interface SystemStateRepository extends MongoRepository<SystemState, String>
{
	
	
}