package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class TaskNotFoundException extends Exception {

	private static String messagePrefix = "Runtime Task id= ";

	private static String messageSuffix = " not found";

	private static String buildMessage(String processDefinitionId) {

		return messagePrefix + processDefinitionId + messageSuffix;
	}

	public TaskNotFoundException(String processDefinitionId) {
		super(buildMessage(processDefinitionId));
	}

}
