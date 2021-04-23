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
import com.kms.katalon.core.testobject.ConditionType
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
import org.testng.Assert

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

//WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Click_Publisher'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Click_Show Filter'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/select_Filter_Catalog_Status'), [('statusLabel'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogFilterStatus')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(2)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Search_Catalog'), [('catalogSearch'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Click_Edit_Single_Catalog'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/Select_UOM'), [('UOM'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UOM2')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/Select_Currency'), [('currency'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Currency2')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/editSelectUnselect_Language'), [('Language'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Language1')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/editSelectUnselect_Language'), [('Language'):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Language2')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/Click_OK for Catalog Edit'), [:],
	FailureHandling.STOP_ON_FAILURE)

WebUI.delay(3)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/OnlyLoginToCS'), [('userEmail') :
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UserEmail2')
		, ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Password2')], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_Company'), [('CompanyName') :
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Click_Catalog-Authoring'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(5)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Select_Catalog by Search'), [("catalogName"):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

String strLang = WebUI.getText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/select_language-dropdown'))

//TestObject currencyObject = new TestObject()
//currencyObject = currencyObject.addProperty("xpath", ConditionType.EQUALS, "//*[@id='catalog-versions-list-table']//tr[1]//td[5]")
String strCurr = WebUI.getText(findTestObject('1_Actions_AppSpecific/2_Content Studio/4_Author/table_td-currency'))

//TestObject uomObject = new TestObject()
//uomObject = uomObject.addProperty("xpath", ConditionType.EQUALS, "//*[@id='catalog-versions-list-table']//tr[1]//td[6]")

String strUOM = WebUI.getText(findTestObject('1_Actions_AppSpecific/2_Content Studio/4_Author/table_td-uom'))

CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(strLang, CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Language2'), 'Expected and actual language string not equal')

CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(strUOM, CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UOM2'), 'Expected and actual UOM string not equal')

CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(strCurr, CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Currency2'), 'Expected and actual currency string not equal')


