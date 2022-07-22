package com.hrm.employee;

import org.testng.annotations.Test;

import commons.BasePage;
import commons.BaseTest;
import commons.GlobalConstants;
import pageObjects.AddEmployeePageObject;
import pageObjects.DashboardPageObject;
import pageObjects.EmployeeListPageObject;
import pageObjects.LoginPageObject;
import pageObjects.MyInfoPageObject;
import pageObjects.pageGenerator;
import utilities.DataUlti;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class Registration_And_Login extends BaseTest{
	String employeeID, statusValue;
	String empFirstName, empLastName, empUserName, empPassword, empFullName, editEmpFirstName, editEmpLastName;
	String adminUserName, adminPassword;
	
	String uploadFilesPath = GlobalConstants.getGlobalConstants().getUploadFile();
	String avatarFilePath = uploadFilesPath + "Avatar.jpg";
	
	@Parameters({"envName","serverName","browser", "ipAddress", "portNumber", "osName", "osVersion"})
	 @BeforeClass 
	  public void beforeClass(@Optional("local") String envName, @Optional("dev") String serverName, @Optional("chrome") String browserName, @Optional("localhost") String ipAddress, @Optional("4444") String portNumber, @Optional("Windows") String osName, @Optional("10")String osVersion) {
		log.info("PreCondition - Step 01: Open browser and navigate to '"+ envName + "'");
		driver = getBrowserDriver(envName, serverName, browserName, ipAddress, portNumber, osName, osVersion); 
		driver.manage().window().maximize();
		loginPage = pageGenerator.getLoginPage(driver);
		fakeData = DataUlti.getData();
		
		adminUserName = "Admin";
		adminPassword = "admin123";
		
		statusValue = "Enabled";
		empFirstName = fakeData.getFirstName();
		empLastName = fakeData.getLastName();
		empUserName = fakeData.getUserName();
		empPassword = "12345678";
		empFullName = empFirstName +" "+ empLastName;
		
		editEmpFirstName = fakeData.getEditFirstName();
		editEmpLastName = fakeData.getEditLastName();
		
		log.info("Pre-Condition - Step 01: Login with Admin role");
		dashboardPage = loginPage.loginToSystem(driver, adminUserName, adminPassword);
	 }

 @Test
  public void Employee_01_Add_New_Employee() {
	 log.info("Add_New_01 - Step 01: Open Employee List page");
	 dashboardPage.openSubMenuPage(driver, "PIM", "Employee List");
	 employeeListPage = pageGenerator.getEmployeeListPage(driver);
	 
	 log.info("Add_New_01 - Step 02: Click to 'Add' button");
	 employeeListPage.clickToButtonByID(driver, "btnAdd");
	 addEmployeePage = pageGenerator.getAddEmployeePage(driver);
	 
	 log.info("Add_New_01 - Step 03: Enter valid info to 'First Name' textbox");
	 addEmployeePage.enterToTextboxByID(driver, "firstName", empFirstName);
	 
	 log.info("Add_New_01 - Step 04: Enter valid info to 'Last Name' textbox");
	 addEmployeePage.enterToTextboxByID(driver, "lastName", empLastName);
	 
	 log.info("Add_New_01 - Step 05: Get value of 'Employee ID'");
	 employeeID = addEmployeePage.getTextboxValueByID(driver, "employeeId");
	 
	 log.info("Add_New_01 - Step 06: Click to 'Create Login Details' checkbox");
	 addEmployeePage.clickToCheckboxByLable(driver, "Create Login Details"); 
	 
	 log.info("Add_New_01 - Step 07: Enter valid info to 'User Name' textbox");
	 addEmployeePage.enterToTextboxByID(driver, "user_name", empUserName);

	 log.info("Add_New_01 - Step 08: Enter valid info to 'Password' textbox");
	 addEmployeePage.enterToTextboxByID(driver, "user_password", empPassword);
	 
	 log.info("Add_New_01 - Step 09: Enter valid info to 'Confirm Password' textbox");
	 addEmployeePage.enterToTextboxByID(driver, "re_password", empPassword);
	 
	 log.info("Add_New_01 - Step 10: Select '"+ statusValue +"' value in 'Status' dropdown");
	 addEmployeePage.selectItemInDropDownByID(driver, "status", statusValue);

	 log.info("Add_New_01 - Step 11: Click to 'Save' button");
	 addEmployeePage.clickToButtonByID(driver, "btnSave");
	 myInfoPage = pageGenerator.getMyInfoPage(driver);
	 
	 log.info("Add_New_01 - Step 12: Open Employee List page");
	 myInfoPage.openSubMenuPage(driver, "PIM", "Employee List");
	 employeeListPage = pageGenerator.getEmployeeListPage(driver);
	 
	 //employeeListPage.sleepInSecond(10);
	 verifyTrue(employeeListPage.isJQueryAjaxLoadedSuccess(driver));
	 log.info("Add_New_01 - Step 13: Enter valid info to 'Employee Name' textbox");
	 employeeListPage.enterToTextboxByID(driver, "empsearch_employee_name_empName", empFullName);
	 verifyTrue(employeeListPage.isJQueryAjaxLoadedSuccess(driver));	 
	 //employeeListPage.sleepInSecond(10);
	 
	 log.info("Add_New_01 - Step 14: Click to 'Search' button");
	 employeeListPage.clickToButtonByID(driver, "searchBtn");
	 verifyTrue(employeeListPage.isJQueryAjaxLoadedSuccess(driver));	 
	 //employeeListPage.sleepInSecond(10);

	 log.info("Add_New_01 - Step 15: Verify Employee Information displayed at 'Result Table'");
	  
	 verifyEquals(employeeListPage.getValueInTableIDAtColumnNameAndRowIndex(driver, "resultTable", "Id", "1"), employeeID);
	 verifyEquals(employeeListPage.getValueInTableIDAtColumnNameAndRowIndex(driver, "resultTable", "First (& Middle) Name", "1"), empFirstName);
	 verifyEquals(employeeListPage.getValueInTableIDAtColumnNameAndRowIndex(driver, "resultTable", "Last Name", "1"), empLastName);
  }
 
 @Test
 public void Employee_02_Upload_Avatar() {
	 log.info("Upload_Avatar_02 - Step 01: Logout to system");
	 loginPage = employeeListPage.logoutToSystem(driver);
	 
	 log.info("Upload_Avatar_02 - Step 02: Login with employee role '"+ empUserName + "' ");
	 dashboardPage = loginPage.loginToSystem(driver, empUserName, empPassword);
	 
	 log.info("Upload_Avatar_02 - Step 03: Open 'Personal Details' page");
	 dashboardPage.openMenuPage(driver, "My Info");
	 myInfoPage = pageGenerator.getMyInfoPage(driver);
	 
	 log.info("Upload_Avatar_02 - Step 04: Click on change photo image");
	 myInfoPage.clickToChangePhotoImage();
	 
	 log.info("Upload_Avatar_02 - Step 05: Upload new avatar image");
	 myInfoPage.uploadImage(driver, avatarFilePath);
	 
	 log.info("Upload_Avatar_02 - Step 06: Click to 'Upload' button");
	 myInfoPage.clickToButtonByID(driver, "btnSave");
	 
	 log.info("Upload_Avatar_02 - Step 07: Verify success message is displayed");
	 verifyTrue(myInfoPage.isSuccessMessageDisplayed(driver, "Successfully Uploaded"));
	 	 
	 log.info("Upload_Avatar_02 - Step 08: Verify new Avatar image is displayed");
	 verifyTrue(myInfoPage.isNewAvatarImageDisplayed());
 }
 
 @Test
 public void Employee_03_Personal_Details() {
	 log.info("Personal_Details_03 - Step 01: Open 'Personal Details' tab at side bar");
	 myInfoPage.openTabAtSideBarByName("Personal Details");
	 
	 log.info("Personal_Details_03 - Step 02: Verify all fields at 'Personal Details' tab are disabled");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtEmpFirstName"));
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtEmpLastName"));
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtEmployeeId"));
	 
	 log.info("Personal_Details_03 - Step 03: Click to 'Edit' button at 'Personal Details' form ");
	 myInfoPage.clickToButtonByID(driver, "btnSave");
	 
	 log.info("Personal_Details_03 - Step 04: Verify 'Employee ID' textbox is disable");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtEmployeeId"));

	 log.info("Personal_Details_03 - Step 05: Verify 'Driver's License Number' textbox is disable");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtLicenNo"));
	 
	 log.info("Personal_Details_03 - Step 06: Verify 'SSN Number' textbox is disable");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtNICNo"));
	 
	 log.info("Personal_Details_03 - Step 07: Verify 'SIN Number' textbox is disable");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_txtSINNo"));
	 
	 log.info("Personal_Details_03 - Step 08: Verify 'Date of Birth' textbox is disable");
	 verifyFalse(myInfoPage.isFieldEnabledByName(driver, "personal_DOB"));
	 
	 log.info("Personal_Details_03 - Step 09: Enter new value to 'First Name' textbox");
	 myInfoPage.enterToTextboxByID(driver, "personal_txtEmpFirstName", editEmpFirstName);
	 
	 log.info("Personal_Details_03 - Step 10: Enter new value to 'Last Name' textbox");
	 myInfoPage.enterToTextboxByID(driver, "personal_txtEmpLastName", editEmpLastName);
	 
	 log.info("Personal_Details_03 - Step 11: Select 'Gender' value ");
	 myInfoPage.clickToRadioByLable(driver, "Male");
	 
	 log.info("Personal_Details_03 - Step 12: Select 'Nationality' value ");
	 myInfoPage.selectItemInDropDownByID(driver, "personal_cmbNation", "America");
	 
	 log.info("Personal_Details_03 - Step 13: Select 'Marital Status' value ");
	 myInfoPage.selectItemInDropDownByID(driver, "personal_cmbMarital", "Single");
	 
	 log.info("Personal_Details_03 - Step 14: Click to 'Save' button at 'Personal Details' form");
	 myInfoPage.clickToButtonByID(driver, "btnSave");
	 
	 log.info("Personal_Details_03 - Step 15: Verify success message is displayed");
	 verifyTrue(myInfoPage.isSuccessMessageDisplayed(driver, "Successfully Saved"));
	 
	 log.info("Personal_Details_03 - Step 16: Verify 'First Name' textbox is updated successfully");
	 verifyEquals(myInfoPage.getTextboxValueByID(driver, "personal_txtEmpFirstName"), editEmpFirstName);
	 
	 log.info("Personal_Details_03 - Step 17: Verify 'Last Name' textbox is updated successfully");
	 verifyEquals(myInfoPage.getTextboxValueByID(driver, "personal_txtEmpLastName"),editEmpLastName);
	 
	 log.info("Personal_Details_03 - Step 18: Verify 'Gender' radio is updated successfully");
	 verifyTrue(myInfoPage.isRadioButtonSelectedByLabel(driver, "Male"));
	 
	 log.info("Personal_Details_03 - Step 19: Verify 'Marital Status' dropdown is updated successfully");
	 verifyEquals(myInfoPage.getSelectedValueInDropDownByID(driver, "personal_cmbMarital"),"Single");
	 
	 log.info("Personal_Details_03 - Step 20: Verify 'Nationality' dropdown is updated successfully");
	 verifyEquals(myInfoPage.getSelectedValueInDropDownByID(driver, "personal_cmbNation"),"America");

 }
 
 @Test
 public void Employee_04_Contact_Details() {
	 
 }
 
 @Test
 public void Employee_05_Emergency_Details() {
	 
 }
 
 @Test
 public void Employee_06_Assigned_Dependents() {
	 
 }
 
 @Test
 public void Employee_07_Edit_View_Job() {
	 
 }
 
 @Test
 public void Employee_08_Edit_View_Salary() {
	 
 }
 
 @Test
 public void Employee_08_Edit_View_Tax() {
	 
 }
 @Test
 public void Employee_09_Edit_View_Tax() {
	 
 }
 
 @Test
 public void Employee_10_Qualifications() {
	 
 }
 
  @Test
  public void Employee_11_Search_Employee() {
	  
  }
 
 
  @AfterClass(alwaysRun = true)
 	public void afterClass() {
 	  closeBrowserAndDriver("local");
 	} 
  
  	WebDriver driver;
  	AddEmployeePageObject addEmployeePage;
	DashboardPageObject dashboardPage;
	EmployeeListPageObject employeeListPage;
	LoginPageObject loginPage;
	MyInfoPageObject myInfoPage;
	DataUlti fakeData;
}
