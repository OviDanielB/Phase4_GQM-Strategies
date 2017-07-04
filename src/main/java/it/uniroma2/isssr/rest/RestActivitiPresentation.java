package it.uniroma2.isssr.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.uniroma2.isssr.Exception.*;
import it.uniroma2.isssr.model42.activiti.form.ActivitiFormVariableProperty;
import it.uniroma2.isssr.model42.activiti.task.ActivitiTaskVariable;
import it.uniroma2.isssr.model42.rest.DTOActivitiTaskVariable;
import it.uniroma2.isssr.model42.rest.DTOUpdateActivitiTask;
import it.uniroma2.isssr.model42.rest.response.DTOResponseActivitiTaskVariable;
import it.uniroma2.isssr.model42.rest.response.activiti.DTOResponseActivitiProcess;
import it.uniroma2.isssr.model42.rest.response.activiti.DTOResponseActivitiTask;
import it.uniroma2.isssr.controller42.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>Title: RestActivitiPresentation</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * 
 * Classe che si occupa di esporre i servizi Rest con cui l'applicazione comunica
 * con Activiti, attraverso Activiti-Rest, al mondo esterno.
 * Tra i vari servizi esposti, fondamentalmente si annoverano:
 * - quelli relativi agli utenti, di cui in ogni caso ci si è serviti di quelli dell'altra applicazione,
 * la gqm3141. 
 * - quelli relativi agli User Tasks;
 * - quelli relativi ai processi;
 * - quelli relativi alle form-properties. 
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@RestController
@RequestMapping("/activiti/")
public class RestActivitiPresentation {
	
	/**
	 * Iniezione attraverso l'annotazione autowired della dipendenza da Service
	 * che si occupa di offrire una implementazione dei servizi rest offerti
	 * di gestione ed interazione con i task di Activiti.
	 * */
	@Autowired
	ActivitiTaskService activitiTaskService;
	
	/**
	 * Iniezione attraverso l'annotazione autowired della dipendenza da Service
	 * che si occupa di offrire una implementazione dei servizi rest offerti
	 * di gestione ed interazione con i processi di Activiti.
	 * */
	@Autowired
	ActivitiProcessService activitiProcessService;
	
	/**
	 * Iniezione attraverso l'annotazione autowired della dipendenza da Service
	 * che si occupa di offrire una implementazione dei servizi rest offerti
	 * di gestione ed interazione con i form di Activiti.
	 * */
	@Autowired
	ActivitiFormService activitiFormService;
	
	/**
	 * Iniezione attraverso l'annotazione autowired della dipendenza da Service
	 * che si occupa di offrire una implementazione dei servizi rest offerti
	 * di gestione ed interazione con gli utenti di Activiti.
	 * */
	@Autowired
	ActivitiUserService activitiUserService;
	
	/**
	 * Iniezione attraverso l'annotazione autowired della dipendenza da Service
	 * che si occupa di offrire una implementazione dei servizi rest offerti
	 * all'applicazione della grafica concordati con il gruppo gqm3141. Tali servizi
	 * sono stati precedentemente offerti in altro modo, in maniera provvisoria,
	 * e solo alla fine si è trovato un accordo per operare una azione condivisa,
	 * allineandosi secondo richiesto dal buon senso.
	 * */
	@Autowired
	Gqm3141Service gqm3141Service;
	
	@Autowired
	ActivitiProcessService processService;
	
