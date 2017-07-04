package com.gqm3242.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Title: Gqm32Properties</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Dipartimento di Ingegneria Informatica, Università degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Classe che si occupa di generare i metadati relativi alle variabili salvate 
 * in application.properties
 * </p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@Component
@ConfigurationProperties(prefix="gqm32")

public class Gqm32Properties {
	
	/** The user db. */
	private String userDB;
	
	/** The password db. */
	private String passwordDB;
	
	/** The database db. */
	private String databaseDB;
	
	/** The port db. */
	private String portDB;
	
	/** The host db. */
	private String hostDB;
	
	/** the name*/

	private String activitiUsername;
	
	private String activitiPassword;
	
	private String appIp;
	
	private String appPort;
	
	private String activitiExplorerIp;
	
	private String activitiExplorerPort;
	
	private String activitiRestIp;
	
	private String activitiRestPort;
	
	private String gqm3141Ip;
	
	private String gqm3141Port;
	
	private String gqm3141Baseurl;
	
	private String appName;
	
	private String esbPath;
	
	public static enum state {
		   MODIFIED(0), NOT_MODIFIED(1), NEW(2);
		   
		   private int value;
		    private state(int value) {
		            this.value = value;
		    }
		    public int getValue(){
		    	return value;
		    }
		    }
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getGqm3141Ip() {
		return gqm3141Ip;
	}

	public void setGqm3141Ip(String gqm3141Ip) {
		this.gqm3141Ip = gqm3141Ip;
	}

	public String getGqm3141Port() {
		return gqm3141Port;
	}

	public void setGqm3141Port(String gqm3141Port) {
		this.gqm3141Port = gqm3141Port;
	}

	public String getAppIp() {
		return appIp;
	}

	public void setAppIp(String appIp) {
		this.appIp = appIp;
	}

	public String getAppPort() {
		return appPort;
	}

	public void setAppPort(String appPort) {
		this.appPort = appPort;
	}

	public String getActivitiExplorerIp() {
		return activitiExplorerIp;
	}

	public void setActivitiExplorerIp(String activitiExplorerIp) {
		this.activitiExplorerIp = activitiExplorerIp;
	}

	public String getActivitiExplorerPort() {
		return activitiExplorerPort;
	}

	public void setActivitiExplorerPort(String activitiExplorerPort) {
		this.activitiExplorerPort = activitiExplorerPort;
	}

	public String getActivitiRestIp() {
		return activitiRestIp;
	}

	public void setActivitiRestIp(String activitiRestIp) {
		this.activitiRestIp = activitiRestIp;
	}

	public String getActivitiRestPort() {
		return activitiRestPort;
	}

	public void setActivitiRestPort(String activitiRestPort) {
		this.activitiRestPort = activitiRestPort;
	}

	public String getActivitiPassword() {
		return activitiPassword;
	}

	public void setActivitiPassword(String activitiPassword) {
		this.activitiPassword = activitiPassword;
	}

	/**
	 * Gets the user db.
	 *
	 * @return the user db
	 */
	public String getUserDB() {
		return userDB;
	}
	
	/**
	 * Sets the user db.
	 *
	 * @param userDB the new user db
	 */
	public void setUserDB(String userDB) {
		this.userDB = userDB;
	}
	
	/**
	 * Gets the password db.
	 *
	 * @return the password db
	 */
	public String getPasswordDB() {
		return passwordDB;
	}
	
	/**
	 * Sets the password db.
	 *
	 * @param passwordDB the new password db
	 */
	public void setPasswordDB(String passwordDB) {
		this.passwordDB = passwordDB;
	}
	
	/**
	 * Gets the database db.
	 *
	 * @return the database db
	 */
	public String getDatabaseDB() {
		return databaseDB;
	}
	
	/**
	 * Sets the database db.
	 *
	 * @param databaseDB the new database db
	 */
	public void setDatabaseDB(String databaseDB) {
		this.databaseDB = databaseDB;
	}
	
	/**
	 * Gets the port db.
	 *
	 * @return the port db
	 */
	public String getPortDB() {
		return portDB;
	}
	
	/**
	 * Sets the port db.
	 *
	 * @param portDB the new port db
	 */
	public void setPortDB(String portDB) {
		this.portDB = portDB;
	}
	
	/**
	 * Gets the host db.
	 *
	 * @return the host db
	 */
	public String getHostDB() {
		return hostDB;
	}
	
	/**
	 * Sets the host db.
	 *
	 * @param hostDB the new host db
	 */
	public void setHostDB(String hostDB) {
		this.hostDB = hostDB;
	}

	public String getActivitiUsername() {
		return activitiUsername;
	}

	public void setActivitiUsername(String activitiUsername) {
		this.activitiUsername = activitiUsername;
	}

	public String getGqm3141Baseurl() {
		return gqm3141Baseurl;
	}

	public void setGqm3141Baseurl(String gqm3141Baseurl) {
		this.gqm3141Baseurl = gqm3141Baseurl;
	}

	public String getEsbPath() {
		return esbPath;
	}

	public void setEsbPath(String esbPath) {
		this.esbPath = esbPath;
	}
	
	
	
	
	
}
