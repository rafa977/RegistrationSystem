package com.theproject.x.response;

public class RestResponse {
	
	private boolean success = true;
	
	private String message = "Success / Επιτυχία";
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
