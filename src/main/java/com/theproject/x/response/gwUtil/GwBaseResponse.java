package com.theproject.x.response.gwUtil;

public class GwBaseResponse {

	private String status; // Success, Error, Ignore
	private String message;
	private String referenceCode;
	private Boolean success;

	public GwBaseResponse(String status) {
		this.status = status;
	}

	public GwBaseResponse() {

	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getStatus() {
		return status;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

}
