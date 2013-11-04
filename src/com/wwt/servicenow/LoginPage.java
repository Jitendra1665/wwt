package com.wwt.servicenow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
	
	protected  WebDriver driver = null;
	
	@FindBy(css="iframe[id='gsft_main']")
	private WebElement loginframe;
	
	@FindBy(css="input[id='user_name']")
	private WebElement username;
	
	@FindBy(css="input[id='user_password']")
	private WebElement password;
	
	@FindBy(css="button[class='login']")
	private WebElement loginButton;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean loginApplication(String UNAME, String PWD){
		try{
			Thread.sleep(5000);
			driver.switchTo().frame(loginframe);
			username.click();
			username.sendKeys(UNAME);
			password.click();
			password.sendKeys(PWD);
			loginButton.click();
			return true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
