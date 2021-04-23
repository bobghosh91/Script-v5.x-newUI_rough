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
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Import-Export'), 
    [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.maximizeWindow(FailureHandling.STOP_ON_FAILURE)

WebUI.uploadFile(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/input_PAF_uploadPafToImport'), 
    ((RunConfiguration.getProjectDir() + '/Data Files/ReferenceData/PAF/') + GlobalVariable.currentTestCaseID) + '.zip')

WebUI.delay(GlobalVariable.minuteDelay)

WebUI.click(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/button_Import-PAF'))

WebUI.delay(240)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/button_Edit'))