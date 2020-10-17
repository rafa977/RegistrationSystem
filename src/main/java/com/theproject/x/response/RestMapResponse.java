package com.theproject.x.response;

import java.util.HashMap;
import java.util.Map;

public class RestMapResponse<K, V> extends RestResponse {

	private Map<K, V> data = new HashMap<K, V>();

	public Map<K, V> getData() {
		return data;
	}

	public void setData(Map<K, V> data) {
		this.data = data;
	}

}

