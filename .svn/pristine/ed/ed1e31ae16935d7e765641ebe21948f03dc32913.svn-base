/*****************************************************
 * Class Name: StatsPage
 * Class Purpose: Stats Page Objects
 * Created by:Jitendra/Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StatsPage {
	
	protected final WebDriver driver; // Webdriver variable
	
	/* Web Elements for Stats page */
	@FindBy(css="div[id='result-navigation'] div[class='pull-left notification-position'] " +
			"span[class='notification not-found-notify-success notification-green ng-binding']")
	private WebElement successNumber;

	@FindBy(css="div[id='result-navigation'] div[class='pull-left notification-position'] " +
			"span[class='notification not-found-notify-error notification-red ng-binding']")
	private WebElement errorNumber;

	@FindBy(css="div[id='search-meta-container'] h3[class='span3'] span[class='matched-count ng-binding']")
	private WebElement actualMatchedCountCss;

	@FindBy(css="div[id='search-meta-container'] h3[class='span3'] span[class='not-found-count ng-binding']")
	private WebElement actualNotFoundCountCss;

	@FindBy(css="div[id='not-found-table-container'] table[id='not-found-table'] " +
			"td[class='term ng-binding']")
	private List <WebElement> notFoundResultText;

	@FindBy(css="div[id='matched-terms-table-container'] table[id='matched-terms-table'] " +
			"tr[class='ng-scope'] td[class='term ng-binding']")
	private List <WebElement> matchedResultText;

	@FindBy(css="div#content-main div#result-navigation div.notification-position button.btn")
	private WebElement statsButton1;

	@FindBy(css="div#search-meta-container.row-fluid div.span9 div.span3 button.btn")
	private WebElement statReSubmitButton;

	/* Constructor */
	public StatsPage(WebDriver driver){
		this.driver = driver;
	}
	
	/* Method to check Not Found displayed or not*/
	public boolean notFoundNumberDisplay(){
		boolean errorNumberDisplayFlag = false;
		if(errorNumber.isDisplayed())		{
			errorNumberDisplayFlag=true;
		}
		return errorNumberDisplayFlag;
	}
	
	/* Method to check  Success displayed found or not*/
	public boolean successNumberDisplay(){
		boolean successNumberDisplayFlag = false;
		if(successNumber.isDisplayed()){
			successNumberDisplayFlag=true;
		}
		return successNumberDisplayFlag;
	}
	
	/* Method to get Success status count*/
	public int getSuccessStatCount(){
		return new Integer(successNumber.getText());
	}
	
	/* Method to get Not Found status count*/
	public int getNotFoundStatCount(){
		return new Integer(errorNumber.getText());
	}
	
	/* Method to click Status Button*/
	public void clickStatusButton(){
		statsButton1.click();
	}
	
	/* Method to get Stats page match count*/
	public int getStatPageMatchCount(){
		return new Integer(actualMatchedCountCss.getText());
	}
	
	/* Method to get Stats page Not Found count*/
	public int getStatPageNotFoundCount(){
		return new Integer(actualNotFoundCountCss.getText());
	}
	
	/* Verifying the count of the matched results displayed on the stats page and the stats button */
	public boolean verifyMatchCount() throws InterruptedException{
		boolean matchedFlag = false;
		clickStatusButton();
		Thread.sleep(3000);
		if(getSuccessStatCount()==getStatPageMatchCount()){
			matchedFlag = true;
		}
		return matchedFlag;
	}
	
	/*Verifying the count of the non matched results displayed on the stats page and the stats button */
	public boolean verifyNotFoundCount() throws InterruptedException{
		boolean notFoundFlag = false;
		clickStatusButton();
		Thread.sleep(3000);
		if(getNotFoundStatCount()==getStatPageNotFoundCount()){
			notFoundFlag = true;
		}
	
		return notFoundFlag;
	}

	/*verify search results in the table. */
	public boolean verifySearchValues(String searchedItem) throws InterruptedException
	{
		boolean searchAndActualResultsMatchFlag = false;
		for(int i = 0; i<matchedResultText.size(); i++){
			if(searchedItem.contains(matchedResultText.get(i).getText())){
				searchAndActualResultsMatchFlag = true;
			}
		}
		return searchAndActualResultsMatchFlag;
	}

	/* Verification of the StatsButton enablement. */
	public boolean verifyStatsButton() throws InterruptedException{
		boolean searchAndActualResultsNotFoundFlag = false;
		if(statsButton1.isEnabled()){
			searchAndActualResultsNotFoundFlag = true;
		}
		return searchAndActualResultsNotFoundFlag;
	}
	
	// Clicking on Stats Matched Results Button
	public void clickStatMatchedResultsButton() throws Exception{
		if(successNumberDisplay()){
			clickStatusButton();
			Thread.sleep(3000);
		}
	}		
	
	/*verify the non matched search results when there are */
	public boolean verifyNotFoundSearchItem(String searchedItem) throws InterruptedException{
		boolean searchAndActualResultsNotFoundFlag = false;
		for(int i = 0; i<notFoundResultText.size(); i++){
			if(searchedItem.contains(notFoundResultText.get(i).getText())){
				searchAndActualResultsNotFoundFlag = true;
			}
		}
		return searchAndActualResultsNotFoundFlag;
	}

	/* Clicking on Stats Matched Results Button */
	public void verifyStatNotFoundResults() throws Exception{
		if(notFoundNumberDisplay()){
			clickStatusButton();
			Thread.sleep(3000);
		}else{
			System.out.println("There is not stats diaplayed for the given search criteria");
		}
	}

	/*  Verification of Stat Resubmit Button Dispaly */
	public boolean statReSubmitButtonDisplay(){
	   boolean statResubmitFlag = false;
	   if(statReSubmitButton.getText().contains("Resubmit Search")){
		   statResubmitFlag = true;
	   }
	   return statResubmitFlag;
	}
}
