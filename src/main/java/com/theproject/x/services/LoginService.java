package com.theproject.x.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.theproject.x.models.keycloak.KeycloakAccessToken;
import com.theproject.x.response.RestBaseResponse;

@Service("loginService")
@PropertySource({ "classpath:application.properties" })
public class LoginService {

	@Autowired
	private Environment env;

	@Autowired
	private KeycloakService keycloakService;
	
	public RestBaseResponse<String> keycloakUserAccessToken(String username, String password) throws JsonMappingException, JsonProcessingException {		
	    RestTemplate restTemplate = new RestTemplate();	    
	    URI uriKeycloak = URI.create(env.getProperty("base.url.keycloak.access.token"));
		RestBaseResponse<String> response = new RestBaseResponse<String>();		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", username);
		map.add("password", password);
		map.add("client_id", env.getProperty("user.client"));
		map.add("grant_type", "password");
		map.add("client_secret", env.getProperty("admin.user.keycloak.client.secret"));
		ResponseEntity<KeycloakAccessToken> tokenResponse = null;
		
		try {
			  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			  tokenResponse = restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity, KeycloakAccessToken.class);
		      response.setData(tokenResponse.getBody().getAccessToken());
		      response.setSuccess(true);
		      return response;

	      }
	    	catch (final HttpClientErrorException e) {
	    		response.setSuccess(false);
	    		response.setMessage("Unauthorized. Wrong Username or Password");
    
			    return response;
	    	}
	}
	
	public RestBaseResponse<String> keycloakUserLogout(String userId) throws JsonMappingException, JsonProcessingException {		
	    RestTemplate restTemplate = new RestTemplate();	    
	    String adminAccessToken = keycloakService.keycloakAdminAccessToken();
	    
		URI uriKeycloak = URI.create(env.getProperty("base.url.keycloak.update.user") + "/" + userId + "/logout");
		RestBaseResponse<String> response = new RestBaseResponse<String>();		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    headers.set("Authorization", "Bearer " + adminAccessToken);	      
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("id", userId);
		map.add("realm", env.getProperty("keycloak.realm"));
		
		try {
			  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			  restTemplate.exchange(uriKeycloak, HttpMethod.POST, entity, KeycloakAccessToken.class);
		      response.setSuccess(true);
		      return response;

	      }
	    	catch (final HttpClientErrorException e) {
	    		response.setSuccess(false);
	    		response.setMessage("Something went wrong. Please try again.");
    
			    return response;
	    	}
	}
	
	
	
}
