package it.uniroma2.isssr.exception;

@SuppressWarnings("serial")
public class CollectedDataNotValidException extends Exception {
	
	private static String messagePrefix = "The value ";
	
	private static String messageSuffix = " is invalid for metric ";
	
	private static String buildMessage(String value, String metricName){
		
		return messagePrefix + value + messageSuffix+ metricName;
	}
	
	public CollectedDataNotValidException(String value, String metricName) {
		super(buildMessage(value, metricName));
	}
	

}
