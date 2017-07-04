package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class BusinessWorkflowNotCreatedException extends Exception {

	private static String messagePrefix = "Error with Activiti REST server; "
			+ "No business workflow model42 found";
	
	public BusinessWorkflowNotCreatedException() {
		super(messagePrefix);
	}

	public BusinessWorkflowNotCreatedException(Throwable cause) {
		super(cause);
	}

}
