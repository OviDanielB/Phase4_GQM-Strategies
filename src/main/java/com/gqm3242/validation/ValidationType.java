package com.gqm3242.validation;

public enum ValidationType {
	COMPLETENESS,
	INTERNAL_CONSISTENCY,
	PLAUSIBILITY,
	ABNORMAL_DATA,
	EXTERNAL_CONSISTENCY;
}
/**
 * se è INTERNAL_CONSISTENCY allora
 * 		è ONE o MANY
 * 			se è ONE è confronto o con misura vecchia o con l'ultimo dato inserito
 * 			
 * 			se è MANY operazione su quei dati
 * 			S
 * se è PLAUSIBILITY
 * 
 * se è ABNORMAL_DATA
 * 
 * se è EXTERNAL_CONSISTENCY
 */
