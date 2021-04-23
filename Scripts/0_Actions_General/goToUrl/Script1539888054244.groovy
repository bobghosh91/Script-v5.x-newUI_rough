import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
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
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory 
 
//if(GlobalVariable.currentTestCaseSkippedStatus.equals('true')){
//	KeywordUtil.markFailedAndStop("This test case is to be skipped. Please see csv reports for details.")
//}
CustomKeywords.'genericFunctions.EnvironmentSetup.setupForBrowserWithRespectToProxy'()

String url = ""

if(!specifiedURL.toString().contentEquals("")){
	url= specifiedURL 
}

else 
{
		url = GlobalVariable.URL
}

if ((GlobalVariable.currentTestCaseCaptureNetworkTraffic == null) && GlobalVariable.chromeBetaToUsedOnWindowsExecution.equals(
    'false')) {
    WebUI.openBrowser('')
}
	
	if( ((GlobalVariable.envInfo.get("browserName").equals("chrome"))  && (GlobalVariable.currentTestCaseCaptureNetworkTraffic.toString().equalsIgnoreCase("null")) &&
		(RunConfiguration.executionProfile.contains("KSI Kitchens (Gen Imperial)")
		 || RunConfiguration.executionProfile.contains("Content Generic Metric")
		 || RunConfiguration.executionProfile.contains("Der Kreis (Gen Metric)")
		 || RunConfiguration.executionProfile.contains("Aviva (Nobilia)"))) ) {
	 		
		   WebUI.callTestCase(findTestCase('0_Actions_General/launchChromeWithOldReferrerPolicy'), [:], FailureHandling.STOP_ON_FAILURE)
		  
	}
		 
WebUI.maximizeWindow()

GlobalVariable.envInfo.put('browserVersion', CustomKeywords.'genericFunctions.EnvironmentSetup.getBrowserNameAndVersion'().get(
		'browserVersion'))

if (GlobalVariable.envInfo.get("browserName").equals("chrome")) {
	
		CustomKeywords.'genericFunctions.EnvironmentSetup.triggerChromeComponentsUpdate'()
}
		
String appDetails = ""
	
println GlobalVariable.useUrlFromPlatformLauncherOverUrlFromSpreadsheet

if ((GlobalVariable.useUrlFromPlatformLauncherOverUrlFromSpreadsheet).toString().equals("true")) {
			
	if ((url).toString().isEmpty()) {
			
		appDetails = WebUI.callTestCase(findTestCase('0_Actions_General/PlatformLauncher-Page/obtainUrlFromPlatformLauncher'), [:], FailureHandling.STOP_ON_FAILURE)
	}
	else {
		DriverFactory.getWebDriver().get(url)
	}
}

if ((GlobalVariable.useUrlFromPlatformLauncherOverUrlFromSpreadsheet).toString().equals("false")) {
		
	DriverFactory.getWebDriver().get(url)
} 
//if (RunConfiguration.executionProfile.contains("2020 Metric") || RunConfiguration.executionProfile.contains("2020 Imperial") || RunConfiguration.executionProfile.contains("Eggo")|| RunConfiguration.executionProfile.contains("Reform")|| RunConfiguration.executionProfile.contains("Reform Imperial") || RunConfiguration.executionProfile.contains("Ikea")) {
if (RunConfiguration.executionProfile.contains("2020 Metric")
	|| RunConfiguration.executionProfile.contains("2020 Imperial")
	|| RunConfiguration.executionProfile.contains("Eggo")
	|| RunConfiguration.executionProfile.contains("Reform")
	|| RunConfiguration.executionProfile.contains("Reform Imperial")
	|| RunConfiguration.executionProfile.contains("Content Generic Metric")
	|| RunConfiguration.executionProfile.contains("Der Kreis (Gen Metric)")
	|| RunConfiguration.executionProfile.contains("Aviva (Nobilia)")
	){
	if(skipPrivacyPolicyMessage){
		WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_Actions_Common/acceptPrivacyPolicyMessage'), [:])
	}
}else if(RunConfiguration.executionProfile.contains("Ikea")){
	//below code is added to handle internal login popup in IKEA QAT and Staging Env
	if(GlobalVariable.platformEnv in ["staging-azure", "staging-aws", "staging","qat"]){
		WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/12_Actions_Ikea/manageAccessToIKEAInternally'), [:], FailureHandling.STOP_ON_FAILURE)
	}
	
	if(skipPrivacyPolicyMessage){
		if(GlobalVariable.platformEnv in ["staging-azure", "staging-aws", "staging"]){
			WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/12_Actions_Ikea/acceptPrivacyPolicyMessage'), [:])
		}else{
			WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_Actions_Common/acceptPrivacyPolicyMessage'), [:])
		}
	}
}
	
	
KeywordUtil.logInfo("Execution Profile : " + RunConfiguration.executionProfile + "\n" + "Launched URL : " + WebUI.getUrl() + "\n" + "App Details : " + appDetails + "\n" + "Launcher Flags : " + GlobalVariable.launcherFlagsToggle)