package com.testvagrant.assignment.Listeners;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.testvagrant.assignment.Base.TestBase;
import com.testvagrant.assignment.dataProvider.Config;

public class CaptureScreenShot implements ITestListener, IInvokedMethodListener, ISuiteListener {

	final static Logger logback = LoggerFactory.getLogger(CaptureScreenShot.class);
	Config conf = new Config(System.getProperty("user.dir") + "/config/config.properties");
	private Document document = null;
	PdfPTable successTable = null, failTable = null, skippedTable = null, notRunTable = null, totalTestCase = null;
	private HashMap<Integer, Throwable> throwableMap = null;
	private int nbExceptions = 0;
	int totalRun = 0;
	String browserName;
	int totalPassed = 0, totalFailed = 0, totalSkipped = 0, totalNotRun = 0;
	static String methodName;
	List<String> notRunMethod = new ArrayList<String>();

	/**
	 * creating constructor
	 */
	public CaptureScreenShot() {
		this.document = new Document();
		this.throwableMap = new HashMap<Integer, Throwable>();
	}

	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		logback.debug("About to end executing Test " + arg0.getName(), true);
		Reporter.log("About to end executing Test " + arg0.getName(), true);
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		logback.debug("About to begin executing Test " + context.getName(), true);
		Reporter.log("About to begin executing Test " + context.getName(), true);

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		printTestResults(result);
		Object currentClass = result.getInstance();
		WebDriver webDriver = ((TestBase) currentClass).getDriver();

