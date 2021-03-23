package com.capgemini.qa.page;

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
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;
import com.capgemini.qa.base.BasePage;
import com.thoughtworks.selenium.SeleniumException;
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
	public void captureScreenshotOnSuccess(WebDriver driver, String methodName){
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.
					getProperty("user.dir")+"\\screenshotsOnSuccess"+"\\"+"screenshotOnSuccess"+"_"+methodName+common.getCurrentTimeInstance()+"."+"png"));
			/*FileUtils.copyFile(scrFile, new File(System.
					getProperty("user.dir")+"\\TestJarFunc\\screenshotsOnSuccess"+"\\"+"screenshotOnSuccess"+"_"+methodName+common.getCurrentTimeInstance()+"."+"png"));*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method for Creating a new File
	 * @param filePath
	 */
	public void verifyAndCreateFile(String filePath){
		
		File file = new File(filePath);
		
		try{
			if (file.exists()){
				System.out.println("File already exists");
			}
			
			if (!file.exists()){
				
				boolean createNewFile = file.createNewFile();
				if (createNewFile){				
					System.out.println("Printing " + createNewFile + " if file is created");
				}else{
					System.out.println("Printing " + createNewFile + " if file is not created");
				}
			}
		}
		catch (IOException e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * Method to connecting to Database
	 * @param hostName
	 * @param port
	 * @param dbName
	 * @param username
	 * @param password
	 * @throws SQLException
	 */
	public void connectingToDatabase(String hostName, String port, String dbName,
			String username, String password, String Query) throws SQLException{
		
		Connection conn = null;
		Statement statement = null;
		
		try{
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://"+hostName+":"+port+"/"+dbName+"", username, password);
			System.out.println("Opened database successfully");
			
			statement = conn.createStatement();
			String sqlQuery = Query;
			statement.executeQuery(sqlQuery);
						
		}catch(Exception ex){
			System.err.println( ex.getClass().getName() + " : " + ex.getMessage() );
	         System.exit(0);
		}finally {
			statement.close();
			conn.close();
		}
	}
	
	/**
	 * getPasswordFromGmail
	 * @param userEmail
	 * @param userEmailPassword
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String getPasswordFromGmail(String userEmail,
			String userEmailPassword) throws InterruptedException, IOException {

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		File tempFile;

		try {
			Session session = Session.getInstance(props, null);
			System.out.println("########## session is #####" + session);
			Store store = session.getStore();
			System.out.println(userEmail + "," + userEmailPassword);
			store.connect("imap.gmail.com", userEmail, userEmailPassword);
			Thread.sleep(5000);
			Folder inbox = store.getFolder("INBOX");
			Thread.sleep(2000);
			inbox.open(Folder.READ_ONLY);
			Thread.sleep(2000);
			Message msg = inbox.getMessage(1);

			SearchTerm searchTerm = new SearchTerm() {

				@Override
				public boolean match(Message message) {
					try {
						if (message
								.getSubject()
								.contains(
										"Passenger Details")) {
							return true;
						}
					} catch (MessagingException ex) {
						ex.printStackTrace();
					}
					return false;
				}
			};
			Message[] foundMessages = inbox.search(searchTerm);
			List<Message> totalMessageList = Arrays.asList(foundMessages);
			Collections.sort(totalMessageList, new Comparator<Message>() {
				public int compare(Message o1, Message o2) {
					try {
						return o1.getReceivedDate().compareTo(
								o2.getReceivedDate());
					} catch (MessagingException e) {
						e.printStackTrace();
					}
					return 0;
				}
			});
			msg = totalMessageList.get(totalMessageList.size() - 1);

			Object content = msg.getContent();
			if (content instanceof Multipart) {
				Multipart mp = (Multipart) content;
				BodyPart bp = mp.getBodyPart(0);

				System.out.println("SENT DATE:" + msg.getSentDate());
				System.out.println("SUBJECT:" + msg.getSubject());
				System.out.println("CONTENT:" + bp.getContent());
				gmailBodyText = (String) bp.getContent();
			} else {
				gmailBodyText = content.toString();
			}
			Pattern pattern = Pattern
					.compile("(?si)http(.*)");
			Matcher m = pattern.matcher(gmailBodyText);
			if (m.find()) {
				gmailBodyText = m.group(1).trim();
			}
			System.out.println("msgBody CONTENT:" + gmailBodyText);

		} catch (Exception mex) {
			mex.printStackTrace();
		}
		System.out.println("returned gmailBodyText inside method>>> : "
				+ gmailBodyText);
		return gmailBodyText;

	}

	public void searchUsingGoogleSearchBar(WebDriver driver, Hashtable<String, String> data) {
		
		//Wait for the search bar
		common.explicitWaitElementToBeVisible(driver, 10, "gooleHomePage_searchBar");
		common.isElementPresent(driver, "gooleHomePage_searchBar");
		
		//Send a search string
		common.sendKeys(driver, "gooleHomePage_searchBar", data.get("SearchString"));
		common.explicitWaitElementToBeVisible(driver, 10, "googleHome_page_searchBtn");
		
		//Click on google search button
		common.clickByJavaScript(driver, "googleHome_page_searchBtn");
		System.out.println("Searched for Capgemini Careers");
		
		//Get the Hyperlinks on the search results page
		int listOfItems = common.listOfItems(driver, "googleSearch_results_page_anchorTags");
		
		for (int i = 1; i <= listOfItems; i++) {
			String eachresult = driver.findElement(By.xpath("(//a)["+i+"]")).getText();
			if (eachresult.contains(data.get(data.get("PageLinkText")))) {
				Assert.assertTrue(eachresult.contains(data.get(data.get("PageLinkText"))));
				break;
			}
		}
	}
	
	
}// End of Class