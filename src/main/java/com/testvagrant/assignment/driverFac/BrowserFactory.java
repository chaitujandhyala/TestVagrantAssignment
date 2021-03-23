package com.testvagrant.assignment.driverFac;

import com.testvagrant.assignment.dataProvider.Config;

public class BrowserFactory extends DriverTypeProvider{
	
	Config conf=new Config(System.getProperty("user.dir")+"/config/config.properties");
	 public IDriverProvider browser;
	
	/**
	 * It will return type of Browser. If it is a remote browser return remote browser otherwise local browser
	 * @return
	 */
	@Override
	public IDriverProvider getBrowserType(){
		if(conf.getValue("driver.browsertype").equalsIgnoreCase("local")){
			browser=new BrowserProvider();
			
			return browser;
			
		}
		return null;
	}
	
	/**
	 * to get driver type. It will return different types of driver as per the requirement
	 * We can make use of this to implement the for RemoteWebDriver execution(which is not needed now)
	 */
	@Override
	public IBrowserProvider getDriverType() {
		// TODO Auto-generated method stub
		if(conf.getValue("driver.drivertype").equalsIgnoreCase("local")){
			System.out.println("local driver called");
			return new LocalDriver(null);
		}
		return null;
	}
}
