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
import org.openqa.selenium.Keys as Keys

//WebUI.openBrowser('https://www.google.com')
//WebUI.maximizeWindow()
////WebUI.acceptAlert()
////WebUI.setText(findTestObject('1_Actions_AppSpecific/3_Google/Page_Google/InputTextField'), 'cricbuzz')

//WebUI.setText(findTestObject('1_Actions_AppSpecific/3_Google/Page_Google/InputTextField'), 'cricbuzz')
//
//WebUI.sendKeys(findTestObject('1_Actions_AppSpecific/3_Google/Page_Google/InputTextField'), Keys.chord(Keys.ENTER), FailureHandling.STOP_ON_FAILURE)
//
//WebUI.delay(05)
//
//WebUI.click(findTestObject('Object Repository/1_Actions_AppSpecific/3_Google/Page_cricbuzz - Google Search/span_Cricbuzz'), FailureHandling.STOP_ON_FAILURE)
//
////CustomKeywords.'genericFunctions.Assertions.
//
//String strUOM1 = WebUI.getAttribute(findTestObject('Object Repository/1_Actions_AppSpecific/3_Google/Page_Cricket Score, Schedule, Latest News, _b1ecde/CricbuzzLogo'),'Title')
//println strUOM1
//CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(strUOM1, 'Cricbuzz Logo', 'Expected and actual language string is equal')

WebUI.setText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/Role Assignment/input_Email'))

WebUI.delay(5)
