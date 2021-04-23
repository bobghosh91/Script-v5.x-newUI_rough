import static org.junit.Assert.*
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
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import appSpecificFunctions.commonFunctions as commonFunctions
import genericFunctions.DataProcessing as DataProcessing

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/3_Publisher/button_New'))

Date date = new Date()
String secPart = date.format("ss")

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'('Data Files\\Test-Input.xlsx', 'Content Studio_DataSet1', GlobalVariable.currentTestCaseID, 
    'CatalogName', 'CSAuto' + commonFunctions.timeStamp + secPart)

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'('Data Files\\Test-Input.xlsx', 'Content Studio_DataSet1', GlobalVariable.currentTestCaseID, 
    'CatalogCode', 'CSACC' + commonFunctions.timeStamp + secPart)

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/input_Catalog Name'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName'))

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/input_Catalog Code'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogCode'))

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/select_Select-authoring Company'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName'), 
    true)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/select_Select Currency'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Currency1'), 
    false)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/select_Select Unit'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UOM1'), 
    true)

CustomKeywords.'appSpecificFunctions.PublisherPage.selectEntryFromList'(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/select_Market'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Market1'))

CustomKeywords.'appSpecificFunctions.PublisherPage.selectEntryFromList'(findTestObject('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/select_Language'), 
    CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Language1'))

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/1_New_Catalog/Click_OK for Catalog Creation'), [:], FailureHandling.STOP_ON_FAILURE)

