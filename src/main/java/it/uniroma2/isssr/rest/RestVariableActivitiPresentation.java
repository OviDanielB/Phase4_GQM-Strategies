package it.uniroma2.isssr.rest;

import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.controller42.VariableActivitiService;
import it.uniroma2.isssr.model42.rest.DTOVariableActiviti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Title: RestVariableActivitiPresentation</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * Classe che espone i metodi per gestire della variabili salvate all'interno del
 * DB ed utilizzate come storage durante il riassegnamento di uno UserTask, proprio
 * come viene operato nella fase 4. Quando uno UserTask si presenta ad un certo utente,
 * il principale problema che si verifica è quello di dover completare un form properties
 * associato ad un task, come un Workflow Developer ha pensato ed editato nel 
 * workflow a cui esso è associato. Quel form properties non può essere direttamente
 * settato in Activiti, in quanto il submit del form di uno User Task comporta anche il
 * complete di quello User Task. Invece volendolo riassegnarlo questa situazione
 * comporta una limitazione. Tra le varie soluzioni si è concordata con la fase 3 
 * quella di ricorrere ad uno storage esterno ad Activiti. Le form properties vengono
 * salvate, e solo alla fine recuperate.
 * 
 * E' evidente che esiste solo una VariabileActiviti associata ad un MeasureTask,
 * perché segue l'intero ciclo di vita di quello User Task.
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/variableActiviti/")
public class RestVariableActivitiPresentation {
	@Autowired
	VariableActivitiService variableActivitiService;
	
	/**
	 * Servizio Rest per la get di una VariableActiviti
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException
	 * @throws AnomalySystemException
	 */
	@RequestMapping(value = "/variable", method = RequestMethod.GET)
	public ResponseEntity<?> getVariableActiviti(
			@RequestParam("taskId") String taskId) throws EntityNotFoundException, AnomalySystemException {

		return variableActivitiService.getVariableActiviti(taskId);
		
	}
	/**
	 * Servizio Rest per la creazione di una VariableActiviti
	 * 
	 * @param dtoVariableActiviti
	 * @return
	 * @throws BodyEmptyException
	 * @throws IdKeyNullException
	 * @throws ConflictException
	 * @throws AnomalySystemException 
	 */
	
	@RequestMapping(value = "/variable", method = RequestMethod.POST)
	public ResponseEntity<?> createVariableActiviti(
			@RequestBody DTOVariableActiviti dtoVariableActiviti) throws BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException {

		return variableActivitiService.createVariableActiviti(dtoVariableActiviti);
		

	}
	/**
	 * Servizio Rest per l' aggiornamento di una VariableActiviti. Può
	 * essere importante in un ottica progettuale fornire questo servizio Rest
	 * per la modifica delle ActivitiFormVariableProperties da parte di un
	 * utente ad esempio diverso da chi le ha salvate.
	 * 
	 * 
	 * @param dtoVariableActiviti
	 * @return
	 */
	/*@RequestMapping(value = "/variable", method = RequestMethod.PUT)
	public ResponseEntity<?> updateVariableActiviti(
			@RequestBody DTOVariableActiviti dtoVariableActiviti) {

		return variableActivitiService.createVariableActiviti(dtoVariableActiviti);
		
	}*/
	/**
	 * Servizio Rest per la cancellazione di una VariableActiviti
	 * 
	 * @param dtoVariableActiviti
	 * @return
	 * @throws EntityNotFoundException 
	 * @throws AnomalySystemException 
	 */
	@RequestMapping(value = "/variable", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteVariableActiviti(@RequestParam("taskId") String taskId) throws EntityNotFoundException, AnomalySystemException {

		return variableActivitiService.deleteVariableActiviti(taskId);
		
	}
	
	/**
	 * Servizio Rest per ottenere tutte le VariableActiviti presenti nel DB
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllVariables", method = RequestMethod.GET,
			produces = "application/json")
	public @ResponseBody
    ResponseEntity<?> getAllVariablesActiviti() {
		
		return variableActivitiService.getAllVariablesActiviti();
		
	}
}
