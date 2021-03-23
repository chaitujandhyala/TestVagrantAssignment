package com.testvagrant.assignment.driverFac;

import org.openqa.selenium.WebDriver;

public interface IBrowserProvider {
	
	public WebDriver getCurrentDriver(IDriverProvider driverProvider);
	
	public WebDriver getCurrentDriver(IDriverProvider driverProvider,String browserName);

}
