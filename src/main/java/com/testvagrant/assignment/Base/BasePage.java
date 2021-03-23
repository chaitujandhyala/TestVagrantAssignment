package com.testvagrant.assignment.Base;

import com.testvagrant.assignment.dataProvider.Config;
import com.testvagrant.assignment.dataProvider.Xls_Reader;
import com.testvagrant.assignment.util.CommonMethods;

public class BasePage {
		
	public static CommonMethods common = new CommonMethods(System.getProperty("user.dir")
			+ "/locator/objectRepository.properties");
	public  static Xls_Reader admin_xls = new Xls_Reader(System.getProperty("user.dir")
			+ "/XLS/testDataSheet.xls");
	public static Config validations = new Config(System.getProperty("user.dir")
			+ "/validations/applicationValidations.properties");
	public static Config conf = new Config(System.getProperty("user.dir")
			+ "/config/config.properties");

}
