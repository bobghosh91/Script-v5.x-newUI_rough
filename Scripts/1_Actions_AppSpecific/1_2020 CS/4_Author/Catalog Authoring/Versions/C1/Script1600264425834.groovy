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

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import java.time.LocalDateTime as LocalDateTime
import java.time.format.DateTimeFormatter as DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder.StringLiteralPrinterParser as StringLiteralPrinterParser
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import oracle.net.aso.q as q
import org.openqa.selenium.Keys as Keys
import catalog_1.Cataloggen as Cataloggen
import catalog_1.AuthorPage
import catalog_1.upload as upload
import catalog_1.writeData as writeData

WebUI.callTestCase(findTestCase('Demo/Login'), [:], FailureHandling.CONTINUE_ON_FAILURE)

WebUI.waitForElementVisible(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/a_Versions'), 25)

AuthorPage.catName()


/*WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/a_Versions'))

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/span_Select Catalog'))

WebUI.doubleClick(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/input_select2-search__field'))

WebUI.setText(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/input_select2-search__field'), findTestData('Data').getValue('CatalogName', 1))

WebUI.sendKeys(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/input_select2-search__field'), Keys.chord(Keys.ENTER))*/

WebUI.delay(10)

//println(ImpS)
if (WebUI.waitForElementPresent(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Work In Progress'), 2, FailureHandling.CONTINUE_ON_FAILURE)) {
	
	WebUI.verifyMatch(WebUI.getText(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Work In Progress')), 'Work In Progress', true)

	println('Work In Progress')

	WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_ImportExport'))

	upload.upoloadcsv()

	WebUI.delay(30)
	
	ImpC = WebUI.getText(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Import Completed'))
	
	WebUI.verifyMatch(ImpC, 'Import Completed', true)
	
} else {
	println('In Published')

	//ImpS1 = WebUI.waitForElementPresent(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Published'),2)
	
	//println(ImpS1)

   // WebUI.verifyMatch(WebUI.getText(ImpS1), 'Published')
	
	WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_New'))
	
	WebUI.click(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Create New Version'))
	
	WebUI.delay(5)
	
}

//WebUI.click(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Verify'))

//WebUI.click(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/a_Send To APPS-QAAsVerify APPS'))

//WebUI.delay(20)

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Release Notes'))

WebUI.delay(5)

WebUI.setText(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/textarea_generate-release-note'), 'qa')

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Generate'))

WebUI.delay(10)

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Close'))

WebUI.delay(5)

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Certify'))

WebUI.delay(50)

ImpCern = WebUI.getText(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Certification Completed. Da_Error_not'))

WebUI.verifyMatch(ImpCern, 'Certification Completed. Data Errors not found.', true)

println(ImpCern)

/*ImpCererr = WebUI.getText(findTestObject('Object Repository/Author/Page_2020 Cloud  AU.1.8.Main.Build3/td_Certification Completed. Da'))

WebUI.verifyMatch(ImpCererr, 'Certification Completed. Data Errors found.', false)*/
WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Submit for Review'))

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/button_Ok'))

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/span_caret'))

WebUI.click(findTestObject('Author/Page_2020 Cloud  AU.1.8.Main.Build3/a_Logout'))


