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
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper

import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions as Actions
import org.testng.Assert

Actions actions = new Actions(DriverFactory.getWebDriver())

WebElement we
List <WebElement> weList
try{
	we = WebUiCommonHelper.findWebElement(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/button_selectAllItemsInMailList'), GlobalVariable.midWait)
	weList = WebUiCommonHelper.findWebElements(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_mailBoxEmailList'), GlobalVariable.midWait)
}catch (Exception e) {
			println"Exception occoured while featching object." +e.message
			return
}
while(weList.size() != 0)
{
	actions.moveToElement(we).build().perform()
	WebUI.click(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/button_selectAllItemsInMailList'), FailureHandling.STOP_ON_FAILURE)
	WebUI.delay(GlobalVariable.minuteDelay)
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/button_delete'))
	WebUI.delay(GlobalVariable.minWait)
	//CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/span_emptyEmailTrophyIcon'))
	//WebUI.verifyElementNotPresent(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_mailBoxEmailList'), GlobalVariable.minuteDelay, FailureHandling.STOP_ON_FAILURE)
	weList.clear()
	try{
	weList =WebUiCommonHelper.findWebElements(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_mailBoxEmailList'), GlobalVariable.minWait)
	}catch (Exception e) {
		println"Exception occoured while featching object div_mailBoxEmailList. Email Inbox is Empty." + e.message		 
	}
}