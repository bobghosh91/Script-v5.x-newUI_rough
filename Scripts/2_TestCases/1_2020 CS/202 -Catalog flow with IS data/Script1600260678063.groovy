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

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/LoginToCS'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_Catalog'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Catalog_Creation'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Search_Catatog In Table'), [('catalogSearch') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

//WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/LoginToCS'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Version'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Select_Catalog'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Import_Native_Catalog'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Check_Import_Completed'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Release_Note'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Certify'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Submit For Review'), [:], FailureHandling.STOP_ON_FAILURE)

//WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_Publish_for_Review'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_Catalog'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Search_Catatog In Table'), [('catalogSearch') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Catalog/Click_Publish'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

