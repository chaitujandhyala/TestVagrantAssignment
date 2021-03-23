package com.capgemini.qa.test;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.capgemini.qa.base.TestBase;
import com.capgemini.qa.page.AppReusables;
import com.capgemini.qa.util.CommonMethod;
import com.capgemini.qa.util.TestUtil;

public class SampleGoogleTest extends TestBase {

	public static Logger APPLICATION_LOGS = LoggerFactory
			.getLogger(SampleGoogleTest.class.getName());
	
	TestUtil util = new TestUtil();
	CommonMethod common = new CommonMethod(System.
			getProperty("user.dir")+"/locator/objectRepository.properties");
	AppReusables apage = new AppReusables();

	
	/*
	 * Verifying the search and respective results on google page
	 */
	@DataProvider
	public Object[][] getDataFor_1() {
		return TestUtil.getData(apage.admin_xls, "TestDataSheet",
				"testVerifyFindResultsArea_01");
	}

	@Test(enabled = true, dataProvider = "getDataFor_1")
	public void testVerifyFindResultsArea_01(
			Hashtable<String, String> data) throws InterruptedException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		System.out.println("MethodName: " + methodName);

		APPLICATION_LOGS.info(">>>Starting execution of:'" + methodName
				+ "'<<<");
		util.checkRunModeAndSkipTestCase(methodName, apage.admin_xls, data);
		
		//Access Google Home Page
		driver.get(conf.getValue("google_home_url"));
		
		//Search on google home page
		apage.searchUsingGoogleSearchBar(driver, data);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}// End of Class
