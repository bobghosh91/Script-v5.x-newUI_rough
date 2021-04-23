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

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_Catalog-New_button'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Input_Catalog Name'), [('catalogName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Input_Catalog Code'), [('catalogCode') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CatalogCode')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_Authoring Company'), [('authoringCompany') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_Currency'), [('currency') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Currency')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_UOM'), [('UOM') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'UOM')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_Market'), [('Market') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Market')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_Language'), [('Market') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Market'), ('Language') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'Language')], FailureHandling.STOP_ON_FAILURE)

//WebUI.callTestCase(findTestCase('null'), [:], FailureHandling.CONTINUE_ON_FAILURE)
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Select_Model Library-In_Catalog'), [('libraryName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'LibraryName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_OK for Catalog Creation'), [:], FailureHandling.STOP_ON_FAILURE)



