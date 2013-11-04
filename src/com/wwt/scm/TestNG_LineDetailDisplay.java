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

public class TestNG_LineDetailDisplay {

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
	InstallSiteCustomerFacet siteCustomerFacet=null;
	AddressFacet addressCustFacet=null;
	LineDetails LineDetailsPage=null;

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
				BROWSER = scriptproperties.getProperty("TestNG_LineDetailDisplay_BROWSERTYPE");
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
				siteCustomerFacet = PageFactory.initElements(driver, InstallSiteCustomerFacet.class);
				addressCustFacet = PageFactory.initElements(driver, AddressFacet.class);
				LineDetailsPage =PageFactory.initElements(driver, LineDetails.class); 
				logIn = PageFactory.initElements(driver,LoginPage.class);		
				/* logging into application */				
				logIn.ClickOnlogIn(userId, passWord);
			}else{
				homePage.clickOnResetButton();
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

	@Test(enabled=true, priority=5,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_0(String testString) throws Exception{
		try{
			Assert.assertTrue(homePage.validatehomepagedispay(), "failed to validate Homepage display");
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}

	@Test(enabled=true, priority=10,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_1(String custName) throws Exception{
		try{
			custFacet.clickCustomerIcon();
			custFacet.enterCustomerName(custName);	
			custFacet.addCustomerstoSearch();
			custFacet.closeCustomerFacet();
			homePage.clickOnRunSearchButton();
			LineDetailsPage.clickonFirstContractandValidate();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}		
	@Test(enabled=true, priority=15,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_2(String testSrring) throws Exception{
		try{
			homePage.validatehomepagedispay();
			homePage.clickOnRunSearchButton();			
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
	}
	@Test(enabled=true, priority=20,dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_4(String testSrring) throws Exception{
		try{
			homePage.clickOnRunSearchButton();			
			LineDetailsPage.clickonFirstContractandValidate();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}
	@Test(enabled=true, priority=25, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_6(String InstallsiteCustName) throws Exception{
		try{
			siteCustomerFacet.clickInstallSiteCustomerIcon();
			siteCustomerFacet.enterinstallsiteCustName(InstallsiteCustName);
			siteCustomerFacet.addInstallSiteCusttoSearch();
			siteCustomerFacet.closeInstallSiteCustFacet();		
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Site Name"), "Wordwrap validation failed");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	

	@Test(enabled=true, priority=30, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_7(String custAddress) throws Exception{
		try{
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custAddress);
			addressCustFacet.addAddresstoSearch();
			addressCustFacet.closeAddressFacet();
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Site Address"), "Wordwrap validation failed");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}
	}	
	@Test(enabled=true, priority=35, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_8(String custCity) throws Exception{
		try{
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custCity);
			addressCustFacet.addAddresstoSearch();
			addressCustFacet.closeAddressFacet();	
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("City"), "Wordwrap validation failed");
			homePage.clickOnResetButton();
		}catch(Throwable  a){
			test_status = false;
			err_msg = a.getMessage();
		} finally{
			methodname=Thread.currentThread().getStackTrace()[1].getMethodName();
			updateResults();
		}	
	}
	@Test(enabled=true, priority=40, dataProvider="getData", dataProviderClass=GetDataProvider.class)
	public void LineAndDetailDisplay_1_9(String custCountry) throws Exception{
		try{
			addressCustFacet.clickAddressIcon();
			addressCustFacet.enterAddress(custCountry);
			addressCustFacet.addAddresstoSearch();
			addressCustFacet.closeAddressFacet();	
			homePage.clickOnRunSearchButton();
			Assert.assertTrue(searchResultsPage.validateWordWrapping("Country"), "Wordwrap validation failed");
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
