package com.gqm3242.utils;

import com.gqm3242.model.CollectedData;
import com.gqm3242.model.MeasureTask;
import com.gqm3242.model.Metric;
import com.gqm3242.model.ValidationOp;
import com.gqm3242.repositories.CollectedDataRepository;
import com.gqm3242.repositories.gqm3141.MeasureTaskRepository;
import com.gqm3242.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class ValidationPhase4 {
	
	@Autowired
	CollectedDataRepository collectedDataRepository;
	
	@Autowired
	MeasureTaskRepository measureTaskRepository;
	
	private static final Logger log = LoggerFactory.getLogger(GlobalConst.class);

	public boolean validate(ValidationOp validationOp){
		
		//creazione variabili dall'oggetto ValidatioOp
		String name = validationOp.getName();
		ValidationType type = validationOp.getType();
		String description = validationOp.getDescription();
		ValidationCardinality cardinality = validationOp.getCardinality();
		ComparisonType compType = validationOp.getCompType();
		String measureTaskId = validationOp.getMeasureTaskId();
		String refMeasureTaskId = validationOp.getRefMeasureTaskId();
		String[] referenceParams = validationOp.getReferenceParams();
		List<Countermeasure> countermeasures = validationOp.getCountermeasures();
		
		DatasetOperation thisOp = validationOp.getThisOp();
		DatasetOperation refOp = validationOp.getRefOp();
		boolean userDefined = validationOp.getUserDefined();
		boolean doContromeasure = validationOp.isContromeasureDone();
		boolean result;
		//tipo di validazione
		switch(type){
			case INTERNAL_CONSISTENCY:
				result = internalConsistency(cardinality, compType, userDefined, measureTaskId,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case PLAUSIBILITY:
				result = plausibility(cardinality, compType, userDefined, measureTaskId,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case ABNORMAL_DATA:
				result = abnormalData(cardinality, compType, userDefined, measureTaskId ,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case EXTERNAL_CONSISTENCY:
				result = externalConsistency(cardinality, compType, userDefined,measureTaskId,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
		default:
			result = false;
		}
		return result;
	}

	
	
	public boolean one2One(String measureTaskId, ComparisonType compType, boolean userDefined,String[] referenceParams,String refMeasureTaskId){
	
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(measureTaskId);
		MeasureTask measureTask = measureTaskList.get(0);
		Metric metric = measureTask.getMetric();
		//se metrica è nulla errore
		if(metric==null){
			log.warn("Metrica nulla ritorno false");
			return false;
		}
		Boolean hasUserDefinedList = metric.getHasUserDefinedList();
		if(hasUserDefinedList){
			//è una stringa, quindi non la gestiamo
			log.warn("Campo stringa non gestiamo e la ritorno true");
			return true;
		}
		else{
			String set = metric.getSet();
			List<CollectedData> collectedDataList =collectedDataRepository.findByTaskId(measureTaskId);
			CollectedData thisCollectedData=null;
			
			//controllo se c'è un solo elemento di CollectedData
			/*if(collectedDataList.size()==1){
				//c'è un solo elemento e abbiamo deciso che la validazione è true 
				log.warn("C'è un sono elemento abbiamo deciso che la validazione è true");
				return true;
			}*/
			//prendo le collectedData da Validare che è sempre una
			/*else{*/
				for(int i=0;i<collectedDataList.size();i++){
					if(!collectedDataList.get(i).isValidated()){
						thisCollectedData = collectedDataList.get(i);
					}
				}
			/*}*/
//			//costruisco una lista di collecteData già validate
//			List<CollectedData> validateCollectedData = new ArrayList<CollectedData>();
//			//nel validazione oneToone prendo solo quella massima da validare
//			List<CollectedData> collectedrefDataList =collectedDataRepository.findByTaskId(refMeasureTaskId);
//
//			if(collectedrefDataList==null || collectedrefDataList.isEmpty()){
//				log.warn("dato non c'è");
//				return true;
//			}
//			for(int i=0;i<collectedrefDataList .size();i++){
//				if(collectedDataList.get(i).isValidated()){
//					validateCollectedData.add(collectedDataList.get(i));
//				}
//			}
//			
//			if(validateCollectedData.isEmpty()){
//				log.warn("lista di validateCollectedData vuota");
//
//				return true;
//			}
//			CollectedData max = maxCollectData(validateCollectedData);
//			String valueMax = max.getValue();
			
			//List<String> valueString = new ArrayList<String>();
			
			if(thisCollectedData==null){
				//qualcosa è andato storto
				log.warn("thisCollectedData è null");
				return false;
			}
			if(!userDefined){//se non sono definite dall'utente
			
			log.warn("Sono nella validazione non definita dall'utente");
			
			if(set.equals("integers")|| set.equals("reals") ){
				//costruisco una lista di collecteData già validate
				List<CollectedData> validateCollectedData = new ArrayList<CollectedData>();
				//nel validazione oneToone prendo solo quella massima da validare
				List<CollectedData> collectedrefDataList =collectedDataRepository.findByTaskId(refMeasureTaskId);

				if(collectedrefDataList==null || collectedrefDataList.isEmpty()){
					log.warn("dato non c'è");
					return true;
				}
				for(int i=0;i<collectedrefDataList .size();i++){
					if(collectedDataList.get(i).isValidated()){
						validateCollectedData.add(collectedDataList.get(i));
					}
				}
				
				if(validateCollectedData.isEmpty()){
					log.warn("lista di validateCollectedData vuota");

					return true;
				}
				CollectedData max = maxCollectData(validateCollectedData);
				String valueMax = max.getValue();
				//se è null è un problema... non è settato, forse dalla 2.2, forse dal bus, o dalla fase 3.1 e 4.1?
				log.warn("Eseguo la validazione con la mia CollectedData ovvero quella a false e l'ultimo dato di quelle true");
				return doFunctionSimpleUserNotDefine(thisCollectedData.getValue(),valueMax,compType);
			}
			
			log.warn("Qualcosa è andato male non è integers o reals");
            return false;
			}
			else{
				log.warn("Sono nella validazione definita dall'utente");

				if(set.equals("integers") || set.equals("reals") ){
					log.warn("Controllo se ha inserito un intervallo o un valore");

					if(referenceParams.length==1){
					log.warn("L'utente ha definito un valore");

				   //se è null è un problema... non è settato, forse dalla 2.2, forse dal bus, o dalla fase 3.1 e 4.1?
					return doFunctionSimpleUserDefine(thisCollectedData.getValue(),referenceParams[0],compType);
					}
					else{
					log.warn("L'utente ha definito un intervallo");
					log.warn("è possibile che referenceParams sia nullo");
					return doFunctionIntervalUserDefine(thisCollectedData.getValue(),referenceParams,compType);
						}
					}

				log.warn("Qualcosa è andato male non è integers o reals");

			return false;
			}
		}
	}
	
	

	
	public boolean many2One(String measureTaskId, ComparisonType compType, 
			boolean userDefined,DatasetOperation thisOp,String[] referenceParams,String refMeasureTaskId){
		
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(measureTaskId);
		MeasureTask measureTask = measureTaskList.get(0);
		Metric metric = measureTask.getMetric();
		if(metric==null){
			log.warn("Metrica nulla");
			return false;
		}
		Boolean hasUserDefinedList = metric.getHasUserDefinedList();
		if(hasUserDefinedList){
			//è una stringa, quindi non la gestiamo
			log.warn("Stringa quindi non gestiamo");
			return true;
		}
		
			String set = metric.getSet();
			List<CollectedData> collectedDataList =collectedDataRepository.findByTaskId(measureTaskId);
			
			if(collectedDataList== null){
				//qualcosa è aandato storto
				log.warn("collectedDataList è null");
				return false;
			}
			//controllo se c'è un solo elemento di CollectedData
			/*if(collectedDataList.size()==1){
				//c'è un solo elemento e abbiamo deciso che la validazione è true 
				log.warn("C'è un sono elemento abbiamo deciso che la validazione è true");
				return true;
			}*/
		
			List<String> valueString = new ArrayList<String>();
			
			//prendo tutte le collectedData con campo isValidate true e false
			for(int i=0;i<collectedDataList.size();i++){
	              valueString.add(collectedDataList.get(i).getValue());
	  			}
			String date = thisOpSwitch(thisOp,valueString);
			log.warn("operazione da fare è "+thisOp+"risultato è"+date);
			
		
			
			//se non sono definite dall'utente
			if(!userDefined){
				log.warn("Sono nella validazione non definita dall'utente");
				List<CollectedData> collectedDataListRef =collectedDataRepository.findByTaskId(refMeasureTaskId);

				List<CollectedData> validateCollectedData = new ArrayList<CollectedData>();

				if(collectedDataListRef==null || collectedDataListRef.isEmpty()){
					log.warn("il valore è null e ritorno true");
					return true;
				}
				//prendo tutte le collectedData con campo isValideted true
				for(int i=0;i<collectedDataListRef.size();i++){
					if(collectedDataListRef.get(i).isValidated()){
						validateCollectedData.add(collectedDataListRef.get(i));
					}
				}
				
				if(validateCollectedData.isEmpty()){
					log.warn("validateCollectedData è null");
					return true;
				}
				//prendo la collectedData massima
				CollectedData max = maxCollectData(collectedDataListRef);
				log.warn("CollectedData massima ha date"+max.getDate());
				String valueMax = max.getValue();
				if(set.equals("integers")|| set.equals("reals") ){
					//se è null è un problema... non è settato, forse dalla 2.2, forse dal bus, o dalla fase 3.1 e 4.1?
					log.warn("Eseguo la validazione con la mia CollectedData ovvero quella a false e l'ultimo dato di quelle true");
					return doFunctionSimpleUserNotDefine(date,valueMax,compType);
				}
				log.warn("Qualcosa è andato male non è integers o reals");
	            return false;
			}
			//se  definite dall'utente
			else{
				log.warn("Sone nella validazione definita dall'utente");
				if(set.equals("integers") || set.equals("reals") ){
					log.warn("Controllo se ha inserito un intervallo o un valore");
					if(referenceParams.length==1){
					log.warn("L'utente ha definito un valore");

				//se è null è un problema... non è settato, forse dalla 2.2, forse dal bus, o dalla fase 3.1 e 4.1?
					return doFunctionSimpleUserDefine(date,referenceParams[0],compType);
					}
					else{
						log.warn("L'utente ha definito un intervallo");
						log.warn("è possibile che referenceParams sia nullo");
					return doFunctionIntervalUserDefine(date,referenceParams,compType);
						}
					}
				log.warn("Qualcosa è andato male non è integers o reals");
			return false;

			
			}
	}

	public boolean one2Many(String measureTaskId, ComparisonType compType, 
			boolean userDefined,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(measureTaskId);
		MeasureTask measureTask = measureTaskList.get(0);
		Metric metric = measureTask.getMetric();
		if(metric==null){
			log.warn("Metrica nulla");
			return false;
		}
		Boolean hasUserDefinedList = metric.getHasUserDefinedList();
		if(hasUserDefinedList){
			//è una stringa, quindi non la gestiamo
			log.warn("Stringa quindi non gestiamo");
			return true;
		}
		
			String set = metric.getSet();
			List<CollectedData> collectedDataList =collectedDataRepository.findByTaskId(measureTaskId);
			if(collectedDataList== null){
				//qualcosa è aandato storto
				log.warn("collectedDataList è null");
				return false;
			}
			CollectedData thisCollectedData = null;
			//controllo se c'è un solo elemento di CollectedData
			/*if(collectedDataList.size()==1){
				//c'è un solo elemento e abbiamo deciso che la validazione è true 
				log.warn("C'è un sono elemento abbiamo deciso che la validazione è true");
				return true;
			}*/
			
			//prendo tutte le collectedData validate
			List<String> valueStringValidate = new ArrayList<String>();
			List<CollectedData> collectedDataListRef =collectedDataRepository.findByTaskId(refMeasureTaskId);
			if(collectedDataListRef ==null || collectedDataListRef.isEmpty()){
				log.warn("collectedDataListRef è null o vuoto");
				return true;
			}
			for(int i=0;i<collectedDataListRef.size();i++){
				if(collectedDataListRef.get(i).isValidated()){
					valueStringValidate.add(collectedDataListRef.get(i).getValue());
				}
			}
			if(valueStringValidate.isEmpty()){
				log.warn("valueStringValidate è vuoto");
				return true;
			}
			String daterefOp = thisOpSwitch(refOp,valueStringValidate);
			
			//prendo la collectedData non validata
			for(int i=0;i<collectedDataList.size();i++){
				if(!collectedDataList.get(i).isValidated()){
					thisCollectedData = collectedDataList.get(i);
				}
			}
			
			if(thisCollectedData==null){
				log.warn("non esiste una collectedData non validata");
				return false;
			}
			
			return doFunctionSimpleUserNotDefine(thisCollectedData.getValue(),daterefOp,compType);

			
	}

	public boolean many2Many(String measureTaskId, ComparisonType compType, 
			boolean userDefined,DatasetOperation thisOp,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		List<MeasureTask> measureTaskList = measureTaskRepository.findByTaskId(measureTaskId);
		MeasureTask measureTask = measureTaskList.get(0);
		Metric metric = measureTask.getMetric();
		if(metric==null){
			log.warn("Metrica nulla");
			return false;
		}
		Boolean hasUserDefinedList = metric.getHasUserDefinedList();
		if(hasUserDefinedList){
			//è una stringa, quindi non la gestiamo
			log.warn("Stringa quindi non gestiamo");
			return true;
		}
		
			String set = metric.getSet();
			List<CollectedData> collectedDataList =collectedDataRepository.findByTaskId(measureTaskId);
			if(collectedDataList== null){
				//qualcosa è aandato storto
				log.warn("collectedDataList è null");
				return false;
			}
			//controllo se c'è un solo elemento di CollectedData
			/*if(collectedDataList.size()==1){
				//c'è un solo elemento e abbiamo deciso che la validazione è true 
				log.warn("C'è un sono elemento abbiamo deciso che la validazione è true");
				return true;
			}*/
			//prendo tutte le collected data
		
			List<String> valueStringValidateAndNot = new ArrayList<String>();
			for(int i=0;i<collectedDataList.size();i++){
				valueStringValidateAndNot.add(collectedDataList.get(i).getValue());
	  			}
			
			String datethisOp = thisOpSwitch(thisOp,valueStringValidateAndNot);
			
			List<String> valueStringValidate = new ArrayList<String>();
			
			collectedDataList = collectedDataRepository.findByTaskId(refMeasureTaskId);
			
	        if(collectedDataList== null || collectedDataList.isEmpty()){
	        	log.warn("collectedDataList è vuota o nulla");
	        	return true;
	        }
			for(int i=0;i<collectedDataList.size();i++){
				if(collectedDataList.get(i).isValidated()){
					valueStringValidate.add(collectedDataList.get(i).getValue());
				}
			}
			if(valueStringValidate.isEmpty()){
				log.warn("valueStringValidate è vuota");
				return true;
			}
			String daterefOp = thisOpSwitch(refOp,valueStringValidate);

			return doFunctionSimpleUserNotDefine(datethisOp,daterefOp,compType);


	}
	
	public String thisOpSwitch(DatasetOperation thisOp,List<String> values){

		switch(thisOp){
//		SUM,
//		AVERAGE,
//		VARIANCE,
//		CONFIDENCE_INTERVAL;
		case SUM:
			log.warn("thisOpSwitch SUM");
			return sum(values);
		case AVERAGE:
			log.warn("thisOpSwitch AVERAGE");
			return average(values);
		case VARIANCE:
			log.warn("thisOpSwitch VARIANCE");
			return variance(values);
		case CONFIDENCE_INTERVAL:
			log.warn("thisOpSwitch CONFIDENCE INTERVAL");
			return confidenceInterval(values);
		}
		log.warn("thisOpSwitch QUALCOSA NON VA");
		return null;
		
	}
	
	public boolean doFunctionSimpleUserNotDefine(String thisCollectedData,String valueString,ComparisonType compType)
	{
		switch(compType){
//		LESS_THAN,
//		GREATER_THAN,
//		EQUAL,
		case LESS_THAN:
			log.warn("doFunctionSimpleUserNotDefine LESS THAN");
			return lessThan(thisCollectedData,valueString);
		case GREATER_THAN:
			log.warn("doFunctionSimpleUserNotDefine GREATER THAN");
			return greaterThan(thisCollectedData,valueString);
		case EQUAL:
			log.warn("doFunctionSimpleUserNotDefine EQUAL");
			return equal(thisCollectedData,valueString);
		default:
			break;
		}
		return true;
	}
	
	public boolean doFunctionSimpleUserDefine(String thisCollectedData,String valueString,ComparisonType compType)
	{
		switch(compType){
//		LESS_THAN,
//		GREATER_THAN,
//		EQUAL,
		case LESS_THAN:
			log.warn("doFunctionSimpleUserDefine LESS THAN");
			return lessThan(thisCollectedData,valueString);
		case GREATER_THAN:
			log.warn("doFunctionSimpleUserDefine GREATER THAN");
			return greaterThan(thisCollectedData,valueString);
		case EQUAL:
			log.warn("doFunctionSimpleUserDefine EQUAL");
			return equal(thisCollectedData,valueString);
		default:
			break;
		}
		log.warn("doFunctionSimpleUserDefine PROBLEM");
		return false;
	}
	public boolean doFunctionIntervalUserDefine(String thisCollectedData,String[] valueString,ComparisonType compType)
	{
		switch(compType){
//		INSIDE_INTERVAL,
//		OUTSIDE_INTERVAL,
//		LEFT_OF_INTERVAL,
//		RIGHT_OF_INTERVAL;
		case INSIDE_INTERVAL:
			log.warn("doFunctionIntervalUserDefine INSIDE INTERVAL");
			return insideInterval(thisCollectedData,valueString[0],valueString[1]);
		case OUTSIDE_INTERVAL:
			log.warn("doFunctionIntervalUserDefine OUTSIDE INTERVAL");
			return outsideInterval(thisCollectedData,valueString[0],valueString[1]);
		case LEFT_OF_INTERVAL:
			log.warn("doFunctionIntervalUserDefine LEFT OF INTERVAL");
			return leftInterval(thisCollectedData,valueString[0],valueString[1]);
		case RIGHT_OF_INTERVAL:
			log.warn("doFunctionIntervalUserDefine RIGHT OF INTERVAL");
			return rightInterval(thisCollectedData,valueString[0],valueString[1]);
		default:
			break;

		}
		log.warn("doFunctionIntervalUserDefine PROBLEM");
		return false;
	}
	
	public boolean lessThan(String a,String b){
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		if(aI<=bI){	
			log.warn("più piccolo");
			return true;
		}
		log.warn("non più piccolo");
		return false;
	}
	
	public boolean greaterThan(String a,String b){
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		if(aI<=bI){
			log.warn("non più grande");
			return false;
		}
		log.warn(" più grande");
		return true;
	}
	
	public boolean equal(String a,String b){
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		if(aI==bI){
			log.warn("uguale");
			return true;
		}
		log.warn("non uguale");
		return false;
	}
	
	public boolean insideInterval(String value,String a,String b){
	    double valueI = Double.valueOf(value);
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		
		if(aI<=valueI && valueI<=bI){
			log.warn("nell'intervallo");
			return true;
		}
		log.warn("non nell'intervallo");
		return false;
	}
	
	public boolean outsideInterval(String value,String a,String b){
		double valueI = Double.valueOf(value);
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		
		if(aI<=valueI && valueI<=bI){
			log.warn("non fuori dell'intervallo");
			return false;
		}
		log.warn("fuori dell'intervallo");
		return true;
	}
	
	public boolean leftInterval(String value,String a,String b){
	    double valueI = Double.valueOf(value);
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);
		
		if(aI<valueI){
			log.warn("a sinistra dell'intervallo");
			return true;
		}
		log.warn("non a sinistra dell'intervallo");
		return false;
	}
	
	public boolean rightInterval(String value,String a,String b){
	    double valueI = Double.valueOf(value);
		double aI = Double.valueOf(a);
		double bI = Double.valueOf(b);		
		if(valueI>bI){
			log.warn("a destra dell'intervallo");
			return true;
		}
		log.warn("non a destra dell'intervallo");
		return false;
	}
	
	public static String average(List<String> values) {
		int size = values.size();
		double result=0.0;
		for(int i=0; i< size; i++){
		 result =result+Integer.valueOf(values.get(i));
		}
		result=result/size;
		log.warn("La media è"+String.valueOf(result));
		return String.valueOf(result);
	}
	
	public static String sum(List<String> values) {
		int size = values.size();
		double result=0.0;
		for(int i=0; i< size; i++){
		 result =result+Integer.valueOf(values.get(i));
		}
		log.warn("La somma è"+String.valueOf(result));
		return String.valueOf(result);
	}
	
	public static String confidenceInterval(List<String> values) {
		return String.valueOf(0.95);
	}
	
	public static String variance(List<String> values) {
		String average = average(values);
		double m = Integer.valueOf(average);
		double sommaScartiQuad = 0.0;
		double variance = 0.0;
		for(int i=0; i<values.size(); i++)
			sommaScartiQuad += (Integer.valueOf(values.get(i))-m)*(Integer.valueOf(values.get(i))-m);
		variance= sommaScartiQuad/values.size();
		log.warn("La varianza è"+String.valueOf(variance));
		return String.valueOf(variance);
	}
	
	public boolean internalConsistency(ValidationCardinality cardinality, 
			ComparisonType compType, Boolean userDefined, String measureTaskId,DatasetOperation thisOp,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		boolean result;
		switch(cardinality){
			case COMPARE_MANY_TO_MANY:
				result = many2Many(measureTaskId, compType, userDefined,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_MANY_TO_ONE:
				result = many2One(measureTaskId, compType, userDefined,thisOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_MANY:
				result = one2Many(measureTaskId, compType, userDefined,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_ONE:
				result = one2One(measureTaskId, compType, userDefined,referenceParams,refMeasureTaskId);
				break;
			default:
				result = false;
		}
		return result;
		
	}
	
	public boolean plausibility(ValidationCardinality cardinality, 
			ComparisonType compType, Boolean userDefined, String measureTaskId,DatasetOperation thisOp,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		boolean result;
		switch(cardinality){
		
			case COMPARE_MANY_TO_MANY:
				result = many2Many(measureTaskId, compType, userDefined,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_MANY_TO_ONE:
				result = many2One(measureTaskId, compType, userDefined,thisOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_MANY:
				result = one2Many(measureTaskId, compType, userDefined,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_ONE:
				result = one2One(measureTaskId, compType, userDefined,referenceParams,refMeasureTaskId);
				break;
			default:
				result = false;
		}
		return result;
		
	}
	public boolean abnormalData(ValidationCardinality cardinality, 
			ComparisonType compType, Boolean userDefined, String measureTaskId,DatasetOperation thisOp,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		boolean result;
		switch(cardinality){
		
			case COMPARE_MANY_TO_MANY:
				result = many2Many(measureTaskId, compType, userDefined,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_MANY_TO_ONE:
				result = many2One(measureTaskId, compType, userDefined,thisOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_MANY:
				result = one2Many(measureTaskId, compType, userDefined,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_ONE:
				result = one2One(measureTaskId, compType, userDefined,referenceParams,refMeasureTaskId);
				break;
			default:
				result = false;
		}
		return result;
		
	}
	
	public boolean externalConsistency(ValidationCardinality cardinality, 
			ComparisonType compType, Boolean userDefined, String measureTaskId,DatasetOperation thisOp,DatasetOperation refOp,String[] referenceParams,String refMeasureTaskId){
		boolean result;
		switch(cardinality){
		
			case COMPARE_MANY_TO_MANY:
				result = many2Many(measureTaskId, compType, userDefined,thisOp,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_MANY_TO_ONE:
				result = many2One(measureTaskId, compType, userDefined,thisOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_MANY:
				result = one2Many(measureTaskId, compType, userDefined,refOp,referenceParams,refMeasureTaskId);
				break;
			case COMPARE_ONE_TO_ONE:
				result =  one2One(measureTaskId, compType, userDefined,referenceParams,refMeasureTaskId);
				break;
			default:
				result = false;
		}
		return result;
		
	}
	
	public static Date parser(String dateInString){
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		Date date = null;
		try {

			date = formatter.parse(dateInString);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static CollectedData maxCollectData(List<CollectedData> list){
		CollectedData max =new CollectedData(); 
		if(list.size() == 1){
			return list.get(0);
		}
		else{
				for(int i=0; i < list.size(); i++){
				Date a= parser(list.get(i+1).getDate());
				Date b= parser(list.get(i).getDate());
				if(a.after(b)){
					max=list.get(i+1);
				}
				else{
					max=list.get(i);
				}
			}
				log.warn("Collected data più recente è"+max.getDate());
				return max;
				}
	}
	
	
}