package it.uniroma2.isssr.utils.phase42;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Title: GlobalConst</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Classe che funge da contenitore per costanti globalli dell'applicazione
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@Configuration
public class GlobalConst {

	// Define the logger object for this class
	private static final Logger log = LoggerFactory.getLogger(GlobalConst.class);
	

	public static String ACTIVITI_USERNAME;
	
	public static String ACTIVITI_PASSWORD;

	public static String ACTIVITI_EXPLORER_IP;
	
	public static String ACTIVITI_EXPLORER_PORT;

	public static String ACTIVITI_REST_IP;
	
	public static String ACTIVITI_REST_PORT;

	public static String GQM3141_BASEURL;
	
	public static String BUS_PATH;
	
	public static String APPLICATION_NAME;
	
	public static final String root = "http://";
	
	public static final String actRest = "/activiti-rest/service/";
	
	public static final String getActRestFormId = "form/form-data?taskId=";
	
	 public GlobalConst (){
		 
	 }
	
	@Value("${host.activiti.username}")
		public void setACTIVITI_USERNAME(String activitiUsername) {
			ACTIVITI_USERNAME = activitiUsername;
	}

	@Value("${host.activiti.password}")
	public void setACTIVITI_PASSWORD(String activitiPassword) {
		ACTIVITI_PASSWORD = activitiPassword;
	}

	@Value("${host.activiti.address}")
	public void setACTIVITI_EXPLORER_IP(String activitiExplorerIp) {
		ACTIVITI_EXPLORER_IP = activitiExplorerIp;
	}

	@Value("${host.activiti.port}")
	public void setACTIVITI_EXPLORER_PORT(String activitiExplorerPort) {
		ACTIVITI_EXPLORER_PORT = activitiExplorerPort;
	}
	
	@Value("${host.activiti.address}")
	public void setACTIVITI_REST_IP(String activitiRestIp) {
		ACTIVITI_REST_IP = activitiRestIp;
	}
	
	@Value("${host.activiti.port}")
	public void setACTIVITI_REST_PORT(String activitiRestPort) {
		ACTIVITI_REST_PORT = activitiRestPort;
	}

	
	@Value("${spring.application.name}")
	public void setAPPLICATION_NAME(String applicationName) {
		APPLICATION_NAME = applicationName;
	}


	@Value("${gqm32.gqm3141-baseurl}")
	public void setGQM3141_BASEURL(String gqm3141Baseurl) {
		GQM3141_BASEURL = gqm3141Baseurl;
	}

	@Value("${host.bus.path}")
	public void setBUS_PATH(String eSB_PATH) {
		BUS_PATH = eSB_PATH;
	}

	public void someMethod(){
		log.warn(GlobalConst.APPLICATION_NAME);
	}

}
