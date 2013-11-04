/*****************************************************
 * Class Name: LineDetails
 * Class Purpose: Line Details Page Objects
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LineDetails {
	
    private WebDriver driver; // driver variable
    
    /* WebElements for Line Details Page */
    @FindBy(css="a.customer_name")
    private WebElement CustomerButtonLocator;
    
    @FindBy(css="span#customer_name-title.facet-title-popup")
    private WebElement CustomerButtonHoverLocator;    
    
    @FindBy(css="span#cl_wwt_sales_channel-icon i.icon-large")
    private WebElement SalesChannelButtonLocator;
    
    @FindBy(css="span#cl_wwt_sales_channel-title.facet-title-popup")
    private WebElement SalesChannelButtonHoverLocator;
    
    @FindBy(css="span#st_site_name-icon i.icon-large")
    private WebElement installsiteCustButtonLocator;
    
    @FindBy(css="span#st_site_name-title.facet-title-popup")
    private WebElement installsiteCustButtonHoverLocator;

    @FindBy(css="span#add_address_concat-icon i.icon-large")
    private WebElement addressButtonLocator;

    @FindBy(css="span#add_address_concat-title.facet-title-popup")
    private WebElement addressButtonHoverLocator;
    
    @FindBy(css="a#calendarSearch span#calendarSearch-icon i#calendar-search-icon.icon-large")
    private WebElement additionalFilersButtonLocator;

    @FindBy(css="span#calendarSearch-title.facet-title-popup")
    private WebElement additionalFilersButtonHoverLocator;
    
    @FindBy(css="div#main-search-input button[class*='search-button']")
    private WebElement allRunButtonLocator;
    
    @FindBy(css="span#runSearch-title.facet-title-popup-button")
    private WebElement allRunButtonHoverLocator;        
    
    @FindBy(css="div#linesTable.ng-scope div.pull-left div.lines-cell a.ng-binding")
    private List <WebElement> contractnumberLocator;
    
    @FindBy(css="div#result-navigation.row-fluid div.span4 div.btn-group button.btn.pull-left")
    private WebElement contractDetailButtonLocator; 
    
	/* Constructor */
    public LineDetails(WebDriver driver) {
        this.driver = driver;
    }	
	
	/* Clicking and validate on Contract Number link (First Link) */
	public void clickonFirstContractandValidate() throws InterruptedException{
		if (contractnumberLocator.size() > 0 ){
			contractnumberLocator.get(0).click();
			//waitforsearchComplete();
			Thread.sleep(2000);	
		}	
	}
	
	/* Generic method Checking Search Status locator */
	public void waitforsearchComplete(){
		WebDriverWait mywait = new WebDriverWait(driver,180);
		mywait.until(ExpectedConditions.textToBePresentInElement(By.cssSelector("div.alert-container div.ng-binding"), "complete"));	
	}
	
	/* Return Hover text for Customer button */
	public String customerButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(CustomerButtonLocator, CustomerButtonHoverLocator);
	}
	
	/* Return Hover text for Sales Channel button */
	public String saleschannelButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(SalesChannelButtonLocator, SalesChannelButtonHoverLocator);
	}
	
	/* Return Hover text for Install Site Customer button */	
	public String installsiteCustButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(installsiteCustButtonLocator, installsiteCustButtonHoverLocator);
	}
	
	/* Return Hover text for Address button */
	public String addressButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(addressButtonLocator, addressButtonHoverLocator);
	}

	/* Return Hover text for Additional filters button */
	public String additionalFiltersButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(additionalFilersButtonLocator, additionalFilersButtonHoverLocator);
	}
	
	/* Return Hover text for All search button */
	public String allRunButtonhovertext() throws InterruptedException{
		Thread.sleep(5000);
		return hovertextReturn(allRunButtonLocator, allRunButtonHoverLocator);
	}	
	
	/* common function to pull Hover text */
	public String hovertextReturn(WebElement parentElement, WebElement hoverelement) throws InterruptedException{
		Actions builder = new Actions(driver);			
		Action mouseover = builder.moveToElement(parentElement).build();
		mouseover.perform();
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;  // Get InnerHTML of Icons
		String htext = (String) (js.executeScript("return arguments[0].innerHTML", hoverelement));
		return htext;
	}
}
