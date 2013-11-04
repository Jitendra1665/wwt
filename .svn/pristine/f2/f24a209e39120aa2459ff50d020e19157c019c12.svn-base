package com.wwt.scm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitSleep {
	
	protected final WebDriver driver;
	
	@FindBy(css="div[id='navbar'] li[class='dropdown'] a[class='dropdown-toggle'] span")
	private WebElement tiedTextCss;
	
	public WaitSleep(WebDriver driver) {
        this.driver = driver;
    }
	
	public void waitUntilPageLoads() throws InterruptedException
	{
		WebDriverWait waiting = new WebDriverWait(driver, 50);
		WebElement LoginFrameLoad;
		LoginFrameLoad = waiting.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id=" +
				"'result-navigation'] div[class='alert-container span4'] div[class*='ng-binding alert alert-success']")));
		Thread.sleep(2000);
		LoginFrameLoad.getText();
		
	}
	
	public void waitUntilPageLoads(String cssSelectorPATH) throws InterruptedException
	{
		WebDriverWait waiting = new WebDriverWait(driver, 50);
		WebElement LoginFrameLoad;
		LoginFrameLoad = waiting.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelectorPATH)));
		Thread.sleep(2000);
		LoginFrameLoad.getText();
	}
	
	
	public void waitUntilPageLoad(String Xpath) throws InterruptedException
	{
		WebDriverWait waiting = new WebDriverWait(driver, 50);
		WebElement LoginFrameLoad;
		LoginFrameLoad = waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath)));
		Thread.sleep(2000);
		LoginFrameLoad.getText();
		
	}
	
	public boolean waitUntilPageLoad(String Xpath, String compareWithString) throws InterruptedException
	{
		WebDriverWait waiting = new WebDriverWait(driver, 50);
		WebElement LoginFrameLoad;
		LoginFrameLoad = waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath)));
		while(LoginFrameLoad.getText()!=compareWithString)
		{
			LoginFrameLoad = waiting.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath)));
		}
		return true;
	}
	
	
	
}
