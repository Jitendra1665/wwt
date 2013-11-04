package com.wwt.servicenow;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class SetDriverConfig {
	protected WebDriver driver = null;
	
	public SetDriverConfig(WebDriver driver){
		this.driver = driver;
	}
	
	//Get the Driver which according to our Choice given in the properties file
	public WebDriver getDriver(String BROWSER) throws IOException{
		if("IE".equals(BROWSER)){
			System.setProperty("webdriver.ie.driver",".\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}else if("GC".equals(BROWSER)){
			System.setProperty("webdriver.chrome.driver",".\\chromedriver.exe");
			driver = new ChromeDriver();
		}else{
			driver = new FirefoxDriver();
		}	
		return driver;		
	}
	// to launch browser with given url and congiguration
	public void launchBroswer(Properties driverProperites)throws IOException{

		driver.manage().window().maximize();
		String SELENIUMWAITTIME = driverProperites.getProperty("DRIVERWAITTIME");
		Integer SECONDS = new Integer(SELENIUMWAITTIME);	
		driver.manage().timeouts().implicitlyWait(SECONDS, TimeUnit.SECONDS);
		String URI = driverProperites.getProperty("URL");
		driver.get(URI);		
	}
}
