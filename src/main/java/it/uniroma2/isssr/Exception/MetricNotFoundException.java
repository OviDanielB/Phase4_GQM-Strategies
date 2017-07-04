package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class MetricNotFoundException extends Exception {
	
	private static String messagePrefix = "We found no metric to validate the collected value";
	
	private static String buildMessage() {

		return messagePrefix;
	}

	public MetricNotFoundException() {
		super(buildMessage());
	}

}
