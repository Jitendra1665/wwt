/*****************************************************
 * Class Name: GetInputData
 * Class Purpose: data Provider class
 * Created by:Jitendra/ Srinivas
 *****************************************************/
package com.wwt.scm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.DataProvider;

public class GetInputData {

	@DataProvider(name="getData")
	public static String[][] readInputDataExcel(Method m){
		try{
			int rowCount=0, yrowcount=0,ycolcount=0;
			String exceltestcasename=null, execStatus=null, params=null;
			String testcasename = m.getName();
			//Loading Properties file
			FileInputStream ConfigFile = new FileInputStream(".\\scm.properties");
			Properties propertiesObj = new Properties();
			propertiesObj.load(ConfigFile);
			// Finding Row Count of input data sheet
			FileInputStream iStream = new FileInputStream(".\\" + propertiesObj.getProperty("INPUTDATA_WORKBOOKNAME"));
			Workbook workbook = WorkbookFactory.create(iStream);
			Sheet worksheet = workbook.getSheet(propertiesObj.getProperty("INPUTDATA_SHEETNAME"));
			rowCount = worksheet.getPhysicalNumberOfRows();
		    Row currentrow = worksheet.getRow(0);
		    // Reading the actual Row count and Column count for test case
		    for(int i=1; i< rowCount;i++){
		    	currentrow = worksheet.getRow(i);
		    	exceltestcasename = currentrow.getCell(0).toString();
		    	execStatus = currentrow.getCell(1).toString();
		    	// identifying no.of times the test cases need to be executed.
		    	if(exceltestcasename.equalsIgnoreCase(testcasename) && execStatus.equalsIgnoreCase("Y")){
		    		yrowcount = yrowcount+1;
		    		if (yrowcount==1){
		    			params = currentrow.getCell(2).toString();
		    			String[] myparams = params.split(",");
		    			ycolcount = myparams.length;
				    }
				 }
		    }
		    String[][] mydata = new String[yrowcount][ycolcount];
		    int datarow =0, datacol=0;
		    // Reading test case data into mydata array
		    for(int i=1; i< rowCount;i++){
		    	currentrow = worksheet.getRow(i);
		    	exceltestcasename = currentrow.getCell(0).toString();
		    	execStatus = currentrow.getCell(1).toString();
		    	if(exceltestcasename.equalsIgnoreCase(testcasename) && execStatus.equalsIgnoreCase("Y")){
		    		datarow = datarow+1;
		    		datacol=0;
		    		for(int j=3;j<(ycolcount+3);j++){
		    			  datacol=datacol+1;		    			
			    		  if(currentrow.getCell(j)!=null){
			    			  mydata[datarow-1][datacol-1] = currentrow.getCell(j).toString();
			    		  } else {
			    			  mydata[datarow-1][datacol-1] = "";
			    		  }    
			    	  }			    		
		    	}
		    }
		    iStream.close();
		    return mydata; 
			
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new String[0][0];
	} 
	
	public static void updateResultExcel(boolean status, String err_msg, String testcasename, String browsername){
		try{
			int rowCount=0;
			String exceltestcasename=null;
			boolean foundTestcase=false;
			//Loading Properties file
			FileInputStream ConfigFile = new FileInputStream(".\\scm.properties");
			Properties propertiesObj = new Properties();
			propertiesObj.load(ConfigFile);
			// Finding Row Count of input data sheet
			FileInputStream iStream = new FileInputStream(".\\" + propertiesObj.getProperty("INPUTDATA_WORKBOOKNAME"));
			Workbook workbook = WorkbookFactory.create(iStream);
			Sheet worksheet = workbook.getSheet(propertiesObj.getProperty("RESULT_SHEETNAME"));
			rowCount = worksheet.getLastRowNum();
		    Row currentrow = worksheet.getRow(0);
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Calendar cal = Calendar.getInstance();
		    for(int i=1; i< rowCount;i++){
		    	currentrow = worksheet.getRow(i);
		    	exceltestcasename = currentrow.getCell(0).toString();
		    	if(exceltestcasename.equalsIgnoreCase(testcasename)){
		    		foundTestcase = true;
		    		if(status){
		    			if(browsername.equalsIgnoreCase("IE")){
		    				currentrow.createCell(1).setCellValue("Pass");
		    			} else if(browsername.equalsIgnoreCase("FF")){
		    				currentrow.createCell(2).setCellValue("Pass");
		    			} else if(browsername.equalsIgnoreCase("GC")){
		    				currentrow.createCell(3).setCellValue("Pass");
		    			}
		    			currentrow.createCell(5).setCellValue("");
		    		} else{
		    			if(browsername.equalsIgnoreCase("IE")){
		    				currentrow.createCell(1).setCellValue("Fail");
		    			} else if(browsername.equalsIgnoreCase("FF")){
		    				currentrow.createCell(2).setCellValue("Fail");
		    			} else if(browsername.equalsIgnoreCase("GC")){
		    				currentrow.createCell(3).setCellValue("Fail");
		    			}		    			
		    			currentrow.createCell(5).setCellValue(err_msg);
		    		}
		    		currentrow.createCell(4).setCellValue(dateFormat.format(cal.getTime())+ " - " + browsername);
		    		break;
				 }
		    }
		    
		    if(!foundTestcase){
		    	rowCount++;
		    	currentrow = worksheet.createRow(rowCount); 			    	
		    	currentrow.createCell(0).setCellValue(testcasename);
	    		if(status){
	    			if(browsername.equalsIgnoreCase("IE")){
	    				currentrow.createCell(1).setCellValue("Pass");
	    			} else if(browsername.equalsIgnoreCase("FF")){
	    				currentrow.createCell(2).setCellValue("Pass");
	    			} else if(browsername.equalsIgnoreCase("GC")){
	    				currentrow.createCell(3).setCellValue("Pass");
	    			}
	    			currentrow.createCell(5).setCellValue("");
	    		} else{
	    			if(browsername.equalsIgnoreCase("IE")){
	    				currentrow.createCell(1).setCellValue("Fail");
	    			} else if(browsername.equalsIgnoreCase("FF")){
	    				currentrow.createCell(2).setCellValue("Fail");
	    			} else if(browsername.equalsIgnoreCase("GC")){
	    				currentrow.createCell(3).setCellValue("Fail");
	    			}		    			
	    			currentrow.createCell(5).setCellValue(err_msg);
	    		}
	    		currentrow.createCell(4).setCellValue(dateFormat.format(cal.getTime())+ " - " + browsername);
		    }
		  FileOutputStream oStream = new FileOutputStream(".\\" + propertiesObj.getProperty("INPUTDATA_WORKBOOKNAME"));  
		  workbook.write(oStream);
		  oStream.flush();
		  oStream.close();
		    
		 } catch(Exception e){
		 	e.printStackTrace();
		 }		
	}
}
