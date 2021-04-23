import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.CapabilityType

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-features=IntensiveWakeUpThrottling,CalculateNativeWinOcclusion")

// Copy the path to chromedriver.exe
String pathToChromeDriver = DriverFactory.getChromeDriverPath()
System.setProperty("webdriver.chrome.driver", pathToChromeDriver)

if (GlobalVariable.currentTestCaseCaptureNetworkTraffic.toString().equalsIgnoreCase("true")){
	
	options.addArguments("--ignore-certificate-errors")
	options.setCapability(CapabilityType.PROXY, CustomKeywords.'genericFunctions.EnvironmentSetup.getBrowserMobProxy'())
	
	if (GlobalVariable.chromeBetaToUsedOnWindowsExecution.equals("true")) {
	
		options.setBinary(GlobalVariable.chromeBetaBinaryPathOnWindows);
	}
}

WebDriver driver = new ChromeDriver(options);
WebUI.delay(GlobalVariable.minuteDelay)
WebUI.closeBrowser()
DriverFactory.changeWebDriver(driver)