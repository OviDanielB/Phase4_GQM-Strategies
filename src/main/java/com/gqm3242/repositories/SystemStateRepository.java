package com.gqm3242.repositories;

import com.gqm3242.model.SystemState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemStateRepository extends MongoRepository<SystemState, String>
{
	
	
}