package org.xbot.core.basement;

public class ServiceResult {
	private boolean result;
	private String message;
	private Object returnObject;
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	 
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	
	
}
