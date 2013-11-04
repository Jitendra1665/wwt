package com.wwt.scm;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ColumnDataSort {
	
	@FindBy(css="div[id='lines-header-container'] div[id='lines-header'] span[class*='ng-binding']")
	private List <WebElement> searchResultColumnHeadingsCss;
	
	@FindBy(css="div[id='lines-header-container'] div[id='lines-header'] div")
	private List <WebElement> lineHeaderValuesClassNames;
	
	@FindBy(css="div[id='content-main'] div[id='linesTable'] div")
	private List <WebElement> linesTableValuesClassNames;
	
	@FindBy(css="div[id*='lines-header-container'] div[id*=lines-header][class*=navbar-inner]"
			+ "span[class*='main-header'] span[class*='column-name'] i[class*='icon-caret']")
	private WebElement caretIcon;

	
	List <String> tableColumnClassNames = null;
	List <String> columnHeadingNames = null;
	List <String> actualColumnNames = null;
	List <String> columnClass = null;
	List <String> columnHeaderClassNames = null;
	
	protected final WebDriver driver;
	
	public ColumnDataSort(WebDriver driver) 
	{
		this.driver = driver;
	}
	
	
	 //1. Search Result Column Headers list
    public List <String> getColumnHeaders()
    {
    	int columnsCount = searchResultColumnHeadingsCss.size();
    	List <String> columnNamesList = new ArrayList <String>();
    	for(int i = 0; i<columnsCount; i++)
    	{
    		columnNamesList.add(searchResultColumnHeadingsCss.get(i).getText());
    	}
    	    	
    	return columnNamesList;
    }
        
    //2. Sort the Required Column
    public void sortRequiredColumn(String columnNameToSort)
    {
    	int counter=0;
    	actualColumnNames = getColumnHeaders();
    	System.out.println(actualColumnNames);
    	for(int i = 0; i<actualColumnNames.size(); i++)
    	{
    		if(actualColumnNames.get(i).contains(columnNameToSort))
    		{    			
    			//Actions mouseClickToSortAction = new Actions(driver);
    			//mouseClickToSortAction.moveToElement(searchResultColumnHeadingsCss.get(i)).click();
    			//System.out.println(i + " : "+searchResultColumnHeadingsCss.get(i));
    			searchResultColumnHeadingsCss.get(i).click();
    			counter++;
    			//System.out.println(i + " : "+searchResultColumnHeadingsCss.get(i));
    		}
    		if(counter==1)
    		{
    			break;
    		}
    	}
     	if(counter==0)
    	{
    		System.out.println("There is no such column as you specified To Sort : "+columnNameToSort);
    	}
    }
    
   // 2.1 Sort the Another Required Column when already one column is sorted.
    public void sortAnotherRequiredColumn(String columnNameToSort, String ascORdesc) throws InterruptedException
    {
    	int counter=0;
    	actualColumnNames = getColumnHeaders();
  		Actions builder =  new Actions(driver);
  		String caretIconCurrentStatus = caretIcon.getAttribute("class");
    		for(int i = 0; i<getColumnHeaders().size(); i++)
        	{    	
    			if(actualColumnNames.get(i).equals(columnNameToSort) && caretIconCurrentStatus.contains("up") && ascORdesc=="asc")
    			{    			
    				builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    				Thread.sleep(5000);
    				searchResultColumnHeadingsCss.get(i).click();
    				counter++;
    				break;
    			}else if(actualColumnNames.get(i).equals(columnNameToSort) && caretIconCurrentStatus.contains("down") && ascORdesc=="asc")
    			{    			
    				builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    				Thread.sleep(5000);
    				counter++;
    				break;
    			}else if(actualColumnNames.get(i).equals(columnNameToSort) && caretIconCurrentStatus.contains("up") && ascORdesc=="desc")
    			{    			
    				builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    				Thread.sleep(5000);
    				counter++;
    				break;
    			}else if(actualColumnNames.get(i).equals(columnNameToSort) && caretIconCurrentStatus.contains("down") && ascORdesc=="desc")
    			{    			
    				builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    				Thread.sleep(5000);
    				searchResultColumnHeadingsCss.get(i).click();
    				counter++;
    				break;
    			}
    		
        	}
     	if(counter==0)
    	{
    		System.out.println("There is no such column as you specified To Sort : "+columnNameToSort);
    	}
    }
    
  //3. get the required class name to see the carrot icon of that particular column
    public List <String> getColumnHeaderClassNames()
    {
    	columnHeaderClassNames = new ArrayList <String> ();
    	for(int i = 0; i<lineHeaderValuesClassNames.size(); i++){
    		columnHeaderClassNames.add(lineHeaderValuesClassNames.get(0).getAttribute("class"));
    	}
      	return columnHeaderClassNames;
    }
       
    
    //5. get the required table values class name to see the carrot icon of that particular column
    public List <String> getColumnTableValuesClassNames()
    {
    	columnHeaderClassNames = new ArrayList <String> ();
    	for(int i = 0; i<linesTableValuesClassNames.size(); i++)
    	{
    		columnHeaderClassNames.add(linesTableValuesClassNames.get(0).getAttribute("class"));
    	}
      	return columnHeaderClassNames;
    } 	
    
    //6. Reading the column Values for the given column Name
    public List <String> getColumnTableValues(String columnName)
    {
    	List <String> columnTableValues = new ArrayList <String> ();
    	List <WebElement> tableColumn = null;
    	
    	//tableColumnClassNames = getColumnTableValuesClassNames();
    	//columnHeadingNames = getColumnHeaders();
    	
    	for(int i=0; i<tableColumnClassNames.size();i++){
    		int counter=0;
    		if(columnHeadingNames.get(i).equalsIgnoreCase(columnName)){
    			
    			tableColumn = driver.findElements(By.xpath("//div[div[div[div[span[span[contains(text(),'"+columnHeadingNames.get(i)+"')]]]]]]" +
    	    			"/div[@id='linesTable']/div[contains(@class,'"+tableColumnClassNames.get(i)+"')]/div"));
    			counter++;
    		}
    		if(counter>0){
    			break;
    		}
    	}
    	
    	for(int j = 0; j<tableColumn.size(); j++){
    		columnTableValues.add(tableColumn.get(j).getText());
    	}
    	return columnTableValues;
    }
	
    //7. Clicking on the given Number
    public void clickOnLinkInTableColumnValue(String linkName)
    {
    	//List <String> tableColumnClassNames = getColumnTableValuesClassNames();
    	List <WebElement> linksToClick = null;
    	
    	for(int i = 0; i<tableColumnClassNames.size(); i++)
    	{
    		linksToClick = driver.findElements(By.xpath("//div[div[div[div[span[span[contains(text(),'"+tableColumnClassNames.get(i)+"')]]]]]]" +
        			"/div[@id='linesTable']/div[contains(@class,'"+tableColumnClassNames.get(i)+"')]/div/a"));	
    	}
    	for(int j = 0; j<linksToClick.size(); j++)
    	{
    		int counter = 0;
    		if(linksToClick.get(j).getText().equals(linkName))
    		{
    			linksToClick.get(j).click();
    			counter++;
    		}
    		if(counter>0)
    		{
    			break;
    		}
    	}
    }

    //8. Sorting the required Column
    public void sortRequiredColumn(String s, String ascOrDesc) throws InterruptedException
    {
    	//String caretIconStatus=null;
    	
    	Actions builder = new Actions(driver);
    	List<String> headerNames = getColumnHeaders();
    	
    	for(int i = 0; i<headerNames.size(); i++)
    	{    	
    		if( (headerNames.get(i).contains(s)) && (ascOrDesc=="asc"))
    		{
    			builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    			//caretIconStatus = caretIcon.getAttribute("class");
    		}
    		else if( (headerNames.get(i).contains(s)) && (ascOrDesc=="desc"))
    		{
    			builder.clickAndHold(searchResultColumnHeadingsCss.get(i)).click().perform();
    			Thread.sleep(6000);
    			searchResultColumnHeadingsCss.get(i).click();
    			//caretIconStatus = caretIcon.getAttribute("class");
    		}
    	}
    	Thread.sleep(5000);
    	return ;
    }
    
		
    
    
/* //8. get the required name.
    public void checkRequiredColumnValue()
    {
    	List <String> allValueNames = getColumnTableValues("linkName");
    	
    	//List <WebElement> linksToClick = null;
    	//String a;
    	
    	for(int i = 0; i<allValueNames.size(); i++)
    	{
    		if(allValueNames.get(i).contains(" "))
    		{
    			allValueNames.get(i).split(" ");
    		}
    	}
    	
    }  */
    
}
