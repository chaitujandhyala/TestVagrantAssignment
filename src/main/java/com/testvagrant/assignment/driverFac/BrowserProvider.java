package com.testvagrant.assignment.driverFac;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.testvagrant.assignment.dataProvider.Config;

public class BrowserProvider implements IDriverProvider{
	
	String nameBrowser;
	Config conf=new Config(System.getProperty("user.dir")+"/config/config.properties");
	WebDriver driver;
	
	public WebDriver getDriver() {
			// TODO Auto-generated method stub
			if (driver==null){
				String browser=conf.getValue("driver.browsername");
				return getBrowser(browser);
		}
		return driver;
	}

	public WebDriver getDriver(String browserName) {
		// TODO Auto-generated method stub
		return getBrowser(browserName);
	}
	
	public WebDriver getBrowser(String browser){
		if(driver==null){
			if(browser.equalsIgnoreCase("firefox")){
				driver=new FirefoxDriver();
				return driver;
			}
			else if(browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", conf.getValue("chrome.path"));
				return new ChromeDriver();
			}
			else if(browser.equalsIgnoreCase("ie")){
				System.setProperty("webdriver.ie.driver", conf.getValue("ie.path"));
				return new InternetExplorerDriver();
			}
		}
		return driver;
	}

	public String getBrowserName() {
		// TODO Auto-generated method stub
		return null;
	}

}
