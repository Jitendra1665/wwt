package com.wwt.scm;


import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class GetWebDriverType  {
	
	protected WebDriver driver = null;
		
	public GetWebDriverType(WebDriver driver) throws FileNotFoundException{
		this.driver = driver;
	}
	
	//Get the Driver which according to our Choice given in the properties file
	public WebDriver getDriver(String BROWSER) throws IOException{
		if("FF".equals(BROWSER)){
			driver = new FirefoxDriver();
		}else if("IE".equals(BROWSER)){
			System.setProperty("webdriver.ie.driver",".\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}else if("GC".equals(BROWSER)){
			System.setProperty("webdriver.chrome.driver",".\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		return driver;
	}

}
