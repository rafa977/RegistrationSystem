package com.theproject.x.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
public class Sessions {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Long userId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "device_info_id", updatable = false)
	private UserDevice deviceInfoId;
	
	private Date timestamp;
	private String ip;
	private int open;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserDevice getDeviceInfoId() {
		return deviceInfoId;
	}
	public void setDeviceInfoId(UserDevice deviceInfoId) {
		this.deviceInfoId = deviceInfoId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	
	
}
