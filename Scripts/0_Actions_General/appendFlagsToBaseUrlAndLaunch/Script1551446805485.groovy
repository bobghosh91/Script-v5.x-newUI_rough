import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import org.testng.Assert
import org.apache.commons.lang.StringUtils
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

if(GlobalVariable.currentTestCaseSkippedStatus.equals('true')){
	KeywordUtil.markFailedAndStop("This test case is to be skipped. Please see csv reports for details.")
}
if (flagCombination.equals('<doNotAppendAnything>') == false) {

	//When base url is blank, launch it through the launcher
	
	boolean wentThroughLauncher = false
	
	String baseURL = GlobalVariable.URL
	
	if (baseURL.equals('') || baseURL.equals(null)) {
	
		WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : '', ('skipPrivacyPolicyMessage') : skipPrivacyPolicyMessage],FailureHandling.STOP_ON_FAILURE)
		wentThroughLauncher = true
	}
	
	baseURL = GlobalVariable.URL
	
	Assert.assertTrue(flagCombination.toString().startsWith("?"), "The flag combination is supposed to start with \"?\", so that it can be appended on various environments except pre-prod. For pre-prod, it is handled in program.")
	
	if (baseURL.endsWith('#')) {
		baseURL = StringUtils.strip(GlobalVariable.URL, '#')
	}
	
	//---------------------------------------------------------------------------------------
	
	String fetchURL = CustomKeywords.'genericFunctions.EnvironmentSetup.processFlagCombinationAndFormUrl'(baseURL, flagCombination)

	KeywordUtil.logInfo("Final URL to launch is : " + fetchURL)
	
	if(GlobalVariable.envInfo.get("fetchURL").toString().isEmpty()) { //if fetchURL entry is empty, this is the starting URL, so store it
		GlobalVariable.envInfo.replace('fetchURL', fetchURL)
	}
	
	//---------------------------------------------------------------------------------------
	
	//launch formed url (base url + flag combination) in new session, or continue in existing
	if ((wentThroughLauncher == true) && (relaunchBrowser == true)) {
		
		CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
		WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : fetchURL, ('skipPrivacyPolicyMessage') : skipPrivacyPolicyMessage],FailureHandling.STOP_ON_FAILURE)
		
		//if (RunConfiguration.executionProfile.contains("2020 Metric") || RunConfiguration.executionProfile.contains("2020 Imperial") || RunConfiguration.executionProfile.contains("Eggo")) {
		if (RunConfiguration.executionProfile.contains("2020 Metric") 
			|| RunConfiguration.executionProfile.contains("2020 Imperial") 
			|| RunConfiguration.executionProfile.contains("Eggo")
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
	}
	else if ((wentThroughLauncher == true) && (relaunchBrowser == false)) {
		
		WebUI.navigateToUrl(fetchURL, FailureHandling.STOP_ON_FAILURE)
		
	}
	else if ((wentThroughLauncher == false)) {
		
		WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : fetchURL, ('skipPrivacyPolicyMessage') : skipPrivacyPolicyMessage],FailureHandling.STOP_ON_FAILURE)
		//if (RunConfiguration.executionProfile.contains("2020 Metric") || RunConfiguration.executionProfile.contains("2020 Imperial") || RunConfiguration.executionProfile.contains("Eggo")) {
		if (RunConfiguration.executionProfile.contains("2020 Metric") 
			|| RunConfiguration.executionProfile.contains("2020 Imperial") 
			|| RunConfiguration.executionProfile.contains("Eggo")
			|| RunConfiguration.executionProfile.contains("Content Generic Metric") 
			|| RunConfiguration.executionProfile.contains("Der Kreis (Gen Metric)") 
			|| RunConfiguration.executionProfile.contains("Aviva (Nobilia)") 
			){
			if(skipPrivacyPolicyMessage){
				WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_Actions_Common/acceptPrivacyPolicyMessage'), [:])
			}
		}
	}
	else {
		KeywordUtil.markErrorAndStop("Some error occured/unsupported combination encountered while launching url with flags.")
	}
	
}
else {
	
	WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('skipPrivacyPolicyMessage') : skipPrivacyPolicyMessage],FailureHandling.STOP_ON_FAILURE)
}
