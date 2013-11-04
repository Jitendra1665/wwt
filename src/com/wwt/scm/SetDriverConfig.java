/*****************************************************
 * Class Name: SetDriverConfig
 * Class Purpose: To set the driver configuration for SCM
 * Created by:Jitendra/Srinivas
 *****************************************************/
package com.wwt.scm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SetDriverConfig  {
	
	protected WebDriver driver = null;
		
	public SetDriverConfig(WebDriver driver) throws FileNotFoundException{
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
	//Setting the Driver Configuration 
	public void launchBroswer (Properties driverProperites) throws IOException
	{
		driver.manage().window().maximize();
		String SELENIUMWAITTIME = driverProperites.getProperty("DRIVERWAITTIME");
		Integer SECONDS = new Integer(SELENIUMWAITTIME);
			
		driver.manage().timeouts().implicitlyWait(SECONDS, TimeUnit.SECONDS);
		String URI = driverProperites.getProperty("APP_URL");
		driver.get(URI);
	}
	
}
