package it.uniroma2.isssr.Exception;

/**
 * E' già presente un documento nel DB con quella chiave
 * @author Fabio Alberto Coira
 *
 */
public class ConflictException extends Exception{
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String message;
	
	public  ConflictException (int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
