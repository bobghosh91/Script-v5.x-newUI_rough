import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import genericFunctions.EnvironmentSetup as EnvironmentSetup
import appSpecificFunctions.commonFunctions as commonFunctions

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Click_library-New_button'), [:], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'('Data Files\\Test-Input.xlsx', GlobalVariable.testDataSheetName, 
    GlobalVariable.currentTestCaseID, 'LibraryName', 'Library' + commonFunctions.timeStamp)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/3_Publisher/Model Library/Library_Name'), [('libraryName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'LibraryName')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(1)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Authoring_Company_Select'), [('companyName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'AuthoringCompany')],FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Library_shared'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/3_Publisher/Model Library/Library_Ok'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(10)



