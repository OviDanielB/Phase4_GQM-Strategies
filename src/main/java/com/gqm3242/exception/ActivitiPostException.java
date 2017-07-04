package com.gqm3242.exception;

public class ActivitiPostException extends Exception{

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String message;
	
	public ActivitiPostException(int errorCode, String message) {
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
