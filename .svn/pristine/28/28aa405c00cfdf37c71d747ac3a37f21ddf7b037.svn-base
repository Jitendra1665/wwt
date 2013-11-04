/*****************************************************
 * Class Name: AddressFacet
 * Class Purpose: Address Facet Page Object
 * Created by:Jitendra/Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class AddressFacet {
	
		String addressSearchString=null; //to read address search string from Facet
		protected final WebDriver driver; // webdriver variable
		
		/* WebElements for Address Facet Page Object */
		
		@FindBy(css="a[class='add_address_concat']")
		private WebElement addressIcon;
		
		@FindBy(css="div#add_address_concat.modal div.modal-footer div.input-append input.input-medium")
		private WebElement addressfacetserachbox;
		
		@FindBy(css="div#add_address_concat.modal div.modal-header button.close")
		private WebElement addressFacetdropdown;	

		@FindBy(css="div#add_address_concat-data.facet-popover div.row-fluid div.span12 div.span12 div.span2 button#remove-selected-facets.btn")
		private WebElement removeadressfromFacet;
		
		@FindBy(css="div#add_address_concat.modal div.modal-body div.pull-left ul.nav li.ng-scope a[ng-click*='select']")
		private List<WebElement> addressFacetSearchList;
		
		@FindBy(css="div[id='add_address_concat'] button[title='Select All Search Facets']")
		private WebElement selectAllSearchedAddressFacets;
		
		@FindBy(css="div[id='add_address_concat'] button[class='btn btn-success applyFacet']")
		private WebElement applyButton;

		/* Constructor */ 
	    public AddressFacet(WebDriver driver) {    	
	        this.driver = driver;
	    }
	    
	    /* Method to click on Address Icon */
	    public void clickAddressIcon() throws InterruptedException{
	    	 	addressIcon.click();
	    	 	Thread.sleep(1000);
	    }
	    
	    /*Method to enter the Address to Address Facet */ 
	    public void enterAddress(String addressString) throws InterruptedException {
	    		addressSearchString = addressString;
	    		addressfacetserachbox.click();
	    		addressfacetserachbox.sendKeys(addressString);
	    		Thread.sleep(5000);
	    }
	    
	    /*Method to close opened Address Facet */
	    public void closeAddressFacet() throws InterruptedException{ 
	    		addressFacetdropdown.click(); 
	    		Thread.sleep(6000);
	    }
	    
	    /* Method to Remove address from search facet */
	    public void removeAddressfromSearchFacet(){
	    	removeadressfromFacet.click();
	    }
	   
		/* Verify Search items displayed in facet has Customer name */
	    public boolean verifyAddressFacetSearchResult(){
	    	if (addressFacetSearchList.size()==0 ) return false;
		    	for(int i=0;i<addressFacetSearchList.size();i++){
		    		if ( !addressFacetSearchList.get(i).getAttribute("title").toLowerCase().contains(addressSearchString.toLowerCase().trim())){
		    			return false;
		    		}
		    	}
	    	return true;
	    } 
	    
	    /* Method to get total count from the items displayed in facet search including multiple rows */
	    public int getRecordcountfromAddressFacetSearch() {
	    	int reccount = 0; // variable for record count for address facet search List
	    	if (addressFacetSearchList.size()==0) return 0;
		    for(int i=0; i<addressFacetSearchList.size();i++){
		    	int mycount = CommonUtilLibrary.readcountfromfacetline(addressFacetSearchList.get(i).getText());
		    	reccount = reccount+mycount ;
		    }
		   	return reccount;   	
	    }
	    
	    /* Method to add all addresses displayed in facet search to Main search option */
	    public boolean addAddresstoSearch(){
	    	try{	
	    		 int saleschannellistsize = addressFacetSearchList.size();
	    		 if (saleschannellistsize==0) return false;
		    	while(saleschannellistsize > 0){
		    		addressFacetSearchList.get(0).click();
		    		Thread.sleep(1000);
		    		if (saleschannellistsize > 1){
		    			saleschannellistsize = addressFacetSearchList.size();    				
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
	    
	    /* Method to Select all searched address facet results */
	    public void selectAllSearchedAddressFacets(){
	    	selectAllSearchedAddressFacets.click();
	    }
	    
	    /* Method to Select all searched apply facet results */
	    public void clickApply(){
	    	applyButton.click();
	    } 
	    
 }