/*****************************************************
 * Class Name: InstallSiteCustomerFacet
 * Class Purpose: Page objects for Install site Facet
 * Created by:Jitendra/Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class InstallSiteCustomerFacet {
	
		String installsiteSearchString=null;	// variable to read install site search string
		protected final WebDriver driver; // webdriver variable
		
		/* WebElements for Install site Facet */
		@FindBy(css="a[class='st_site_name']")
		private WebElement installSiteCustomerIcon;
		
		@FindBy(css="div#st_site_name.modal div.modal-footer div.input-append input.input-medium")
		private WebElement installSiteCustinputbox;
		
		@FindBy(css="div#st_site_name.modal div.modal-header button.close")
		private WebElement installfilterdropdown;

		@FindBy(css="div#st_site_name-data.facet-popover div.row-fluid div.span12 div.span12 div.span2 button#remove-selected-facets.btn")
		private  WebElement removeinsCustfacet;
		
		@FindBy(css = "div[id='st_site_name'] div[ng-show*='input.facetQueryInput.length > 2'] ul[class='nav nav-tabs nav-stacked'] a")
		private List<WebElement> installsiteCustFacetList;
		
		@FindBy(css="div#st_site_name-data.facet-popover div.row-fluid div.span12 div.facet-data-container div.search-facet-tab div.span12 ul#facet-return-list-item li a")
		private List<WebElement> siteFacetSearchNamesList;
		
		@FindBy(css="div#st_site_name button[title='Select All Search Facets']")
		private WebElement selectAll;
		
		@FindBy(css="div#st_site_name.modal div.modal-footer button[ng-click*='apply']")
		private  WebElement siteCustApplyButton;
		
		/* Constructor */
		public InstallSiteCustomerFacet(WebDriver driver) {
			this.driver = driver;
		}
	
		/* Clicking on Install Site Customer Icon */
		public void clickInstallSiteCustomerIcon() throws InterruptedException{
			Thread.sleep(5000);
			installSiteCustomerIcon.click();
			Thread.sleep(2000);
		}
		
		/* 2. Entering the Install Site Customer Customer name to Filter in the Install Site Customer Filter Box. */
		public void enterinstallsiteCustName(String installsiteName) throws InterruptedException{
			installsiteSearchString = installsiteName;
			installSiteCustinputbox.click();
			installSiteCustinputbox.sendKeys(installsiteName);
			Thread.sleep(6000);			
		}
		
	    /* Close the Install Site Customer Filter List */
		public void closeInstallSiteCustFacet() throws InterruptedException{
	    	installfilterdropdown.click(); 
	    	Thread.sleep(5000);
		}  
		
		/* remove install site customer from facet */
	    public void removeInsCustfromFacet() throws InterruptedException{
	    	removeinsCustfacet.click();
		    Thread.sleep(2000);	    		
	    }
	    
		/* Verify Search items displayed in facet has Customer name */
	    public boolean verifyInstallsiteFacetSearchResult(){
	    	if (installsiteCustFacetList.size()==0) return false;
	    	for(int i=0;i<installsiteCustFacetList.size();i++){
	    		if ( !installsiteCustFacetList.get(i).getAttribute("title").toLowerCase().startsWith(installsiteSearchString.toLowerCase().trim())){
	    			return false;
	    		}
	    	}
	    	return true;
	    } 
	    
	    /* To get total count from the items displayed in facet search including multiple rows */
	    public int getRecordcountfromInstallSiteFacetSearch() {
	    	int reccount = 0;
	    	if (installsiteCustFacetList.size()==0) return 0;
		    for(int i=0; i<installsiteCustFacetList.size();i++){
		    	int mycount = CommonUtilLibrary.readcountfromfacetline(installsiteCustFacetList.get(i).getText());
		    	reccount = reccount+mycount ;
		    }
		   	return reccount;   	
	    }
	    
	    /* To add all sales channels displayed in facet search to Main search option */
	    public boolean addInstallSiteCusttoSearch(){
	    	try{	
	    		int instalSitelistsize = installsiteCustFacetList.size();
	    		if (instalSitelistsize==0) return false;
		    	while(instalSitelistsize > 0){
		    		installsiteCustFacetList.get(0).click();
		    		Thread.sleep(1000);
		    		if (instalSitelistsize > 1){
		    			instalSitelistsize = installsiteCustFacetList.size();    				
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
	    
	    /* To Get All the site names */ 
	    public String getAllSiteNamesList(){
	    	String s = "";
	    	int counter = 0;
	    	int totalSiteFacetNameSize = siteFacetSearchNamesList.size();
	    	for(int i = 0; i<totalSiteFacetNameSize; i++){
	    		counter++;
	    		s = s+siteFacetSearchNamesList.get(i).getAttribute("title");
	    		if(counter<totalSiteFacetNameSize){
	    			s=s+", ";
	    		}	
	    	}
	    	return s;
	    }
	    
	    /* select All displayed site names */
		 public void selectAll(){
			 selectAll.click();
		 }
		  
		/* Clicking on Apply button in Site facet Search Window */
		public void clickApply() throws InterruptedException{
			Thread.sleep(3000);
			siteCustApplyButton.click();
		}
}