package com.testvagrant.assignment.Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.testvagrant.assignment.dataProvider.Config;
import com.testvagrant.assignment.driverFac.DriverFactory;

public class TestBase {

	protected WebDriver driver;
	DriverFactory driverFactory = new DriverFactory();
	protected Config conf = new Config(System.getProperty("user.dir") + "/config/config.properties");

	public WebDriver getDriver() {
		return driver;
	}

	@Parameters("browser")
	@BeforeClass

	public void setUp(@Optional() String browserName) {

		if (conf.getValue("tesng.parameter").equalsIgnoreCase("YES")) {
			driver = driverFactory.getDriver(browserName);

		} else {
			driver = driverFactory.getDriver();

		}
		driver.manage().window().maximize();
		driver.manage().timeouts().getImplicitWaitTimeout();
	}

	@AfterClass
	public void tearDown() {
		// TODO Auto-generated method stub
		if (driver != null) {
			try {
				driver.quit();
			} catch (WebDriverException e) {
				System.out.println("***** CAUGHT EXCEPTION IN DRIVER TEARDOWN *****");
				System.out.println(e);
			}

		}

	}
}
