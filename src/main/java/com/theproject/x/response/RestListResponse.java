package com.theproject.x.response;

import java.util.ArrayList;
import java.util.List;

public class RestListResponse<T> extends RestResponse{
	
	private List<T> data = new ArrayList<T>();

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	
}
