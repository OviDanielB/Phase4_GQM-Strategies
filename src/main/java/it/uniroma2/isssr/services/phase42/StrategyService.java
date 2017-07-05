
package it.uniroma2.isssr.services.phase42;

import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseStrategy;
import org.springframework.http.ResponseEntity;


/**
 * <p>Title: StrategyService</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * 
 * <p>Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Interfaccia per la dichiarazione di metodi che verranno invocati nella richiesta 
 * del servizio REST e restituiranno la relativa risposta. Qui si espongono
 * tutti quei metodi per interagire con le strategy</p> 
 * 
 * @author Daniele Capri, Fabio Alberto Coira
 * @version 1.0
 *
 */
public interface StrategyService {

	/**
	 * Creates the strategy.
	 *
	 * @param name the name
	 * @param description the description
	 * @return the response entity
	 */


	public ResponseEntity<DTOResponseStrategy> createStrategy(String name,
                                                              String description, String organizationalUnit, String organizationalUnitId);

	
	/* Andrebbero inseriti dei metodi per la delete e l'update!
	 * 
	 * 
	 * */
	
	/**
	 * Gets the strategies.
	 *
	 * @return the strategies
	 */
	ResponseEntity<DTOResponseStrategy> getStrategies();


	ResponseEntity<DTOResponseStrategy> getStrategy();
	ResponseEntity updateStrategyF1();
	
	/**
	 * Gets the strategies whit a not utilized organizational unit.
	 *
	 * @return the strategies
	 */
	public ResponseEntity<DTOResponseStrategy> getStrategiesFree();
	
	

}

