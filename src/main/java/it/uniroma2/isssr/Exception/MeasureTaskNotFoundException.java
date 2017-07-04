package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class MeasureTaskNotFoundException extends Exception {

	private static String messagePrefix = "Measure task id= ";

	private static String messageSuffix = " not found";

	private static String buildMessage(String taskId) {

		return messagePrefix + taskId + messageSuffix;
	}

	public MeasureTaskNotFoundException(String taskId) {
		super(buildMessage(taskId));
	}

}
