package it.uniroma2.isssr.Exception;

@SuppressWarnings("serial")
public class StrategyDataException extends Exception {

	private static final String MESSAGE = "There was an consistency exception in the data concerning workflowDatas";

	
	public StrategyDataException() {
		super(MESSAGE);
	}

	public StrategyDataException(Throwable cause) {
		super(MESSAGE, cause);
	}

	public StrategyDataException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(MESSAGE, cause, enableSuppression, writableStackTrace);
	}

}
