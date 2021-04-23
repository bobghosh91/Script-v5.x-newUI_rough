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

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/LoginToCS'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Click-Model_Library'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/New_Creation-Library'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Search_Library InTable'), [('librarySearch') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'LibraryName')], FailureHandling.STOP_ON_FAILURE)


//WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/LoginToCS'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/Click_Models'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Select_ModelLibrary'), [:],FailureHandling.STOP_ON_FAILURE)

/*WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Select_ModelLibrary by Search'), [('libraryName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'LibraryName')], FailureHandling.STOP_ON_FAILURE)*/
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/Click_Import'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/Import_Model'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/Import_Thumbnail'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/Click_Start_Import'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Click_Version'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Select_ModelLibrary'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Click_Certify'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/Click_Submit For Review'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Click-Model_Library'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Search_Library InTable'), [('librarySearch') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
            GlobalVariable.currentTestCaseID, 'LibraryName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/3_Publisher/Model Library/Click_Publish'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

