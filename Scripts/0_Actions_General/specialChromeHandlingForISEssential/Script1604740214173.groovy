import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

// Copy the path to chromedriver.exe
String pathToChromeDriver = DriverFactory.getChromeDriverPath()
System.setProperty("webdriver.chrome.driver", pathToChromeDriver)
	  
// It is important that this chromeProfilePath ends with User Data and not with the profile folder (Profile 2)
String machineUsername = System.getProperty("user.name")
//String chromeProfilePath = "C:\\Users\\"+machineUsername+"\\AppData\\Local\\Google\\Chrome\\User Data\\";
ChromeOptions chromeProfile = new ChromeOptions();

chromeProfile.addArguments("--disable-features=IntensiveWakeUpThrottling,CalculateNativeWinOcclusion")
WebDriver driver = new ChromeDriver(chromeProfile);

WebUI.delay(GlobalVariable.minuteDelay)
WebUI.closeBrowser()
DriverFactory.changeWebDriver(driver)