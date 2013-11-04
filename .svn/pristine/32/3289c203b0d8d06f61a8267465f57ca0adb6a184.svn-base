/*****************************************************
 * Class Name: LineStatusFacet
 * Class Purpose: Line status Page Objects
 * Created by:Jitendra/Srinivas
 *****************************************************/

package com.wwt.scm;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LineStatusFacet {
	
		protected final WebDriver driver; // driver variable
		
		/* WebElements for Line Status Page Objects */
		@FindBy(css="div.nav-collapse ul.nav li.facet-nav-button a#calendarSearch")
		private WebElement lineStatusIcon;

		@FindBy(css="div#dateLineStatus.modal div.modal-body div#lineStatus-data.pull-right div.container-fluid div.line-status-container h5")
		private List<WebElement> statusLineTypes;
		
		@FindBy(css="div[id='lineSatus-data'] div[class='row-fluid']")
		private List<WebElement> cssAllVisibleAndUnVisbileLineStatusTypes;
				
		@FindBy(css="div#dateLineStatus.modal div.modal-header button.close")
		private WebElement closeLineSFacet;
				
		@FindBy(css="div#dateLineStatus.modal div.modal-body div#lineStatus-data.pull-right div.container-fluid div.line-status-container label.checkbox")
		private List<WebElement> statusChecboxesList;
		
		/* Constructor */
		public LineStatusFacet(WebDriver driver) {
	        this.driver = driver;
	    }
	
		/* 1. Clicking on Line Status Icon */
		public void clickLineStatusIcon() throws InterruptedException{
	    		Thread.sleep(2000);
				lineStatusIcon.click();
				Thread.sleep(2000);
	    }
	    
		/* 2. Method to close Line Status facet */
		public void closeLinsestatusFacet() throws InterruptedException{
			closeLineSFacet.click();
			Thread.sleep(2000);
		}
		
		/* 3. Verifying if the Required List Type is available or not */
		public boolean verifyingGivenlineStatus(String statusLineTypeName){
			boolean statusTypeAvailability = false;
			List <WebElement> lineStatusTypes_List = statusLineTypes;
			int lineStatusTypesCount = lineStatusTypes_List.size();
			for(int i=0;i<lineStatusTypesCount; i++){
				String listName = lineStatusTypes_List.get(i).getText();
				if(listName.toLowerCase().trim().contains(statusLineTypeName.toLowerCase().trim())){
				  statusTypeAvailability = true;
				  break;
				}
			}
			return statusTypeAvailability;
		}

		/* 4. Select the LineStatus  CheckBox based on given LineStatus */
		public boolean selectLineCheckbox(String checkBoxToSelect){
			for( WebElement i : statusChecboxesList){
				if (i.getText().toLowerCase().trim().contains(checkBoxToSelect.toLowerCase().trim())){
					i.click();
					return true;
				}	
			}
			return false;
		}
		
		/* 5. Select the LineStatus  CheckBox based on given LineStatus */
		public boolean verifyLineStatus(String lineStatusType){
			for( WebElement i : statusChecboxesList){
				if (i.getText().toLowerCase().trim().contains(lineStatusType.toLowerCase().trim())){
					i.click();
					return true;
				}	
			}
			return false;
		}
}		