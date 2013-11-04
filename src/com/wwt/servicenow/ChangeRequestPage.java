package com.wwt.servicenow;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class ChangeRequestPage {
	protected WebDriver driver;
	
	@FindBy(css="iframe[id='gsft_main']")
	private WebElement mframe;

	@FindBy(css="input[id='sys_display.change_request.requested_by']")
	private WebElement crRequestedBy;
	
	@FindBy(css="input[id='sys_display.change_request.cmdb_ci']")
	private WebElement crConfItem;
	
	@FindBy(css="select[id='change_request.priority']")
	private WebElement crPriority;
	
	@FindBy(css="input[id='change_request.short_description']")
	private WebElement crShortDescription;	

	@FindBy(css="select[id='change_request.approval']")
	private WebElement crApproval;	
	
	@FindBy(css="select[id='change_request.state']")
	private WebElement crState;	
	
	@FindBy(css="select[id='change_request.category']")
	private WebElement crCatagory;	
	
	@FindBy(css="input[id='sys_display.change_request.assignment_group']")
	private WebElement crAssignGroup;
	
	@FindBy(css="input[id='sys_display.change_request.assigned_to']")
	private WebElement crAssignedTo;
	
	@FindBy(css="input[id='sys_display.change_request.u_project']")
	private WebElement crProject;
	
	@FindBy(css="input[id='sys_display.change_request.u_proj_task']")
	private WebElement crProjectTask;
	
	@FindBy(css="input[id='sys_display.change_request.u_dept_pl']")
	private WebElement crDeptPL;
	
	@FindBy(css="textarea[id='change_request.work_notes']")
	private WebElement crWorkNotes;
	
	@FindBy(css="textarea[id='change_request.comments']")
	private WebElement crAdditionalComments;
	
	@FindBy(css="textarea[id='change_request.justification']")
	private WebElement crJustification;
	
	@FindBy(css="select[id='change_request.u_business_impact']")
	private WebElement crBusinessimpact;
	
	@FindBy(css="select[id='change_request.u_service_outage']")
	private WebElement crServiceOutage;

	@FindBy(css="select[id='change_request.u_resource_requirement']")
	private WebElement crResourceRequirement;
	
	@FindBy(css="select[id='change_request.u_implementation_strategy']")
	private WebElement crImplStrategy;

	@FindBy(css="input[id='change_request.requested_by_date']")
	private WebElement crReqByDate;
	
	@FindBy(css="select[id='change_request.u_release_type']")
	private WebElement crReleaseType;
	
	@FindBy(css="input[id='sys_display.change_request.u_ad_hoc_approver']")
	private WebElement crAdhocApprover;

	@FindBy(css="input[id='change_request.u_proposed_release_date']")
	private WebElement crReleaseDate;
	
	@FindBy(css="input[id='change_request.u_proposed_int_release_date']")
	private WebElement crINTReleaseDate;
	
	@FindBy(css="input[id='change_request.u_proposed_dev_release_date']")
	private WebElement crDevReleaseDate;
	
	@FindBy(css="textarea[id='change_request.change_plan']")
	private WebElement crChangePlan;
	
	@FindBy(css="textarea[id='change_request.backout_plan']")
	private WebElement crBackoutPlan;
	
	@FindBy(css="textarea[id='change_request.test_plan']")
	private WebElement crTestPlan;
	
	@FindBy(css="button[id='sysverb_insert']")
	private WebElement crSubmit;
	
	@FindBy(css="button[id='Save']")
	private WebElement crSave;
	
	@FindBy(css="input[name='check_change_request.sysapproval_approver.sysapproval']")
	private WebElement requestlectCheckbox;
	
	@FindBy(css="input[id='allcheck_change_request.sysapproval_approver.sysapproval']")
	private WebElement actionselectCheckbox;
	
	@FindBy(css="select[class^='list_action_option'][title*='Actions on selected rows']")
	private WebElement approveListbox;
	
	@FindBy(css="select[class^='list_action_option'][title*='Actions on selected rows'] option")
	private List<WebElement> approveList;	
	
	@FindBy(css="td[class='vt'][style='background-color: lightgreen'] a" )
	private WebElement approvedisplayColumn;
	
	@FindBy(css="select[class*='list_action_option'][title='Actions on selected rows...']")
	private WebElement approveDropDown;
	
	@FindBy(css="select[class*='list_action_option'][title='Actions on selected rows...'] option[action_name*='Approve_Edit']")
	private WebElement approveOption;
	
	@FindBy(css="table[class*='wide'] tbody tr[class='header'] td[class='column_head'][align='right']")
	private WebElement colHeader;
	
	@FindBy(css="div[id*='context'][class='context_menu'] div[class='context_item'][title='Create a deployment list for the change request'] ")
	private WebElement createDeploymentContextMenuList;
		
	@FindBy(css="div[id='u_deployment_object.form_scroll'] form[id='u_deployment_object.do'] button[id='sysverb_insert'][class*='form_action_button']")
	private WebElement submitDeploymentObject;

	@FindBy(css="div table[id='list_nav_u_deployment.u_deployment_object.u_deployment'][class*='list_nav'] tbody tr[class*='list_nav'] td[class*='list_nav_top'] span button[id='sysverb_new'][class*='selected_action']")
	private WebElement buttonNewdeploymentObjects;
		
    @FindBy(css="input[id='u_deployment_object.u_name']")
    private WebElement depName;
    
    @FindBy(css="input[id='sys_display.u_deployment_object.u_configuration_item']")
    private WebElement depConfigItem;   
    
    @FindBy(css="input[id='u_deployment_object.u_version']")
    private WebElement depVersion;          
           
    @FindBy(css="input[id='sys_display.u_deployment_object.u_type']")
    private WebElement depType;       
                  
    @FindBy(css="input[id='u_deployment_object.u_order']")
    private WebElement depOrder;
    
    @FindBy(css="input[id='u_deployment_object.u_path']")
    private WebElement depPath;
    
    @FindBy(css="textarea[id='u_deployment_object.u_notes']")
    private WebElement depNotes;
    
    @FindBy(css="select[id='u_deployment_object.u_deployment_time']")
    private WebElement depTime;              
         
    @FindBy(css="input[id='ni.u_deployment_object.u_apply_in_sand']")
    private WebElement depApplyinSand; 

    @FindBy(css="input[id='sys_display.u_deployment_object.u_deployment_group']")
    private WebElement depGroup;    
    
    @FindBy(css="img[id='view.u_deployment_object.u_deployment_group'][name='view.u_deployment_object.u_deployment_group']")
    private WebElement depGroupSearchImage; 
        
    @FindBy(css="select[id='sys_readonly.u_deployment_object.u_type.u_deployment_method'][disabled='true'] option")
    private List<WebElement> depMethod;      
	
	public ChangeRequestPage(WebDriver driver) {
			this.driver = driver;
	}	
	
	public void switchFrame() throws InterruptedException{
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(mframe);
	}
	public void enterRequestedBy(String REQUESTEDBY){
		crRequestedBy.sendKeys(REQUESTEDBY);
	}
	
	public void enterConfItem(String CONFITEM){
		crConfItem.sendKeys(CONFITEM);
	}
	
	public void selectPriority(String PRIORITY){
		crPriority.sendKeys(PRIORITY);
	}	
	
	public void enterShortDescription(String SDESC){
		crShortDescription.sendKeys(SDESC);
	}	
	
	public void selectApproval(String APPROVAL){
		crApproval.sendKeys(APPROVAL);
	}	
	
	public void selectCRState(String CRSTATE){
		crState.sendKeys(CRSTATE);
	}		
	
	public void selectCatagory(String CATAGORY){
		crCatagory.sendKeys(CATAGORY);
	}	
	
	public void enterAssignGroup(String ASNGROUP){
		crAssignGroup.sendKeys(ASNGROUP);
	}	
	
	public void enterAssignedTo(String ASGNTO){
		crAssignedTo.sendKeys(ASGNTO);
	}		

	public void enterProject(String PROJECT) throws InterruptedException{
		crProject.sendKeys(PROJECT);
		crDeptPL.click();
		driver.findElement(By.xpath("//*[@id='status.change_request.u_project']")).click();
		Thread.sleep(2000);
	}
	
	public void enterProjectTask(String PROJECTTASK){
		crProjectTask.sendKeys(PROJECTTASK);
	}	
		
	public void enterDeptPL(String DEPT){
		crDeptPL.sendKeys(DEPT);
	}
	
	public void enterWorkNotes(String WNOTES){
		crWorkNotes.sendKeys(WNOTES);
	}
	
	public void enterAddlComments(String ADDLCOMMENTS){
		crAdditionalComments.sendKeys(ADDLCOMMENTS);
	}	

	public void enterJustification(String JUSTIFICATION){
		crJustification.sendKeys(JUSTIFICATION);
	}
	public void selectBusiImpact(String BUSIIMPACT){
		crBusinessimpact.sendKeys(BUSIIMPACT);
	}	
	public void selectServiceOutage(String SRVOUTAGE){
		crServiceOutage.sendKeys(SRVOUTAGE);
	}	
	
	public void selectResourceReq(String RESRCREQ){
		crResourceRequirement.sendKeys(RESRCREQ);
	}	
	
	public void selectImplStrategy(String IMPLSTRGY){
		
		crImplStrategy.sendKeys(IMPLSTRGY);
	}	
	
	public void enterReqByDate(String REQBYDate){
		crReqByDate.sendKeys(REQBYDate);
	}	
	
	public void selectRelType(String RELTYPE) throws InterruptedException{
		crReleaseType.sendKeys(RELTYPE);
		crChangePlan.click();
		Thread.sleep(3000);
	}	
	
	public void enterAdhocApprover(String AHAPPROVER) throws InterruptedException{
		crAdhocApprover.click();
		crAdhocApprover.sendKeys(AHAPPROVER);
		crChangePlan.click();
		Thread.sleep(4000);
	}
	public void enterRelDate(String RELDATE){
		
			crReleaseDate.sendKeys(RELDATE);
	}
	
	public void enterDEVRelDate(String DEVRELDATE){
		crDevReleaseDate.sendKeys(DEVRELDATE);
	}	

	public void enterChangePlan(String CRPLAN){	
		crChangePlan.sendKeys(CRPLAN);
	}	
	public void enterBackOutPlan(String BKOUTPLAN){
		crBackoutPlan.sendKeys(BKOUTPLAN);
	}	
	public void enterTestPlan(String TESTPLAN){
		
		crTestPlan.sendKeys(TESTPLAN);
	}	
	public void clickSubmit(){
		crSubmit.click();
	}	
	
	public void clickSave() throws InterruptedException{
		crSave.click();
		Thread.sleep(3000);
	}	
	
	public boolean approveCR() throws InterruptedException{
		Thread.sleep(5000);
		requestlectCheckbox.click();
		Thread.sleep(2000);
		for(int i=0; i<approveList.size();i++){
			if (approveList.get(i).getText().contains("Approve")){
				approveList.get(i).click();
				break;
			}
		}
		if(approvedisplayColumn.getText().contains("Approved")){
			return true;
		}
		return false;
	}
	
	
	// move to Column Header
	public void moveToColHeader() throws InterruptedException{
		Thread.sleep(5000);
		Actions mouseMoveToColHeader = new Actions(driver);
		mouseMoveToColHeader.moveToElement(colHeader).perform();
		mouseMoveToColHeader.contextClick(colHeader).perform();
	}
	
	public void clickCreateDeployment() throws InterruptedException{
		createDeploymentContextMenuList.click();
		Thread.sleep(5000);
	}
	
	public void clickNewDeployObjects() throws InterruptedException{
		buttonNewdeploymentObjects.click();
		Thread.sleep(5000);
	}
	
	public void enterDepName(String DEPNAME){
		depName.sendKeys(DEPNAME);
	}
	
	public void enterConfigurationItem(String DEPCONFITEM){
		depConfigItem.sendKeys(DEPCONFITEM);
	}
	
	public void enterVersion(String DEPVERSION){
		depVersion.sendKeys(DEPVERSION);
	}
	
	public void enterType(String DEPTYPE){
		depType.sendKeys(DEPTYPE);
	}
	
	public void enterOrder(String DEPORDER){
		depOrder.sendKeys(DEPORDER);
	}
	
	public void enterPath(String DEPPATH){
		depPath.sendKeys(DEPPATH);
	}
	
	public void enterNotes(String DEPNOTES){
		depNotes.sendKeys(DEPNOTES);
	}
	
	public void enterDeploymentTime(String DEPTIME){
		depTime.sendKeys(DEPTIME);
	}
	
	public boolean verifyApplyInSand(){
		if(depApplyinSand.isSelected()){
			return true;
		}
		return false;	
	}
	
	public boolean verifyDeployGroup(){
		if(!depGroup.isEnabled()){
			if(depGroupSearchImage.isDisplayed()){
				return true;
			}
		}
		return false;
	}
	
	public boolean verifyDeployMethod(){
		
		for(WebElement depMethodList : depMethod){
			if("Manual Automated".contains(depMethodList.getText().trim())){
				return true;
			}
		}
		return false;
	}
	
	public void submitDeployForm() throws InterruptedException{
			Thread.sleep(5000);
			submitDeploymentObject.click();
		}
	
}

