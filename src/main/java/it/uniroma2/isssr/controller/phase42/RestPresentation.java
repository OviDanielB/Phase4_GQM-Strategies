
package it.uniroma2.isssr.controller.phase42;

import it.uniroma2.isssr.services.phase42.ActivitiTaskService;
import it.uniroma2.isssr.services.phase42.StrategyService;
import it.uniroma2.isssr.hermes.Bus2fase32;
import it.uniroma2.isssr.model.phase42.rest.DTOStrategy;
import it.uniroma2.isssr.model.phase42.rest.response.DTOResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: RestPresentation</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * ...
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@RestController
@RequestMapping("/strategy/")
public class RestPresentation {
	
	/** The strategy service. */
	@Autowired
	StrategyService strategyService;
	

	@Autowired
	ActivitiTaskService taskService;

	

	
	@Autowired
	Bus2fase32 bus2fase32;
	

	/********************************
	 * Questo metodo a noi non serve
	 * 
	 * @param dtoStrategy, che lego dal body della richiesta
	 * @return il messaggio di risposta
	 */
	@RequestMapping(value = "/createStrategy", method = RequestMethod.POST)
	public ResponseEntity<DTOResponseStrategy> createStrategy(@RequestBody DTOStrategy dtoStrategy) {

		return strategyService.createStrategy(dtoStrategy.getName(), dtoStrategy.getDescription(), dtoStrategy.getOrganizationalUnit(), dtoStrategy.getOrganizationalUnitId());

	}

	/**
	 * Questo metodo deve essere richiesto al bus!
	 * Ma non faccio la modifica qui, ma in strategyService.
	 *
	 * @return the strategies
	 */
	
	@RequestMapping(value = "/getStrategies", method = RequestMethod.GET)
	public ResponseEntity<DTOResponseStrategy> getStrategies() {

		return strategyService.getStrategies();

	}
	
	
	
	/**
	 * Restituisce le strategie che hanno una unità organizzativa non ancora utilizzata
	 * 
	 *
	 * @return the strategies
	 */
	
	@RequestMapping(value = "/getStrategiesFree", method = RequestMethod.GET)
	public ResponseEntity<DTOResponseStrategy> getStrategiesFree() {

		return strategyService.getStrategiesFree();

	}
	
	
	@RequestMapping(value = "/getStrategiesF2", method = RequestMethod.GET)
	public ResponseEntity<DTOResponseStrategy> getStrategiesF2() {
		strategyService.updateStrategyF1();
		return bus2fase32.getStrategies();

	}
	
}
