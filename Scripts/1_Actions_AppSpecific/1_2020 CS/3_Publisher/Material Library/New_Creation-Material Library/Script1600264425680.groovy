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

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Click_Material library New_button'), [:], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'('Data Files\\Test-Input.xlsx', "CS", 
  GlobalVariable.currentTestCaseID, 'MaterialLibraryName', 'MatLib' + commonFunctions.timeStamp )

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Input_Material Library Name'), [('materiallibraryName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'MaterialLibraryName')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(1)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Select_Authoring Company'), [('authoringCompany') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Select_Type-Material and Texture'), [('Type') : 'Materials & Textures'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Select_Shared With'), [('sharedWith') : 'Everyone'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Material Library/Click_Ok for Mat Lib Careation'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(10)

