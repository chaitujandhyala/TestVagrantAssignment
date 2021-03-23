package com.testvagrant.assignment.dataProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class Config {

	FileInputStream fis;
	Properties pr;

	/**
	 * Constructor declared for file path initialization
	 * 
	 * @param filePath
	 */
	public Config(String filePath) {

		try {

			fis = new FileInputStream(filePath);
			pr = new Properties();
			pr.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * return properties value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return pr.getProperty(key);
	}

	/**
	 * return all the keys present in a properties file
	 * 
	 * @return
	 */
	public Set<Object> getAllKeys() {
		Set<Object> keys = pr.keySet();
		return keys;
	}

}
