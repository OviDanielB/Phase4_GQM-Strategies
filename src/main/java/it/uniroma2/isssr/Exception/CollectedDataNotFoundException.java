package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class CollectedDataNotFoundException extends Exception {

	private static String messagePrefix = "Collected data id= ";

	private static String messageSuffix = " not found";

	private static String buildMessage(String collectedDataId) {

		return messagePrefix + collectedDataId + messageSuffix;
	}

	public CollectedDataNotFoundException(String collectedDataId) {
		super(buildMessage(collectedDataId));
	}

}
