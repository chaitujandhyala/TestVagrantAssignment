package com.testvagrant.assignment.util;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testvagrant.assignment.Base.BasePage;
import com.testvagrant.assignment.Listeners.CaptureScreenShot;
import com.testvagrant.assignment.dataProvider.Config;

public class CommonMethods extends BasePage {
	
	final static Logger logback = LoggerFactory.getLogger(CaptureScreenShot.class);
	
	/**
	 * creating constructor and loading properties file
	 * 
	 * @param filePath
	 */
	public CommonMethods(String filePath) {
		conf = new Config(filePath);
	}
	
	/**
	 * 
	 * @param driver
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public WebElement findElement(final WebDriver driver, String objectLocater) {

		String objecttypeandvalues = conf.getValue(objectLocater);

		String[] splits = objecttypeandvalues.split("~");

		// String[] splits = objectLocater.split(":");
		String objecttype = splits[0];
		logback.debug("obj type: " + objecttype);
		String objectvalue = splits[1];
		logback.debug("obj val: " + objectvalue);

		if (objecttype.equalsIgnoreCase("id")) {
			return driver.findElement(By.id(objectvalue));
		} else if (objecttype.equalsIgnoreCase("xpath")) {
			return driver.findElement(By.xpath(objectvalue));

		} else if (objecttype.equalsIgnoreCase("name")) {
			return driver.findElement(By.name(objectvalue));
		} else if (objecttype.equalsIgnoreCase("class")) {
			return driver.findElement(By.className(objectvalue));
		} else if (objecttype.equalsIgnoreCase("tagname")) {
			return driver.findElement(By.tagName(objectvalue));
		} else if (objecttype.equalsIgnoreCase("link")) {
			return driver.findElement(By.linkText(objectvalue));
		} else if (objecttype.equalsIgnoreCase("css")) {
			return driver.findElement(By.cssSelector(objectvalue));
		}
		return null;

	}
	
	/**
	 * 
	 * @param driver
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public List<WebElement> findElements(final WebDriver driver, String objectLocater) {

		String objecttypeandvalues = conf.getValue(objectLocater);

		String[] splits = objecttypeandvalues.split("~");
		String objecttype = splits[0];
		System.out.println("obj type: " + objecttype);
		String objectvalue = splits[1];
		System.out.println("obj val: " + objectvalue);

		if (objecttype.equalsIgnoreCase("id")) {

			return driver.findElements(By.id(objectvalue));

		} else if (objecttype.equalsIgnoreCase("xpath")) {
			return driver.findElements(By.xpath(objectvalue));

		} else if (objecttype.equalsIgnoreCase("name")) {
			return driver.findElements(By.name(objectvalue));
		} else if (objecttype.equalsIgnoreCase("class")) {
			return driver.findElements(By.className(objectvalue));
		} else if (objecttype.equalsIgnoreCase("tagname")) {
			return driver.findElements(By.tagName(objectvalue));
		} else if (objecttype.equalsIgnoreCase("css")) {
			return driver.findElements(By.cssSelector(objectvalue));
		}
		return null;
	}
	
	/**
	 * waitForPageLoad
	 * @param driver
	 * @param waitTimeInSec
	 * @param conditions
	 * @return
	 */
	public boolean waitForPageLoad(WebDriver driver, 
			Duration waitTimeInSec, ExpectedCondition<Boolean>... conditions) {
	    boolean isLoaded = false;
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	    				.withTimeout(waitTimeInSec)
	    				.ignoring(StaleElementReferenceException.class)
	    				.pollingEvery(waitTimeInSec);
	    for (ExpectedCondition<Boolean> condition : conditions) {
	        isLoaded = wait.until(condition);
	        if (isLoaded == false) {
	            //Stop checking on first condition returning false.
	            break;
	        }
	    }
	    return isLoaded;
	}
	
