package com.testvagrant.assignment.driverFac;

import org.openqa.selenium.WebDriver;

public class DriverFactory implements IDriverProvider{
	BrowserFactory bFactory=new BrowserFactory(); 
	//Creating Driver objects
	private IDriverProvider driverProvider;
	//private IBrowserProvider browserProvider;
	private WebDriver driver;
	public static String nameBrowser;
	
	
	
	public IDriverProvider getBrowser(){
		if(driverProvider==null){
			driverProvider=bFactory.getBrowserType();
		}
		return driverProvider;
	}
	
	public IBrowserProvider getDriverFactory(){
		
		return bFactory.getDriverType();
	}
	
	public WebDriver getDriver() {
		// TODO Auto-generated method stub
		if(driver==null){
			IBrowserProvider driverFact=getDriverFactory();
			IDriverProvider browser=getBrowser();
			driver=driverFact.getCurrentDriver(browser);
			nameBrowser=browser.getBrowserName();
			System.out.println("browser"+nameBrowser);
		}
		return driver;
	}

	public WebDriver getDriver(String browserName) {
		// TODO Auto-generated method stub
		if(driver==null){
		
			IBrowserProvider driverFact=getDriverFactory();
			IDriverProvider browser=getBrowser();
			driver=driverFact.getCurrentDriver(browser,browserName);
			nameBrowser=browser.getBrowserName();
			System.out.println("browser"+nameBrowser);
		}
		return driver;
	}

	public String getBrowserName() {
		// TODO Auto-generated method stub
		return nameBrowser;
	}

}
