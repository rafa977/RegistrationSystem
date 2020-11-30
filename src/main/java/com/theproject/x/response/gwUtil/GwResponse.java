package com.theproject.x.response.gwUtil;

public class GwResponse<T> extends GwBaseResponse {

	private T data;

	public GwResponse(String status, T data) {
		super(status);
		this.data = data;
	}

	public GwResponse(String status) {
		super(status);
	}

	public GwResponse() {
		super();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
