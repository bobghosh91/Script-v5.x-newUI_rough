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


CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Publish'))

attribute = WebUI.getAttribute(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Publish'), 'title')

println(attribute)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Ok_Publish'))

WebUI.delay(GlobalVariable.midWait)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Retire'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/Publish Check'))

//WebUI.delay(GlobalVariable.maxWait)

def Status = WebUI.getText(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/Publish Check'),FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(Status, 'Published', true)

println(Status)



//println ("Catalog Publishing Completed.........")



