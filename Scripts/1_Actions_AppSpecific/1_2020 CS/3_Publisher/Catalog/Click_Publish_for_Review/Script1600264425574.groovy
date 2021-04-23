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

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Publish For Review'))

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Publish For Review'))

WebUI.delay(1)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/a_Send for Review in Productio'))

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Ok_Publish For Review'))

//WebUI.delay(GlobalVariable.midWait)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/Publish for Review check'))

//WebUI.delay(GlobalVariable.maxWait)


def Status = WebUI.getText(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/Publish for Review check'),FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(Status, 'Published For Review', true)

println(Status)

/*def PR=WebUI.getText(findTestObject('Object Repository/Publishing/td_Publish Failed'))

println (PR)

WebUI.verifyMatch(PR, 'Publish Failed', true)*/

//println ("Catalog Publish for Review Sucessfully....................................................................................")

