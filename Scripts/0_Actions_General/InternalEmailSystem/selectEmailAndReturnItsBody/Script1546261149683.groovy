import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement

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

import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions as Actions
import org.testng.Assert

if(partialSubjectMatch == true){
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_specificEmailFromEmailList_partialSub',
		[('emailSubject') : emailSubject]))
	
}else{
CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_specificEmailFromEmailList',
	[('emailSubject') : emailSubject]))
}
CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_emailBodyContainer'))

WebElement e = WebUiCommonHelper.findWebElement(findTestObject('Object Repository/0_Actions_General/InternalEmailSystem/div_emailBodyContainer'), GlobalVariable.midWait)

String contents = (String)((JavascriptExecutor) DriverFactory.getWebDriver()).executeScript("return arguments[0].innerHTML;", e);

return contents
