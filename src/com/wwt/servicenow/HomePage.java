package com.wwt.servicenow;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {
	
	protected WebDriver driver;
	
	@FindBy(css="iframe[id='gsft_nav']")
	private WebElement navigationFrame;
	
	@FindBy(css="td[id='ITIL - Change']~td[class='nav_menu_expand'] img[id^='img']")
	private WebElement ITILChangeMenu;
	
	@FindBy(css="body span[class='submenu'] table tbody tr[id^='module'][moduletype='NEW'][modulename='Create New'] a[href^='change_request']")
	private WebElement itilChangeCreateNew;
	
	public HomePage(WebDriver driver){
		this.driver =driver;
	}
	
	public void clickCreateNewChange() throws InterruptedException{
		Thread.sleep(3000);
		driver.switchTo().frame(navigationFrame);
		System.out.println(ITILChangeMenu.getAttribute("alt"));
		//if(ITILChangeMenu.getAttribute("alt").equalsIgnoreCase("collapse")){
			//ITILChangeMenu.click();
		//}
		itilChangeCreateNew.click();
		Thread.sleep(4000);
		
	}

}