		if (webDriver != null) {

			String filename = System.getProperty("user.dir") + "/failureScreenshotData/" + browserName + "_"
					+ result.getMethod().getMethodName() + getCurrentTimeInstance() + ".png";
			File f = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(f, new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Throwable throwable = result.getThrowable();
		if (!(throwable instanceof WebDriverException)) {
			// pdf
			totalFailed++;
			if (this.failTable == null) {
				this.failTable = new PdfPTable(new float[] { .2f, .3f, .3f, .1f, .3f });
				this.failTable.setTotalWidth(20f);
				Paragraph p = new Paragraph("FAILED TESTS", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
				p.setAlignment(Element.ALIGN_CENTER);
				PdfPCell cell = new PdfPCell(p);
				cell.setColspan(5);
				cell.setBackgroundColor(Color.RED);
				this.failTable.addCell(cell);

				cell = new PdfPCell(new Paragraph("Browser Name"));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				this.failTable.addCell(cell);
				cell = new PdfPCell(new Paragraph("Class"));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				this.failTable.addCell(cell);
				cell = new PdfPCell(new Paragraph("Method"));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				this.failTable.addCell(cell);
				cell = new PdfPCell(new Paragraph("Time (ms)"));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				this.failTable.addCell(cell);
				cell = new PdfPCell(new Paragraph("Exception"));
				cell.setBackgroundColor(Color.LIGHT_GRAY);
				this.failTable.addCell(cell);
			}

			PdfPCell cell = new PdfPCell(new Paragraph(browserName));
			this.failTable.addCell(cell);
			cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
			this.failTable.addCell(cell);
			cell = new PdfPCell(new Paragraph(result.getName().toString()));
			this.failTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
			this.failTable.addCell(cell);

			if (throwable != null) {

				logback.error("Exception occurred in method " + result.getName() + " : Exception is : " + throwable);
				this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
				this.nbExceptions++;
				Paragraph excep = new Paragraph(
						new Chunk(throwable.toString(), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.UNDERLINE))
								.setLocalGoto("" + throwable.hashCode()));
				cell = new PdfPCell(excep);
				this.failTable.addCell(cell);

			} else {
				this.failTable.addCell(new PdfPCell(new Paragraph("")));
			}
		}

		// For Not Run
		// pdf
		Throwable thr = result.getThrowable();
		if (thr instanceof WebDriverException) {
			totalNotRun++;
			notRunMethod.add(result.getName());
			if (this.notRunTable == null) {
				this.notRunTable = new PdfPTable(new float[] { .2f, .3f, .3f, .1f, .3f });
				this.notRunTable.setTotalWidth(20f);
				Paragraph p = new Paragraph("NOT RUN TESTS", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
				p.setAlignment(Element.ALIGN_CENTER);
				PdfPCell cell1 = new PdfPCell(p);
				cell1.setColspan(5);
				cell1.setBackgroundColor(Color.BLUE);
				this.notRunTable.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("Browser Name"));
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				this.notRunTable.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("Class"));
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				this.notRunTable.addCell(cell1);
				cell1 = new PdfPCell(new Paragraph("Method"));
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				this.notRunTable.addCell(cell1);
				cell1 = new PdfPCell(new Paragraph("Time (ms)"));
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				this.notRunTable.addCell(cell1);
				cell1 = new PdfPCell(new Paragraph("Selenium Exception"));
				cell1.setBackgroundColor(Color.LIGHT_GRAY);
				this.notRunTable.addCell(cell1);
			}

			PdfPCell cell1 = new PdfPCell(new Paragraph(browserName));
			this.notRunTable.addCell(cell1);
			cell1 = new PdfPCell(new Paragraph(result.getTestClass().toString()));
			this.notRunTable.addCell(cell1);
			cell1 = new PdfPCell(new Paragraph(result.getName().toString()));
			this.notRunTable.addCell(cell1);
			cell1 = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
			this.notRunTable.addCell(cell1);
			// String exception = result.getThrowable() == null ? "" :
			// result.getThrowable().toString();
			// cell = new PdfPCell(new Paragraph(exception));
			// this.failTable.addCell(cell);

			logback.debug("Selenium Exception occurred in method " + result.getName() + " : Exception is : " + thr);
			this.throwableMap.put(new Integer(thr.hashCode()), thr);
			this.nbExceptions++;
			Paragraph excep = new Paragraph(new Chunk("Selenium Exception Occurred",
					new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.UNDERLINE)).setLocalGoto("" + thr.hashCode()));
			cell1 = new PdfPCell(excep);
			this.notRunTable.addCell(cell1);
		}

	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		printTestResults(result);
		if (skippedTable == null) {
			this.skippedTable = new PdfPTable(new float[] { .2f, .3f, .3f, .1f, .3f });
			Paragraph p = new Paragraph("SKIPPED TEST", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			PdfPCell cell = new PdfPCell(p);
			cell.setColspan(5);
			cell.setBackgroundColor(Color.YELLOW);
			this.skippedTable.addCell(cell);

			cell = new PdfPCell(new Paragraph("Browser Name"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.skippedTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Class"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.skippedTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Method"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.skippedTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Time (ms)"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.skippedTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Exception"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.skippedTable.addCell(cell);
		}

		PdfPCell cell = new PdfPCell(new Paragraph(browserName));
		this.skippedTable.addCell(cell);
		cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
		this.skippedTable.addCell(cell);
		cell = new PdfPCell(new Paragraph(result.getName().toString()));
		this.skippedTable.addCell(cell);
		cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
		this.skippedTable.addCell(cell);

		Throwable throwable = result.getThrowable();
		if (throwable != null) {
			this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
			this.nbExceptions++;
			Paragraph excep = new Paragraph(
					new Chunk("Test Case Skipped", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.UNDERLINE))
							.setLocalGoto("" + throwable.hashCode()));
			cell = new PdfPCell(excep);
			this.skippedTable.addCell(cell);
		} else {
			this.skippedTable.addCell(new PdfPCell(new Paragraph("")));
		}

		//
		totalSkipped++;
	}

	// @Test(timeOut = 240000, dataProvider = "getTestData",
	// dataProviderClass=CaptureScreenShot.class)
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		totalRun++;

	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

		totalPassed++;
		printTestResults(result);
		if (successTable == null) {
			this.successTable = new PdfPTable(new float[] { .2f, .3f, .3f, .1f, .3f });
			Paragraph p = new Paragraph("PASSED TESTS", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			PdfPCell cell = new PdfPCell(p);
			cell.setColspan(5);
			cell.setBackgroundColor(Color.GREEN);
			this.successTable.addCell(cell);

			cell = new PdfPCell(new Paragraph("Browser Name"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.successTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Class"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.successTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Method"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.successTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Time (ms)"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.successTable.addCell(cell);
			cell = new PdfPCell(new Paragraph("Exception"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.successTable.addCell(cell);
		}

		PdfPCell cell = new PdfPCell(new Paragraph(browserName));
		this.successTable.addCell(cell);
		cell = new PdfPCell(new Paragraph(result.getTestClass().toString()));
		this.successTable.addCell(cell);
		cell = new PdfPCell(new Paragraph(result.getName().toString()));
		this.successTable.addCell(cell);
		cell = new PdfPCell(new Paragraph("" + (result.getEndMillis() - result.getStartMillis())));
		this.successTable.addCell(cell);

		Throwable throwable = result.getThrowable();
		if (throwable != null) {
			this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
			this.nbExceptions++;
			Paragraph excep = new Paragraph(
					new Chunk(throwable.toString(), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.UNDERLINE))
							.setLocalGoto("" + throwable.hashCode()));
			cell = new PdfPCell(excep);
			this.successTable.addCell(cell);
		} else {
			this.successTable.addCell(new PdfPCell(new Paragraph("")));
		}

	}

	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
		// TODO Auto-generated method stub
		String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());
		logback.debug(textMsg, true);
		Reporter.log(textMsg, true);
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult arg1) {
		// TODO Auto-generated method stub
		if (method.isTestMethod()) {
			browserName = method.getTestMethod().getXmlTest().getLocalParameters().get("browser");
			if (browserName == null) {
				browserName = conf.getValue("driver.browsername");
			}
		}
		String textMsg = "About to begin executing following method : " + returnMethodName(method.getTestMethod());
		logback.debug(textMsg, true);
		Reporter.log(textMsg, true);
	}

	/**
	 * user defined methods
	 * 
	 * @param result
	 */

	public String getCurrentTimeInstance() {
		Calendar calendar = Calendar.getInstance();
		String currentTimeInstance = "-" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-"
				+ calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND) + "-"
				+ calendar.get(Calendar.MILLISECOND);
		return currentTimeInstance;

	}
	// This will provide the debugrmation on the test

	private void printTestResults(ITestResult result) {

		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);

		if (result.getParameters().length != 0) {

			String params = null;

			for (Object parameter : result.getParameters()) {

				params += parameter.toString() + ",";

			}

			Reporter.log("Test Method had the following parameters : " + params, true);
			logback.debug("Test Method had the following parameters : " + params, true);

		}

		String status = null;

		switch (result.getStatus()) {

		case ITestResult.SUCCESS:

			status = "Pass";

			break;

		case ITestResult.FAILURE:

			status = "Failed";

			break;

		case ITestResult.SKIP:

			status = "Skipped";

		}

		Reporter.log("Test Status: " + status, true);
		logback.debug("Test Status: " + status, true);

	}