	/********************************************************************************************/
	/** SERVIZI REST DI ACTIVITI
	 * 
	 *
	 * Servizio Rest utilizzato per richiedere l'elenco di tutti gli Users 
	 * di Activiti-Explorer. E' stato il primo servizio offerto per testare una 
	 * interazione con Activiti, al tempo in cui non esisteva una web application
	 * per la grafica condivisa, e si pensava di dover gestire autonomamente gli
	 * utenti all'interno dell'applicazione. Tale operazione è stata offerta 
	 * dal gruppo gqm3141, ma per sviluppi di questa applicazione, potrebbe
	 * esser utile mantenere tale servizio rest.
	 * 
	 * @return Lista degli utenti presenti all'interno di Activiti
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException
	 */
	@RequestMapping(value="/users", method = RequestMethod.GET)
	   public ResponseEntity<?> getUsers() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
			return activitiUserService.getUsers();
	}
	/**
	 * Servizio Rest utilizzato per richiedere l'elenco di tutti gli User Task 
	 * presenti nel MySQL di Activiti-Explorer. E' stato il secondo servizio rest
	 * offerto. Potrebbe essere utile tirare su l'elenco completo di tutti i task, mageri
	 * filtranodolo secodondo qualche particolare criterio, per poi successivamente renderne
	 * disponibile solo una sottoparte ad un utente finale. Potrebbe
	 * anche essere una funzionalità per cui un Gqm-expert (execution manager)
	 * potrebbe voler sapere quanti utenti utenti stanno operando in activiti,
	 * cosa stanno facendo.
	 * 
	 * Potrebbe anche essere utilizzato per sapere se un utente è uno user di Activiti,
	 * o di gqm, avendo precedentemente fatto un porting degli utenti di gqm in Activiti
	 * (di questo si è occupato la fase 3.1/4.1). 
	 * 
	 * @return Lista degli UserTasks presenti nel DB MySQL di Activiti.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 */
	@RequestMapping(value="/userTasks", method = RequestMethod.GET)
	   public ResponseEntity<?> getTasks() throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
			return activitiTaskService.getTasks();
	}
	/**
	 * 
	 * Servizio Rest utilizzato per richiedere l'elenco di tutti gli User 
	 * Tasks presenti nel MySQL di Activiti-Explorer assegnati all'utente {username}
	 * 
	 * Sempre nella logica degli User Task, è un servizio pensato per un utente loggato
	 * all'interno dell'applicazione, che vuole poter visualizzare unicamente i suoi
	 * User Task per poterli completare. 
	 * 
	 * @param username : Lo username dell'utente di cui si vuole i task
	 * @return : La lista degl User Task di cui in Activiti è assignee l'utente username
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 */
	@RequestMapping(value="/userTasks/{username}",
			method = RequestMethod.GET)
	   public ResponseEntity<?> getUserTasks(@PathVariable("username") String username) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
			return activitiTaskService.getUserTasks(username);
	
	}
	/**
	 * Servizio Rest che restituisce tutte le informazioni relative allo User Task 
	 * di Activiti. E' un utilissimo servizio esposto, soprattutto per 
	 * conoscere il taskDefitionKey di quello User Task, di cui il taskId non
	 * è altri che il numero di istanza a runtime (che quindi non è una informazione
	 * permanente, ma precaria, che si perderà al completamento del task). 
	 * E' anche utile per conoscere l'assignee di quel task.
	 * 
	 * @param taskId: id dell'istanza a runtime dello User Task di Activiti
	 * @return Un body che contiene tutte le info relative a quel task.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 */
	@RequestMapping(value="/userTaskByTaskId/{taskId}",
			method = RequestMethod.GET)
	   public ResponseEntity<DTOResponseActivitiTask> getUserTaskByTaskId(
			   @PathVariable("taskId") String taskId) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
			return activitiTaskService.getUserTaskByTaskId(taskId);
	
	}
	
	/**
	 * Servizio Rest per richiedere la lista degli User Task assegnati ad un certo 
	 * CandidateGroup. Questo servizio rest è pensato per conoscere un task 
	 * di cui poter fare il claim nell'interfaccia comune, se magari quel task
	 * non dovesse avere un assignee, ma se però, come si è pensato potesse 
	 * verificarsi, esso abbia un candidate group associato, in funzione del ruolo
	 * di quell'utente di gqm. (E' qui si rende fortemente necessaria l'informazione
	 * relativa al mapping degli utenti di gqm su quelli di Activiti, offerta 
	 * dal gruppo gqm3141)
	 * 
	 * @param candidateGroup : ovvero un gruppo di Activiti a cui è assegnato uno User Task
	 * @return Lista degli user tasks assegnati ad un certo candidateGroup
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 */
	@RequestMapping(value="/userTasksByCandidateGroup/{candidateGroup}",
			method = RequestMethod.GET)
	   public ResponseEntity<DTOResponseActivitiTask> userTasksByCandidateGroup(
			   @PathVariable("candidateGroup") String candidateGroup) throws JsonParseException, JsonMappingException, IOException, ActivitiGetException{
			return activitiTaskService.getUserTasksByCandidateGroup(candidateGroup);
	
	}
	

	/**
	 * Servizio per richiedere lo stato attuale di un processo in esecuzione. 
	 * In questo servizio era di particolare interesse per un task executor
	 * conoscere la posizione del task all'interno di un processo. In generale
	 * esso restituisce lo stato di un processo di Activiti attraverso una immagine
	 * png, quindi effettivamente ha una portata più ampia.
	 * 
	 * @param id : id di runtime del processo di cui si vuole conoscere lo stato corrente
	 * @return Una immagine in formato png dello stato del processo
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="/processInstanceState/{id}",
			method = RequestMethod.GET)
	   public HttpEntity<byte[]> getProcessInstanceState(@PathVariable("id") String id) throws IOException
	{
			return activitiProcessService.getProcessInstanceState(id);
	}
	
	/**
	 * Servizio Rest utilizzato per aggiornare lo stato di uno User Task 
	 * di taskId {id} attraverso una azione di tipo action = complete
	 * senza variabili (n.b. le variabili non servono se non si sospende l'azione, ad
	 * un utente generico nella fase 3 ). Questa soluzione era sta quella precedentemente
	 * adottata. Non si prevedeva l'inserimento di un form, e pertanto per completare un 
	 * task era sufficiente una post con una azione di tipo complete.
	 * 
	 * Si passa il seguente body:
	 * {
	 * 		"action": "complete",
	 * 		"variables" = [] 
	 * }
	 * 
	 * @param id : id di runtime dello User Task
	 * @param dtoUpdateActivitiTask : ovvero il body della post
	 * 
	 * @return: no content (in teoria è intuitivo perché non deve poter 
	 * tornare nulla il completamento di un task..  )
	 */
	   
	@RequestMapping(value = "/userTasks/{id}",
			method = RequestMethod.POST)
	public ResponseEntity<DTOResponseActivitiTask> completeUserTasksWithoutVariables(
            @PathVariable("id") String id, @RequestBody DTOUpdateActivitiTask dtoUpdateActivitiTask) {
		return activitiTaskService.updateActivitiTask(id, 
				dtoUpdateActivitiTask.getAction());
	}
	
	/**
	 * Servizio Rest utilizzato per aggiornare una variabile già esistente di Activiti
	 * Si passa il body della variabile con la quale si intende aggiornare quella 
	 * esistente
	 * 
	 * Il body da passare è il seguente
	 * {
	 * 		"name" : "myTaskVariable",
	 * 		"scope" : "local",
	 * 		"type" : "string",
	 * 		"value" : "Hello my friend"
	 * 	}
	 * 
	 * @param id
	 * @param dtoUpdateActivitiTask
	 * @return la variabile aggiornata
	 * @throws ActivitiPutException
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "/updateUserTaskVariable/{id}/{name}",
			method = RequestMethod.PUT)
	public ResponseEntity<DTOResponseActivitiTaskVariable> updateUserTaskVariable(
            @PathVariable("id") String id, @PathVariable("name") String name,
            @RequestBody DTOActivitiTaskVariable dtoUpdateActivitiTaskVariable) throws JsonParseException, JsonMappingException, IOException, ActivitiPutException {
		return activitiTaskService.updateActivitiTaskVariable(id, name,
				dtoUpdateActivitiTaskVariable);
	}

			
	/**
	 * Servizio Rest che automatizza il processo di completamento del task da parte 
	 * dell'utente, ovvero non passo le variabili (lo User Task già ce le ha settate 
	 * come locali o globali). Questo perché senza una gestione condivisa con la fase
	 * 3.1/4.1 del problema del riassegnamento e del completamento di un task, 
	 * l'operazione poteva essere risolta in diversi modi. Qui si fornisce una
	 * soluzione alternativa al problema. 
	 * In pratica è un ampliamento del servizio /userTasks/{id}. Qui quello che si 
	 * fa è semplicemente quello di passare come body:
	 * 
	 * {
	 * 		"action": "complete",
	 * 		"variables" = [] 
	 * }
	 * ma quando con Rest Template si fa la chiamata rest ad Activiti, 
	 * si invierà un body:
	 *
	 * {
	 * 		"action": "complete",
	 * 		"variables" = [{variabile1}, {variabile2},{variabile3}] 
	 * }
	 * dove le variabili vengono recuperate in maniera opportuna, e 
	 * passate nel JsonBody.
	 * 
	 * @param id: id runtime dello User Task in Activiti
	 * @param dtoUpdateActivitiTask : ovvero il body della post
	 * @return ...
	 * @throws IOException 
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws ActivitiGetException 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/completeUserTasksWithStoredTaskVariables/{id}",
			method = RequestMethod.POST)
	public ResponseEntity<?> completeUserTasksWithStoredVariables(
			  @PathVariable("id") String id,
			  @RequestBody DTOUpdateActivitiTask dtoUpdateActivitiTask) throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
		return activitiTaskService.completeUserTasksWithStoredVariables(id, 
				dtoUpdateActivitiTask.getAction());
	}
	
	/**
	 * Servizio Rest che restituisce l'elenco delle FormProperties di un Task. Serve
	 * per avere nome, tipo e altre informazioni del form associato ad uno User Task
	 * da un utente di Activiti-Explorer. 
	 * Questo servizio è introdotto per gestire i form properties. Sono dei forms
	 * che sono definiti in fase di editing dello User Task e servono per
	 * salvare dei valori chiave utili all'interno dell' intero processo.
	 * Si rende utile conoscere quindi quante properties esistono e vanno
	 * completate da un utente che ha un frontend in cui visualizza la propria 
	 * schermata per completare il proprio User Task
	 * 
	 * @param taskId: id runtime dello User Task in Activiti
	 * @return Lista delle form properties
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/formDataTask/{taskId}",
			method = RequestMethod.GET)
	  public ResponseEntity<?> getFormDataFromRuntimeTaskId(
			  @PathVariable("taskId") String taskId)
					  throws JsonParseException, JsonMappingException,
					  IOException, JSONException, ActivitiGetException {
			return activitiFormService.getActiviFormTaskById(taskId);
	  }
	/**
	 * Servizio Rest che restituisce tutte le variabili di un Task a partire 
	 * dal suo {taskId}. 
	 * Ad uno User Task possono essere assegnate delle variabili all'interno di
	 * Activiti. Infatti può essere utile ad esempio assegnare una variabile
	 * di stato per poter riconoscere la fase di uno User Task all'interno di un
	 * ciclo di vita più ampio rispetto a quello che normalmente segue un generico
	 * User Task di un task executor: è il caso ad esempio in fase 4 di ciò che
	 * accade ad uno User Task che segue l'esecuzione dei piani di misura
	 * e di validazione, passando per diverse fasi di vita.
	 * 
	 * Inoltre una form properties una volta completata, essa stessa genera
	 * variabili di scope global, se è stato fatto un submit di quelle form properties
	 * e inoltre se essere erano definite nell'editing dello User Task come generatrici
	 * di una variabile ad esse associate.
	 * 
	 * 
	 * @param taskId : id runtime dello User Task in Activiti
	 * @param scope : scope delle variabili, se local o global
	 * @return Lista delle variabili presenti all'interno di Activiti assegnate a quello
	 * 		specifico User Task avente taskId {taskId} e con scope {scope}
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 * @throws JSONException
	 */
	
	@RequestMapping(value = "/taskVariables/{taskId}/{scope}",
			method = RequestMethod.GET)
	  public ResponseEntity<?> getTaskVariablesFromRuntimeTaskId(
			  @PathVariable("taskId") String taskId,
			  @PathVariable("scope") String scope)
					  throws JsonParseException, JsonMappingException, IOException, JSONException, ActivitiGetException {
			return activitiTaskService.getTaskVariablesFromRuntimeTaskId(taskId,scope);
	  }
	
	/**
	 * Servizio rest che consente di creare nuove variabili da assegnare ad un task
	 * a partire dal suo {taskId}. Questo servizio è molto utile 
	 * per creare le variabili di stato da poter assegnare ad un task in fase 4 ad esempio
	 * per gestire il ciclo di vita del riassegnamento di un task, ma può essere utile 
	 * per qualsiasi circostanza per cui si ritenga necessario creare variabili.
	 * Ad esempio il suo impiego rientrava in una soluzione proposta per lo
	 * storage delle form properties di un task di cui non si voleva fare subito 
	 * il completamento, ma appunto il riassegnamento. Infatti inserire le 
	 * variabili con scope global, e poi fare la complete del task con le variabili
	 * già settate, consente di completare il task facendo poi diventare quelle
	 * variabili con scope global, che era un risultato analogo a quanto fatto per
	 * il submit delle formproperties.
	 * 
	 * Il body di tale chiamata rest è così fatto:
	 * 
	 * [  
   	 *	{  
	 *     	"name":"name1",
	 *     	"scope":"global",
	 *     	"type":"string",
	 *     	"value":"value1"
   	 *	},
   	 * 	{  
	 *     	"name":"name2",
	 *     	"scope":"global",
	 *     	"type":"date",
	 *     	"value":"2016-11-20T09:33:00Z"
     *	},
   	 * 	{  
     *		"name":"name3",
     * 		"scope":"global",
     * 		"type":"boolean",
     * 		"value":true
   	 * 	},
   	 *	{  
     *		"name":"name4",
     * 		"scope":"global",
     * 		"type":"long",
     * 		"value":1
   	 *	}
	 * ]
	 *
	 * Si sottolinea come non sia possibile su Activiti salvare contemporaneamente
	 * sia variabili con scope local che gloabal, perciò per fare quel salvataggio
	 * dovranno essere effettuate due chiamate rest con i rispettivi body.
	 * 
	 * @param taskId: id runtime dello User Task in Activiti
	 * @param activitiTaskVariables: il body della post, ovvero una list di task
	 * 		variables.
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	
	@RequestMapping(value = "/taskVariables/{taskId}",
			method = RequestMethod.POST)
	  public ResponseEntity<?> createTaskVariableFromRuntimeTaskId(
			  @PathVariable("taskId") String taskId,
			  @RequestBody List<ActivitiTaskVariable> activitiTaskVariables)//DTOActivitiTaskVariables dtoActivitiTaskVariables)
					  throws JsonParseException, JsonMappingException, IOException {
			
			return activitiTaskService.createTaskVariableFromRuntimeTaskId(taskId,
					activitiTaskVariables);
	  }
	
	/*****************************************************************************
	 * SERVIZI CONCORDATI CON I GRUPPO GQM3141 
	 * La necessità è quella di sospendere uno User Task per quanto riguarda la
	 * fase 4 di GQM+Strategies. Un Data Collector deve potersi inserire
	 * su quel task se è un Task di misura. Tale servizio è stato concordato 
	 * in quanto un Task Executor, attore della fase 3.2/4.2 deve poter completare
	 * uno User Task per mandare avanti l'esecuzione di un Workflow di Activiti,
	 * ma se il Task è un MeasureTask allora deve poter settare delle variabili
	 * di stato, così da segnalare all'altra fase l'ok per la gestione di quel tipo
	 * di task.
	 * Infine deve essere sempre compito di un utente della fase 3.2/4.2 di GQM+Strategies
	 * il Validator, di poter, nella fase 4, recuperare le List<ActivitiFormVariableProperty> salvate
	 * in una VariableActiviti indicizzate dal taskId di runtime, per poter completare
	 * finalmente tale UserTask
	 * 
	 *********************************************************************************/
	/**
	 * 1.SOSPENDO UNO USERTASK (OVVERO SE E' UN MEASURE TASK SALVO LE VARIABILI DEL
	 * FORM NEL DB, SETTANDO DELLE VARIABILI DI STATO, ALTRIMENTI COMPLETO QUEL TASK)
	 * Servizio Rest pensato come endpoint per chi vuole SOSPENDERE uno User Task 
	 * ma al contempo  vuole tenere in memoria variabili di un form. L'idea è quella di ricevere 
	 * variabili come una post inviate in un JSON, ma poi salvarle in MondgoDB invece di salvarle
	 * ad esempio con scope global o peggio ancora local, per poi riprenderle in un secondo momento
	 * al momento del salvataggio, e infine completare il task completandone il form (invece
	 * di utilizzare una post per completare lo User Task con una action: complete)
	 * 
	 * E' la soluzione concordata per l'interazione con il gruppo gqm3141.
	 * Viene effettuato un salvataggio su MongoDB come VariableActiviti
	 * le variabili associate che si vuole salvare, e pertanto richiama il 
	 * service gqm3141.(La variabili il cui salvataggio è contemplato sono
	 * solamente quelle in risposta ad un form, in quanto non si è prevista
	 * la creazione di variabili da parte dell'utente, sebbene ovviamente tale
	 * servizio rest sia stato comunque esposto. Pertanto nel
	 * body non sono presenti ActivitiTaskVariable, ma ActivitiFormVariableProperty )
	 * 
	 * Il body di questa chiamata è quello della submit di un form:
	 * 
	 *   [{
  	 *		"id" : "new_property_1",
  	 *		"value" : "pippo"
	 *	 },
	 *	 {
  	 *		"id": "new_property_2",
  	 *		"value": "value1"  
	 *	 },
	 *	 {
  	 *		"id" : "new_property_3",
  	 *		"value" : "11-20-2016 22:33"
	 *	 },
	 *	 {
  	 *		"id" : "new_property_4",
  	 *		"value" : true
	 *	 },
	 *	 {
  	 *		"id" : "new_property_5",
  	 *		"value" : 1
	 *	}]
	 *
	 * @param taskId : id runtime dello User Task in Activiti
	 * @param activitiFormVariableProperties : lista di variabili
	 * 		di risposta al completamento di un form in Activiti
	 * @return 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiGetException 
	 * @throws ActivitiPostException
	 * @throws ConflictException
	 * @throws AnomalySystemException
	 * @throws IdKeyNullException 
	 * @throws BodyEmptyException
	 */
	@RequestMapping(value = "/suspendTask/{taskId}",
			method = RequestMethod.POST)
	public ResponseEntity<?> suspendTaskSavingFormVariablesinDBByTaskId(
			@PathVariable("taskId") String taskId,
			  @RequestBody List<ActivitiFormVariableProperty> activitiFormVariableProperties)
					  throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException, BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException {
						return gqm3141Service.suspendTaskAndSaveFormVariablesInDB(taskId, activitiFormVariableProperties);
		
	}
	/**
	 * 2.RECUPERO LE VARIABILI DAL DB E COMPLETO DEFINITIVAMENTE IL TASKID
	 * 
	 * Servizio Rest che si occupa di completare un task attraverso una chiamata ad Activiti Rest
	 * Le variabili del form salvate nel DB vengono recuperate, e viene settato il 
	 * form Properties
	 * del task completandolo. 
	 * Si è scelto di esporre il servizio di DELETE perché il risultato visibile sull'applicazione
	 * è la rimozione dalla base dei dati dei record variables associati allo User Task di indirizzo
	 * {taskId}. Il risultato su Activiti-Explorer è quello di effettuare una POST attraverso 
	 * restTemplate ad un end point di Activiti-Rest per il completamento del form.
	 * 
	 * Anche questo servizio è stato concordato con il gruppo gqm3141, e pertanto richiama
	 * il gqm3141Service.
	 * 
	 * @param taskId : id runtime dello User Task in Activiti
	 * @return ResponseEntity con Httpstatus.NO_CONTENT in caso di successo
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws AnomalySystemException 
	 * @throws EntityNotFoundException 
	 * @throws ActivitiPostException 
	 */
	@RequestMapping(value = "/completeValidationTask/{taskId}",
			method = RequestMethod.POST)
	public ResponseEntity<?> completeTaskWithFormVariablesinDBByTaskId(
			@PathVariable("taskId") String taskId)
					  throws JsonParseException, JsonMappingException, IOException, EntityNotFoundException, AnomalySystemException, ActivitiPostException {
						return gqm3141Service.completeTaskWithFormVariablesinDBByTaskId(
								taskId);
		
	}
	
	
	/**
	 * @throws EntityNotFoundException 
	 * 
	 */
	@RequestMapping(value = "/completeUserTask/{taskId}",
			method = RequestMethod.POST)
			public ResponseEntity<?> completeUserTask(
					@PathVariable("taskId") String taskId,
					@RequestBody List<ActivitiFormVariableProperty> activitiFormVariableProperties)
					  throws JsonParseException, JsonMappingException, IOException, ActivitiGetException, ActivitiPostException, BodyEmptyException, IdKeyNullException, AnomalySystemException, ConflictException, EntityNotFoundException {
						return gqm3141Service.completeUserTask(taskId, activitiFormVariableProperties);
		
	}
	/**
	 * Servizio rest che consente di completare uno User Task di Activiti
	 * a partire dal submit del form. In Activiti si è constatato che quando si 
	 * effettua il submit del form lo User Task viene direttamente completato e 
	 * le form properties divengono variabili con scope global.
	 * 
	 * Il body di questa chiamata è il seguente:
	 * 
	 *   [{
  	 *		"id" : "new_property_1",
  	 *		"value" : "pippo"
	 *	 },
	 *	 {
  	 *		"id": "new_property_2",
  	 *		"value": "value1"  
	 *	 },
	 *	 {
  	 *		"id" : "new_property_3",
  	 *		"value" : "11-20-2016 22:33"
	 *	 },
	 *	 {
  	 *		"id" : "new_property_4",
  	 *		"value" : true
	 *	 },
	 *	 {
  	 *		"id" : "new_property_5",
  	 *		"value" : 1
	 *	}] 
	 * 
	 * @param taskId : id runtime dello User Task in Activiti
	 * @param activitiFormVariableProperties : lista di variabili
	 * 		di risposta al completamento di un form in Activiti 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ActivitiPostException 
	 */
	@RequestMapping(value = "/submitFormDataAndCompleteTask/{taskId}",
			method = RequestMethod.POST)
	  public ResponseEntity<?> submitFormDataAndCompleteTask(
            @PathVariable("taskId") String taskId, @RequestBody List<ActivitiFormVariableProperty> activitiFormVariableProperties )
					  throws JsonParseException, JsonMappingException, IOException, ActivitiPostException {
			return activitiFormService.submitFormDataAndCompleteTask(taskId, activitiFormVariableProperties);
	  }
	
	/**
	 * 
	 * Servizio di redirezione sulla pagina di Activiti-Explorer
	 * per effettuare il login
	 * 
	 * @param response
	 * @param username
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/activitiExplorerLogin/{username}/{password}", method = RequestMethod.GET)
	  public void activitiExplorerLogin(HttpServletResponse response, @PathVariable("username") String username,
                                        @PathVariable("password") String password) throws IOException {
	    response.sendRedirect("http://qips.sweng.uniroma2.it/activiti-explorer/?username="+username
	    		+"&password="+password);
	}
	    
	/**
	 * Servizio di redirezione sulla pagina del Modeler di Activiti-Explorer
	 * per editare il model42 di id {id}
	 * 
	 * @param response
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value = "/activitiExplorerModeler/{id}", method = RequestMethod.GET)
	  public void activitiExplorerModeler(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
	    response.sendRedirect("http://qips.sweng.uniroma2.it/activiti-explorer/modeler.html?modelId="+id);
	  }
	
	/**
	 * Restituisce i processi attivi
	 * @param username
	 * @param password
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value="/processes/{username}/{password}",
			method = RequestMethod.GET)
	   public ResponseEntity<DTOResponseActivitiProcess> getProcess(@PathVariable("username") String username,
																	@PathVariable("password") String password) throws JsonParseException, JsonMappingException, IOException{
			return processService.getProcess(username, password);
			
	}
	
	/***********************************************************************************************/

}
