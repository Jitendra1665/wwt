package com.wwt.scm;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestNG_ExtUserSearchFilterDriver {

	FileInputStream ConfigFIS = null; //variable for Properties file inputstream
	protected WebDriver driver = null; // Variable for driver
	protected ThreadLocal<RemoteWebDriver> threadDriver = null;	// Remote Webdriver for Grid execution
	Properties scriptproperties = null; //Script properties object for properties file
	String BROWSER = null; // Browser variable
	List<String> addResults = new ArrayList<String>(); // List for CSV results data holder
	String inputdata_filetype=null; //variable for input file type (whether csv or excel)
	boolean test_status; // test run status variable to write to results
	String err_msg=null; // error message variable to write to results
	String methodname=null; // variable to read test case name to report to results
 	/* Variables for Page Objects */	
	SetDriverConfig setDriver = null;
	LoginPage logIn = null;
	HomePage homePage = null;
	SearchResults searchResultsPage = null;
	ContractHeader contractHeaderPage = null;
	LineStatusFacet lineStatus = null;
	ColumnHideOrFreeze colHideOrFreeze = null;
	CustomerFacet custFacet = null;
	InstallSiteCustomerFacet siteCustomerFacet=null;
	AddressFacet addressCustFacet=null;
	DateSelectionFacet dateSelectionFacet = null;
	LineDetails LineDetailsPage=null;
	StatsPage statspage=null;
	String scenario = null;
		
	@AfterClass
	public void afterTest() throws IOException{
		if(inputdata_filetype.equals("CSV")){
			GetDataProvider.resultUpdate(addResults);
		}
		driver.quit();
	}

	@BeforeMethod(enabled = true)
	public void launchAndLogin() throws Exception {
		try{
			test_status=true;
			err_msg="";		
			methodname="";
			if (driver==null){ // If Browser not opened open new browser else use already opened browser
				ConfigFIS = new FileInputStream(".\\scm.properties");
				scriptproperties = new Properties();
				scriptproperties.load(ConfigFIS);
				String URI = scriptproperties.getProperty("APP_URL");
				BROWSER = scriptproperties.getProperty("TestNG_ExtUserSearchFilterDriver_BROWSERTYPE");
				inputdata_filetype = scriptproperties.getProperty("SCM_INPUT_DATA_FILE_TYPE");
				if(scriptproperties.getProperty("RUN_MODE").equals("GRID")){
			        threadDriver = new ThreadLocal<RemoteWebDriver>();
			        DesiredCapabilities dc = new DesiredCapabilities();
			        if(BROWSER.equalsIgnoreCase("IE")){
			        	dc= DesiredCapabilities.internetExplorer();
			        	dc.setBrowserName("internet explorer"); 
			        	dc.setPlatform(org.openqa.selenium.Platform.VISTA);
					} else if (BROWSER.equalsIgnoreCase("GC")) {
			        	dc= DesiredCapabilities.chrome();
			        	dc.setBrowserName("chrome"); 
			        	dc.setPlatform(org.openqa.selenium.Platform.VISTA);	      		
					} else {
						BROWSER="FF";
			        	dc= DesiredCapabilities.firefox();
			        	dc.setBrowserName("firefox"); 
			        	dc.setPlatform(org.openqa.selenium.Platform.VISTA);				
					}
			        threadDriver.set(new RemoteWebDriver(new URL(scriptproperties.getProperty("HUB_URL")), dc));		
			        driver = threadDriver.get();  
				} else	{
			        // driver in non-Grid mode
					setDriver =  PageFactory.initElements(driver,SetDriverConfig.class);					
					driver = setDriver.getDriver(BROWSER);
				}
			    driver.get(URI);
			    driver.manage().window().maximize();
				String SELENIUMWAITTIME = scriptproperties.getProperty("DRIVERWAITTIME");
				Integer SECONDS = new Integer(SELENIUMWAITTIME);
				driver.manage().timeouts().implicitlyWait(SECONDS, TimeUnit.SECONDS);
				String userId = scriptproperties.getProperty("SCM_LOGIN_USERNAME");
				String passWord = scriptproperties.getProperty("SCM_LOGIN_PASSWORD");
			    /* Assigning Page Objects */
				homePage = PageFactory.initElements(driver, HomePage.class);			
				searchResultsPage = PageFactory.initElements(driver, SearchResults.class);
				statspage = PageFactory.initElements(driver, StatsPage.class);
				contractHeaderPage = PageFactory.initElements(driver, ContractHeader.class);
				colHideOrFreeze = PageFactory.initElements(driver, ColumnHideOrFreeze.class);
				lineStatus = PageFactory.initElements(driver, LineStatusFacet.class);
				custFacet= PageFactory.initElements(driver, CustomerFacet.class);
				siteCustomerFacet = PageFactory.initElements(driver, InstallSiteCustomerFacet.class);
				addressCustFacet = PageFactory.initElements(driver, AddressFacet.class);
				dateSelectionFacet = PageFactory.initElements(driver, DateSelectionFacet.class);
				LineDetailsPage = PageFactory.initElements(driver, LineDetails.class);
				logIn = PageFactory.initElements(driver, LoginPage.class);
				/* logging into application */
				logIn.ClickOnlogIn(userId, passWord);
			}else{
				homePage.clickHomeButton();
			}			
		} catch(Exception e){
			methodname = Thread.currentThread().getStackTrace()[1].getMethodName();
			CommonUtilLibrary.screenCapture(driver, methodname);
			e.printStackTrace();
		}
	}
	
	@Test(priority=5, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_0(String searchFilterValue) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_0() ");
		try{	 
			homePage.clickOnResetButton();
			scenario = "External users should not see search fields Product PO or Maint PO in Superflexible drop down";	
			homePage.clickFilterDropDown();
			assertFalse(homePage.validateSearchFilterValue(searchFilterValue), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_24_0() ");
	}
	
	@Test(priority=10, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_1(String tiedCustName) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_1() ");
		try{	 
			  homePage.clickOnResetButton();
			  scenario = "External user should default to their mapped WWT customer values and see no others";
			  //ToDo - need to change the code to verify the customer name column values,  instead of tied cust value.
			  //assertTrue(homePage.verifyTiedCustomerValue(tiedCustName));
			  homePage.clickOnRunSearchButton();
			  searchResultsPage.selectSearchResultType("Contract Results");
			  assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Contract Results"), scenario); 		  
			  assertTrue(contractHeaderPage.verifyColumnData("Customer", tiedCustName), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}		
		System.out.println("Completed Test Case - SearchAndFilter_24_1() ");
	}
	
	@Test(priority=15, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_2(String faceValueColName, String searchSiteFacetValue) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_2() ");
		try{	 
			scenario = "Sites and Addresses should only be those tied to their WWT customer in the data; Validate output is only for sites selected in search";
			homePage.clickOnResetButton();
			siteCustomerFacet.clickInstallSiteCustomerIcon();
			siteCustomerFacet.enterinstallsiteCustName(searchSiteFacetValue);
			assertTrue(siteCustomerFacet.verifyInstallsiteFacetSearchResult());
			siteCustomerFacet.selectAll();
			siteCustomerFacet.clickApply();
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.validateFacetSearchDataWithColDataInAllPages(faceValueColName, searchSiteFacetValue), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		} 		
		System.out.println("Completed Test Case - SearchAndFilter_24_2() ");
	}
	
	@Test(priority=20, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_3(String verifyFacetName) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_3() ");
		try{	 
			scenario = "External user should not see Sales Channel search field";
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			System.out.println(verifyFacetName);
			assertFalse(homePage.verifyGivenFacet(verifyFacetName), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		} 		
		System.out.println("Completed Test Case - SearchAndFilter_24_3() ");
	}
	
	@Test(priority=25, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_4(String verifyDateTypeListNamesInDateFacet, String contractPageColHeaderName, String fromToDateType) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_4() ");
		try{	 
			homePage.clickOnResetButton();
			scenario = "External user should only see Contract End Date range option;  All other date range sections for Creation Date, Product PO Date, Maintenance PO Date, Product PO Ship Date should be hidden from the external user";
			dateSelectionFacet.clickDateSelectionFacet();
			assertFalse(dateSelectionFacet.verifyDateTypeList(verifyDateTypeListNamesInDateFacet), scenario);
			dateSelectionFacet.closeFilterDateDropDown();
			scenario = "Output in Lines tab should only show end dates in range entered";
			homePage.clickOnRunSearchButton();
			searchResultsPage.selectSearchResultType("Contract Results");
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Contract Results"), scenario);
			//contractHeaderPage.sortRequiredColumn(colHeaderName, "asc");
			String earliestEndDate = contractHeaderPage.getGivenColumnValuesList(contractPageColHeaderName).get(0);
			SimpleDateFormat  DF = new SimpleDateFormat("MMM d, yyyy");
			Date date1 = DF.parse(earliestEndDate);
			SimpleDateFormat mydf = new SimpleDateFormat("yyyy/M/d");
			String fromToDate = mydf.format(date1);
			dateSelectionFacet.clickDateSelectionFacet();
			String dateType = fromToDateType;
			dateSelectionFacet.Dateset(dateType, "from", fromToDate);
			dateSelectionFacet.Dateset(dateType, "to", fromToDate);				
			dateSelectionFacet.closeFilterDateDropDown();
			searchResultsPage.selectSearchResultType("Line Results");
			// Scenario 1
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			homePage.clickOnRunSearchButton();
			//Scenario 2
			Assert.assertTrue(searchResultsPage.validateDateRange(dateType, fromToDate,fromToDate), scenario);		
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}		
		System.out.println("Completed Test Case - SearchAndFilter_24_4() ");
	}
	
	@Test(priority=30, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_5(String verifyListStatusTypes, String verifyListStatusChxBoxName) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_5() ");
		try{			
			homePage.clickOnResetButton();	
			lineStatus.clickLineStatusIcon();
			scenario = "The only line statuses they should see are Include Expired Lines, Include Terminated Lines  and End of End of Support ";									
			Assert.assertTrue(lineStatus.verifyingGivenlineStatus("Expired/Terminated Lines"), scenario);
			lineStatus.selectLineCheckbox("Include Expired Lines");
			lineStatus.selectLineCheckbox("Include Terminated Lines ");
			scenario = "External user should not see Publish statuses in Line Status list of values (Published, Not Published, Needs Review).";
			//Assert.assertFalse(lineStatus.verifyingGivenlineStatus(verifyListStatusTypes), scenario);
			Assert.assertFalse(lineStatus.verifyLineStatus(verifyListStatusChxBoxName), scenario);
			Assert.assertTrue(lineStatus.verifyingGivenlineStatus("Line Support Status"), scenario);
			lineStatus.selectLineCheckbox("End of Support ");
			lineStatus.closeLinsestatusFacet();
			scenario = "Output in Details page should only contain lines that have an End of Support date value.";
			homePage.clickOnRunSearchButton();
			System.out.println("closed and Searched");
			assertFalse(searchResultsPage.ExternalUser_validateColumnData("End Of Support", ""), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		} 		
		System.out.println("Completed Test Case - SearchAndFilter_24_5() ");
	}
	
	@Test(priority=35, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_24_6(String contractTabHeaderList) throws Exception{ 
		System.out.println("Started Test Case - SearchAndFilter_24_6() ");
		try{	
			scenario = "External user should not see counts of published lines in Contract header output;  ";
			homePage.clickOnResetButton();
			lineStatus.clickLineStatusIcon();
			scenario = "The only line statuses they should see are Include Expired Lines, Include Terminated Lines  and End of End of Support ";									
			Assert.assertTrue(lineStatus.verifyingGivenlineStatus("Expired/Terminated Lines"), scenario);
			lineStatus.selectLineCheckbox("Include Expired Lines");
			lineStatus.selectLineCheckbox("Include Terminated Lines ");
			Assert.assertTrue(lineStatus.verifyingGivenlineStatus("Line Support Status"), scenario);
			lineStatus.selectLineCheckbox("End of Support ");
			lineStatus.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();
			searchResultsPage.selectSearchResultType("Contract Results");
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Contract Results"), scenario);
			assertFalse(contractHeaderPage.verifyGivenContractsTabColumnHeaderList(contractTabHeaderList), scenario);
			scenario = "The only columns in the Contract output will be WWT Customer, Contract #, Service Level and Earliest End Date";
			contractTabHeaderList = "Contract Number , Customer, Service Level, Earliest End Date";
			assertTrue(contractHeaderPage.verifyGivenContractsTabColumnHeaderList(contractTabHeaderList), scenario);						
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}		
		System.out.println("Completed Test Case - SearchAndFilter_24_6() ");
	}
	
	@Test(priority = 40, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_10(String selectSearchOption,String searchData, String hideColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_2_10() ");
		try {
			scenario = "Hide columns in output display";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifyColumnHeaderAvailability(hideColumnName), scenario);
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.hideOrUnHide(hideColumnName);
			colHideOrFreeze.clickApply();
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(hideColumnName), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_10() ");
	}

	@Test(priority = 45, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_11(String unHideColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_2_11() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickUnHideAll();
			colHideOrFreeze.clickApply();
			// Hiding the column first to Verify the Un hide functionality
			String hideColumn = unHideColumnName;
			assertTrue(searchResultsPage.verifyColumnHeaderAvailability(hideColumn));
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.hideOrUnHide(hideColumn);
			colHideOrFreeze.clickApply();
			// Code for un Hiding the hidden column.
			scenario = "Unhide columns in output display";
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(unHideColumnName));
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.hideOrUnHide(unHideColumnName);
			colHideOrFreeze.clickApply();
			assertTrue(searchResultsPage.verifyColumnHeaderAvailability(unHideColumnName), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_11() ");
	}

	@Test(priority = 50, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_12(String selectSearchOption,String searchData, String freezeColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_2_12() ");
		try {
			scenario = "Freeze columns in output display, Ensure that the columns have frozen by scrolling";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.freezeOrUnFreeze(freezeColumnName);
			// assertTrue(colHideOrFreeze.verifyFreeze(freezeColumnName),
			// scenario);
			// assertTrue(colHideOrFreeze.verifyFreezeCheckBox(freezeColumnName),
			// scenario);
			colHideOrFreeze.clickApply();
			assertTrue(colHideOrFreeze.validatefrozenCols(freezeColumnName),scenario);
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(freezeColumnName),scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_12() ");
	}

	@Test(priority = 55, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_13(String unFreezeColumnName) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_2_13() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickUnfreezeAll();
			colHideOrFreeze.clickApply();
			// Freeze the column first to Verify the Un hide functionality
			String freezeColumn = unFreezeColumnName;
			assertTrue(searchResultsPage.verifyColumnHeaderAvailability(freezeColumn));
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.freezeOrUnFreeze(freezeColumn);
			colHideOrFreeze.clickApply();
			// Code to Validate Un Freeze functionality
			scenario = "Un-Freeze columns in output display, Ensure that the columns have un-frozen by scrolling";
			assertTrue(colHideOrFreeze.validatefrozenCols(freezeColumn), scenario);
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(freezeColumn),	scenario);
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.freezeOrUnFreeze(unFreezeColumnName);
			colHideOrFreeze.clickApply();
			assertTrue(searchResultsPage.verifyColumnHeaderAvailability(unFreezeColumnName));
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case -  LineAndDetailDisplay_2_13() ");
	}

	@Test(priority = 60, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_14(String selectSearchOption,String searchData, 
										  String freezeDateTypeColumnName, String sortType) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_2_14() ");
		try {
			//toDO - not able to code this test case because we are getting blank rows on Earliest end date for External User.
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case -  LineAndDetailDisplay_2_14() ");
	}
	
	
	@Test(priority = 65, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_15(String colNames) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_2_15() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.clickOnRunSearchButton();
			scenario = "External users should not see the following fields in the output: Publish Flag or checkbox, Sales Channel, Product PO#, Product PO Date, Maint PO# , Maint PO Date, Product PO Ship Date, Creation Date";
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(colNames), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case -  LineAndDetailDisplay_2_15() ");
	}
	
	
	@Test(priority = 70, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_16(String buttonName) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_2_16() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			scenario = "External users should not have an Action button for Publishing";
			assertFalse(searchResultsPage.verifyActionDropDown(buttonName));
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case -  LineAndDetailDisplay_2_16() ");
	}
	
	@Test(priority=75, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_0(String testString) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_0() ");
		try{
			homePage = PageFactory.initElements(driver,HomePage.class);
			Assert.assertTrue(homePage.validatehomepagedispay(), "failed to validate Homepage display");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_0() ");
	}

	@Test(priority=80, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_1(String custName, String colHeadings) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_1() ");
		try{
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			//LineDetailsPage.clickonFirstContractandValidate()
			searchResultsPage.verifyColumnHeaderAvailability(colHeadings);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_1() ");
	}		
	@Test(priority=85, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_2(String testSrring) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_2() ");
		try{
			homePage = PageFactory.initElements(driver,HomePage.class);
			homePage.validatehomepagedispay();
			homePage.clickOnRunSearchButton();			
			LineDetailsPage = PageFactory.initElements(driver,LineDetails.class);
			Assert.assertEquals(LineDetailsPage.customerButtonhovertext(),"Customer","Failed to validate hover text of Customer");
			Assert.assertEquals(LineDetailsPage.saleschannelButtonhovertext() ,"Sales Channel",  "Failed to validate hover text of Sales Channel");
			Assert.assertEquals(LineDetailsPage.installsiteCustButtonhovertext() ,"Install Site",  "Failed to validate hover text of Install Site Customer");
			Assert.assertEquals(LineDetailsPage.addressButtonhovertext() ,"Address",  "Failed to validate hover text of Address");
			//Assert.assertEquals(LineDetailsPage.datesearchButtonhovertext(),"Date Search",  "Failed to validate hover text of Date Search");
			//Assert.assertEquals(LineDetailsPage.linestatusButtonhovertext() ,"Line Status",  "Failed to validate hover text of Line Status");
			Assert.assertEquals(LineDetailsPage.additionalFiltersButtonhovertext() ,"Additional Filters",  "Failed to validate hover text of Additional Filters");			
			//Assert.assertEquals(LineDetailsPage.allRunButtonhovertext() ,"Run Search",  "Failed to validate hover text of All Search");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_2() ");
	}
	@Test(priority=90, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_4(String testSrring) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_4() ");
		try{
			LineDetailsPage = PageFactory.initElements(driver,LineDetails.class);
			homePage.clickOnRunSearchButton();
			LineDetailsPage.clickonFirstContractandValidate();
			assertTrue(searchResultsPage.verifySearchResultType_Contract("Con"));
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_4() ");
	}
	
	@Test(priority=95, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_6(String InstallsiteCustName) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_6() ");
		try{
			homePage.clickOnResetButton();
			siteCustomerFacet.clickInstallSiteCustomerIcon();
			siteCustomerFacet.enterinstallsiteCustName(InstallsiteCustName);
			siteCustomerFacet.selectAll();
			siteCustomerFacet.clickApply();
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Site Name"), "Wordwrap validation failed");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_6() ");
	}	

	@Test(priority=100, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_7(String custAddress) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_7() ");
		try{
			homePage.clickOnResetButton();
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custAddress);
			addressCustFacet.selectAllSearchedAddressFacets();
			addressCustFacet.clickApply();
			//addressCustFacet.addAddresstoSearch();
			//addressCustFacet.closeAddressFacet();	
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Site Address"), "Wordwrap validation failed");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_7() ");
	}	
	@Test(priority=105, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_8(String custCity) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_8() ");
		try{
			homePage.clickOnResetButton();
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custCity);
			addressCustFacet.selectAllSearchedAddressFacets();
			addressCustFacet.clickApply();
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("City"), "Wordwrap validation failed");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_8() ");
	}
	
	@Test(priority=110, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_2_9(String custCountry) throws Exception{
		System.out.println("Started Test Case - LineAndDetailDisplay_2_9() ");
		try{
			homePage.clickOnResetButton();
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custCountry);
			addressCustFacet.selectAllSearchedAddressFacets();
			addressCustFacet.clickApply();
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Country"), "Wordwrap validation failed");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_2_9() ");
	}	
	
	/* Method to write Results to CSV or Excel  */
	protected void updateResults()throws IOException, Exception {
		if(inputdata_filetype.equals("CSV")){
			addResults.add(methodname+","+test_status+","+BROWSER+","+err_msg);
		}else{
			GetDataProvider.updateResultExcel(test_status,err_msg, methodname, BROWSER);
		}
		if (!test_status) {
			CommonUtilLibrary.screenCapture(driver, methodname); 
			driver.quit();
			driver=null; 
			throw new Exception(err_msg);
		}
	}
	

}
