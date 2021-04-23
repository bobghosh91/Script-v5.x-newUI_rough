import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser('')

WebUI.navigateToUrl('http://apps-canary.2020.net/Launcher/index.html')

WebUI.waitForPageLoad(30)

WebUI.maximizeWindow()

//List <String> allEnvironments = ["canary", "dev", "qa", "uat", "dev-aws", "prod-azure","staging-aws", "prod-aws",]
//List <String> allCustomers = ["2020 Metric", "2020 Imperial", "Cabinets.Com"]

KeywordUtil.logInfo(RunConfiguration.getProjectDir())

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/input_toggleForCustomerApps'))

for (String environment : allEnvironments) {
	
	WebUI.delay(2)
	
	CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/button_env', [('environment') : environment]))
	
	WebUI.delay(2)
	
	for (String customer : allCustomers) {
		
		if(customer.equalsIgnoreCase("2020 Metric") || customer.equalsIgnoreCase("2020 Imperial") || customer.equalsIgnoreCase("Cabinets.Com") ){
		
			CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/list_cutomerName', [('customer') : customer]))
			
			WebUI.delay(2)
			
			CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('0_Actions_General/PlatformLauncher-Page/div_idealSpaces'))
			
			WebUI.delay(2)
			
			WebUI.switchToWindowIndex(1)
			
			println (WebUI.getUrl())
			
			String appUrlsSheetPath = RunConfiguration.getProjectDir()+ "/Data Files/App-Urls.xlsx"
			
			String rowName =  customer+ "-" + environment
			
			String dataToWrite = WebUI.getUrl()
			
			CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'(appUrlsSheetPath, "URL", rowName, "URL", dataToWrite)
			
			WebUI.closeWindowIndex(1)
			
			WebUI.switchToWindowIndex(0)
			WebUI.delay(2)
		}
	}
}

WebUI.closeBrowser()