/*****************************************************
 * Class Name: DateSelectionFacet
 * Class Purpose: Date Selection Page Object
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class DateSelectionFacet {
	
		WebElement verifyMonthYear = null;
		
		protected final WebDriver driver; // driver Variable
		
		/* WebElements of Date Selection Page Object */
		@FindBy(css="i[id='calendar-search-icon']")
		private WebElement dateSearchIcon;
		
		@FindBy(css="div#dateLineStatus.modal div.modal-body div.pull-left div.container-fluid div.control-group")
		private List<WebElement> filterDateTypes;
		
		@FindBy(css="div#dateLineStatus.modal div.modal-header button.close")
		private WebElement closedatetypeFace;

		/* Constructor */
	    public DateSelectionFacet(WebDriver driver) {
		       this.driver = driver;
		}
    
	    /* 1. click on Date Selection Facet */
	    public void clickDateSelectionFacet() throws InterruptedException{
	    	Thread.sleep(3000);
	    	dateSearchIcon.click();
	    }
	      
	    /* 2. Clicking on the Date Calendar Icon. */
	    public int getDateTypeSize() throws InterruptedException{
	    	return filterDateTypes.size();		
		}	   
    
	    /* 3. verify displayed date Types List in matching */ 
	    public boolean verifyDateTypeList(String userDateTypeList) throws InterruptedException{
	    	boolean dateTypeFlag = false;
	    	for(int i = 0; i<filterDateTypes.size(); i++){
	    		if(userDateTypeList.toLowerCase().trim().contains(filterDateTypes.get(i).getText().toLowerCase().trim())){
	    			dateTypeFlag = true;
	    			Thread.sleep(5000);
	    			break;    	
	    		}
	    	}
			return dateTypeFlag;
		}
	      
	    /* 4. Method to click on required Date */
	    public void clickOnRequiredDateType(String dateType, String fromTo) throws InterruptedException{
	    	int dateTypeIndex = 0; 	// Variable to hold the date type ( from / to )
			WebElement endDate_StartDateInbox = null;
			for(int i = 0; i<filterDateTypes.size(); i++){
				if(filterDateTypes.get(i).getText().toLowerCase().trim().contains(dateType.toLowerCase().trim())){	  
				dateTypeIndex = i+1;
					  break;
				}
			}
			int calendarButtonType = 1;
			if(!fromTo.equalsIgnoreCase("from")){
				  calendarButtonType = 2;
			}
			Thread.sleep(3000);
			String s = "//div[@id='dateLineStatus']/div[2]/div[1]/div/div["+dateTypeIndex+"]/div/input["+calendarButtonType+"]"; // dynamic xpath for date type
			Thread.sleep(1000);
			endDate_StartDateInbox = driver.findElement(By.xpath(s));		   				
			Actions mouseClick = new Actions(driver);
			mouseClick.moveToElement(endDate_StartDateInbox).click().perform();
		}
	  

		/*5. Verifying if the current displaying year is the one which we are looking for.*/
		public boolean verifyYear(String year, int calenderToClick) throws InterruptedException{
				boolean yearVerifycation = false;
				String syear = "//*[@id='ng-app']/body/div["+calenderToClick+"]/div[1]/table/thead/tr[1]/th[2]"; // dynamic xpath for year
				Thread.sleep(2000);
				verifyMonthYear = driver.findElement(By.xpath(syear));
				String YR = verifyMonthYear.getText();
				if(YR==year){
						yearVerifycation = true;
				}
				return yearVerifycation;
		}
	
		/* 6. If the displaying year is not the current year then it will pick the required year. */
		public int yearPicker(String yr, int from_to) throws InterruptedException{
				Integer YEARtoSELECT = new Integer(yr);
				String sCurrentYear = "//*[@id='ng-app']/body/div["+from_to+"]/div[2]/table/thead/tr/th[2]"; // dynamic xpath for year
				WebElement verifyCurrentYear = driver.findElement(By.xpath(sCurrentYear));
				String presentDisplayYear = verifyCurrentYear.getText();
				Integer displayYear = new Integer(presentDisplayYear);
				int yearDiff = 0;
				if(YEARtoSELECT < displayYear){
					yearDiff = displayYear - YEARtoSELECT;
				}else if(YEARtoSELECT>displayYear){
					yearDiff = YEARtoSELECT-displayYear;
					
				}
				return yearDiff;		
		}
		/* 7. Method to click on arrow to click on Data Calender */
		public int arrowToClick(String CurrentYear, int From_to){
			Integer YEARtoSELECT = new Integer(CurrentYear);
			String sCurrentYear = "//*[@id='ng-app']/body/div["+From_to+"]/div[2]/table/thead/tr/th[2]"; // dynamic xpath for arrow
			WebElement verifyCurrentYear = driver.findElement(By.xpath(sCurrentYear));
			String presentDisplayYear = verifyCurrentYear.getText();
			Integer displayYear = new Integer(presentDisplayYear);
			int Arrow_to_Click = 1;
			if(YEARtoSELECT > displayYear){
				Arrow_to_Click = 3;
			}
			return Arrow_to_Click;
		}
		
		/* 6. If the year is current year, then we need to verify the month is current month or not. */ 
		public boolean verifyMonth(String month,int fromTo) throws InterruptedException{
			boolean monthBoolean = true;
			String smonth = "//*[@id='ng-app']/body/div["+fromTo+"]/div[1]/table/thead/tr[1]/th[2]"; // dynamic xpath for Month
			Thread.sleep(1000);
			verifyMonthYear = driver.findElement(By.xpath(smonth));
			String monthYear = verifyMonthYear.getText();
			if(!(monthYear.contains(month))){
					monthBoolean = false;
			}
			return monthBoolean;
		}
		
		/* 7. If the month which we are selecting is not the current month then this method will pick the required month. */
		void monthPicker(String monthToSelect, int divFrom) throws InterruptedException{
				int l=0;
				l=new Integer(monthToSelect);
				String monthPickTable = "//*[@id='ng-app']/body/div[" +divFrom+"]/div[2]/table/tbody/tr/td/span["+l+"]"; // dynamic xpath for Month
				Thread.sleep(1000);
				WebElement monthSelect = driver.findElement(By.xpath(monthPickTable));		   				
				Thread.sleep(1000);
				Actions mouseClick = new Actions(driver);
				Thread.sleep(1000);
				mouseClick.click(monthSelect).perform();
		}	

		/* 8. To get the required Month from the Months display menu */
		public void monthHeaderClick() throws InterruptedException{
			Actions clickForMonthSelection = new Actions(driver);
			Thread.sleep(1000);
			clickForMonthSelection.click(verifyMonthYear).perform();
		}
	
		/* 9. To get the required Date from the Calendar Display menu */ 
		public void yearHeaderClick(int arrowClick, int fromTo) throws InterruptedException{
			String arrow = "//*[@id='ng-app']/body/div["+fromTo+"]/div[2]/table/thead/tr/th["+arrowClick+"]/i"; // dynamic xpath for year
			WebElement getYear = driver.findElement(By.xpath(arrow));
			Actions clickForYear = new Actions(driver);
			Thread.sleep(1000);
			clickForYear.click(getYear).perform();
		}
		
		/* 10. Selecting the date. */
		public void dateSelector(String requiredDate, int fromOrTo) throws InterruptedException{
			for(int row = 1; row<=6; row++){
				for(int col = 1; col<=7; col++){
					int c = 0;
					if(c==0){
						String date1 = "//*[@id='ng-app']/body/div["+fromOrTo+"]/div[1]/table/tbody/tr["+row +"]/td["+col+"]"; // dynamic xpath for Day
						WebElement dateElement = driver.findElement(By.xpath(date1));
						String dateClassAttribute = dateElement.getAttribute("class");
						if(!(dateClassAttribute.equalsIgnoreCase("day old") || dateClassAttribute.equalsIgnoreCase("day new"))){ // To select Day
							String expectedDate = dateElement.getText();
							if(expectedDate.equals(requiredDate)){
								dateElement.click();
								col=8;
								row=7;
								break;
							}
						}
					}
				}
			}
		}
	
		/* 11. Closing the Date menu  */
	    public void closeFilterDateDropDown() throws InterruptedException{
	    	closedatetypeFace.click(); 
	    	Thread.sleep(2000);
		    }
	    /* 12. Method to set date in date selection facet  */
	    public void Dateset(String dateType, String fromTo,  String ddate) throws InterruptedException, ParseException{
	    		clickOnRequiredDateType(dateType, fromTo);
	     		String darray[] = ddate.split("/");
	     		String dYear = darray[0];
	     		String dMonth = darray[1];
	     		String dDay = darray[2];
		 		int datetoclick = 2; //dateType(from / To);
		 		boolean booleanYearto = verifyYear(dYear, datetoclick);
		 		if(!booleanYearto ){
			 		monthHeaderClick();
					int find_yearDiff_if_greater_less_than_currentYear = yearPicker(dYear, datetoclick);
					int incre_decre_arrowType_toClick =  arrowToClick(dYear, datetoclick);
					//Will Click on Side arrow (increment or decrement buttons) based on our year type
					for(int i = 1; i<=find_yearDiff_if_greater_less_than_currentYear; i++){
						yearHeaderClick(incre_decre_arrowType_toClick, datetoclick);
					}
					monthPicker(dMonth, datetoclick); 
			
		 		} else{
		 			boolean boolanMonth = verifyMonth(dMonth, datetoclick);  
		 			if(!boolanMonth){
		 				monthHeaderClick();
		 				monthPicker(dMonth, datetoclick);
		 			}
		 		}
		 		dateSelector(dDay, datetoclick);  
		 		Thread.sleep(3000);
	    }
    
}