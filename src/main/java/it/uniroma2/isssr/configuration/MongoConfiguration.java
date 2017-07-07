package it.uniroma2.isssr.configuration;


import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.Arrays;

/**
 * <p>Title: MongoConfiguration</p>
 *	
 * <p>Copyright: Copyright (c) 2016</p>
 * 
 * <p>Company: Dipartimento di Ingegneria Informatica, Universita' degli studi di Roma
 * Tor Vergata, progetto di ISSSR, gruppo 3: Fabio Alberto Coira, 
 * Federico Di Domenicantonio, Daniele Capri, Giuseppe Chiapparo, Gabriele Belli,
 * Luca Della Gatta</p> 
 * 
 * <p>Class description:
 * Classe di configurazione per MongoDB.
 * E' annotata con @configuration, che tagga la classe come sorgente di definizione
 * di beans per l'application context.</p> 
 * 
 * @author Fabio Alberto Coira
 * @version 1.0
 *
 */

@Configuration
class MongoConfiguration extends AbstractMongoConfiguration {
	
	/*********************************************************
	 * Sono settati gli attributi necessari alla configurazione di una istanza
	 * di 500Mb su MongoDB di MongoLab.
	 * 
	 */
	/*@Value("${spring.data.mongodb.username}") */
	protected String user = null;//null;
	/* @Value("${spring.data.mongodb.password}") */
	protected String password = null;//null;
	@Value("${host.mongodb.database}")
	protected String database;//"training";
	@Value("${host.mongodb.port}")
	protected Integer port;//27017;
	@Value("${host.mongodb.host}")
	protected String host;//"127.0.0.1";


	@Override
	protected String getDatabaseName() {
		return database;
	}


	@SuppressWarnings("deprecation")
	@Override
	public Mongo mongo() throws Exception {
		return new Mongo();
	}


	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		
		MongoClient mongoClient = null;
		ServerAddress serverAddress = new ServerAddress(host,port);
		
		
		if(user!=null){
		// Set credentials
		MongoCredential credential = MongoCredential.createCredential(user, getDatabaseName(), password.toCharArray());

		// Mongo Client
		mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
		
		}else{
			
		
		mongoClient = new MongoClient(serverAddress);
			
		}
		
		// Mongo DB Factory
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, getDatabaseName());
		
		
		return simpleMongoDbFactory;

	}


	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		
		mongoTemplate.setWriteConcern(WriteConcern.SAFE);
		
		return mongoTemplate;
	}

}


