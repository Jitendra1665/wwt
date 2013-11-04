/*****************************************************
 * Class Name: SalesChannelFacet
 * Class Purpose: Sales Channel Page Objects
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class SalesChannelFacet {
	
	String searchSalesChannelName=null; // variable to store searched sales channel name
	protected final WebDriver driver; // driver variable
	
	/* WebElements for Sales Channel page*/
	@FindBy(css="a[class='cl_wwt_sales_channel']")
	private WebElement salesChannelIcon;

	@FindBy(css="div#cl_wwt_sales_channel.modal div.modal-footer div.input-append input.input-medium")
	private WebElement SalesChannelNameInputBox;

	@FindBy(css="div#cl_wwt_sales_channel.modal div.modal-header button.close")
	private WebElement closesalesChannelFilter;

	@FindBy(css="div#cl_wwt_sales_channel-data.facet-popover div.row-fluid div.span12 div.span12 div.span2 button#remove-selected-facets.btn")
	private WebElement removeselectedfacet;

	@FindBy(css="div#cl_wwt_sales_channel.modal div.modal-body div.pull-left ul.nav li.ng-scope a[ng-click*='selectFacet']")
	private List<WebElement> saleschannelfacetsearchlist;

	/* Constructor */
    public SalesChannelFacet(WebDriver driver) {
        this.driver = driver;
    }
    
    /* Clicking on Sales Channel Icon */
    public void clicksalesChannelIcon(){		   	
	    salesChannelIcon.click();
    }
    
    /* Entering the Sales Channel Customer name to Filter in the Sales Channel Filter Box. */
    public void enterSalesChannelName(String salesChannelFilterName) throws InterruptedException{
	    searchSalesChannelName = salesChannelFilterName;
	    SalesChannelNameInputBox.click();
		SalesChannelNameInputBox.sendKeys(salesChannelFilterName);
		Thread.sleep(3000);
    }
    
    /* Close the Sales Channel Filter List */
    public void closeSalesChannelFacet() throws InterruptedException{   	
		closesalesChannelFilter.click(); 
		Thread.sleep(5000);
    }
    
    /* Remove Channel from Facet */
	public void removeChennelfromFacet() throws InterruptedException{
		removeselectedfacet.click();
		Thread.sleep(2000);
	}
	
	/* Verify Search items displayed in facet has Sales channel name */
    public boolean verifyFacetSearchResult(){
    	if(saleschannelfacetsearchlist.size()==0) return false;
    	for(int i=0;i<saleschannelfacetsearchlist.size();i++){
    		if ( !saleschannelfacetsearchlist.get(i).getText().toLowerCase().contains(searchSalesChannelName.toLowerCase())){
    			return false;
    		}
    	}
    	return true;
    } 
    
    /* To get total count from the items displayed in facet search including multiple rows */
    public int getRecordcountfromSalesChannelFacetSearch() {
    	int reccount = 0;
    	if(saleschannelfacetsearchlist.size()==0) return 0;
    	for(int i=0; i<saleschannelfacetsearchlist.size();i++){
    		int mycount = CommonUtilLibrary.readcountfromfacetline(saleschannelfacetsearchlist.get(i).getText());
    		reccount = reccount+mycount ;
    	}
	   	return reccount;   	
    }
    
    /* to add all sales channels displayed in facet search to Main search option */
    public boolean addsalesChanneltoSearch(){
    	try{	
    		int saleschannellistsize = saleschannelfacetsearchlist.size();
    		if (saleschannellistsize==0) return false;
	    	while(saleschannellistsize > 0){
	    		saleschannelfacetsearchlist.get(0).click();
	    		Thread.sleep(1000);
	    		if (saleschannellistsize > 1){
	    			saleschannellistsize = saleschannelfacetsearchlist.size();    				
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
    

   
    
    
   
   
   


   
   