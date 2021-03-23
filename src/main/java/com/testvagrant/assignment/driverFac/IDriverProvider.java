package com.testvagrant.assignment.driverFac;

import org.openqa.selenium.WebDriver;

public interface IDriverProvider {
	
	/**
	 * to provide driver 
	 * Browser can be set from config file
	 * @return
	 */
	public WebDriver getDriver();
	
	/**
	 * to provide driver [browser is set from testNG Parameter]
	 * @param browser
	 * @return
	 */
	public WebDriver getDriver(String browserName);
	
	public String getBrowserName();
	

}
