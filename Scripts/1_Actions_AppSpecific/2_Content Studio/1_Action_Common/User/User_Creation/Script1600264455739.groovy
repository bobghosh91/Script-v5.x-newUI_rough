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

WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : ''], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/iframe_ids'))

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/Click_Register'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_Email'), [('Email') : "qaguiautomationtest11@2020spaces.com"], 
	FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_Email-Confirm'), [('EmailComfirm') : "qaguiautomationtest11@2020spaces.com"], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_Password'), [('Password') : 'auto123#'], 
	FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_Password-Confirm'), [('PasswordConfirm') : 'auto123#'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_First-Name'), [('FName') : 'CS'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/enter_Last-Name'), [('LName') : 'CS'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/Select_market'), [('Market') : 'United States'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/check_Accept Term'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/Click_Sign-Up'), [:], FailureHandling.STOP_ON_FAILURE)

