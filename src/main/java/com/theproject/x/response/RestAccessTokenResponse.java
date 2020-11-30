package com.theproject.x.response;

import com.theproject.x.models.keycloak.KeycloakAccessToken;

public class RestAccessTokenResponse extends RestResponse{
	
	private KeycloakAccessToken accessToken;

	public KeycloakAccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(KeycloakAccessToken accessToken) {
		this.accessToken = accessToken;
	}
	
	
}
