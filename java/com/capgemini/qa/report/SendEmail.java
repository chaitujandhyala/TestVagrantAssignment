package com.capgemini.qa.report;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.testng.TestNG;

public class SendEmail {
	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	public static SendEmail sendMail = new SendEmail();
	
	public static void main(String[] args){
		Properties config = new Properties();
		
		try{
			sendMail.runTheXmlSuite();
			FileInputStream fs = new FileInputStream(
				System.getProperty("user.dir") + "/config/configuration.properties");
			config.load(fs);
			String[] msgBody = {config.getProperty("emailBody"),config.getProperty("emailSignature")};
			
			//TO Mail ID's
			String []recipientsTO =				
				config.getProperty("to").split(",");
			//CC Mail Id's
			String []recipientsCC = 
				config.getProperty("cc").split(",");
			String []attachments = {

				System.getProperty("user.dir")+"\\Single_Run_TestSuite.pdf"};
		 
			send(config.getProperty("senderEmail"), 
				config.getProperty("senderEmailPwd"), 
				config.getProperty("emailSubject")+config.getProperty("buildNo"), 
				msgBody, attachments, recipientsCC, recipientsTO);
			System.out.println("Msg Bdy[1]"+ msgBody[1]);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * Sends an email using the Gmail SMTP server.
	 *
	 * @param senderEmail     the sender's email address
	 * @param senderPassword  the sender's password, required for authentication
	 *                        with the SMTP server
	 * @param subject         the email's subject
	 * @param msg             the email's message contents
	 * @param attachment      the email attachment, or null
	 * @param recipients      a list of recipient email addresses
	 * @throws RuntimeException  if an error occurs while sending the email
	 */
	public static void send(final String senderEmail, final String senderPassword,
		String subject, String msg, String attachments[], String... recipients)
      	throws RuntimeException {
	    Properties props = new Properties();
	    props.put("mail.smtp.host", SMTP_HOST);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.port", SMTP_PORT);
	    props.put("mail.smtp.socketFactory.port", SMTP_PORT);
	    props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	    props.put("mail.smtp.socketFactory.fallback", "false");
	
	    Session session = Session.getDefaultInstance(props,
	        new Authenticator() {
	          	protected PasswordAuthentication getPasswordAuthentication() {
	          		return new PasswordAuthentication(senderEmail, senderPassword);
          		}
	        });
	    session.setDebug(true);

	    try {
	    	MimeMessage message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(senderEmail));
    		InternetAddress[] addressTo = new InternetAddress[recipients.length];
    		for (int i = 0; i < recipients.length; i++) {
    			addressTo[i] = new InternetAddress(recipients[i]);
    		}
    		message.setRecipients(Message.RecipientType.TO, addressTo);
	
    		// Set the subject, message body, and attachment.
    		message.setSubject(subject);
    		if (attachments == null) {
		        message.setText(msg, "UTF-8");
		        message.setHeader("Content-Type", "text/html; charset=UTF-8");
    		} else {
    			Multipart multipart = new MimeMultipart("mixed");
    			BodyPart msgBodyPart = new MimeBodyPart();
    			msgBodyPart.setContent(msg, "text/html; charset=UTF-8");
    			msgBodyPart.setHeader("Content-Type", "text/html; charset=UTF-8");
    			// add it
    			multipart.addBodyPart(msgBodyPart);
    			
    			
		        for (String str : attachments) {
			        MimeBodyPart attachmentPart = new MimeBodyPart();
		            DataSource source = new FileDataSource(str);
		            attachmentPart.setDataHandler(new DataHandler(source));
		            attachmentPart.setFileName(source.getName());
		         
		            multipart.addBodyPart(attachmentPart);
		        }
	        
	        	message.setContent(multipart);
			}
	
    		Transport.send(message);
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	}
	
	/**
	 * Sends an email using the Gmail SMTP server.
	 *
	 * @param senderEmail     the sender's email address
	 * @param senderPassword  the sender's password, required for authentication
	 *                        with the SMTP server
	 * @param subject         the email's subject
	 * @param msg             the email's message contents
	 * @param attachment      the email attachment, or null
	 * @param ccRecipients    a list of recipient email addresses that are to be cc'ed
	 * @param recipients      a list of recipient email addresses
	 * @throws RuntimeException  if an error occurs while sending the email
	 */
	public static void send(final String senderEmail, final String senderPassword,
		String subject, String msg[], String attachments[], String ccRecipients[], 
		String... recipients)
      	throws RuntimeException {		
	    Properties props = new Properties();
	    props.put("mail.smtp.host", SMTP_HOST);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.port", SMTP_PORT);
	    props.put("mail.smtp.socketFactory.port", SMTP_PORT);
	    props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	    props.put("mail.smtp.socketFactory.fallback", "false");
	
	    Session session = Session.getDefaultInstance(props,
	        new Authenticator() {
	          	protected PasswordAuthentication getPasswordAuthentication() {
	          		return new PasswordAuthentication(senderEmail, senderPassword);
          		}
	        });
	    session.setDebug(true);

	    try {
	    	MimeMessage message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(senderEmail));
    		InternetAddress[] addressTo = new InternetAddress[recipients.length];
    		for (int i = 0; i < recipients.length; i++) {
    			addressTo[i] = new InternetAddress(recipients[i]);
    		}
    		message.setRecipients(Message.RecipientType.TO, addressTo);
    		if(ccRecipients.length>0){
	    		InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
	    		for (int i = 0; i < ccRecipients.length; i++) {
	    			addressCC[i] = new InternetAddress(ccRecipients[i]);
	    		}
	    		message.setRecipients(Message.RecipientType.CC, addressCC);
    		}    		
    		// Set the subject, message body, and attachment.
    		message.setSubject(subject);
    		if (attachments == null) {
		        message.setText(msg[0], "UTF-8");
		        message.setHeader("Content-Type", "text/html; charset=UTF-8");
    		} else {
    			
    			// Unformatted text version
    	        BodyPart textPart = new MimeBodyPart();
    	        textPart.setContent(msg[0], "text/plain"); 
    	        // HTML version
    	       //Unformatted text version--Adding signature
    	        BodyPart textPart2 = new MimeBodyPart();
    	        textPart2.setContent(msg[1], "text/plain"); 
    	        // Create the Multipart.  Add BodyParts to it.
    	        Multipart mp = new MimeMultipart("mixed");
    	        mp.addBodyPart(textPart);
    	        mp.addBodyPart(textPart2);
    			
		        for (String str : attachments) {
			        MimeBodyPart attachmentPart = new MimeBodyPart();
		            DataSource source = new FileDataSource(str);
		            attachmentPart.setDataHandler(new DataHandler(source));
		            attachmentPart.setFileName(source.getName());		     
		            mp.addBodyPart(attachmentPart);
		        }	        	
	        	message.setContent(mp);
			}
    		Transport.send(message);
	    } catch (MessagingException e) {
	    	e.printStackTrace();
	    	throw new RuntimeException(e);
	    }
	}
	
	/**
	 * Method to runTheXmlSuite
	 */
	
	public void runTheXmlSuite(){
		try{
						
			List<String> suites = new ArrayList<String>();
			suites.add(System.getProperty("user.dir") +"\\singleRun.xml");
			TestNG tng = new TestNG();
			tng.setTestSuites(suites);
			tng.run();
			System.out.println("Required xml run has triggered successfully");
			
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	
	
}