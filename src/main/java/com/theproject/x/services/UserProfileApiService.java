package com.theproject.x.services;

import java.io.IOException;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theproject.x.models.User.UserModel;
import com.theproject.x.response.RestBaseResponse;
import com.theproject.x.response.gwUtil.GwResponse;
import com.theproject.x.util.ObjectConverter;

@Service("UserProfileService")
@PropertySource({ "classpath:application.properties" })
public class UserProfileApiService {

	@Autowired
	private HttpService httpService;
	
	@Autowired
	private ObjectConverter objectConverter;

	@Autowired
	private Environment env;

	ObjectMapper objectMapper = new ObjectMapper();

	public RestBaseResponse<String> saveToDB(UserRepresentation user) throws Throwable {
		RestBaseResponse<String> response = new RestBaseResponse<String>();
		GwResponse<String> apiResponse = new GwResponse<String>();
		String url = env.getProperty("up.base.url") + env.getProperty("up.saveUser.endpoint");
		
		UserModel regUser = (UserModel) objectConverter.convertObject(user,
				new TypeReference<UserModel>() {
				});
		regUser.setKcUserId(user.getId());
		
		try {
			String jsonStr = objectMapper.writeValueAsString(regUser);
			
			apiResponse = httpService.HttPostRequest(jsonStr, url);
			if(apiResponse.getSuccess() == false) {
				response.setSuccess(false);
				response.setMessage(apiResponse.getMessage());
				return response;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public Boolean checkIfUserExists(String parameter) throws Throwable {
		RestBaseResponse<String> response = new RestBaseResponse<String>();
		GwResponse<String> apiResponse = new GwResponse<String>();
		
		String url = env.getProperty("up.base.url") + env.getProperty("up.usernameValid.endpoint") + "?username=" + parameter;
		
		try {			
			apiResponse = httpService.HttGetRequest(url);
			if(apiResponse.getSuccess() == false) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return true;
	}
	
}
