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
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil as log

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/LoginToCS'), [('userEmail') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'UserEmail'), ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Password')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/Click_User Management then click User tab'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/input_filter-email'), CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
        GlobalVariable.currentTestCaseID, 'UserEmail2'))

WebUI.sendKeys(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/input_filter-email'), Keys.chord(Keys.TAB), 
    FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.delay(2)
WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.delay(2)
WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/datatable_email search result'))

WebUI.delay(3)

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Edit'))

WebUI.delay(5)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content-report-manager'))

try {
    verifyChkbox = WebUI.verifyElementNotChecked(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content-report-manager'), 
        GlobalVariable.maxWait)
	println verifyChkbox
}
catch (Exception e) {
    println('Exception at verifying element Unchecked state. checkbox maybe already ticked')
	println(e.toString());
	log.markFailedAndStop('Step Failed as checkbox was already ticked');
} 

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/checkbox_Content-report-manager'))

WebUI.delay(2)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/button_Ok'))

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/OnlyLoginToCS'), [('userEmail') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'UserEmail2')
        , ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Password')], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Report Management'),'Report Management')

CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Report Management'))

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Report Management'))

WebUI.delay(2)
CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/nav-item-report-page'))

CustomKeywords.'genericFunctions.Assertions.assertTestObjectTextIsPresentOnUi'(findTestObject('1_Actions_AppSpecific/2_Content Studio/5_Role Assignment/a_Catalog Reports'),'CATALOG REPORTS')

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)