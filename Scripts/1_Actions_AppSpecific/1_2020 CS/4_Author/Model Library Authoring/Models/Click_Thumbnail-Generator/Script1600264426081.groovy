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

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Generate Thumbnails'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Ok_TG'))

WebUI.delay(4)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Close_TG'))

//WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Close_TG'))

//CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Generate Thumbnails'))

//WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/button_Generate Thumbnails'))

//String absoluteFilePathActualImage = CustomKeywords.'genericFunctions.Assertions.assertImageIsPresent'("D:/CIC Automation/Script v3.x/Data Files/ReferenceData/22/SMALL/FlowersGeneric1.jpg", 50)