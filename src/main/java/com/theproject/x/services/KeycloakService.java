package com.theproject.x.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.response.RestBaseResponse;

@Service("keycloakService")
@PropertySource({ "classpath:application.properties" })
public class KeycloakService {

	@Autowired
	private Environment env;
	
	@Autowired
	RestTemplate restTemplate;

	public String keycloakAdminAccessToken() {		
	    RestTemplate restTemplate = new RestTemplate();
	    
		URI uriKeycloak = URI.create(env.getProperty("base.url.keycloak.access.token"));
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", env.getProperty("admin.user.keycloak.username"));
		map.add("password", env.getProperty("admin.user.keycloak.password"));
		map.add("client_id", env.getProperty("admin.user.keycloak.client_id"));
		map.add("grant_type", "password");
		map.add("credentials", "true");
		map.add("scope", "openid");
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        ResponseEntity<KeycloakAccessToken> tokenResponse = restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity,
        		KeycloakAccessToken.class);
                
	      return tokenResponse.getBody().getAccessToken();
	}

	public RestBaseResponse<String> keycloakHttpPost(HttpMethod method, boolean isEntity, String adminAccessToken, URI uriKeycloak, MultiValueMap<String, String> map){
		RestBaseResponse<String> response = new RestBaseResponse<String>();		
		ResponseEntity<KeycloakAccessToken> tokenResponse = null;

		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);	      
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        if(adminAccessToken != null) {
        	 headers.set("Authorization", "Bearer " + adminAccessToken);
        }
        
        try {
        	  if(isEntity) {
				  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
				  tokenResponse = restTemplate.exchange(uriKeycloak, method, entity, KeycloakAccessToken.class);
				  
			      response.setData(tokenResponse.getBody().getAccessToken());
        	  }else {
        		  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        		  restTemplate.exchange(uriKeycloak, method, entity, String.class);
        	  }
        	  
			  response.setSuccess(true);
		      return response;

	      } catch (final HttpClientErrorException e) {
    		response.setSuccess(false);
    		return response;
    	}
	}
	
}
