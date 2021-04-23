import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.chrome.ChromeDriver as ChromeDriver
import org.openqa.selenium.logging.LogEntries as LogEntries
import org.openqa.selenium.logging.LogEntry as LogEntry
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType as WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/iframe_ids'))

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/input_Email (required)_email'),userEmail, FailureHandling.STOP_ON_FAILURE)

WebUI.setText(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/input_Password (required)_password'),Password, FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/button_Sign In'),FailureHandling.CONTINUE_ON_FAILURE)

WebUI.delay(3)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/3_User/a_logout_navbarDropdown'))

WebUI.delay(2)