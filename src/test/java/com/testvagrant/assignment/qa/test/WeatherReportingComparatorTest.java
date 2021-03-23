package com.testvagrant.assignment.qa.test;

import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.testvagrant.assignment.Base.TestBase;
import com.testvagrant.assignment.qa.page.AppReusables;
import com.testvagrant.assignment.util.TestUtil;

import junit.framework.Assert;

public class WeatherReportingComparatorTest extends TestBase {
	
	public static Logger APPLICATION_LOGS = LoggerFactory
			.getLogger(WeatherReportingComparatorTest.class.getName());
	TestUtil util;
	AppReusables apage;
	
	/*
	 * This test does the validation of the weather report from the UI and the API
	 */
	@DataProvider
	public Object[][] getDataProvider() {
		return TestUtil.getData(apage.admin_xls, "TestDataSheet",
				"testThatValidateWeatherReport");
	}

	@Test(enabled = true, dataProvider = "getDataProvider")
	public void testThatValidateWeatherReport(
			Hashtable<String, String> data) throws InterruptedException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		util = new TestUtil();
		apage = new AppReusables();

		System.out.println("MethodName: " + methodName);

		APPLICATION_LOGS.info(">>>Starting execution of:'" + methodName
				+ "'<<<");
		util.checkRunModeAndSkipTestCase(methodName, apage.admin_xls, data);
		
		//Access NDTV Home Page
		driver.get(conf.getValue("NDTV_WEB_URL"));
		
		//Click on menu
		apage.clickOnTopMenuItem(driver);
		
		//Click on Weather Sub Menu item
		apage.clickOnWeatherSubMenuLink(driver);
		
		//Get the temp from UI
		//Temp from API - Comparing both the values and asserting them
		apage.assertTheTempValues(driver, conf.getValue("PUBLIC_WEATHER_API_URL"), 
				data.get("City"), data.get("City"), conf.getValue("appid"));
	}
}
