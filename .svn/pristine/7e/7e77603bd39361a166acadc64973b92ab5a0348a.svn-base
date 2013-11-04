/*****************************************************
 * Class Name: GetInputData
 * Class Purpose: data Provider class
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	
	public final WebDriver driver; // driver variable
	
	/* WebElements for HomePage Page Objects*/
	@FindBy(css="div[id='navbar'] button[class*='btn btn-warning reset-search-button']")
	private WebElement  resetSearchButtonCSS;
	
	@FindBy(css="div[id='main-search-input'] button[class*='btn btn-info search-button']")
	private WebElement  runSearchButtonCSS;
		
	@FindBy(css="div#main-search-input button.reset-search")
	private WebElement searchFilterDropDownButton;
	
	@FindBy(css="div#main-search-input ul.dropdown-menu li.ng-scope a.ng-binding")
	private List <WebElement> searchCriterianFilterOptions;
		
	@FindBy(css="div#content-main div#result-navigation div.notification-position button")
	private WebElement statsButton;
	
	@FindBy(css="div[id='main-search-input'] textarea[class*='input-medium " +
			"textarea search-textarea']")
	private WebElement searchTextFieldCSS;
	
	@FindBy(css="div#result-navigation.row-fluid div.span4 div.pull-left span.notification-green")
	private WebElement greenStatus;
	
	@FindBy(css="div[class='container-fluid'] div[class*='nav-collapse'] li[class*='facet-nav-button'] a span")
	private List<WebElement> facetsList;
	
	@FindBy(css="div#main-search-input div.filtered-by-popdown")
	private WebElement nameOnRunSearchButtonCSS;
	
	@FindBy(css="div.container-fluid a.brand")
	private WebElement recentCRrequesttextLocator;
	
	@FindBy(css="div#result-navigation.row-fluid div.span4 div.btn-group button[ng-click*='Line']")
	private WebElement linesButton;
	
	@FindBy(css="div[id='navbar'] li[class='dropdown'] a[class='dropdown-toggle'] span")
	private WebElement tiedTextCss;

	@FindBy(css="div#navbar.navbar div.navbar-inner div.container-fluid a.brand")
	private WebElement homeButton;
	
	/* Constructor */
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    
    /* 1. Verify the Text Display on the Button is matching with the Argument that was provided by us */
    public boolean verifyTextOnSearchButton(String expectedButtonName){
    	boolean verifyButtonName = false;
       	String buttonName = nameOnRunSearchButtonCSS.getText();
    	if(buttonName.contains(expectedButtonName)){
    		verifyButtonName = true;
    	}
    	return verifyButtonName;
    }

    /* 2. Enter any required Text in the Search Text Area Field */
    public void	enterSearchCriteria(String contractNumber) throws InterruptedException{
    	searchTextFieldCSS.sendKeys(contractNumber);
    	Thread.sleep(5000);
    }
    
    /* 3. To Clear the Text in the Filter Text Area. */
    public void	clearFilterTextArea(){
    	searchTextFieldCSS.clear();
    }
    
    /* 4. Click on Run(Search) button. */
    public void	clickOnRunSearchButton() throws InterruptedException{
    	//runSearchButtonCSS.click();
    	Actions builder = new Actions(driver);
		builder.moveToElement(runSearchButtonCSS).perform();
		builder.click(runSearchButtonCSS).perform();
    	waitforalertcomplete();
    	//Thread.sleep(5000);
    }  
    
    /* 5. Click on list select dropdown */
    public void clickFilterDropDown() throws InterruptedException{
    	searchFilterDropDownButton.click();
    	Thread.sleep(3000);
    }
    
    /* 6. Clicking on Reset Button */
    public void clickOnResetButton() throws InterruptedException{
    	resetSearchButtonCSS.click();
    	Thread.sleep(5000);
    }
    
   /* 7. Selecting the Required Filter criteria. */   
    public void selectingSearchOption(String requiredOption) throws InterruptedException{  	
       	for(int i = 0; i<searchCriterianFilterOptions.size(); i++){
       		if(searchCriterianFilterOptions.get(i).getText().equals(requiredOption)){
       			searchCriterianFilterOptions.get(i).click();
       			break;
       		}
       	}  
     }
    
    /* 8. Check whether home page appears or not */
    public boolean validatehomepagedispay(){
    	return (recentCRrequesttextLocator.isDisplayed());
    }
    
    /* 9. Returning the Search Filter Values available in the Drop down */
    public List<String> getSearchFilterValues(){
    	List<String> filterValues = new ArrayList<String>();
    	int filterValuesSize = searchCriterianFilterOptions.size();
    	for(int i = 0; i<filterValuesSize; i++){
    		filterValues.add(searchCriterianFilterOptions.get(i).getText());
    	}
    	return filterValues;
    }
    
    /* 10. Verifying whether the given facet is available in the displayed facet list */
    public boolean verifyGivenFacet(String facetNameToVerify){
    	boolean facetAvailabilityFlag = false;
    	int totalFacets = facetsList.size();
    	for(int i = 0; i<totalFacets; i++){
    		if((facetNameToVerify!=null) && (facetsList.get(i).getAttribute("id").equalsIgnoreCase(facetNameToVerify))){
    			facetAvailabilityFlag = true;
    		}
    	}
    	return facetAvailabilityFlag;
    }
        
    /* 11. Validating the search Filter value whether it is available in the list or not.*/
    public boolean validateSearchFilterValue(String validateSearchValue) throws InterruptedException{
       	boolean searchValFlag = false;
       	int filterValuesSize = searchCriterianFilterOptions.size();
    	for(int i = 0; i<filterValuesSize; i++){
       		if(validateSearchValue.contains(searchCriterianFilterOptions.get(i).getText().trim())){
       			searchValFlag=true;
       			break;
       		}
       	}  
       	return searchValFlag;
     }
    
  	/* 12 . get the customer name displayed */
  	public String getTiedCustomerValue(){
  		return tiedTextCss.getText();
  	}
    
  	/* 13. verifies if the customer name who has logged in to the scm is correct or not */
  	public boolean verifyTiedCustomerValue(String expectedCustValue){
  		boolean tiedCustomerFlag = false;
  		if(expectedCustValue.trim().toLowerCase().contains(tiedTextCss.getText().toLowerCase().trim())){
  			tiedCustomerFlag = true;
  		}
  		return tiedCustomerFlag;
  	}
  	
  	/* 14. Method to click on Home Button */
    public void clickHomeButton() throws InterruptedException{
    	homeButton.click();
    	Thread.sleep(3000);
    }
    /* 15. Method to wait until alert complete appears on results page */
    public boolean waitforalertcomplete() throws InterruptedException{
    	boolean mywait = (new WebDriverWait(driver, 30000))
    				.until(ExpectedConditions.textToBePresentInElement(By.cssSelector("div[id='mainNotificationPullDown'][pop-down='true']"), "Complete!"));
    	Thread.sleep(3000);
    	return mywait;
    }
    
}