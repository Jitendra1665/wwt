/*****************************************************
 * Class Name: LoginPage
 * Class Purpose: Login Page Page Objects
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
	
		protected final WebDriver driver; //driver Variable
		
		/* WebEleemnts for Login Page Objects */
		@FindBy(id="content")
		private WebElement iframe1;	

		@FindBy(id="credentials")
		private WebElement iframe2;	

		@FindBy(id="username")
		private WebElement userNameTxt;	

		@FindBy(id="password")
		private WebElement passwordTxt;	
		
		@FindBy(css="#submit")
		private WebElement submit;	
		
		@FindBy(css="[href=\"#tabs-1\"]")
		private WebElement favoritesTabLink;	
		
		/* Constructor */
		public LoginPage(WebDriver driver) {
	        this.driver = driver;
	    }
		
		/* Method to login into SCM */
		public void ClickOnlogIn(String usrName, String pwd) throws Exception{
			Thread.sleep(2000);
		   	driver.switchTo().frame(iframe1);
	        driver.switchTo().frame(iframe2);
			userNameTxt.sendKeys(usrName);  
	        passwordTxt.sendKeys(pwd);
	        submit.click();   
			Thread.sleep(4000);
		}
}