	// This will return method names to the calling function

	private String returnMethodName(ITestNGMethod method) {

		return method.getRealClass().getSimpleName() + "." + method.getMethodName();

	}

	public void onFinish(ISuite suite) {

		// creating total test count table
		if (totalTestCase == null) {
			this.totalTestCase = new PdfPTable(new float[] { .2f, .2f, .2f, .2f, .2f });
			Paragraph p = new Paragraph("TEST EXECUTION SUMMARY",
					new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			PdfPCell cell = new PdfPCell(p);
			cell.setColspan(5);
			cell.setBackgroundColor(Color.ORANGE);
			this.totalTestCase.addCell(cell);

			cell = new PdfPCell(new Paragraph("Total TC Run"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.totalTestCase.addCell(cell);
			cell = new PdfPCell(new Paragraph("Total TC Passed"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.totalTestCase.addCell(cell);
			cell = new PdfPCell(new Paragraph("Total TC Skipped"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.totalTestCase.addCell(cell);
			cell = new PdfPCell(new Paragraph("Total TC Not Run"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.totalTestCase.addCell(cell);
			cell = new PdfPCell(new Paragraph("Total TC Failed"));
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			this.totalTestCase.addCell(cell);

		}

		PdfPCell cell = new PdfPCell(new Paragraph(Integer.toString(totalRun)));
		this.totalTestCase.addCell(cell);
		cell = new PdfPCell(new Paragraph(Integer.toString(totalPassed)));
		this.totalTestCase.addCell(cell);
		cell = new PdfPCell(new Paragraph(Integer.toString(totalSkipped)));
		this.totalTestCase.addCell(cell);
		cell = new PdfPCell(new Paragraph(Integer.toString(totalNotRun)));
		this.totalTestCase.addCell(cell);

		Paragraph excep = new Paragraph(
				new Chunk(Integer.toString(totalFailed), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.UNDERLINE))
						.setLocalGoto("" + this.totalFailed));
		cell = new PdfPCell(excep);
		this.totalTestCase.addCell(cell);

		// creating table for pass fail skip test cases
		try {
			if (this.totalTestCase != null) {
				this.totalTestCase.setSpacingBefore(15f);
				this.document.add(this.totalTestCase);
				this.totalTestCase.setSpacingBefore(15f);
			}

			if (this.failTable != null) {
				this.failTable.setSpacingBefore(15f);
				this.document.add(this.failTable);
				this.failTable.setSpacingAfter(15f);
			}

			if (this.successTable != null) {
				this.successTable.setSpacingBefore(15f);
				this.document.add(this.successTable);
				this.successTable.setSpacingBefore(15f);
			}

			if (this.skippedTable != null) {
				this.skippedTable.setSpacingBefore(15f);
				this.document.add(this.skippedTable);
				this.skippedTable.setSpacingBefore(15f);
			}

			if (this.notRunTable != null) {
				this.notRunTable.setSpacingBefore(15f);
				this.document.add(this.notRunTable);
				this.notRunTable.setSpacingBefore(15f);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Paragraph p = new Paragraph("EXCEPTIONS SUMMARY",
				FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
		try {
			this.document.add(p);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		Set<Integer> keys = this.throwableMap.keySet();

		assert keys.size() == this.nbExceptions;

		for (Integer key : keys) {
			Throwable throwable = this.throwableMap.get(key);

			Chunk chunk = new Chunk(throwable.toString(),
					FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(255, 0, 0)));
			chunk.setLocalDestination("" + key);
			Paragraph throwTitlePara = new Paragraph(chunk);
			try {
				this.document.add(throwTitlePara);
			} catch (DocumentException e3) {
				e3.printStackTrace();
			}

			StackTraceElement[] elems = throwable.getStackTrace();
			String exception = "";
			for (StackTraceElement ste : elems) {
				Paragraph throwParagraph = new Paragraph(ste.toString());
				try {
					this.document.add(throwParagraph);
				} catch (DocumentException e2) {
					e2.printStackTrace();
				}
			}

		}

		this.document.close();

	}

	public void onStart(ISuite suite) {
		try {
			PdfWriter.getInstance(this.document, new FileOutputStream(suite.getName() + "_Results" + ".pdf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.document.open();

		Paragraph p = new Paragraph(suite.getName() + " \n TESTNG RESULTS",
				FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(0, 0, 255)));

		try {
			this.document.add(p);
			this.document.add(new Paragraph(new Date().toString()));
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

	}

}
