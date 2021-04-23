import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.testobject.*
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil as log

TestObject tObj = new TestObject()

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/LoginToCS'), [('userEmail') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
	GlobalVariable.currentTestCaseID, 'UserEmail'), ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
	GlobalVariable.currentTestCaseID, 'Password')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/Click_User Management then click User tab'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/input_filter-email'), CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UserEmail2'))
	
WebUI.sendKeys(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/input_filter-email'), Keys.chord(Keys.TAB), FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.delay(2)
WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Edit'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Edit Companies'))

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/input_Edit Companies Search'),CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_CS Automation'))

WebUI.verifyElementNotChecked(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_CS Automation'), 0, FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_CS Automation'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Update User Company'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/i_Edit Role Icon'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/i_Edit Role Icon'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content Publisher'))

WebUI.verifyElementNotChecked(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content Publisher'), 0, FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content Publisher'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Edit Roles'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Ok'))

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/OnlyLoginToCS'), [('userEmail') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
	GlobalVariable.currentTestCaseID, 'UserEmail2'), ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
	GlobalVariable.currentTestCaseID, 'Password2')], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Publishing'))

CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Publishing'),'Publishing')

WebUI.waitForPageLoad(GlobalVariable.maxWait)

WebUI.switchToFrame(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/iframe_content_studio_main'), GlobalVariable.maxWait)

tObj.addProperty('xpath', ConditionType.EQUALS, "//*[contains(text(),'New') and @class='btn btn-primary mr-2']")
CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'New')

tObj.addProperty('xpath', ConditionType.EQUALS, "//*[contains(text(),'Usage Analytics') and @class='btn btn-primary mr-2']")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'Usage Analytics')

tObj.addProperty('xpath', ConditionType.EQUALS, "//*[contains(text(),'Access Requests') and @class='btn btn-secondary']")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'Access Requests')

//This xpath check search field
tObj.addProperty('xpath', ConditionType.EQUALS, "//*[@id='catalogs-list-table_filter']//following-sibling::input")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsVisible'(tObj)

tObj.addProperty('xpath', ConditionType.EQUALS, "//*[@class='dt-button btn btn-secondary']/span[text()='Show Filter']")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'Show Filter')
 
tObj.addProperty('xpath', ConditionType.EQUALS, "//*[@id='edit-catalog-version-btn' and contains(text(),'Edit Markets')]")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(tObj)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'Edit Markets')

tObj.addProperty('xpath', ConditionType.EQUALS, "//*[@id='generate-release-note-popup-btn' and contains(text(),'Release Notes')]")
CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(tObj,'Release Notes')