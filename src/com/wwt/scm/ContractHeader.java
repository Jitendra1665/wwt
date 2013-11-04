/*****************************************************
 * Class Name: ContractHeader
 * Class Purpose: Page object for Contract Header
 * Created by:Jitendra/Srinivas
 *****************************************************/
package com.wwt.scm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import static org.testng.Assert.assertEquals;
//import static org.testng.Assert.assertNotEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ContractHeader {
	
	protected final WebDriver driver; // driver variable
	
	/* WebElements for Contract Header */
	@FindBy(css="div[id='result-navigation'] div[class='span4 left-button-group'] div[class*='btn-group pull-left'] button")
	private List <WebElement> buttonsList;
	
	@FindBy(css="div#result-navigation.row-fluid div.span4 div.btn-group button[ng-click*='ContractResults']")
	private WebElement clickOnContracts;
	
	@FindBy(css="div#bigContainer div#linesTable.ng-scope div.pull-left div.lines-cell span[ng-repeat*='customer']")
	private List <WebElement> contractsPageCustNameList;
	
	@FindBy(css="div#linesHeader div.result-headers div.ng-binding")
	private List <WebElement> contractHeaderColumnsList;
	
	@FindBy(css="div[id='contract-table-container'] table[id='contract-table'] tr[class='contract-grid-result-row ng-scope'] td[class='header-service-level'] span")
	private List <WebElement> serviceLevelList;
	
	/* Constructor */
    public ContractHeader(WebDriver driver) {
        this.driver = driver;
    }
    
    /* 1. Clicking on Contracts Button. */
    public void clickOnButton(String buttonType) throws InterruptedException{
    	for(int i = 0; i<buttonsList.size();i++){
       		if((buttonsList.get(i).getText().contains(buttonType))){
    			buttonsList.get(i).click();
    			break;
    		}
    	}
    	Thread.sleep(5000);
    }
    
    /* 2. Verify if we are on correct page (contracts page) when clicked on the Contracts link */ 
    public boolean verifyContractsButtonEnabled(){
    	boolean verifyContractsEnabled = false; // Variable to check contracts button enabled
    	for(int i = 0; i<buttonsList.size();i++){
       		if((buttonsList.get(i).isEnabled())){
       			verifyContractsEnabled  = true;
    		}
    	}
    	return verifyContractsEnabled;
    }
    	
    /* 3. Get the Contract Column Header Column Names List */
    public List <String> getContractsTabColumnHeaderList() throws InterruptedException{
       	List <String> coulmnsHeaderList = new ArrayList <String> (); // Variable to read headers list
       	for(int i = 1; i<contractHeaderColumnsList.size(); i++){
    		coulmnsHeaderList.add(contractHeaderColumnsList.get(i).getText());
    	}	
    	return coulmnsHeaderList;
    }
      
    /* 4. getTheFirstCustName From Contracts Page. */
    public String getFirstCustNameInContractsPg() throws IOException, InterruptedException{
    	Thread.sleep(3000);
    	return contractsPageCustNameList.get(0).getText().trim();
    }
        
    /* 5. Get the Contract Column Header Column Names List */
    public int getGivenColNumFromList(String colName) throws InterruptedException{
    	contractHeaderColumnsList.size();
    	int colNum = 0;
       	for(int i = 0; i<contractHeaderColumnsList.size(); i++){
    		if(contractHeaderColumnsList.get(i).getText().contains(colName)){
    			colNum = i+2;
    			break;
    		}
    	}	
    	return colNum;
    }
    
    /* 6. Get the Contract Column Header Column Names List */
    public boolean verifyGivenContractsTabColumnHeaderList(String headerList) throws InterruptedException{
    	boolean headerFindFlag = false;
    	contractHeaderColumnsList.size();
       	for(int i = 1; i<contractHeaderColumnsList.size(); i++){
    		if(headerList.toLowerCase().trim().contains(contractHeaderColumnsList.get(i).getText().trim().toLowerCase())){
    			headerFindFlag = true;
    		}
    	}	
    	return headerFindFlag;
    }
      
    /* 7. Get the Values List for the given Column Name */
    public List <String> getGivenColumnValuesList(String colName) throws InterruptedException{
    	List<WebElement> colValues = driver.findElements(By.xpath("//*[@id='linesTable']/div["+getGivenColNumFromList(colName)+"]/div"));
    	List<String> contractsTabColValuesList = new ArrayList<String>();// Array List to store Column values list
    	for(int i = 0; i<colValues.size(); i++){
    		contractsTabColValuesList.add(colValues.get(i).getText());
    	}
		return contractsTabColValuesList;
    }
    
    /* 8. Get the Values List for the given Column Name */
    public boolean verifyColumnData(String colName, String colData) throws InterruptedException{
    	boolean columnDataMatchFlag = true; // Variable to column match flag
    	List<WebElement> colValues = driver.findElements(By.xpath("//*[@id='linesTable']/div["+getGivenColNumFromList(colName)+"]/div"));
    	for(int i = 0; i<colValues.size(); i++){
    		if(colData.toLowerCase().trim().contains(colValues.get(i).getText().toLowerCase().trim())){
    			columnDataMatchFlag = true;
    		}
    	}
		return columnDataMatchFlag;
    }
    
    /* 8. Sorting the required Column */
    public void sortRequiredColumn(String colHeaderName, String ascOrDesc) throws InterruptedException{
    	Actions builder = new Actions(driver);
    	List<String> contractPageHeaderNames = getContractsTabColumnHeaderList();
    	for(int i = 0; i<contractPageHeaderNames.size(); i++){    	
    		if( (contractPageHeaderNames.get(i).contains(colHeaderName)) && (ascOrDesc.equalsIgnoreCase("asc"))){
    			builder.clickAndHold(contractHeaderColumnsList.get(i)).click().perform();
    		}
    		else if( (contractPageHeaderNames.get(i).contains(colHeaderName)) && (ascOrDesc.equalsIgnoreCase("desc"))){
    			builder.clickAndHold(contractHeaderColumnsList.get(i)).click().perform();
    			Thread.sleep(6000);
    			builder.clickAndHold(contractHeaderColumnsList.get(i)).click().perform();
    		}
    	}
    	Thread.sleep(5000);
    }    
}