/*****************************************************
 * Class Name: CustomerFacet
 * Class Purpose: Customer Facet Page Object
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class CustomerFacet {
	String customerSerachString=null; // Variable to read customer name search string
	
	protected final WebDriver driver; // driver variable

	/* WebElements of Customer Facet Page Object*/
	@FindBy(css="a[class='customer_name']") 
	private WebElement customerSearchIcon;
	
	@FindBy(css="div#customer_name.modal div.modal-footer div.input-append input.input-medium")
	private WebElement facetinputBox;
	
	@FindBy(css="div#customer_name.modal div.modal-header button.close")
	private WebElement custfilterdropdown;
	
	@FindBy(css="button[id='remove-selected-facets']")
	private WebElement removeselectedFacets;
	
	@FindBy(css="div#customer_name.modal div.modal-body div.pull-left ul.nav li.ng-scope a[ng-click*='selectFacet']")
	private List<WebElement> customerfacetSearchList;
	
    /* Constructor */
	public CustomerFacet(WebDriver driver) {
        this.driver = driver;
    }
    
	/* Clicking on Customer Icon */
    public void clickCustomerIcon(){
		customerSearchIcon.click();
    }
    
    /* Entering the Customer name to Filter in the Customer Filter Box. */
    public void enterCustomerName(String custFilterName) throws InterruptedException{
    	customerSerachString = custFilterName;
		facetinputBox.sendKeys(custFilterName);
		Thread.sleep(3000);
    }
    
    /* Close Customer facer drop down */
    public void closeCustomerFacet() throws InterruptedException{
    	custfilterdropdown.click();
	    Thread.sleep(5000);
    }
    
    /* Removing Customers from Facet */
    public void removeCustomersfromFacet() throws InterruptedException{
   		removeselectedFacets.click();
   		Thread.sleep(2000);
    } 
    
	/* Verify Search items displayed in facet has Customer name */
    public boolean verifyCustomerFacetSearchResult(){
    	if(customerfacetSearchList.size()==0) return false;
    	for(int i=0;i<customerfacetSearchList.size();i++){
    		if ( !customerfacetSearchList.get(i).getAttribute("title").toLowerCase().contains(customerSerachString.toLowerCase().trim())){	
    		return false;
    		}
    	}	
    	return true;
    } 
    
    /* To get total count from the items displayed in facet search including multiple rows */
    public int getRecordcountfromCustomerFacetSearch() {
    	int reccount = 0; // Variable to read facet count
    	if (customerfacetSearchList.size()==0) return 0;
    	for(int i=0; i<customerfacetSearchList.size();i++){
    		int mycount = CommonUtilLibrary.readcountfromfacetline(customerfacetSearchList.get(i).getText());
    		reccount = reccount+mycount ;
    	}
	   	return reccount;   	
    }
    
    /* To add all Customers displayed in facet search to Main search option */
    public boolean addCustomerstoSearch(){
    	try{	
    		 int saleschannellistsize = customerfacetSearchList.size(); // Variable to read selected facet search List
    		 if (saleschannellistsize==0) return false;
    		 while(saleschannellistsize > 0){
	    		customerfacetSearchList.get(0).click();
	    		Thread.sleep(1000);
	    		if (saleschannellistsize > 1){
	    			saleschannellistsize = customerfacetSearchList.size();    				
	    		} else {
	    			break;
	    		}
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }    
}