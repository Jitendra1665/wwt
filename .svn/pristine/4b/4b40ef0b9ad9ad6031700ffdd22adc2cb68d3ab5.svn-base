/*****************************************************
 * Class Name: SearchResults
 * Class Purpose: Search Results Page Objects
 * Created by:Jitendra/ Srinivas
 *****************************************************/

package com.wwt.scm;

import java.io.FileInputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResults {
	
	int givenHeaderColumnNumber=0; // Variable to capture Header Column number
	
	public final WebDriver driver; // driver variable
	
	/* WebEleemnts for Sales Results page objects */
	@FindBy(css="div#navbar div[class='nav-collapse collapse'] ul[class*='lines-dropdown'] a[class='dropdown-toggle ng-binding']")
	private WebElement defaultSearchResultTypeButton;
		
	@FindBy(css="div#navbar div[class='nav-collapse collapse'] ul[class*='lines-dropdown'] ul[class='dropdown-menu'] li a")
	private List<WebElement> searchResultTypeDropDownList; 
	
	@FindBy(css="div[id='resultPager'] ul[class='inline'] li button[class*='btn'] i[class='icon-caret-left']")
	private WebElement backButton;

	@FindBy(css="div[id='resultPager'] ul[class='inline'] li button[class*='btn'] i[class='icon-caret-right']")
	private WebElement nextButton;
			
	@FindBy(css="div[id='resultPager'] ul[class='inline'] li")
	private List <WebElement> footerList;
	
	@FindBy(css="div[id='linesHeaderContainer'][class='ng-scope'] div[id='linesHeader'] div[class*='result-headers'] div")
	private List <WebElement> headerList;
	
	@FindBy(css="div#content-main div#lineResults.ng-scope div[class*='result-headers']")
	private List <WebElement> headerNameList;
	
	@FindBy(css="div#result-navigation.row-fluid div.span4 div.btn-group button")
	private List<WebElement> buttons;

	@FindBy(css="div#bigContainer div#linesTable.ng-scope div.pull-left div.lines-cell a.ng-binding")
	private List <WebElement> ContractNumbersList;
	
	@FindBy(css="div#bigContainer div#linesTable.ng-scope div.pull-left div.lines-cell span.ng-scope")
	private List <WebElement> contractPageCustName;
			
	@FindBy(css="div#result-navigation.row-fluid div.span4 div.btn-group button")
	private List<WebElement> topButtonsList;
		
	@FindBy(css="div#linesTable.ng-scope div.pull-left.lines-column")
	private List<WebElement> linesTableList;
		
	@FindBy(css="div#navbar.navbar a[ng-show*='isNavActive']")
	private List<WebElement> ActionDropDownItem;
				
	/* Constructor */
	public SearchResults(WebDriver driver) {
        this.driver = driver;
    }
    
	/* 1. Search Result Column Headers list */
    public List <String> getColumnHeaders(){
    	int columnsCount = headerList.size();
    	List <String> columnNamesList = new ArrayList <String>();
    	for(int i = 0; i<columnsCount; i++){
    		columnNamesList.add(headerList.get(i).getText());
    	}	
    	return columnNamesList;
    }    
    
    /* 2. verify the Search Results type (line or contacts) Display available on the page */
    public String getSearchResultType_LineOrContact(){
    	return defaultSearchResultTypeButton.getText().trim();
    }

    /* 3. verify the Search Results type (line or contacts) Display available on the page */
	public boolean verifySearchResultType_LineOrContract(String resultType){
		boolean resultTypeFlag = false;
		if(resultType.trim().toLowerCase().contains(defaultSearchResultTypeButton.getText().trim().toLowerCase())){
			resultTypeFlag = true;
		}
		return resultTypeFlag;
	}
    
	/* 4. verify the Search Results type (line or contacts) Display available on the page */
	public boolean verifySearchResultType_Contract(String resultType){
		boolean resultTypeFlag = false;
		if(defaultSearchResultTypeButton.getText().trim().toLowerCase().contains(resultType.trim().toLowerCase()));{
			resultTypeFlag = true;
		}
		return resultTypeFlag;
	}   
    
	/* 5. To verify whether the given column number is available or not. */
	public boolean verifyColumnHeaderAvailability(String colName){
		boolean colAvailableFlag = false;
		List <String> columnNamesList = getColumnHeaders();
		for(int i = 0; i<columnNamesList.size(); i++){
			if(colName.trim().toLowerCase().contains(columnNamesList.get(i).trim().toLowerCase())){
				if(!columnNamesList.get(i).trim().toLowerCase().equals("")){
					colAvailableFlag = true;
				}
			}
		}
		return colAvailableFlag;
	}
    /* 6. Get the Column Number based on the given Column Name */
    public void getColumnNumber(String ColName){
    	if(headerList.size()==linesTableList.size()){
    		List<String> ColHeaderList = getColumnHeaders();
    		    		
    		for(int i=0;i< ColHeaderList.size(); i++){
    			if( ColName.toLowerCase().trim().equals(ColHeaderList.get(i).toLowerCase().trim())){
    				givenHeaderColumnNumber= i+1;
    				break;
    			}
    		}	
    	}else{
    		System.out.println(" Header Column count and displayed columns counts are not mathing");
    	}
    }
    
    /* 7. Validating data inside the particular given column */
    public boolean validateDataInColumn(String colData){
    	boolean dataFound = false;
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; // dynamic xpath for data value
    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));
    	for(int i=0;i<columnValuesList.size();i++){
    		if(columnValuesList.get(i).getText().trim().toLowerCase().contains(colData.trim().toLowerCase())){
    			dataFound=true;
    		}else{
    			dataFound = false;
    			break;
    		}
    	}
    	return dataFound;
    }
        
    /* 8. Validating data inside the particular given column - useful to check if we give multiple search criteria*/ 
    public boolean validateColumnData(String colDataValues){
    	boolean dataFound = false;
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; // dynamic xpath for data 
     	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));
    	for(int i=0;i<columnValuesList.size();i++){
    		if(colDataValues.toLowerCase().trim().contains(columnValuesList.get(i).getText().toLowerCase().trim())){
    			if(! columnValuesList.get(i).getText().trim().equals("")){
    				dataFound=true;
    			}
    		} else{
    			dataFound = false;
    			break;
    		} 
    	}
    	return dataFound;
    }
    
    /* 9. get the Total Results displayed after validating the Data inside the column is displaying correct */
  	public int getResultcountAfterColumnDataCheck(String ColName, String ColData) throws InterruptedException{
  		int totalRecordsCount = 0;
  		Thread.sleep(5000);
  		int totalPageCount = getPagesCount();
    	getColumnNumber(ColName);
		for(int i=1; i<=totalPageCount; i++){
			if(validateDataInColumn(ColData)){
				totalRecordsCount = totalRecordsCount+ContractNumbersList.size();
				if(i==totalPageCount){
					break;
				}else{
					navigateNextPage();
					Thread.sleep(5000);
				}
			} 			
		}
  		return totalRecordsCount;
  	}
    
  	/* 7. Validating the Date Range displayed for the given column is in between the given date range. */
	public boolean validateDateRange(String colName, String fromDate, String toDate) throws InterruptedException, ParseException{
  		int totalPageCount = getPagesCount();
    	Date tempFromDate, tempTodate,tempdisplayDate;
    	getColumnNumber(colName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; // dynamic xpath for column
		SimpleDateFormat inputformat = new SimpleDateFormat("yyyy/M/d");
		SimpleDateFormat displayformat = new SimpleDateFormat("MMM d,yyyy");
		tempFromDate = inputformat.parse(fromDate);
		tempTodate = inputformat.parse(toDate);
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					if  (j.getText().equalsIgnoreCase("") ) return false;
					tempdisplayDate = displayformat.parse(j.getText().trim());
					if (!(tempdisplayDate.compareTo(tempTodate) <= 0) && !(tempdisplayDate.compareTo(tempFromDate) >= 0)){
						return false;
					}
				}
				if (i==totalPageCount) break;
				navigateNextPage();
				Thread.sleep(5000);
		}    	
  		return true;
  	}
  	
  	/* 8. This method returns true if data found in any column cell, returns false if data not found in any column cell */
  	public boolean verifyDataInColumn(String colName, String data) throws InterruptedException{
  		int totalPageCount = getPagesCount();
  		int foundcount = 0;
  		getColumnNumber(colName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div";
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					if( j.getText().toLowerCase().contains(data.toLowerCase())){
						foundcount = foundcount+1;
						break;
					}
				}
				if ((i==totalPageCount) || (foundcount>0))break;
				navigateNextPage();
				Thread.sleep(5000);
		}    	
  		if (foundcount==0){
  			return false;
  		} else{
  			return true;	
  		}
  	} 
  	
    /* 9. clicking on Contracts button */
    public void selectSearchResultType(String resultType) throws InterruptedException{
    	defaultSearchResultTypeButton.click();
    	for(int i=0; i<searchResultTypeDropDownList.size(); i++){
    		if(searchResultTypeDropDownList.get(i).getText().toLowerCase().trim().contains(resultType.toLowerCase().trim())){
    			searchResultTypeDropDownList.get(i).click();
    			Thread.sleep(2000);
    			break;
    		}
    	}
    }
    
    /* 10. method to check whether count of Review columns greater than 0 */
    public boolean verifyRecodcountInContractTab(String colName) throws InterruptedException{
  		int totalPageCount = getPagesCount();
  		getColumnNumber(colName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; // dynamic xpath for column header
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					String coldatavalue = j.getText().trim();
					if (coldatavalue.equals("")) coldatavalue="0";
					int colvalue = Integer.parseInt(coldatavalue);
					if(colvalue < 0 ){
						return false;
					}
				}
				if (i==totalPageCount) break;
				navigateNextPage();
				Thread.sleep(5000);
		}  
    	return true;
    }
    
    /* 11. method to verify date exists in end of support field in contracts tab */
    public boolean verifyEndOfSupportDateinContractTab(String colName) throws InterruptedException, ParseException{
  		int totalPageCount = getPagesCount();
  		getColumnNumber(colName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div";
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					String coldatavalue = j.getText().trim();
					if (coldatavalue.equals("")) return false;
				}
				if (i==totalPageCount) break;
				navigateNextPage();
				Thread.sleep(5000);
		}  
    	return true;
    }  	
  	
    /* 12. Validating Data in all the pages. */
    public boolean validateColDataInAllPages(String colName, String ColData) throws InterruptedException{
    	boolean dataFound = true;
  		Thread.sleep(5000);
  		int totalPageCount = getPagesCount();
    	getColumnNumber(colName);
    	if (givenHeaderColumnNumber==0){
    		return dataFound;
    	}
		for(int i=1; i<=totalPageCount; i++){
			if(validateColumnData(ColData)){
				if(i<totalPageCount){
					navigateNextPage();
					Thread.sleep(5000);
				}
			}		
		}
    	return dataFound;
    }
    
    /* 13 Validating Data in all the pages for Partial value Search. - like facet Search Criteria. */
    public boolean validateFacetSearchDataWithColDataInAllPages(String colName, String ColData) throws InterruptedException{
    	boolean dataFound = false;
  		Thread.sleep(5000);
  		int totalPageCount = getPagesCount();
    	getColumnNumber(colName);
    	if (givenHeaderColumnNumber==0){
    		System.out.println("given column not found.");
    		return dataFound;
    	}
		for(int i=1; i<=totalPageCount; i++){
			if(validateDataInColumn(ColData)){
				dataFound = true;
				if(i<totalPageCount){
					navigateNextPage();
					Thread.sleep(5000);
				}else{
					break;
				}
			}			
		}
    	return dataFound;
    }
       
    /* 14. Getting the Number of results in the page. */
    public int getResultCount(){
    	return linesTableList.size();
    } 
	   
  	/* 15.Get Total Search Result Rows displayed */
  	public int totalSearchResultsCount() throws InterruptedException{
  		int totalRecordsCount = 0;
  		int totalPageCount = getPagesCount();
		for(int i=1; i<=totalPageCount; i++){
			totalRecordsCount = totalRecordsCount+ContractNumbersList.size();
			if(i==totalPageCount){
				break;
			}else{
				navigateNextPage();
				Thread.sleep(5000);
			}
		}
  		return totalRecordsCount;
  	}
  	    
    /* 16. Clicking on the first Contract Number */
    public String getAndClickfirstContractNumber() throws InterruptedException{
    	String firstContractNumber = ContractNumbersList.get(0).getText().trim();
    	ContractNumbersList.get(0).click();
    	Thread.sleep(5000); 
    	return firstContractNumber;
    }
    
    /* 17. Getting the Actual Contract Number that displayed on the results page when clicked on particular Contract Number */
    public String getActualContactNumber(){
    	return defaultSearchResultTypeButton.getText();  
    }
    
    /* 18. Get the Actual Customer Name that is displayed on the page. */
    public String getFirstCustNameInLinesPg(){
    	return contractPageCustName.get(0).getText();
    }
    
 	/* 19. Returning the TotalPageCount as integer (Converted String number to integer) */
  	public int getPagesCount(){	
  		String tPageCount="0";
		int pcount=0;
  		try{
	  		for(int i = 0; i<footerList.size(); i++){
				String requiredField = footerList.get(i).getText().trim();
				if(requiredField.contains("Total Pages")){
					String[] TotalPageCount = requiredField.split(":");
					tPageCount = TotalPageCount[1].trim();
					tPageCount = tPageCount.replace(",","");
					break;
	 			}
			}
			FileInputStream ConfigFIS = new FileInputStream(".\\scm.properties");
			Properties scriptproperties = new Properties();
			scriptproperties.load(ConfigFIS);
			String PropPagecount = scriptproperties.getProperty("RESULTS_NAVIGATION_PAGES");  
			scriptproperties = null;
			ConfigFIS.close();
			if((PropPagecount.equalsIgnoreCase("All"))){
				pcount =  new Integer(tPageCount);
			} else {
				pcount =  new Integer(tPageCount);
				int pPagecount = new Integer(PropPagecount);
				if(pcount>pPagecount) pcount=pPagecount;
			}
  		}catch(Exception e){
  			e.printStackTrace();
  		}
		return pcount;
  	}
  		
  	/* 20. Returning the Current PageNumber as integer (Converted String number to integer) */
  	public int getCurrentPageNumber(){
  		String currPageNum = null;
  		for(int i = 0; i<footerList.size(); i++){
				String requiredField = footerList.get(i).getText().trim();
				if(requiredField.contains("Page Number:")){
					String[] currentPageNumber = requiredField.split(":");
					currPageNum = currentPageNumber[1].trim();
					currPageNum = currPageNum.replace(",","");
					break;
				}
			}	
  		return new Integer(currPageNum);
  	}	
   		
  	/* 21. Navigate to Next Page directly */
  	public void navigateNextPage() throws InterruptedException{
  				nextButton.click();
  				waitforalertcomplete();
				//Thread.sleep(5000);
  	} 
  	
  	/* 22. Navigate to Previous Page directly */
  	public void navigatePreviousPage() throws InterruptedException{
  				backButton.click();
  				waitforalertcomplete();
  				//Thread.sleep(5000);
  	}
	
    /* 23. Sorting the required Column */
    public void sortRequiredColumn(String s, String ascOrDesc) throws InterruptedException{
    	Actions builder = new Actions(driver);
    	List<String> headerNames = getColumnHeaders();
    	for(int i = 0; i<headerNames.size(); i++){    	
    		if( (headerNames.get(i).toLowerCase().trim().contains(s.toLowerCase().trim())) && (ascOrDesc.equalsIgnoreCase("asc"))){
    			builder.moveToElement(headerList.get(i)).perform();
    			builder.click(headerList.get(i)).perform();
    			Thread.sleep(5000);
    		}
    		else if( (headerNames.get(i).toLowerCase().trim().contains(s.toLowerCase().trim())) && (ascOrDesc.equalsIgnoreCase("desc"))){
    			builder.moveToElement(headerList.get(i)).perform();
    			builder.click(headerList.get(i)).perform();
    			Thread.sleep(5000);
    			builder.moveToElement(headerList.get(i)).perform();
    			builder.click(headerList.get(i)).perform(); 
    			Thread.sleep(5000);
    		}
    	}
    	Thread.sleep(5000);
    }
    
    /* 24. To Get the values from the frozen column */
  	public List <String> GetColumnValues(String colName){
  		getColumnNumber(colName);
      	String DataRequiredForColXpath= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; //dynamic xpath for line header column
  		List <String> ColValues = new ArrayList<String>();
  		List <WebElement> columnTableValues = driver.findElements(By.xpath(DataRequiredForColXpath));
  		for(int i=0;i<columnTableValues.size(); i++){
  			if(! columnTableValues.get(i).getText().equals("")){
  				ColValues.add(columnTableValues.get(i).getText());
  			}
  		}
  		return ColValues;
  	}
    
    
  	/* 25. String format other than date - Check the given Columns List is in ascending Order. */
    public boolean columnAscStringSortCompare(List <String> colVals){
    	boolean ascSortCompareTypeFlag = false;
     	for(int i = 0; i< colVals.size()-1; i++){
    		for (int j=i+1 ; j<colVals.size(); j++){
    			if(colVals.get(i).trim().compareTo(colVals.get(j).trim())<=0){
    				ascSortCompareTypeFlag = true;
    			}else{
    				ascSortCompareTypeFlag = false;
    				break;
    			}
    		}
    		if(!ascSortCompareTypeFlag){
       			break;
    		}
		}
    	return ascSortCompareTypeFlag;
    }
    
    /* 26. String format other than date - Check the given Columns List is in descending Order. */
    public boolean columnDescStringSortCompare(List <String> colVals){
    	boolean descSortCompareTypeFlag = false;
       	for(int i = 0; i< colVals.size()-1; i++){
    		for (int j=i+1 ; j<colVals.size(); j++){
    			if(colVals.get(i).trim().compareTo(colVals.get(j).trim())>=0){
    		  				descSortCompareTypeFlag = true;
    			}else{
    				descSortCompareTypeFlag = false;
    				break;
    			}
    		}
    		if(!descSortCompareTypeFlag){
    			break;
    		}
       	}
       	return descSortCompareTypeFlag;
    }    
	   
    /* 27. Date format to Check the given Columns List is in ascending Order.*/
    public boolean columnAscDateSortCompare(List <String> colVals) throws ParseException{
    	DateFormat  DEFAULT_FORMATTER = new SimpleDateFormat("MMM d, yyyy");
    	boolean ascSortCompareTypeFlag = false;
       	for(int i = 0; i< colVals.size()-1; i++){
    			for (int j=i+1 ; j<colVals.size(); j++){
    				Date date1 = DEFAULT_FORMATTER.parse(colVals.get(i).trim());
    				Date date2 = DEFAULT_FORMATTER.parse(colVals.get(j).trim());
    				if(date1.compareTo(date2)<=0){
    					ascSortCompareTypeFlag = true;
    				}else{
    					ascSortCompareTypeFlag = false;
    					break;
    				}
       		}if(!ascSortCompareTypeFlag){
       			break;
       		}
		}
    	return ascSortCompareTypeFlag;
    }
    
    /* 28. Date format to Check the given Columns List is in descending Order. */
    public boolean columnDescDateSortCompare(List <String> colVals) throws ParseException{
    	DateFormat  DEFAULT_FORMATTER = new SimpleDateFormat("MMM d, yyyy");
    	boolean descSortCompareTypeFlag = false;
    	for(int i = 0; i< colVals.size()-1; i++){
    		for (int j=i+1 ; j<colVals.size(); j++){
    			Date date1 = DEFAULT_FORMATTER.parse(colVals.get(i).trim());
    	        Date date2 = DEFAULT_FORMATTER.parse(colVals.get(j).trim());
    			if(date1.compareTo(date2)>=0){
    				descSortCompareTypeFlag = true;
    			}else{
    				descSortCompareTypeFlag = false;
    				break;
    			}
    		}if(!descSortCompareTypeFlag){
    			break;
    		}
    	}
    	return descSortCompareTypeFlag;
    }    
    
    /* 29. Wait Method until Complete Alert appears */
    public boolean waitforalertcomplete() throws InterruptedException{
    	boolean mywait = (new WebDriverWait(driver, 30000))
    				.until(ExpectedConditions.textToBePresentInElement(By.cssSelector("div[id='mainNotificationPullDown'][pop-down='true']"), "Complete!"));
    	Thread.sleep(3000);
    	return mywait;
    }
       
	/* 30. Validate Word wrapping */
	public boolean validateWordWrapping(String columnName) throws InterruptedException{
  		int totalPageCount = getPagesCount();
  		getColumnNumber(columnName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; //dynamic xpath for header column
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					String displaytext = j.getText();
					String displaytilte= j.getAttribute("Title");
							if(displaytext.contains("...")){
								String dtext = displaytext.replace(".", "");
								if (! displaytilte.contains(dtext)){
									return false;						
								}
							}
				}
				if (i==totalPageCount)break;
				navigateNextPage();
				Thread.sleep(5000);
		}    	
		return true;
	}	

	/* 31. method to verify date exists in end of support field in contracts tab */
    public List<String> getDataForGivenColName(String colName) throws InterruptedException, ParseException{
  		int totalPageCount = getPagesCount();
  		List<String> givenColData = new ArrayList <String>();
  		getColumnNumber(colName);
    	String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; //dynamic xpath for column header
		for(int i=1; i<=totalPageCount; i++){
	    	List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));			
				for(WebElement j :columnValuesList ){
					givenColData.add(j.getText().trim());
				}
		}
		return givenColData;
    }
    
    /* 32. Validating data inside the particular given column - userful to check if we give multiple search criteria. */
    public boolean ExternalUser_validateColumnData(String ColName, String val) throws InterruptedException{
    	boolean dataFound = false;
    	int totalPageCount = getPagesCount();
    	getColumnNumber(ColName);
		String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div";
		int c = 0;
    		for(int i=1; i<=totalPageCount; i++){
    			if(! dataFound && i<totalPageCount){
    				List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));
    				for(int j=0;j<columnValuesList.size();j++){
    					if(columnValuesList.get(i).getAttribute("title").equals(val)){
    							dataFound=true;
    							c++;
    							break;
    					}
    				}
    				if(c==0){
        				navigateNextPage();
        				Thread.sleep(5000);
        			}else{ break; }
    			}
    		}
    		return dataFound;
    	}
    
    /* 32. Validating Data in all the pages.*/
    public boolean ExternalUser_validateColDataInAllPages(String colName, String colData) throws InterruptedException{
    	boolean dataFound = true;
  		Thread.sleep(5000);
  		int totalPageCount = getPagesCount();
  		getColumnNumber(colName);
  		String desiredColCss= "//*[@id='linesTable']/div["+ givenHeaderColumnNumber +"]/div"; //dynamic xpath for header column
  		List<WebElement> columnValuesList = driver.findElements(By.xpath(desiredColCss));
  		for(int i=1; i<=totalPageCount; i++){
 			for(int j=0;j<columnValuesList.size();j++){
  	    		if(columnValuesList.get(i).getAttribute("title").equals(colData) ){
  	    			dataFound=true;
  	    			break;
  	    		}else{
  	    			dataFound = false;
  	    		}
  	    	}if((! dataFound) && (i<totalPageCount)){
 	    			navigateNextPage();
					Thread.sleep(5000);
  	    	}else if(dataFound){
  	    		break;
  	    	}
    	}
  		return dataFound;
    }

    /* 33. Verify Whether the given button name is available or not. */
    public boolean verifyActionDropDown(String buttonName) throws InterruptedException{
    	int buttonsCount = ActionDropDownItem.size(); 
    	for(int i = 0; i<buttonsCount; i++){
        		if(buttonName.toLowerCase().trim().equalsIgnoreCase(ActionDropDownItem.get(i).getText().trim().toLowerCase())){
    			if(! ActionDropDownItem.get(i).isDisplayed()){
    				return false;
    			}
    		}
    	}
		return true;
    }
}