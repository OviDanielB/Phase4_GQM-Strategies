package it.uniroma2.isssr.repositories;

import it.uniroma2.isssr.model.phase2.Ontology;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ovidiudanielbarba on 19/07/2017.
 */
public interface OntologyRepository extends MongoRepository<Ontology, String> {
}
