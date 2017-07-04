package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class IllegalSaveWorkflowRequestBodyException extends Exception {

	private static String messagePrefix = "Incomplete request; please specify modelId, name and scope.";
	
	
	public IllegalSaveWorkflowRequestBodyException() {
		super(messagePrefix);
	}

	public IllegalSaveWorkflowRequestBodyException(Throwable cause) {
		super(messagePrefix, cause);
	}
}