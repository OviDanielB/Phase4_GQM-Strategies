package com.gqm3242.repositories;

import com.gqm3242.model.Attribute;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <p>Title: AttributeRepository</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * The Interface AttributeRepository consente il salvataggio automatico delle strategie.
 * ...
 * </p> 
 * 
 * @author Luca Della Gatta
 * @version 1.0
 *
 */

public interface AttributeRepository extends MongoRepository<Attribute, String> {

}
