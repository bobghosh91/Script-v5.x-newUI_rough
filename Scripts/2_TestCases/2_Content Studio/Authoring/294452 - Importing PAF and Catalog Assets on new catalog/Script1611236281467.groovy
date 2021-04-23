import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.openqa.selenium.JavascriptExecutor
import org.eclipse.core.runtime.Assert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/LoginToCS'), [('userEmail') : 
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UserEmail')
        , ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Password')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_Company'), [('CompanyName') :
CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(2)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Click_Publisher'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Catalog_Creation'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Click_Catalog-Authoring'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Select_Catalog by Search'), [("catalogName"):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Import_PAF'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Import_Catalog Assets'), [:], FailureHandling.STOP_ON_FAILURE)








