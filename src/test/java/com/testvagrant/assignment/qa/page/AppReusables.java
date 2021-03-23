package com.testvagrant.assignment.qa.page;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;

import com.testvagrant.assignment.Base.BasePage;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormatSymbols;

import junit.framework.Assert;

public class AppReusables extends BasePage {
	
	public static Logger APPLICATION_LOGS = LoggerFactory
			.getLogger(AppReusables.class.getName());
	String gmailBodyText = null;
	
	/**
	 * Capturing ScreenShot on success test cases
	 * @param driver
	 * @param methodName
	 */
	public void captureScreenshotOnSuccess(WebDriver driver, String methodName, ITest test){
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.
					getProperty("user.dir")+"\\screenshotsOnSuccess"+"\\"+"screenshotOnSuccess"+"_"
					+test.getTestName()+"_"+getCurrentTimeInstance()+"."+"png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * getCurrentTimeInstance
	 * @return
	 */
	public String getCurrentTimeInstance(){
		Calendar calendar = Calendar.getInstance();
		String currentTimeInstance =  "-"                           
	        + calendar.get(Calendar.YEAR) + "-"
	        + calendar.get(Calendar.MONTH) + "-"
	        + calendar.get(Calendar.DAY_OF_MONTH) + "-"
	        + calendar.get(Calendar.HOUR_OF_DAY) + "-"
	        + calendar.get(Calendar.MINUTE) + "-"
	        + calendar.get(Calendar.SECOND) + "-"
	        + calendar.get(Calendar.MILLISECOND);
		return currentTimeInstance;
	}

	/**
	 * Method to click on top navigation men - dots Icon
	 * @param driver
	 */
	public void clickOnTopMenuItem(WebDriver driver) {
		
		try {
			
			if(common.isElementPresent(driver, "ndtv_noThanks_popUp")) {
				common.clickByJavaScript(driver, "ndtv_noThanks_popUp");
				common.explicitWaitElementToBeVisible(driver, 15, 
						common.findElement(driver, "ndtv_landingScreen_menu_topNavgMore"));
				common.clickByJavaScript(driver, "ndtv_landingScreen_menu_topNavgMore");
				System.out.println("Clicked on the top navigation sub menu icon");
				common.explicitWaitElementToBeVisible(driver, 15, 
						common.findElement(driver, "ndtv_topNavMenu_weather_subMenuItem"));
			}else {
				common.explicitWaitElementToBeVisible(driver, 15, 
						common.findElement(driver, "ndtv_landingScreen_menu_topNavgMore"));
				common.clickByJavaScript(driver, "ndtv_landingScreen_menu_topNavgMore");
				System.out.println("Clicked on the top navigation sub menu icon");
				common.explicitWaitElementToBeVisible(driver, 15, 
						common.findElement(driver, "ndtv_topNavMenu_weather_subMenuItem"));
			}
		
		}catch(Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	
	/**
	 * clickOnWeatherSubMenuLink
	 * @param driver
	 */
	public void clickOnWeatherSubMenuLink(WebDriver driver) {
		
		try {
			common.isElementPresent(driver, "ndtv_topNavMenu_weather_subMenuItem");
			common.clickByJavaScript(driver, "ndtv_topNavMenu_weather_subMenuItem");
			common.explicitWaitElementToBeVisible(driver, 15, 
					common.findElement(driver, "ndtv_weatherReportPage_searchBox_state"));
			
		}catch(Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	
	/**
	 * enterLocationToFetchTemp
	 * @param driver
	 * @param location
	 * @return
	 */
	public Float enterLocationToFetchTemp(WebDriver driver, String location) {
		
		String tempFromUI = "";
		float tempFloatFromUI = 0;
		try {
			common.isElementPresent(driver, "ndtv_weatherReportPage_searchBox_state");
			common.hardWait("5");
			common.explicitWaitElementToBeVisible(driver, 40, 
					common.findElement(driver, "ndtv_weatherReportPage_searchBox_state"));
			common.sendKeys(driver, "ndtv_weatherReportPage_searchBox_state", location);
			
			if(common.isElementPresent(driver, "ndtv_weatherReportPage_location_checkbox")) {
				System.out.println("Location checkbox checked already");
			}else {
				common.clickByJavaScript(driver, "ndtv_weatherReportPage_location_checkbox");
			}
			
			common.isElementPresent(driver, "ndtv_weatherReportPage_cityOnMap");
			common.clickByJavaScript(driver, "ndtv_weatherReportPage_cityOnMap");
			common.isElementPresent(driver, "ndtv_weatherReportPage_city_weatherReportContainer");
			common.isElementPresent(driver, "ndtv_weatherReportPage_city_container_tempInDegrees");
			
			tempFromUI = common.getText(driver, "ndtv_weatherReportPage_city_container_tempInDegrees");
			tempFromUI = tempFromUI.replace("Temp in Degrees: ", "");
			tempFloatFromUI = Float.parseFloat(tempFromUI);
			System.out.println("tempFloatFromUI : " + tempFloatFromUI);
			
		}catch(Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
		return tempFloatFromUI;
	}
	
	/**
	 * validateTempResponseFromAPI
	 * @param city
	 * @param appID
	 */
	public Float validateTempResponseFromAPI(String baseURI, String city, String appID) {

		float tempFloatFromAPI = 0;
		try {
			RestAssured.baseURI = baseURI;
			RequestSpecification request = RestAssured.given();

			Response response = request.queryParam("q", city)
									   .queryParam("appid", appID)
									   .get("/weather");

			String jsonString = response.asString();
			Map<Float, Float> tempuratureFromAPI = response.jsonPath().getMap("main");
			tempFloatFromAPI = tempuratureFromAPI.get("temp");
			Assert.assertEquals(response.getStatusCode(), 200);
			Assert.assertEquals(jsonString.contains(city), true);
			
		}catch(Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
		return tempFloatFromAPI;
	}
	
	/**
	 * Convert Kelvin to Celsius
	 * @param kelvin
	 * @return
	 */
	private static float convertKelvinToCelsius(float kelvin) {
		return (float) (kelvin - 273.15);
	}
	
	/**
	 * Validate and assertTheTempValues
	 * @param driver
	 * @param location
	 * @param city
	 * @param appID
	 */
	public void assertTheTempValues(WebDriver driver, String baseURI, String location, String city, String appID) {
		try {
			
			Float tempFloatFromUI = enterLocationToFetchTemp(driver, location);
			Float tempFloatFromAPI = validateTempResponseFromAPI(baseURI, city, appID);
			tempFloatFromAPI = convertKelvinToCelsius(tempFloatFromAPI);
			
			System.out.println("tempFloatFromUI : " + tempFloatFromUI);
			System.out.println("tempFloatFromAPI : " + tempFloatFromAPI);
			
			if(tempFloatFromUI == tempFloatFromAPI || tempFloatFromUI - tempFloatFromAPI > 2) {
				Assert.assertEquals(tempFloatFromAPI, tempFloatFromUI);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			e.getStackTrace();
		}
	}
	
}// End of Class