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
import appSpecificFunctions.commonFunctions
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Click_Companies-New Button'), [:], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx","CS" , GlobalVariable.currentTestCaseID, 'CompanyName', 'Company '+commonFunctions.timeStamp )
CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx","CS" , GlobalVariable.currentTestCaseID, 'CompanyCode', 'ComC'+commonFunctions.timeStamp )

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Company Name'), [('companyName') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
        GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)


WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Company Code'), [('CompanyCode') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
        GlobalVariable.currentTestCaseID, 'CompanyCode')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_display Code'), [:], FailureHandling.STOP_ON_FAILURE)


WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Address 1'), [('Addressline1'):'201, Kapil Zenith'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Address 2'), [('Addressline2'):'Bavdhan'], FailureHandling.STOP_ON_FAILURE)


WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_City'), [('City'):'Pune'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_State'), [('State'):'Maharashtra'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Country'), [('Country'):'India'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_ZipCode'), [('ZipCode'):'411021'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_phone'), [('Phone'):'020-6654 2020'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_website'), [('Website'):'Spaces.com'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Input_Email'), [('email'):commonFunctions.timeStamp+'@Spaces.com'], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/button_Ok_company'))