	/**
	 * hardWait
	 * @param strWaitTime
	 * @return
	 */
	public boolean hardWait(String strWaitTime) {
		
		if (strWaitTime !=  null && !(strWaitTime.isEmpty())) {
			int waitTime = 0;
			
			try {
				waitTime = Integer.parseInt(strWaitTime);
				for (int i = 0; i < waitTime; i++) {
					System.out.print(".");
					Thread.sleep(1000);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param timeOutInSeconds
	 * @param wb
	 */
	public void explicitWaitElementToBeVisible(WebDriver driver, long timeOutInSeconds, WebElement wb) {
		@SuppressWarnings("deprecation")
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(wb));
	}
	
	/**
	 * 
	 * @param wb
	 * @return
	 */
	public boolean isElementPresent(WebElement wb) {
		try {
			wb.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isElementPresent(final WebDriver driver, WebElement wb) {
		try {
			wb.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Compact way to verify if an element is on the page
	 * 
	 * @param driver
	 * @param by
	 * @return
	 * @throws IOException
	 */
	public boolean isElementPresent(final WebDriver driver, String objectLocater) {
		
		driver.manage().timeouts().getImplicitWaitTimeout();
		if (findElements(driver, objectLocater).size() != 0) {
			driver.manage().timeouts().getImplicitWaitTimeout();
			return true;

		} else {
			driver.manage().timeouts().getImplicitWaitTimeout();
			return false;
		}
	}
	
	/**
	 * to click on the element
	 * 
	 * @param driver
	 * @param we
	 */
	public static void click(WebDriver driver, WebElement we) {
		we.click();
	}

	/**
	 * click using java script function
	 * 
	 * @param driver
	 * @param objectLocater
	 */
	public void clickByJavaScript(final WebDriver driver, String objectLocater) {
		driver.manage().timeouts().getImplicitWaitTimeout();
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", findElement(driver, objectLocater));
	}

	/**
	 * click using java script function
	 * 
	 * @param driver
	 * @param wb
	 */
	public void clickByJavaScript(final WebDriver driver, WebElement wb) {

		((JavascriptExecutor) driver).executeScript("arguments[0].click()", wb);
	}
	
	/**
	 * 
	 * @param driver
	 * @param wb
	 * @return
	 */
	public String getText(WebDriver driver, WebElement wb) {
		return wb.getText();
	}
	
	/**
	 * 
	 * @param driver
	 * @param objectLocater
	 * @return
	 * @throws IOException
	 */
	public String getText(WebDriver driver, String objectLocater) {
		// TODO Auto-generated method stub

		return findElement(driver, objectLocater).getText();

	}
	
	/**
	 * Click on element
	 * 
	 * @param driver
	 * @param objectLocater
	 * @param value
	 * @throws IOException
	 */

	public void sendKeys(final WebDriver driver, String objectLocater, String value) {
		driver.manage().timeouts().getImplicitWaitTimeout();
		findElement(driver, objectLocater).clear();
		findElement(driver, objectLocater).sendKeys(value);
	}
	
	/**
	 * 
	 * @param driver
	 * @param objectLocater
	 * @param keys
	 */
	public void sendKeys(final WebDriver driver, String objectLocater, Keys keys) {
		driver.manage().timeouts().getImplicitWaitTimeout();
		findElement(driver, objectLocater).clear();
		findElement(driver, objectLocater).sendKeys(keys);
	}
	
	/**
	 * Select radio button
	 * 
	 * @param driver
	 * @param by
	 * @param value
	 * @throws IOException
	 * 
	 */
	public void selectRadioButton(WebDriver driver, String objectLocater, String value) {
		List<WebElement> select = findElements(driver, objectLocater);

		for (WebElement radio : select) {
			if (radio.getAttribute("value").equalsIgnoreCase(value)) {
				radio.click();
			}
		}
	}
	
	/**
	 * Select multiple check boxes
	 * 
	 * @param driver
	 * @param by
	 * @param value
	 * @throws IOException
	 * 
	 */
	public void selectCheckboxes(WebDriver driver, String objectLocater, String value) {

		List<WebElement> abc = findElements(driver, objectLocater);
		List<String> list = new ArrayList<String>(Arrays.asList(value.split(",")));

		for (String check : list) {
			for (WebElement chk : abc) {
				if (chk.getAttribute("value").equalsIgnoreCase(check)) {
					chk.click();
				}
			}
		}
	}
	
	/**
	 * Select drop down
	 * 
	 * @param driver
	 * @param by
	 * @param value
	 * @throws IOException
	 * 
	 */
	public void selectDropdown(WebDriver driver, String objectLocater, String value) {
		
		try{
			isElementPresent(driver, objectLocater);
			new Select(findElement(driver, objectLocater)).selectByVisibleText(value);
		}catch(NoSuchElementException ne) {
			System.out.println(ne.getMessage());
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param objectLocater
	 * @return
	 * @throws IOException
	 */
	public int listOfItems(final WebDriver driver, String objectLocater) {
		int count = 0;

		List<WebElement> listOfItems = findElements(driver, objectLocater);

		for (int i = 0; i < listOfItems.size(); i++) {

			// System.out.println(dropdown_items.get(i).getText());
			if (listOfItems.get(i).isDisplayed()) {
				count++;

			}
		}

		System.out.println("Count from ListOfItems In CommonMethod Class is : " + count);

		return count;

	}

}
