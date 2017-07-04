package com.gqm3242.repositories;

import com.gqm3242.model.ValidationOp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ValidationOpRepository extends MongoRepository<ValidationOp, String> {
	/*TODO: metodi necessari: find by measure...*/
}
