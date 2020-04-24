package org.woodwhales.cloud.exception;


public class RequestParamException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequestParamException(String message) {
		super(message);
	}
	
	public static RequestParamException build(String message) {
		return new RequestParamException(message);
	} 
}
