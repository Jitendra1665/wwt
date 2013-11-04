package com.wwt.scm;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.wwt.scm.ColumnHideOrFreeze;
import com.wwt.scm.ContractHeader;
import com.wwt.scm.HomePage;
import com.wwt.scm.LoginPage;
import com.wwt.scm.SearchResults;
import com.wwt.scm.SetDriverConfig;
import com.wwt.scm.StatsPage;

public class TestNG_SearchFilterDriver {

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
	StatsPage statspage = null;
	ColumnHideOrFreeze colHideOrFreeze = null;
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
				BROWSER = scriptproperties.getProperty("TestNG_SearchFilterDriver_BROWSERTYPE");
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

	@Test(priority = 5, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_2_0(String searchFilterOption, String contractNumber) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_2_0() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched";
			homePage.clickOnResetButton();
			//assertTrue(homePage.verifyTextOnSearchButton(searchFilterOption), "Button is not properly selected");
			homePage.enterSearchCriteria(contractNumber);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			//scenario = "No stats will display for the 'All' selection.";
			//assertFalse(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_2_0() ");
	}

	@Test(priority = 10, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_2_1(String getColumnData,String contractNumber, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_2_1() ");
		try {
			homePage.clickOnResetButton();
			homePage.enterSearchCriteria(contractNumber);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(getColumnData, contractNumber), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getSearchResultType_LineOrContact();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum), scenario);
			scenario = "WWT Customer value is upper/lower case.";
			System.out.println("custo name : " + verifyCustName);
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_2_1() ");
	}

