package it.uniroma2.isssr.validation;

public class Descriptions {
	public static final String MISSING_DATA = "Il dato richiesto "
			+ "non è stato inserito.";
	public static final String WRONG_FORMAT = "Il formato del dato "
			+ "inserito non è corretto.";
	public static final String AVERAGE = "La media dei dati "
			+ "raccolti è al di fuori dell'intervallo definito.";
	public static final String SUM_IN_RANGE = "La somma dei dati "
			+ "raccolti è al di fuori dell'intervallo definito. ";
	public static final String VARIANCE = "La varianza dei dati "
			+ "raccolti è al di fuori dell'intervallo definito.";
	public static final String COMPARE_ONE_TO_ONE = "Non viene rispettata la "
			+ "relazione tra due proprietà dello stesso oggetto";
	public static final String COMPARE_ONE_TO_MANY = "Non viene rispettata la "
			+ "relazione tra una proprietà di un oggetto e i dati di un'altra "
			+ "proprietà dello stesso oggetto";
	public static final String COMPARE_MANY_TO_ONE = "Non viene rispettata la "
			+ "relazione tra i dati di una proprietà di un oggetto e un'altra "
			+ "proprietà dello stesso oggetto";
	public static final String COMPARE_MANY_TO_MANY = "Non viene rispettata la "
			+ "relazione tra i dati di una proprietà di un oggetto e i dati di "
			+ "un'altra proprietà dello stesso oggetto";
	
	public static final String REPEAT_MEASURE = "Se il dato acquisito non"
			+ " supera la validazione, sarà richiesto al data collector di "
			+ "ripetere la misura.";
	public static final String DERIVE_AVERAGE = "Se il dato acquisito non "
			+ "supera la validazione viene derivato il dato in base alla media"
			+ " dei dati già presenti nelle passate iterazioni.";
}
