package it.uniroma2.isssr.repository;

import it.uniroma2.isssr.model.CollectedData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectedDataRepository extends MongoRepository<CollectedData, String> {

}
