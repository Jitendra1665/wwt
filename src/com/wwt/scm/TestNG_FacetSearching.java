/*****************************************************
 * Purpose: TestNG file for Facet searching Test cases
 * Created by: Srinivas/Jitendra
 * Created on: Aug-21-2013
 * Last Modified on: Sep-26-2013
 *****************************************************/
package com.wwt.scm;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestNG_FacetSearching {
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
	CustomerFacet custFacet = null;
	SearchResults searchResultsPage = null;
	ContractHeader contractHeaderPage = null;
	SalesChannelFacet salesChannelFacet = null;
	InstallSiteCustomerFacet siteCustomerFacet=null;
	AddressFacet addressCustFacet=null;
	DateSelectionFacet datesearchFacet=null;
	LineStatusFacet linestatussearchFacet=null;

	@BeforeMethod(enabled=true)
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
				BROWSER = scriptproperties.getProperty("TestNG_FacetSearching_BROWSERTYPE");
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
				/* Assigning to page objects to Page classes*/
				homePage = PageFactory.initElements(driver,HomePage.class);
				custFacet = PageFactory.initElements(driver,CustomerFacet.class);
				searchResultsPage = PageFactory.initElements(driver,SearchResults.class);
				salesChannelFacet = PageFactory.initElements(driver, SalesChannelFacet.class);
				siteCustomerFacet = PageFactory.initElements(driver, InstallSiteCustomerFacet.class);
				addressCustFacet = PageFactory.initElements(driver, AddressFacet.class);
				datesearchFacet = PageFactory.initElements(driver, DateSelectionFacet.class);
				linestatussearchFacet = PageFactory.initElements(driver, LineStatusFacet.class);	
				/* logging into application */				
				logIn = PageFactory.initElements(driver,LoginPage.class);				
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
	
	@AfterClass
	public void afterTest() throws IOException{
		if(inputdata_filetype.equals("CSV")){
			GetDataProvider.resultUpdate(addResults);
		}
		driver.quit();
	}
	
	@Test(enabled=true, priority=5, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_9_0(String FACET_SEARCH_CUSTNAME) throws Exception{
		try{
			int facetrecordcount=0,searchrecordcount=0;
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(FACET_SEARCH_CUSTNAME);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			facetrecordcount = custFacet.getRecordcountfromCustomerFacetSearch();
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();
			homePage.clickOnRunSearchButton();
			searchrecordcount = searchResultsPage.totalSearchResultsCount();
			//Assert.assertEquals(searchrecordcount,facetrecordcount,"Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
				test_status = false;
				err_msg = a.getMessage();
		} finally{
				methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
				updateResults();
		}
	}
	@Test(enabled=true, priority=6, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void SearchAndFilter_9_1(String FACET_SEARCH_CUSTNAME) throws Exception{
		try{
			int facetrecordcount=0,searchrecordcount=0;
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(FACET_SEARCH_CUSTNAME);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			facetrecordcount = custFacet.getRecordcountfromCustomerFacetSearch();
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();
			homePage.clickOnRunSearchButton();
			searchrecordcount = searchResultsPage.totalSearchResultsCount();
			//Assert.assertEquals(searchrecordcount,facetrecordcount, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	
	@Test(enabled=true, priority=10, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_10_0(String FECET_SALESCHANNEL_NAME) throws Exception{
		try{
			int searchrecordcount,facetrecordcount;
			salesChannelFacet.clicksalesChannelIcon();
			salesChannelFacet.enterSalesChannelName(FECET_SALESCHANNEL_NAME);
			Assert.assertTrue(salesChannelFacet.verifyFacetSearchResult(),"Failed to validate Search facet display");
			facetrecordcount = salesChannelFacet.getRecordcountfromSalesChannelFacetSearch();
			Assert.assertTrue(salesChannelFacet.addsalesChanneltoSearch(), "Failed to add Sales Channel names to search");
			salesChannelFacet.closeSalesChannelFacet();
			homePage.clickOnRunSearchButton();
			searchrecordcount = searchResultsPage.totalSearchResultsCount();	
			//Assert.assertEquals(searchrecordcount,facetrecordcount, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();			
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	
	@Test(enabled=true, priority=11, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_10_1(String FECET_SALESCHANNEL_NAME) throws Exception{
		try{
			int searchrecordcount,facetrecordcount;
			salesChannelFacet.clicksalesChannelIcon();
			salesChannelFacet.enterSalesChannelName(FECET_SALESCHANNEL_NAME);
			Assert.assertTrue(salesChannelFacet.verifyFacetSearchResult(),"Failed to validate Search facet display");
			facetrecordcount = salesChannelFacet.getRecordcountfromSalesChannelFacetSearch();
			Assert.assertTrue(salesChannelFacet.addsalesChanneltoSearch(),"Failed to add Sales Channel names to search");
			salesChannelFacet.closeSalesChannelFacet();
			homePage.clickOnRunSearchButton();
			searchrecordcount = searchResultsPage.totalSearchResultsCount();	
			//Assert.assertEquals(searchrecordcount, facetrecordcount, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();			
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=true,priority=15,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_11_0(String FACET_INSTALLSITE_CUSTNAME) throws Exception{
		try{
			int inscustreccountfacet=0, insreccountserach=0;
			siteCustomerFacet.clickInstallSiteCustomerIcon();		
			siteCustomerFacet.enterinstallsiteCustName(FACET_INSTALLSITE_CUSTNAME);
			Assert.assertTrue(siteCustomerFacet.verifyInstallsiteFacetSearchResult(), "Failed to validate Search facet display");
			inscustreccountfacet = siteCustomerFacet.getRecordcountfromInstallSiteFacetSearch();
			Assert.assertTrue(siteCustomerFacet.addInstallSiteCusttoSearch(),"Failed to add install site customers to Search");
			siteCustomerFacet.closeInstallSiteCustFacet();
			homePage.clickOnRunSearchButton();	
			insreccountserach = searchResultsPage.totalSearchResultsCount();		
			//Assert.assertEquals(insreccountserach, inscustreccountfacet, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	
	@Test(enabled=true,priority=16,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_11_1(String FACET_INSTALLSITE_CUSTNAME) throws Exception{
		try{	
				int inscustreccountfacet=0, insreccountserach=0;
				siteCustomerFacet.clickInstallSiteCustomerIcon();		
				siteCustomerFacet.enterinstallsiteCustName(FACET_INSTALLSITE_CUSTNAME);
				Assert.assertTrue(siteCustomerFacet.verifyInstallsiteFacetSearchResult(), "Failed to validate Search facet display");
				inscustreccountfacet = siteCustomerFacet.getRecordcountfromInstallSiteFacetSearch();
				Assert.assertTrue(siteCustomerFacet.addInstallSiteCusttoSearch(),"Failed to add install site customers to Search");
				siteCustomerFacet.closeInstallSiteCustFacet();
				homePage.clickOnRunSearchButton();	
				insreccountserach = searchResultsPage.totalSearchResultsCount();		
				//Assert.assertEquals(insreccountserach, inscustreccountfacet, "Testcase failed as no.of records are mismatched");
				homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		
	@Test(enabled=true,priority=20,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_0(String FACET_CUSTADDRESS) throws Exception{
		try{	
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			addressreccountserach = searchResultsPage.totalSearchResultsCount();			
			//Assert.assertEquals(addressreccountserach, adressreccountfacet, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=true,priority=21,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_1(String FACET_CUSTADDRESS) throws Exception{
		try{	
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			addressreccountserach = searchResultsPage.totalSearchResultsCount();			
			//Assert.assertEquals(addressreccountserach, adressreccountfacet, "Testcase failed as no.of records are mismatched");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=25,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_2(String FACET_CUSTADDRESS_STREETVALUE) throws Exception{
		try{	
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_STREETVALUE);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();		
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("Site Address", FACET_CUSTADDRESS_STREETVALUE);
			//Assert.assertEquals( addressreccountserach,adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=30,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_3(String FACET_CUSTADDRESS_STREETNAME) throws Exception{
		try{		
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_STREETNAME);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();			
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("Site Name", FACET_CUSTADDRESS_STREETNAME);
			//Assert.assertEquals(addressreccountserach, adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		
	@Test(enabled=true,priority=35,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_4(String FACET_CUSTADDRESS_CITYNAME) throws Exception{
		try{
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_CITYNAME);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("City", FACET_CUSTADDRESS_CITYNAME);			
			//Assert.assertEquals(addressreccountserach,adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=40,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_5(String FACET_CUSTADDRESS_STATENAME) throws Exception{
		try{	
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_STATENAME);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();	
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("State", FACET_CUSTADDRESS_STATENAME);
			//Assert.assertEquals( addressreccountserach,adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=45,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_6(String FACET_CUSTADDRESS_ZIPCODE) throws Exception{
		try{
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_ZIPCODE);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();		
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("Zip", FACET_CUSTADDRESS_ZIPCODE);
			//Assert.assertEquals(addressreccountserach,adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=50,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_12_7(String FACET_CUSTADDRESS_COUNTRY) throws Exception{
		try{
			int adressreccountfacet=0, addressreccountserach=0;
			addressCustFacet.clickAddressIcon();		
			addressCustFacet.enterAddress(FACET_CUSTADDRESS_COUNTRY);
			Assert.assertTrue(addressCustFacet.verifyAddressFacetSearchResult(), "Failed to validate Search facet display");
			adressreccountfacet = addressCustFacet.getRecordcountfromAddressFacetSearch();
			Assert.assertTrue(addressCustFacet.addAddresstoSearch(), "Failed to add address list to Search");			
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();	
			//addressreccountserach = searchResultsPage.totalSearchResultsCount();	
			addressreccountserach = searchResultsPage.getResultcountAfterColumnDataCheck("Country", FACET_CUSTADDRESS_COUNTRY);
			//Assert.assertEquals(addressreccountserach,adressreccountfacet, "Testcase failed as no.of records/data validation mismatched.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	
	
	@Test(enabled=true,priority=55,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_0(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{	
			datesearchFacet.clickDateSelectionFacet();
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);
			datesearchFacet.Dateset("Maintenance PO Order Date", "from", fromDate);
			datesearchFacet.Dateset("Maintenance PO Order Date", "to", toDate);
			datesearchFacet.Dateset("Product PO Order Date", "from", fromDate);
			datesearchFacet.Dateset("Product PO Order Date", "to", toDate);	
			datesearchFacet.Dateset("Product PO Ship Date", "from", fromDate);
			datesearchFacet.Dateset("Product PO Ship Date", "to", toDate);				
			datesearchFacet.Dateset("End Date", "from", fromDate);
			datesearchFacet.Dateset("End Date", "to", toDate);				
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=true,priority=60,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_1(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{	
			datesearchFacet.clickDateSelectionFacet();	
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);	
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.validateDateRange("Line Creation Date", fromDate,toDate), "Date validation fails.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	
	@Test(enabled=false)
	public void SearchAndFilter_13_2(){
		//to do - Creation Date Search Logic for Parents/Children
	}
	
	@Test(enabled=true,priority=65,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_3(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{	
			datesearchFacet.clickDateSelectionFacet();	
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);				
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.validateDateRange("Maintenance PO Order Date", fromDate,toDate), "Date validation fails.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=false)
	public void SearchAndFilter_13_4(){
		//to do - Creation Date Search Logic for Parents/Children
	}	
	@Test(enabled=true,priority=70,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_5(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{
			datesearchFacet.clickDateSelectionFacet();	
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);			
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.validateDateRange("Product PO Order Date", fromDate,toDate), "Date validation fails.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=false)
	public void SearchAndFilter_13_6(){
		//to do - Creation Date Search Logic for Parents/Children
	}	
	@Test(enabled=true,priority=75,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_7(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{	
			datesearchFacet.clickDateSelectionFacet();
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);				
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.validateDateRange("Product PO Ship Date", fromDate,toDate), "Date validation fails.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=false)
	public void SearchAndFilter_13_8(){
		//to do - Creation Date Search Logic for Parents/Children
	}	
	@Test(enabled=true,priority=80,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_13_9(String fromDateType, String fromDate, String toDateType, String toDate) throws Exception{
		try{	
			datesearchFacet.clickDateSelectionFacet();	
			datesearchFacet.Dateset(fromDateType, "from", fromDate);
			datesearchFacet.Dateset(toDateType, "to", toDate);				
			datesearchFacet.closeFilterDateDropDown();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.validateDateRange("End Date", fromDate,toDate), "Date validation fails.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=false)
	public void SearchAndFilter_13_10(){
		//to do - Creation Date Search Logic for Parents/Children
	}		
	@Test(enabled=true,priority=85,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_14_0(String CutomserName, String lineStatus,String lineStatusType) throws Exception{
		try{
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(CutomserName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();		
			homePage.clickOnRunSearchButton();
			Assert.assertFalse(searchResultsPage.verifyDataInColumn("Service Level Status", "Expired"), "Service Level Status data validation failed. Found expired in search results.");
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.verifyDataInColumn("Service Level Status", "Expired"), "Service Level Status data validation failed. Not Found expired in search results.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=true,priority=90,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_15_0(String CutomserName, String lineStatus,String lineStatusType) throws Exception{
		try{		
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(CutomserName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();		
			homePage.clickOnRunSearchButton();
			Assert.assertFalse(searchResultsPage.verifyDataInColumn("Service Level Status", "Expired"), "Service Level Status data validation failed. Found expired in search results.");
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			Assert.assertTrue(searchResultsPage.verifyDataInColumn("Service Level Status", "Terminated"), "Service Level Status data validation failed. Not Found expired in search results.");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true,priority=95,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_16_0(String lineStatus,String lineStatusType) throws Exception{
		try{
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Needs Review"), "Needs Review column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	
	@Test(enabled=true,priority=96,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void SearchAndFilter_16_1(String custName, String lineStatus,String lineStatusType) throws Exception{
		try{
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(custName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();	
			homePage.clickOnRunSearchButton();
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Needs Review"), "Needs Review column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	
	@Test(enabled=true,priority=97,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_17_0(String lineStatus,String lineStatusType) throws Exception{
		try{	
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Published"), "Published column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	
	@Test(enabled=true,priority=97,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void SearchAndFilter_17_1(String custName, String lineStatus,String lineStatusType) throws Exception{
		try{		
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(custName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();	
			homePage.clickOnRunSearchButton();
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Published"), "Published column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		
	
	@Test(enabled=true,priority=100,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_18_0(String lineStatus,String lineStatusType) throws Exception{
		try{	
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Not Published"), "Not Published column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	
	@Test(enabled=true,priority=101,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void SearchAndFilter_18_1(String custName, String lineStatus,String lineStatusType) throws Exception{
		try{			
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(custName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();	
			homePage.clickOnRunSearchButton();
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyRecodcountInContractTab("Not Published"), "Not Publised column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		  
	@Test(enabled=true,priority=105,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void  SearchAndFilter_19_0(String lineStatus,String lineStatusType) throws Exception {
		try{		
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyEndOfSuppoertDateinContractTab("Earliest End Date"), "Earliest End Date column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");;
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		
	@Test(enabled=true,priority=106,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void SearchAndFilter_19_1(String custName, String lineStatus,String lineStatusType) throws Exception{
		try{				
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(custName);
			Assert.assertTrue(custFacet.verifyCustomerFacetSearchResult(), "Failed to validate Search facet display");
			Assert.assertTrue(custFacet.addCustomerstoSearch(), "Failed to add customers list to search");
			custFacet.closeCustomerFacet();	
			homePage.clickOnRunSearchButton();
			linestatussearchFacet.clickLineStatusIcon();
			Assert.assertTrue(linestatussearchFacet.verifyingGivenlineStatus(lineStatus),"Given Line type Not available");
			linestatussearchFacet.selectLineCheckbox(lineStatusType);
			linestatussearchFacet.closeLinsestatusFacet();
			homePage.clickOnRunSearchButton();	
			searchResultsPage.selectSearchResultType("Contract Results");
			//Assert.assertTrue(searchResultsPage.verifyEndOfSuppoertDateinContractTab("Earliest End Date"), "Earliest End Date column data validation failed");
			searchResultsPage.selectSearchResultType("Line Results");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
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