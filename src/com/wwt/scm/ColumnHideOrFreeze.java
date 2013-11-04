/*****************************************************
 * Class Name: ColumnHideOrFreeze
 * Class Purpose: Page object for Column Hide/Freeze
 * Created by:Jitendra/Srinivas
 *****************************************************/

package com.wwt.scm;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ColumnHideOrFreeze {
	
	protected final WebDriver driver; // driver variable 
	
	/* WebElements for Page Object */
	@FindBy(css="div#resultPager ul.inline li button[class*='btn btn-info btn-mini']")
	private WebElement hideOrFreezeIcon;
	
	@FindBy(css="div[id='colconfigTable'][class='row-fluid'] table tr[class='ng-scope'] td[class='ng-binding']")
	private List <WebElement> allColumnHeadingsList;
	
	@FindBy(css="div[id='configModal'][class='modal'] div[class='row-fluid'] div[class*='well'] button[id='unHideAll'][class*='btn']")
	private WebElement showAllButtonIcon;
	
	@FindBy(css="div[id='configModal'] div[class='row-fluid'] div[class*='well'] button[id='unfreezeAll'][class*='btn']")
	private WebElement unFreezeAllButtonIcon;
	
	@FindBy(css="div[id='configModal'] div[class='row-fluid'] div[class*='well'] button[id='apply'][class*='btn']")
	private WebElement applyBtn;
	
	@FindBy(css="div[id='configModal'] i.icon-remove")
	private WebElement closeHideAndFreezeDropDownList;
		
	@FindBy(css="div#frozenLinesHeader div div")
	private List <WebElement> frozenColumnHeaderList;

	@FindBy(css="div#colconfigTable.row-fluid table.table tbody tr.ng-scope td input[ng-model='column.isHidden']")
	private List <WebElement> verifyUnHideChkBox;
	
	@FindBy(css="div#colconfigTable.row-fluid table.table tbody tr.ng-scope td input[ng-model='column.isFrozen'][checked*='check']")
	private List <WebElement> verifyUnFreezeChkBox;
	
	@FindBy(css="button#unHideAll.btn")
	private WebElement unHideAllButton;
	
	@FindBy(css="button#unfreezeAll.btn")
	private WebElement unfreezeAllButton;

	/* Constructor */
	public ColumnHideOrFreeze(WebDriver driver) {
        this.driver = driver;
    }
	
	/* 1. Method to Click on Config Icon */
	public void clickOnColumnHideOrFreezeIcon() throws InterruptedException{		 	
		 	hideOrFreezeIcon.click();
		 	Thread.sleep(5000); 
	}
	 
	 /* 2. Returns all the Columns that are available to hide or to freeze */
	public List <String> getColumnsInHideIcon(){
		List <String> columnHeaderNamesInHideList = new ArrayList <String> (); // ArrayList to store and return Column Headers List
		for(int i = 0; i < allColumnHeadingsList.size(); i++ ){
			columnHeaderNamesInHideList.add(allColumnHeadingsList.get(i).getText());
		}
		return columnHeaderNamesInHideList;
	}
	 
	 /*3. Returns the corresponding row number of the given column Header that needs to hide/verify/freeze */
	public int getrowfreezehide(String columnHeaderName){
		 boolean colname=false; // Variable to check column name found or not
		 List <String> columnHeaderNamesList = getColumnsInHideIcon();
		 int colRowNum = 0; // Variable to read row number of the freeze/hide column
		 for(int i=0; i<columnHeaderNamesList.size(); i++){
			 	 if(columnHeaderNamesList.get(i).contains(columnHeaderName)){
			 		colRowNum = i+2; //Adding 2 for item starting form 0 and 1 for ignoring the heading.
			 		colname = true; // if Column found colName becomes true
			 		return colRowNum;
				 }
		 }
		 if(!colname){
			 System.out.println("There is no column as per your input : ");
			 return 0;
		 }
		 return colRowNum;
	}
	 
	/* 4. Hide the column Header */
	public void hideOrUnHide(String columnName) throws InterruptedException{
		int nthRow = getrowfreezehide(columnName); // get row number if column exist
		if ( nthRow > 0) { 
			 String hideChkBoxXpath = "//tr[" + nthRow + "]//input[@ng-model='column.isHidden']"; //dynamic Xpath for column
			 WebElement	 hideCheckBox = driver.findElement(By.xpath(hideChkBoxXpath));
			 hideCheckBox = driver.findElement(By.xpath(hideChkBoxXpath));
			 hideCheckBox.click();
		}			
	 }
	
	/* 5. Freeze the column Header	*/
	public void freezeOrUnFreeze(String columnName) throws InterruptedException{
		int nthRow = getrowfreezehide(columnName);// get row number if column exist
		if ( nthRow > 0) {
			 String freezeChkBoxXPath = "//tr[" + nthRow + "]//input[@ng-model='column.isFrozen']"; //dynamic Xpath for column
			 WebElement	 freezeCheckBox = driver.findElement(By.xpath(freezeChkBoxXPath));
			 freezeCheckBox.click();
		} 
	}	
	

	/* 6. verify whether given column Header is hidden or not */ 
	public boolean verifyHide(String columnName){
		boolean columnHideFlag=false; // Variable to check column hide or not
		for(int i  = 0; i<verifyUnHideChkBox.size(); i++){
			if(verifyUnHideChkBox.get(i).getAttribute("checked").equals("checked")){
				if(allColumnHeadingsList.get(i).equals(columnName)){
					columnHideFlag = true; // if column found
				}
			}	
		}
		return columnHideFlag;	
	}
	
	/* 7. find How Many column Header is hidden or not */
	public int getAllHideColCount(){
		int hideCounter=0; // Variable to get hide count
		for(int i = 0; i<verifyUnHideChkBox.size(); i++){
			if(verifyUnHideChkBox.get(i).isSelected()){  // Check if column selected or not
				hideCounter++;
			}
		}
		return hideCounter++;
	}
		 
	/*8. verify whether given column Header is hidden or not */
	public boolean verifyFreeze(String columnName){
		for(int i = 0; i<verifyUnFreezeChkBox.size(); i++){
			if(verifyUnFreezeChkBox.get(i).isSelected()){
				if(allColumnHeadingsList.get(i).equals(columnName)){
					return true;
				}
			}
		}
		return false;	
	}
		
	/* 9. find How Many column Headers are freezed or not */
	public int getAllFreezeColCount(){
		int hideCounter=0; // Variable to get freeze count
		for(int i = 0; i<verifyUnFreezeChkBox.size(); i++){
			if(verifyUnFreezeChkBox.get(i).isSelected()){
				hideCounter++;
			}
		}
		return hideCounter++;
	}
	
	/* 10. verify whether column Header is hidden or not */
	public boolean verifyHideCheckBox(String columnName){
		int nthRow = getrowfreezehide(columnName); // get row number if column exist
		String verifyHideIcon = "//tr[" + nthRow + "]//input[@ng-model='column.isHidden']"; //dynamic Xpath for column
		WebElement hideButton = driver.findElement(By.xpath(verifyHideIcon));
		if(hideButton.isEnabled()){
			return true;
		}
		return false;					 
	}
			 
	/*11. verifies whether column header is freeze or not. */
	public boolean verifyFreezeCheckBox(String columnName){
		int nthRow = getrowfreezehide(columnName);
		String verifyFreezIcon = "//tr[" + nthRow + "]//input[@ng-model='column.isFrozen']"; //dynamic Xpath for column
		WebElement freezButton = driver.findElement(By.xpath(verifyFreezIcon));
		if(freezButton.isEnabled()){
			return true;
		}	
		return false;	
	}
	
	/* 12. UnFreez All Button Click */
	public void unFreezeAll() throws InterruptedException{
		unFreezeAllButtonIcon.click();
	}
		 
	/* 13. Close the Customer Filter List */
	public void closeHideOrFreezDropDown() throws InterruptedException{
		closeHideAndFreezeDropDownList.click(); 
	}
			
	/* 15 Click on Apply Button. */
	public void clickApply() throws InterruptedException{
		applyBtn.click();
		Thread.sleep(5000);
	}
	
	/* 16. Validating the given columns are displayed as frozen in the table or not  */
	public boolean validatefrozenCols(String columnFreezed){
		List <String> freezedColumns = new ArrayList<String>(); // Variable to read column header
		for(int i = 0; i<frozenColumnHeaderList.size(); i++){
			freezedColumns.add(frozenColumnHeaderList.get(i).getText()); // Adding column header to ArrayList
		}		
		boolean flag=true;
		for(int i = 0; i<freezedColumns.size(); i++){
			if(columnFreezed.contains(freezedColumns.get(i))){
				flag=true;
			}else{
				flag = false;
				break;
			}
		}
		return flag;
	}
	
  	/* 17. Clicking on the given Freezed Column Heading */
  	public void sortFrozenCol(String s, String sortType) throws InterruptedException{
  			for(int i =0 ;i<frozenColumnHeaderList.size(); i++){
  			if(frozenColumnHeaderList.get(i).getText().contains(s)){
  				if(sortType.contains("asc")){
  					frozenColumnHeaderList.get(i).click();
  					Thread.sleep(5000);
  					break;
  				} else if(sortType.contains("desc")){
  					frozenColumnHeaderList.get(i).click();
  					Thread.sleep(5000);  
  					frozenColumnHeaderList.get(i).click();
  					Thread.sleep(5000); 
  					break;
  				}
  			}
  			
  		}  		
  	}

	/* 18. clicking on required freeze Columns */
	public void clickonColumntofreeze(String columnToSort){
		int counter = 0; // Variable to count freeze column
		for(int i = 0; i<frozenColumnHeaderList.size(); i++){
			if(frozenColumnHeaderList.get(i).getText().contains(columnToSort)){
				frozenColumnHeaderList.get(i).click();
				counter++;
				break;
			}	
		}
		if(counter==0){
				System.out.println("There is not matching row displayed ... ");
		}
	}
		
	/* 19. Get the Frozen Column Header Count for the given Frozen Column Name. */
	public int frozenColumnNum(String s){
		int frozenColNum=0;// Variable to count freeze column
		for(int i = 0; i<frozenColumnHeaderList.size(); i++){
			if(s.equalsIgnoreCase(frozenColumnHeaderList.get(i).getText().trim())){
				frozenColNum=i+1;
				return frozenColNum;
			}
		}		
		return frozenColNum;
	}
	
	
	/* 20. To Get the values from the frozen column */
	public List <String> freezeColumnValues(String s){
		List <String> frozenValues = new ArrayList<String>();
		String xpath = "//div[@id='frozenLinesTable']/div["+frozenColumnNum(s)+"]/div"; //dynamic Xpath for column
		List <WebElement> freezedColumnTableValues = driver.findElements(By.xpath(xpath));
			
		for(int i=0;i<freezedColumnTableValues.size(); i++){
			if(! freezedColumnTableValues.get(i).getText().equals("")){
				frozenValues.add(freezedColumnTableValues.get(i).getText());
			}
		}
		return frozenValues;
	}
	
	/* Method to Unhide all the Columns */
	public void clickUnHideAll(){
		unHideAllButton.click();
	}
	
	/* Method to Unhide all the Columns */
	public void clickUnfreezeAll(){
		unfreezeAllButton.click();
	}
		
}
