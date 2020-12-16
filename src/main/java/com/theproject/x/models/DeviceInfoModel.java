package com.theproject.x.models;

import javax.persistence.Id;

public class DeviceInfoModel {

	private String userAgent;
	private String os;
	private String browser;
	private String device;
	private String osVersion;
	private String browserVersion;
	private String deviceType;
	private int isDesktop;
	private int isMobile;
	private int isTablet;
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getIsDesktop() {
		return isDesktop;
	}
	public void setIsDesktop(int isDesktop) {
		this.isDesktop = isDesktop;
	}
	public int getIsMobile() {
		return isMobile;
	}
	public void setIsMobile(int isMobile) {
		this.isMobile = isMobile;
	}
	public int getIsTablet() {
		return isTablet;
	}
	public void setIsTablet(int isTablet) {
		this.isTablet = isTablet;
	}

	
}
