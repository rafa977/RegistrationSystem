package com.theproject.x.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VerifToken implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final int EXPIRATION = 60 * 24;
	
	@Id
	private String username;
	private String token;
	private Date timestamp;
	private Date expiredAt;
	
	public VerifToken() {
		
	}
	
	public VerifToken(final String token) {
        this.token = token;
        this.expiredAt = calculateExpiryDate(EXPIRATION);
    }

    public VerifToken(final String token, final String username) {
        this.token = token;
        this.username = username;
        this.expiredAt = calculateExpiryDate(EXPIRATION);
    }
    
	private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Date getExpiredAt() {
		return expiredAt;
	}
	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	
	
}
