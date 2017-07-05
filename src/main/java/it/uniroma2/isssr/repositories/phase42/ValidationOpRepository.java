package it.uniroma2.isssr.repositories.phase42;

import it.uniroma2.isssr.model.phase42.ValidationOp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ValidationOpRepository extends MongoRepository<ValidationOp, String> {
	/*TODO: metodi necessari: find by measure...*/
}