	@Test(priority = 15, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_2_2(String contractNum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_2_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.enterSearchCriteria(contractNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level", scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_2_2() ");
	}

	@Test(priority = 20, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_2_3(String searchVal1, String searchVal2,String searchVal3, String getColumnData) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_2_3() ");
		try {
			scenario = "The values you've selected with either pull back with more than one option to select or not be matched if not present.";
			homePage.clickOnResetButton();
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(getColumnData, multiSearchString), scenario);
			//scenario = "No stats will display for the 'All' selection.";
			//assertFalse(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_2_3() ");
	}

	@Test(priority = 25, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_3_0(String searchByContractNum, String contractNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_3_0() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByContractNum);
			homePage.enterSearchCriteria(contractNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
		/*	assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(), statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(contractNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_3_0() ");
	}

	@Test(priority = 30, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_3_1(String searchByContractNum,String contractNum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_3_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByContractNum);
			homePage.enterSearchCriteria(contractNum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByContractNum, contractNum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNumber = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNumber), scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_3_1() ");
	}

	@Test(priority = 35, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_3_2(String searchByContractNum, String contractNum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_3_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByContractNum);
			homePage.enterSearchCriteria(contractNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level", scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_3_2() ");
	}

	@Test(priority = 40, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_3_3(String searchByContractNum,String searchVal1, String searchVal2, String searchVal3)throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_3_3() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByContractNum);
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByContractNum), "Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByContractNum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_3_3() ");
	}

	@Test(priority = 45, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_42_0_SingleSearch(String searchByInstanceNum, String instanceNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_42_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByInstanceNum);
			homePage.enterSearchCriteria(instanceNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(), statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(instanceNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out
				.println("Completed Test Case - SearchAndFilter_42_0_SingleSearch()");
	}

	@Test(priority = 50, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_42_1(String searchByMaintenancePONum,String maintainancePONum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_42_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByMaintenancePONum);
			homePage.enterSearchCriteria(maintainancePONum);
			homePage.clickOnRunSearchButton();
			//Scenario 1
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByMaintenancePONum, maintainancePONum), scenario);
			//Scenario 2
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum), scenario);
			//Scenario 3 
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_42_1() ");
	}

	@Test(priority = 55, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_42_2(String searchByMaintenancePONum,String maintainancePONum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_42_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByMaintenancePONum);
			homePage.enterSearchCriteria(maintainancePONum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_42_2() ");
	}

	@Test(priority = 60, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_42_0_MultipleSearch(String searchByMaintenancePONum, String searchVal1, String searchVal2, String searchVal3) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_42_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByMaintenancePONum);
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByMaintenancePONum),	"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByMaintenancePONum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_42_0_MultipleSearch() ");
	}

	@Test(priority = 65, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_41_0_SingleSearch(String searchByInstanceNum, String instanceNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_41_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByInstanceNum);
			homePage.enterSearchCriteria(instanceNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),
					statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(instanceNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_41_0_SingleSearch() ");
	}

	@Test(priority = 70, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_41_1(String searchByInstanceNum, String instanceNum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_41_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByInstanceNum);
			homePage.enterSearchCriteria(instanceNum);
			homePage.clickOnRunSearchButton();
			if(searchByInstanceNum.equals("Instance #")){
				searchByInstanceNum = "Cisco Instance #"; 
			}
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			System.out.println("lines button displayed");
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByInstanceNum, instanceNum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum),	scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_41_1() ");
	}

	@Test(priority = 75, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_41_2(String searchByInstanceNum,String instanceNum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_41_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByInstanceNum);
			homePage.enterSearchCriteria(instanceNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(),custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_41_2() ");
	}

	@Test(priority = 80, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_41_0_MultipleSearch(String searchByInstanceNum,String searchVal1, String searchVal2, 
													String searchVal3) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_41_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByInstanceNum);
			String multiSearchString = searchVal1 + "," + searchVal2 + "," + searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByInstanceNum),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByInstanceNum, multiSearchString), scenario);
			/*scenario = "Stats will display for the 'Contract # search' selection.";
			assertTrue(statspage.verifyStatsButton(), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_41_0_MultipleSearch() ");
	}

	@Test(priority = 85, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_4_0_SingleSearch(String searchByCustomerPONum, String customerPONum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_4_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByCustomerPONum);
			homePage.enterSearchCriteria(customerPONum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(customerPONum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_4_0_SingleSearch() ");
	}

	@Test(priority = 90, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_4_1(String searchByCustomerPONum,String CustomerPONum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_4_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByCustomerPONum);
			homePage.enterSearchCriteria(CustomerPONum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByCustomerPONum, CustomerPONum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			System.out.println(actualContractNumberDisplayedOnButton + " : "+ contractNum);
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum),scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(),verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_4_1() ");
	}

	@Test(priority = 95, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_4_2(String searchByCustomerPONum,String CustomerPONum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_4_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByCustomerPONum);
			homePage.enterSearchCriteria(CustomerPONum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_4_2() ");
	}

	@Test(priority = 100, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_4_0_MultipleSearch(String searchByCustomerPONum, String searchVal1, String searchVal2,
												   String searchVal3) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_4_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByCustomerPONum);
			String multiSearchString = searchVal1 + "," + searchVal2 + "," + searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByCustomerPONum),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByCustomerPONum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_4_0_MultipleSearch() ");
	}

	@Test(priority = 105, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_5_0_SingleSearch(String searchByPartNum,String partNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_5_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartNum);
			homePage.enterSearchCriteria(partNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/* assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),
					statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(partNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_5_0_SingleSearch() ");
	}

	@Test(priority = 110, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_5_1(String searchByPartNum, String partNum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_5_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartNum);
			homePage.enterSearchCriteria(partNum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			if (searchByPartNum.equals("Part #")) { searchByPartNum = "Part Number";}
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByPartNum, partNum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum), scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_5_1() ");
	}

	@Test(priority = 115, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_5_2(String searchByPartNum, String partNum,	String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_5_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartNum);
			homePage.enterSearchCriteria(partNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(),custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_5_2() ");
	}

	@Test(priority = 120, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_5_0_MultipleSearch(String searchByPartNum,String searchVal1, String searchVal2, 
												   String searchVal3) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_5_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartNum);
			String multiSearchString = searchVal1 + "," + searchVal2 + "," + searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByPartNum),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			if (searchByPartNum.equals("Part #")) {
				searchByPartNum = "Part Number";
			}
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByPartNum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_5_0_MultipleSearch() ");
	}

	@Test(priority = 125, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_6_0_SingleSearch(String searchByPartLabel,String partLabelName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_6_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartLabel);
			homePage.enterSearchCriteria(partLabelName);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(), statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(partLabelName), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_6_0_SingleSearch() ");
	}

	@Test(priority = 130, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_6_1(String searchByPartLabel,String partLabelName, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_6_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartLabel);
			homePage.enterSearchCriteria(partLabelName);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByPartLabel, partLabelName), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum),scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(),verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_6_1() ");
	}

	@Test(priority = 135, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_6_2(String searchByPartLabel, String partLabelName, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_6_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartLabel);
			homePage.enterSearchCriteria(partLabelName);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(),custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_6_2() ");
	}

	@Test(priority = 140, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_6_0_MultipleSearch(String searchByPartLabel,String searchVal1, String searchVal2,
													String searchVal3) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_6_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByPartLabel);
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByPartLabel),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByPartLabel, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_6_0_MultipleSearch() ");
	}

	@Test(priority = 145, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_61_0_SingleSearch(String searchByProductPONum,String productPONum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_61_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByProductPONum);
			homePage.enterSearchCriteria(productPONum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(productPONum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_61_0_SingleSearch() ");
	}

	@Test(priority = 150, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_61_1(String searchByProductPONum,String productPONum, String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_61_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByProductPONum);
			homePage.enterSearchCriteria(productPONum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByProductPONum, productPONum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum), scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(), verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_61_1() ");
	}

	@Test(priority = 155, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_61_2(String searchByProductPONum,String productPONum, String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_61_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByProductPONum);
			homePage.enterSearchCriteria(productPONum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level", scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_61_2() ");
	}

	@Test(priority = 160, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_61_0_MultipleSearch(String searchByProductPONum, String searchVal1, String searchVal2,
													String searchVal3) throws Exception {

		System.out.println("Started Test Case - SearchAndFilter_61_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchByProductPONum);
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchByProductPONum),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			assertTrue(searchResultsPage.validateColDataInAllPages(searchByProductPONum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_61_0_MultipleSearch() ");
	}

	@Test(priority = 165, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_7_0_SingleSearch(String searchBySerialNum,String serialNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_7_0_SingleSearch() ");
		try {

			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySerialNum);
			homePage.enterSearchCriteria(serialNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(serialNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_7_0_SingleSearch() ");
	}

	@Test(priority = 170, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_7_1(String searchBySerialNum, String serialNum,String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_7_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySerialNum);
			homePage.enterSearchCriteria(serialNum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			if (searchBySerialNum.equals("Serial #")) {
				searchBySerialNum = "Serial Number";
			}
			assertTrue(searchResultsPage.validateColDataInAllPages(searchBySerialNum, serialNum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum),scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(),verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_7_1() ");
	}

	@Test(priority = 175, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_7_2(String searchBySerialNum, String serialNum,
									String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_7_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySerialNum);
			homePage.enterSearchCriteria(serialNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma
			// seperated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(),custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_7_2() ");
	}

	@Test(priority = 180, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_7_0_MultipleSearch(String searchBySerialNum,String searchVal1, String searchVal2,
													String searchVal3)throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_7_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySerialNum);
			String multiSearchString = searchVal1 + "," + searchVal2 + "," + searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchBySerialNum), "Button is not properly selected");
			homePage.clickOnRunSearchButton();
			//assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			if (searchBySerialNum.equals("Serial #")) {	searchBySerialNum = "Serial Number"; }
			assertTrue(searchResultsPage.validateColDataInAllPages(searchBySerialNum, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_7_0_MultipleSearch() ");
	}

	@Test(priority = 185, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_8_0_SingleSearch(String searchBySiteID,String siteIDNum) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_8_0_SingleSearch() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySiteID);
			homePage.enterSearchCriteria(siteIDNum);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			/*assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			scenario = "Stats tab to confirm your selection results match.";
			statspage.clickStatMatchedResultsButton();
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertEquals(statspage.getSuccessStatCount(),statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(siteIDNum), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_8_0_SingleSearch() ");
	}

	@Test(priority = 190, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_8_1(String searchBySiteID, String siteIDNum,String verifyCustName) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_8_1() ");
		try {
			scenario = "The contract number(s) you entered pull back in the search results either by matched or not matched.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySiteID);
			homePage.enterSearchCriteria(siteIDNum);
			homePage.clickOnRunSearchButton();
			scenario = "Lines for matched contracts selected display and can be scrolled through.";
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			//if (searchBySiteID.equals("Site Id")) {  searchBySiteID = "Site Id";  }
			assertTrue(searchResultsPage.validateColDataInAllPages(searchBySiteID, siteIDNum), scenario);
			scenario = "link for contract # to confirm it takes you to the correct contract tab.";
			//System.out.println(searchBySiteID);
			if (searchBySiteID.equals("Site Id")){ 	searchResultsPage.navigatePreviousPage();  }
			String contractNum = searchResultsPage.getAndClickfirstContractNumber().trim();
			String actualContractNumberDisplayedOnButton = searchResultsPage.getActualContactNumber().trim();
			assertTrue(actualContractNumberDisplayedOnButton.contains(contractNum), scenario);
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(searchResultsPage.getFirstCustNameInLinesPg().trim(),verifyCustName);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_8_1() ");
	}

	@Test(priority = 195, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_8_2(String searchBySiteID, String siteIDNum,String custName, String searchResultType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_8_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySiteID);
			homePage.enterSearchCriteria(siteIDNum);
			homePage.clickOnRunSearchButton();
			scenario = "Selection of the button takes you to the contract headers view";
			searchResultsPage.selectSearchResultType(searchResultType);
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract(searchResultType), scenario);
			scenario = "headers view which should be ordered first by the 'Order By Customer', then by 'Service Level'.";
			List<String> colHeaderOrder = contractHeaderPage.getContractsTabColumnHeaderList();
			assertEquals(colHeaderOrder.get(0).trim(), "Customer", scenario);
			assertEquals(colHeaderOrder.get(1).trim(), "Service Level",	scenario);
			// ToDo - Needs to include the validation as below
			// If data contains more than one value that should be comma separated.
			scenario = "WWT Customer value is upper/lower case.";
			assertEquals(contractHeaderPage.getFirstCustNameInContractsPg(), custName, scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_8_2() ");
	}

	@Test(priority = 200, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_8_0_MultipleSearch(String searchBySiteID,String searchVal1, String searchVal2,
													String searchVal3) throws Exception {

		System.out.println("Started Test Case - SearchAndFilter_8_0_MultipleSearch() ");
		try {
			scenario = "Those 3 contracts should return";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(searchBySiteID);
			String multiSearchString = searchVal1 + "," + searchVal2 + ","+ searchVal3;
			homePage.enterSearchCriteria(multiSearchString);
			assertTrue(homePage.verifyTextOnSearchButton(searchBySiteID),"Button is not properly selected");
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.verifySearchResultType_LineOrContract("Line Results"), scenario);
			if (searchBySiteID.equals("Site ID")) { 
				searchBySiteID = "Site Id";
			}
			assertTrue(searchResultsPage.validateColDataInAllPages(searchBySiteID, multiSearchString), scenario);
			//scenario = "Stats will display for the 'Contract # search' selection.";
			//assertTrue(statspage.verifyStatsButton(), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_8_0_MultipleSearch() ");
	}

	// If there is all matching records then we can verify both(matched) stats
	@Test(priority = 205, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_20_0(String selectSearchOption,String verifyColumnValue) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_20_0() ");
		try {
			scenario = "You should see a green number on the 'Stats' tab noting a mached value.";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(verifyColumnValue);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.getPagesCount() > 0);
			/*assertTrue(statspage.successNumberDisplay());
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			statspage.clickStatMatchedResultsButton();
			assertEquals(statspage.getSuccessStatCount(), statspage.getStatPageMatchCount(), scenario);*/
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_20_0() ");
	}

	// If there is all unmatched record is there then we can verify both(matched
	// and notfound) stats functionality is not availble in the current application
	@Test(priority = 210, enabled = false, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_21_0(String selectSearchOption,	String verifyColumnValue) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_21_0() ");
		try {
			scenario = "You should see a red number on the 'Stats' tab noting an unmached value. You should see a red number on the 'Stats' tab noting an unmached value.  Click on that tab to review";
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(verifyColumnValue);
			homePage.clickOnRunSearchButton();
			statspage.clickStatusButton();
			assertTrue(statspage.statReSubmitButtonDisplay(), scenario);
			assertTrue(statspage.notFoundNumberDisplay(), scenario);
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertTrue(statspage.getNotFoundStatCount() > 0, scenario);
			statspage.clickStatMatchedResultsButton();
			assertEquals(statspage.getNotFoundStatCount(),statspage.getStatPageNotFoundCount(), scenario);
			assertTrue(statspage.verifyNotFoundSearchItem(verifyColumnValue),scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_21_0() ");
	}

	// If there is one matching record and another unmatched record is there
	// then we can verify both(matched and notfound) stats
	@Test(priority = 215, enabled = false, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_20_21(String selectSearchOption,String searchVal1, String searchVal2) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_20_21() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			String multipleSearchValues = searchVal1 + ", " + searchVal2;
			homePage.enterSearchCriteria(multipleSearchValues);
			homePage.clickOnRunSearchButton();
			assertTrue(searchResultsPage.getPagesCount() > 0);
			assertTrue(statspage.successNumberDisplay());
			scenario = "You should see a red number on the 'Stats' tab noting a not found value.";
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertTrue(statspage.getNotFoundStatCount() > 0, scenario);
			statspage.verifyStatNotFoundResults();
			assertEquals(statspage.getNotFoundStatCount(), statspage.getStatPageNotFoundCount(), scenario);
			assertTrue(statspage.verifyNotFoundSearchItem(multipleSearchValues), scenario);
			scenario = "You should see a green number on the 'Stats' tab noting a mached value.";
			assertTrue(statspage.notFoundNumberDisplay());
			assertTrue(statspage.verifyStatsButton(), scenario);
			assertTrue(statspage.getSuccessStatCount() > 0, scenario);
			statspage.clickStatMatchedResultsButton();
			assertEquals(statspage.getSuccessStatCount(),statspage.getStatPageMatchCount(), scenario);
			assertTrue(statspage.verifySearchValues(multipleSearchValues),scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_20_21() ");
	}

	@Test(priority = 220, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_22_0(String selectSearchOption, String searchData, String sortColumnName, 
									 String sortType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_22_0() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			scenario = "the 'earliest start and end date' in header of page matches what shows in the sort as the earliest date";
			searchResultsPage.sortRequiredColumn(sortColumnName, sortType);
			//assertFalse(searchResultsPage.columnAscDateSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
			assertTrue(searchResultsPage.columnDescDateSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_22_0() ");
	}

	@Test(priority = 225, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_22_1(String selectSearchOption, String searchData, String sortColumnName, 
									 String sortType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_22_1() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			scenario = "the 'earliest start and end date' in header of page matches what shows in the sort as the earliest date";
			searchResultsPage.sortRequiredColumn(sortColumnName, sortType);
			// ToDo - Comparing the results after sorting - already done for
			// frozen column - need to use the same code once I tested it.
			// (columnAscDateSortCompare is a common Logic to compare date)
			assertTrue(searchResultsPage.columnAscDateSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
			//assertFalse(searchResultsPage.columnDescDateSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_22_1() ");
	}

	@Test(priority = 230, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_22_2(String selectSearchOption,String searchData, String sortColumnName, String sortType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_22_2() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			scenario = "the 'String Values' in header of page matches what shows in the sort desc";
			searchResultsPage.sortRequiredColumn(sortColumnName, sortType);
			// ToDo - Comparing the results after sorting - already done for
			// frozen column - need to use the same code once I tested it.
			// (columnAscDateSortCompare is a common Logic to compare date)
			assertFalse(searchResultsPage.columnAscStringSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
			assertTrue(searchResultsPage.columnDescStringSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_22_2()");
	}

	@Test(priority = 235, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void SearchAndFilter_22_3(String selectSearchOption,String searchData, String sortColumnName, 
									 String sortType) throws Exception {
		System.out.println("Started Test Case - SearchAndFilter_22_3()");
		try {
			homePage.clickOnResetButton();
			homePage.clickFilterDropDown();
			homePage.selectingSearchOption(selectSearchOption);
			homePage.enterSearchCriteria(searchData);
			homePage.clickOnRunSearchButton();
			scenario = "the 'String Values' in header of page matches what shows in the sort asc";
			searchResultsPage.sortRequiredColumn(sortColumnName, sortType);
			// ToDo - Comparing the results after sorting - already done for
			// frozen column - need to use the same code once I tested it.
			// (columnAscDateSortCompare is a common Logic to compare date)
			assertTrue(searchResultsPage.columnAscStringSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
			assertFalse(searchResultsPage.columnDescStringSortCompare(searchResultsPage.GetColumnValues(sortColumnName)), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - SearchAndFilter_22_3() ");
	}

	@Test(priority = 240, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_1_10(String selectSearchOption,
										  String searchData, String hideColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_1_10() ");
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
			// assertTrue(colHideOrFreeze.verifyHide(hideColumnName), scenario);
			// - ToDo - yet to work on this commented line
			colHideOrFreeze.clickApply();
			assertFalse(searchResultsPage.verifyColumnHeaderAvailability(hideColumnName), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case - LineAndDetailDisplay_1_10() ");
	}

	@Test(priority = 245, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_1_11(String unHideColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_1_11() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickUnHideAll();
			colHideOrFreeze.clickUnfreezeAll();
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
		System.out.println("Completed Test Case - LineAndDetailDisplay_1_11() ");
	}

	@Test(priority = 250, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_1_12(String selectSearchOption,
			String searchData, String freezeColumnName) throws Exception {
		System.out.println("Started Test Case - LineAndDetailDisplay_1_12() ");
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
		System.out.println("Completed Test Case - LineAndDetailDisplay_1_12() ");
	}

	@Test(priority = 255, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_1_13(String unFreezeColumnName) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_1_13() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickUnHideAll();
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
		System.out.println("Completed Test Case -  LineAndDetailDisplay_1_13() ");
	}

	@Test(priority = 260, enabled = true, dataProvider = "getData", dataProviderClass = GetDataProvider.class)
	public void LineAndDetailDisplay_1_14(String selectSearchOption, String searchData, String freezeDateTypeColumnName,
										  String sortType) throws Exception {
		System.out.println("Started Test Case -  LineAndDetailDisplay_1_14() ");
		try {
			homePage.clickOnResetButton();
			homePage.clickOnRunSearchButton();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickUnHideAll();
			colHideOrFreeze.clickUnfreezeAll();
			colHideOrFreeze.clickApply();
			scenario = "the 'earliest start and end date' in header of page matches what shows in the sort as the earliest date";
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.freezeOrUnFreeze(freezeDateTypeColumnName);
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon();
			colHideOrFreeze.clickApply();
			colHideOrFreeze.clickOnColumnHideOrFreezeIcon(); //without this script is getting failed
			colHideOrFreeze.sortFrozenCol(freezeDateTypeColumnName, sortType);
			assertTrue(searchResultsPage.columnDescDateSortCompare(colHideOrFreeze.freezeColumnValues(freezeDateTypeColumnName)), scenario);
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
		System.out.println("Completed Test Case -  LineAndDetailDisplay_1_14() ");
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
