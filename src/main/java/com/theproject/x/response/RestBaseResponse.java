package com.theproject.x.response;



public class RestBaseResponse<T> extends RestResponse{
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
