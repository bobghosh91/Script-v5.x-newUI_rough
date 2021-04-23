import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.By as By
import org.openqa.selenium.JavascriptExecutor as JavascriptExecutor
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import internal.GlobalVariable as GlobalVariable

WebUI.callTestCase(findTestCase('0_Actions_General/PlatformLauncher-Page/launchAppFromPlatformLauncher'), [('app') : 'AdminPage Console'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/PlatformLauncher-Page/loginToAdminPageConsole'), [('AdminPageUser') : 'john08@dispostable.com'
        , ('AdminPagePassword') : 'Auto123#'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/PlatformLauncher-Page/deleteUserInAdminPageConsole'), [('userEmail') : 'qaguiautomationtest11@2020spaces.com'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/PlatformLauncher-Page/logoutFromAdminPageConsole'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : ''], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/button_Sign In'))

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/Click_Register'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/fillAccountDetailsandSignUp'), [('Email') : 'qaguiautomationtest11@2020spaces.com'
        , ('Password') : 'Auto123#', ('FName') : 'ContentStudio', ('LName') : 'Automation', ('Market') : 'United States'], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/button_Confirm'))

currentWindow = WebUI.getWindowIndex()

    ((DriverFactory.getWebDriver()) as JavascriptExecutor).executeScript('window.open(\'about:blank\',\'_blank\')')

WebUI.switchToWindowIndex(currentWindow + 1)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/sample'), [:], FailureHandling.STOP_ON_FAILURE)

//WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/clickOnTextInEmailBody'), [('textToSearch') : 'Please, click here to activate your account'], 
//FailureHandling.STOP_ON_FAILURE)
String code = DriverFactory.getWebDriver().findElement(By.xpath('//*[@id=\'Item.MessageUniqueBody\']//b')).getText()

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/deleteAllExistingEmails'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/logoutFromOutlookWebApp'), [('userName') : 'qaguiautomationtest11'], 
    FailureHandling.STOP_ON_FAILURE)

    ((DriverFactory.getWebDriver()) as JavascriptExecutor).executeScript('window.close()')

WebUI.switchToWindowIndex(currentWindow)

WebUI.setText(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/input_Please enter the code sent'), code)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/User/button_Confirm'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/button_Sign In'))


