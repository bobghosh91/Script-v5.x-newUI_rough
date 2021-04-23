import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.JavascriptExecutor

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

	if ((GlobalVariable.platformEnv).toString().equals("pre-prod")) {
		KeywordUtil.markErrorAndStop("Pre-production URL cannot be obtained from Launcher. Please update the URL in XLS spreadsheet and make the flag \"useUrlFromPlatformLauncherOverUrlFromSpreadsheet\" as \"false\".")
	}

	//WebUI.openBrowser('')
	
	WebUI.navigateToUrl('http://apps-canary.2020.net/Launcher/index.html')
	
	WebUI.waitForPageLoad(GlobalVariable.maxWait)
	
	WebUI.maximizeWindow()
	
	//CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForCustomerApps'))
	
	if (GlobalVariable.launcherFlagsToggle.get("customerAppsEnabled").equals("true")) {
		CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForCustomerApps'))
	}else if(GlobalVariable.launcherFlagsToggle.get("essentialAppsEnabled").equals("true")){
		CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForEssentialCust'))
	}else if(GlobalVariable.launcherFlagsToggle.get("essentialSMEAppsEnabled").equals("true")){
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForEssentialSMECust'))
	}
		
	WebUI.delay(GlobalVariable.minuteDelay)
	
	//Currently we are using startFromScratchEnabled flag from data sheet
	//if (GlobalVariable.launcherFlagsToggle.get("startFromScratchEnabled").equals("true")) {
	//   CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForStartFromScratch'))
	//}
		
	WebUI.delay(GlobalVariable.minuteDelay)
		
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/button_env', [('environment') : ((GlobalVariable.platformEnv).trim().toLowerCase())]))
		
	WebUI.delay(GlobalVariable.minuteDelay)
		
	String appToLaunch = CustomKeywords.'genericFunctions.EnvironmentSetup.getCurrentAppNameFromProfileName'()
//	println 'appToLaunch'+appToLaunch
//	if(appToLaunch.equals('2020 Essentials')){
//		appToLaunch = 'Content Generic Metric'
//	}
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/list_cutomerName', [('customer') : appToLaunch]))
	
	WebUI.delay(GlobalVariable.minuteDelay)
	
	if((appToLaunch in ["Content Studio"]) == false) { 
	
		CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/div_idealSpaces'))
	}
	
	WebUI.delay(GlobalVariable.minWait)
	
	WebUI.switchToWindowIndex(1)
			
	WebUI.closeWindowUrl('http://apps-canary.2020.net/Launcher/index.html')
	
	WebUI.delay(GlobalVariable.minuteDelay)
			
	WebUI.switchToWindowIndex(0)
	
	println (WebUI.getUrl())
	
	GlobalVariable.URL = WebUI.getUrl()
	
	if ((GlobalVariable.URL).toString().isEmpty()) {
		KeywordUtil.markErrorAndStop("URL to be launched, is empty.")
	}
	
	WebUI.delay(GlobalVariable.minuteDelay)
	
	try {
		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
	
		String isCoreName = (String) js.executeScript("return (require('json!config/environment.json').appName)")
		String isCoreVersion = (String) js.executeScript("return (require('json!config/environment.json').appVersion)")
		String appName = (String) js.executeScript("return (require('config').appName)")
		String appVersion = (String) js.executeScript("return (require('config').appVersion)")
		
		return (isCoreName + " : " + isCoreVersion + ", " + appName + " : " + appVersion)
	}
	catch (Exception e) {
		return ("Exception occurred while obtaining APP version details: " + e.message)
	}