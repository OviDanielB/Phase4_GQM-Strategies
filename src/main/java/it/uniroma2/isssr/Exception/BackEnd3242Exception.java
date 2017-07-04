package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class BackEnd3242Exception extends Exception {
	
	private static String message = "Collected data id= ";

	private static String buildMessage() {
		return message;
	}

	public BackEnd3242Exception() {
		super(buildMessage());
	}

}
