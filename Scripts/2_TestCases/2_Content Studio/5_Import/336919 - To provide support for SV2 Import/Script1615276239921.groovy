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
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil as log
import java.util.Date as Date
import org.testng.Assert
import java.text.SimpleDateFormat as SimpleDateFormat

def catchphrase, ActualText, strDate
SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
Date date = new Date();
String currentDate = formatter.format(date)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/LoginToCS'), [('userEmail') :
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UserEmail')
		, ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Password')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_Company'), [('CompanyName') :
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)
	
 WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Click_Publisher'), [:], FailureHandling.STOP_ON_FAILURE)
	
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/3_Publisher/Catalog_Creation'), [:], FailureHandling.STOP_ON_FAILURE)
	
WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/1_Login-Page/OnlyLoginToCS'),
[('userEmail') :CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'UserEmail2'), ('Password') : CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'Password2')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_Company'), [('CompanyName') :
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Click_Catalog-Authoring'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/2_Content Studio/4_Author/Select_Catalog by Search'), [("catalogName"):
	CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authoring/Versions/Click_Import-Export'),[:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(GlobalVariable.minWait)

WebUI.uploadFile(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/input_PAF_uploadPafToImport'),((RunConfiguration.getProjectDir() + '/Data Files/ReferenceData/PAF/') + GlobalVariable.currentTestCaseID) + '.zip')

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/button_Import-PAF'))

WebUI.click(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/button_Import-PAF'))

WebUI.delay(GlobalVariable.minWait)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/button_Edit'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/6_Import/i_Metric_show-import-logs-btn'))

WebUI.waitForPageLoad(GlobalVariable.maxWait)

//Capturing all text in the import log
String strnote = WebUI.getText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/6_Import/Import_logs Window'))

String[] s = strnote.split('\\n')
println 'Length is '+s.length
catchphrase = s[s.length-1]
println catchphrase

if (catchphrase.contains('Current job is Success')){
	
	String[] s1 = catchphrase.split(' ')
	println 'Length is '+s1.length
	ActualText =  s1[s1.length-4]+' '+s1[s1.length-3]+' '+s1[s1.length-2]+' '+s1[s1.length-1]
	CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(ActualText, 'Current job is Success', 'Expected and actual Strings in import logs not equal')
	
	String[] s2 = catchphrase.split('Current job is Success')
	strDate = s2[0]
	println "New phrase is $s2"
	if (s2[0].contains(currentDate)){
		Assert.assertTrue(true)
		log.markPassed('Date present in log file matches with System date - PASSED')
	}else{
	log.markFailedAndStop('Step Failed as Date/Time stamp is not matching with system date');
	}
}else{
	log.markFailedAndStop('Step Failed as Success message was not found in log report');
}

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/6_Import/button_Close'))

//WebUI.delay(5)