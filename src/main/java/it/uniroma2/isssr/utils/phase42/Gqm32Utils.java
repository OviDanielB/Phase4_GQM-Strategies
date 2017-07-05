package it.uniroma2.isssr.utils.phase42;

import it.uniroma2.isssr.exception.ActivitiGetException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

/**
 * <p>Title: Gqm32Utils</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Classe di metodi statici di utilità per l'intera applicazione
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@Component
public class Gqm32Utils {
	
	@Autowired
	GlobalConst globalConst;
	//private static final String ADDRESS = "http://qips-test.sweng.uniroma2.it";
	// TODO remove hardcoded
	private static final String ADDRESS = "http://localhost:8080";
	
	public static HttpHeaders customHeaderBasicAuth(){
		String username = GlobalConst.ACTIVITI_USERNAME;
		String password = GlobalConst.ACTIVITI_PASSWORD;
		String plainCreds = username+":"+password;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		return headers;
	
	}
	/**
	 * Metodo per effettuare delle GET attraverso restTemplate verso Activiti.
	 * @param headers
	 * @param URL
	 * @return
	 * @throws ActivitiGetException
	 */
	public static ResponseEntity<String> getHttpRestTemplate(HttpHeaders headers,
                                                             String URL) throws ActivitiGetException{
		RestTemplate template=new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		  ResponseEntity<String> response =
				  template.exchange(URL, HttpMethod.GET, request, String.class);
		  /*visto che activiti rest restituisce 200 quando una get va a 
		    buon fine discrimino i casi di successo o insuccesso di una chiamata 
		    Rest qui con questo controllo (in realtà è un controllo
		    di sicurezza, perché un gestore delle eccezioni è 
		    comunque già presente, quindi se fallisce la chiamata rest in 
		    ogni caso scatta prima l'azione del gestore delle eccezioni per 
		    quella specifica eccezione)*/
		  if (response.getStatusCode() == HttpStatus.OK) {
			  return response;
		  }
		  else throw new ActivitiGetException(response.getStatusCode().value(), ""
		  		+ "GET HTTP Rest template error to Activiti Rest"); 
		
	}
	/**
	 * 
	 * Metodo per effettuare delle POST attraverso restTemplate verso Activiti.
	 * 
	 * @param headers
	 * @param URL
	 * @param request, che in questa versione è un JSONObject
	 * @return
	 */
	public static ResponseEntity<String> postHttpRestTemplate(HttpHeaders headers, String URL,
                                                              JSONObject request){
		
			RestTemplate template=new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		
			ResponseEntity<String> response =
					template.exchange(URL, HttpMethod.POST, entity, String.class);
			return response;
	}
	
	/**
	 * 
	 * Metodo per effettuare delle POST attraverso restTemplate verso Activiti.
	 * @param headers
	 * @param URL
	 * @param request, che in questa versione è un JSONArray
	 * @return
	 */
	public static ResponseEntity<String> postHttpRestTemplate(HttpHeaders headers, String URL,
                                                              JSONArray request){
		
			RestTemplate template=new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		
			ResponseEntity<String> response =
					template.exchange(URL, HttpMethod.POST, entity, String.class);
			return response;
	}
	
	public static String basicUrlCompositor(String serverIp, String port){
		String url = GlobalConst.root + serverIp+":"+port;
		return url;
	}
	
	public static String urlCompositor(String serverIp, String port, String path){
		String base = GlobalConst.root + serverIp+":"+port+"/";
		return base + path;
	}
	
	public static String urlActivitiRest(){
		return GlobalConst.root + GlobalConst.ACTIVITI_REST_IP +":"+ GlobalConst.ACTIVITI_REST_PORT
				+GlobalConst.actRest;
	}
	
	public static String urlActivitiExplorer(){
		return GlobalConst.root + GlobalConst.ACTIVITI_EXPLORER_IP +":"+
				GlobalConst.ACTIVITI_EXPLORER_PORT;
	}
	
	public static String urlGqm3141(){
		return GlobalConst.root + GlobalConst.GQM3141_IP +":"+ GlobalConst.GQM3141_PORT;
	}
	
	public static Timestamp GetCurrentTimeStamp()
	{
		 java.util.Date date= new java.util.Date();
		 
		 return new Timestamp(date.getTime());
	    
	}
}
