package com.theproject.x.response.gwUtil;

import java.util.ArrayList;
import java.util.List;

public class GwListResponse<T> extends GwBaseResponse {

	private List<T> data = new ArrayList<T>();

	public GwListResponse(String status, List<T> data) {
		super(status);
		this.data = data;
	}

	public GwListResponse(String status) {
		super(status);
	}

	public GwListResponse() {
		super();
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
