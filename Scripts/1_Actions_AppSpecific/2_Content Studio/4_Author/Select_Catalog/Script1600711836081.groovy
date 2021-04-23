import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.eclipse.persistence.internal.oxm.record.json.JSONParser.object_return as object_return
import org.openqa.selenium.By as By
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement as WebElement
import org.openqa.selenium.support.ui.Select as Select
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint

String browser = DriverFactory.getExecutedBrowser().name.toLowerCase().replace('_driver', '')

String catalogName = CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')
String market = CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Market')
String company = CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')

String selectCatalog = catalogName + ' | ' + market + ' | ' + company

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/select_Catalog'))

if (browser.contains('firefox')) {
	WebUI.callTestCase(findTestCase("Test Cases/1_Actions_AppSpecific/2_Content Studio/4_Author/Select_Catalog by Search"), [('catalogName') : selectCatalog], FailureHandling.STOP_ON_FAILURE)
 }else{


WebUI.delay(2)

WebUI.selectOptionByLabel(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_Select Catalog'), selectCatalog, false,FailureHandling.CONTINUE_ON_FAILURE)

int index = WebUI.getNumberOfSelectedOption(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_Select Catalog'))

if (index == 0) {
    KeywordUtil.logInfo('Catalog(s) (provided as input) not found.' +index)
	}
}
WebUI.delay(GlobalVariable.minWait)



