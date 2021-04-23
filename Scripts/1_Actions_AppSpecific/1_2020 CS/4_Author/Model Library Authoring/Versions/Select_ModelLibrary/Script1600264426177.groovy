import org.openqa.selenium.Keys
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

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.util.KeywordUtil

String libraryName = CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, "LibraryName")


CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/select_Library'))

WebUI.delay(1)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/select_Library'), libraryName, false)

int index =WebUI.getNumberOfSelectedOption(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Versions/select_Library'))

if (index == 0) {
	KeywordUtil.logInfo("Model library(s) (provided as input) not found.")
}
WebUI.delay(GlobalVariable.minWait)