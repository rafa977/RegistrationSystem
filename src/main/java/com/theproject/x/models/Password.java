package com.theproject.x.models;

public class Password {

    private String oldPassword;

    private String newPassword;

    private String matchNewPassword;
    
    private String username;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMatchNewPassword() {
        return matchNewPassword;
    }

    public void setMatchNewPassword(String matchNewPassword) {
        this.matchNewPassword = matchNewPassword;
    }
    
    public String getUsername() {
 		return username;
 	}

 	public void setUsername(String username) {
 		this.username = username;
 	}
}
