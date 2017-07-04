package it.uniroma2.isssr.validation;

public enum Countermeasure  {
	//devo settare delle variabili di activiti con scope local. state già c'è,
	// va fatto l'update di questa variabile
	REPEATE_MEASURE, //=> cambia lo stato da due a tre e setto il messaggio di errore 
	//e l'id del collectdata che si sta validando
	CONTROL_METRIC, //=> cambia lo stato da due a tre  e setto il messaggio di errore 
	EXCLUDE_DATA;
	
}

/*
public abstract class Countermeasure {
	private String id;
	private String description;
	
	public abstract void repair();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}*/
