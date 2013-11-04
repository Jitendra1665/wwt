/*****************************************************
 * Class Name: CommonUtilLibrary
 * Class Purpose: Common Utility functions
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class CommonUtilLibrary {
	
	/* Method to read count from facet line with the pattern */
    public static int readcountfromfacetline(String displayedChannel){
    	String recordnum=""; 
	    Pattern p =  Pattern.compile("\\((.*?)\\)",Pattern.DOTALL);
		Matcher matcher = p.matcher(displayedChannel.trim());
		while(matcher.find()){
		   	recordnum = matcher.group(1);
		   	recordnum = recordnum.replace(",","");  
		}
    	if(recordnum.equalsIgnoreCase("") ){
    		recordnum = "0";
    	}
	   	 return (new Integer(recordnum));   
    }
    
    /* Method to capture screenshot */
    public static void screenCapture(WebDriver driver, String screenShotName) throws IOException{

    	if (driver!=null){
        	if(driver.getClass().toString().contains("remote")){
        		File scrFile = (((TakesScreenshot) new Augmenter().augment(driver)).getScreenshotAs(OutputType.FILE));
        		FileUtils.copyFile(scrFile, new File(".//ScreenShots//"+screenShotName+".png"));
        	}else{
        		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        		FileUtils.copyFile(scrFile, new File(".//ScreenShots//"+screenShotName+".png"));
        	}
    	}
    }
}
