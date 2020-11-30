package com.theproject.x.services;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theproject.x.response.gwUtil.GwResponse;

@Service("HttpService")
public class HttpService {

	
	public GwResponse<String> HttPostRequest(String json, String endpoint) throws Throwable {
		String adminAccessToken = KeycloakService.adminAccessToken;
		
		GwResponse<String> apiResp = new GwResponse<String>();
		ObjectMapper mapper = new ObjectMapper();
		HttpPost post = new HttpPost(endpoint);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer asdasddd" + adminAccessToken);

		StringEntity stringEntity = new StringEntity(json);
		post.setEntity(stringEntity);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try (CloseableHttpResponse apiResponse = httpClient.execute(post)) {

			if (apiResponse.getEntity() != null) {

				// A Simple JSON Response Read
				String retSrc = EntityUtils.toString(apiResponse.getEntity());
					apiResp = mapper.readValue(retSrc, new TypeReference<GwResponse<String>>() {
				});

//				logger.log(Level.INFO, "HTML: " + apiResp.getMessage());
			}

		} catch (IOException e) {
			e.printStackTrace();
			apiResp.setSuccess(false);
		}
		httpClient.close();

		return apiResp;	
	}
	
	public void HttpGetRequest() {
		
	}
}
