package genericFunctions
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.context.TestCaseContext

import java.text.SimpleDateFormat

import org.apache.commons.io.FileUtils
import org.apache.poi.ss.usermodel.Cell as Cell
import org.apache.poi.ss.usermodel.Row as Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Proxy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver
import com.kms.katalon.core.webui.driver.SmartWaitWebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.keyword.excel.ExcelKeywords

import de.sstoehr.harreader.HarReader
import de.sstoehr.harreader.model.Har
import de.sstoehr.harreader.model.HarEntry
import internal.GlobalVariable as GlobalVariable
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import net.lightbody.bmp.proxy.CaptureType

class EnvironmentSetup {

	//public static BrowserMobProxy bmpServer = new BrowserMobProxyServer()
	//public static Utilities utilities = new Utilities()

	//public BrowserMobProxy bmpServer = new BrowserMobProxyServer()
	public static HashMap <String, BrowserMobProxy> bmpServer = new HashMap <String, BrowserMobProxy> ()
	public Utilities utilities = new Utilities()

	//Update below variable value while taking run for mac and safari.
	public String macPassword = ''

	@Keyword
	def public String getCurrentTimeStamp () {

		def date = new Date()
		def sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
		//println sdf.format(date)
		return (sdf.format(date).toString())
	}

	@Keyword
	def Map <String, String> getOsNameAndVersion () {

		Map <String, String> osDetails = new HashMap <String, String> ()

		osDetails.put("osName", System.properties['os.name'].toLowerCase())
		osDetails.put("osVersion", System.properties['os.version'].toLowerCase())

		return osDetails
	}

	@Keyword
	def Map <String, String> getBrowserNameAndVersion () {

		println "Extracting browser name"

		WebDriver driver = DriverFactory.getWebDriver()
		Capabilities cap = ((SmartWaitWebDriver) DriverFactory.getWebDriver()).getCapabilities();

		String browserName = cap.getBrowserName().toLowerCase();
		String browserVersion = cap.getVersion().toString()

		Map <String, String> browserDetails = new HashMap <String, String> ()
		browserDetails.put("browserName", browserName)
		browserDetails.put("browserVersion", browserVersion)

		return browserDetails
	}

	@Keyword
	def boolean testCaseSupportedOnCurrentEnvironment (String nonSupportedEnvironments) {

		String currentOSName = GlobalVariable.envInfo.get('osName')

		int indexOfOsNameSeparator

		if (currentOSName.contains(' ')) {
			indexOfOsNameSeparator = currentOSName.indexOf(' ')
		} else if (currentOSName.contains('.')) {
			indexOfOsNameSeparator = currentOSName.indexOf('.')
		}

		currentOSName = currentOSName.substring(0, indexOfOsNameSeparator)

		String currentTestcaseBrowserName = GlobalVariable.envInfo.get('browserName')

		String currentPlatformEnv = GlobalVariable.platformEnv

		String currentExecutionProfile = RunConfiguration.executionProfile

		//Set current parameters in an array
		String[] arrayOfCurrentVariables = new String[4]

		(arrayOfCurrentVariables[0]) = currentOSName

		(arrayOfCurrentVariables[1]) = currentTestcaseBrowserName

		(arrayOfCurrentVariables[2]) = currentPlatformEnv

		(arrayOfCurrentVariables[3]) = currentExecutionProfile

		//Parse input and get number of parameters

		List<String> inputList=new ArrayList<String>();

		List<String> inputSubList=new ArrayList<String>();

		int numberOfParameters = 0

		String[] inputArray= nonSupportedEnvironments.split(';')

		inputList.addAll(Arrays.asList(inputArray))

		String nonSupportedEnv

		for (String inputOneCombination : inputList) {

			String[] inputSubArray= inputOneCombination.split('_')
			inputSubList.addAll(Arrays.asList(inputSubArray))
			numberOfParameters = inputSubList.size()

			//check if inputs are present in current environment setup
			int count=0

			for (String inputParameter : inputSubList) {
				for (String currentParameter : arrayOfCurrentVariables) {
					if (inputParameter.trim().equalsIgnoreCase(currentParameter)) {
						nonSupportedEnv += " "+inputParameter
						count++
						break
					}
				}
			}

			if (count == numberOfParameters) {
				KeywordUtil.markWarning('This test case is not supported for current environment : ' + nonSupportedEnv)
				//break
				return true
			}
			nonSupportedEnv =""
			inputSubList.clear()
		}
		return false
	}

	/**
	 * SetUp platform
	 * @param environment to set
	 */
	@Keyword
	def setUpPlatformProfileBase() {

		//println (RunConfiguration.executionProfile)

		String masterProfile = RunConfiguration.executionProfile

		if (RunConfiguration.executionProfile.contains("_")) {
			masterProfile = RunConfiguration.executionProfile.substring(0, RunConfiguration.executionProfile.indexOf("_"))
		}

		String myContext =  masterProfile + '-' + GlobalVariable.platformEnv

		println 'Context->'+ myContext

		DataProcessing dataProcessing = new DataProcessing ()
		EnvironmentSetup environmentSetup = new EnvironmentSetup ()

		if ((GlobalVariable.useUrlFromPlatformLauncherOverUrlFromSpreadsheet).toString().equals("false")) {
			GlobalVariable.URL = dataProcessing.readTestDataFromExcelFile('App-Urls', 'URL', 'Context', myContext, 'URL')
		}

		println 'GlobalVariable.URL->'+GlobalVariable.URL
		if(GlobalVariable.URL.equals('')){
			println "URL is missing in the Data sheet."
		}

		//Code for setting up Env info for TFS integration - START

		String testSuite = RunConfiguration.getExecutionSourceName()
		println "Test Suite Name : " + testSuite

		GlobalVariable.envInfo.put("initialTimeStamp", environmentSetup.getCurrentTimeStamp())
		println(GlobalVariable.envInfo.get("initialTimeStamp"))

		GlobalVariable.envInfo.put("osName", environmentSetup.getOsNameAndVersion().get("osName"))
		println(GlobalVariable.envInfo.get("osName"))

		GlobalVariable.envInfo.put('browserName', DriverFactory.getExecutedBrowser().toString().toLowerCase())
		println(GlobalVariable.envInfo.get("browserName"))

		if (GlobalVariable.envInfo.get("browserName").equals("null")) {

			try {
				
				String remoteBrowserName = RunConfiguration.executionProperties.get("drivers").get("preferences").get("Remote").get("browserName")
				
				if (remoteBrowserName.contains("MicrosoftEdge")) { remoteBrowserName = "edge chromium" }
				
				GlobalVariable.envInfo.put('browserName', remoteBrowserName)
			}
			catch (Exception e) {
				WebUI.openBrowser('')
				GlobalVariable.envInfo.put('browserName', environmentSetup.getBrowserNameAndVersion().get('browserName'))
				WebUI.closeBrowser()
			}
		}
	}

	/**
	 * Process the flag combination and form url
	 * @param capabilityName to set
	 */
	@Keyword
	def String processFlagCombinationAndFormUrl(String baseURL, String flagCombination) {

		//split flag list and store in list - start

		String flagCombinationProcessed = flagCombination.toString().replaceAll("\\?","")
		List<String> flagsProvidedByUser = new ArrayList<String>()

		if(flagCombinationProcessed.contains("&")){
			flagsProvidedByUser.addAll(Arrays.asList(flagCombinationProcessed.split("&")))
		}
		else {
			flagsProvidedByUser.add(flagCombinationProcessed)
		}

		//split flag list and store in list - end

		//form the url based on whether flags exist in url or not - start

		String fetchURL = baseURL
		String baseURLExistingFlags = ''

		if(baseURL.contains("?") == false) {

			// this if block will append the full flag combination after the index.html

			int indexOfHtmlEnding = baseURL.indexOf("/index.html") + "/index.html".length()

			String baseURLPart1 = baseURL.substring(0, indexOfHtmlEnding)
			String baseURLPart2 = baseURL.substring(indexOfHtmlEnding, baseURL.length())

			fetchURL = baseURLPart1 + '?' + flagCombinationProcessed + baseURLPart2
			println fetchURL
		}
		else {

			// this block will form the flags to add when base URL contains 1 or more flag(s)

			StringBuilder flagStringToInclude = new StringBuilder("")
			boolean flagExistsInUrl

			for(String flagByUser : flagsProvidedByUser) {

				if(baseURL.contains(flagByUser)){

					flagExistsInUrl = true
				}
				else {
					flagExistsInUrl = false
				}

				if(flagExistsInUrl == false){

					flagStringToInclude.append(flagByUser.toString())
					flagStringToInclude.append("&")
				}

			}

			// below section will insert/inject the flag combination or its subset, within the flag section present in url

			int indexOfHtmlEnding = baseURL.indexOf("/index.html?") + "/index.html?".length()

			String baseURLPart1 = baseURL.substring(0, indexOfHtmlEnding)
			String baseURLPart2 = baseURL.substring(indexOfHtmlEnding, baseURL.length())

			fetchURL = baseURLPart1 + flagStringToInclude + baseURLPart2
			println fetchURL
		}

		//form the url based on whether flags exist in url or not - end

		return fetchURL
	}

	/**
	 * Get New capability status based on AppConfig.xlsx under Data Files folder
	 * @param capabilityName to set
	 */
	@Keyword
	def boolean capabilityEnabledStatus(String capabilityName) {

		DataProcessing dataProcessing = new DataProcessing()

		String profilesApplicable = dataProcessing.readTestDataFromExcelFile('App-Config', 'App-Config', 'CapabilityName', capabilityName, 'ProfilesApplicable')
		String Way1_CapabilityEnabledStatus = dataProcessing.readTestDataFromExcelFile('App-Config', 'App-Config', 'CapabilityName', capabilityName, 'Way1_CapabilityEnabledStatus')
		String Way2_FeatureFlagsCombinationInUrl = dataProcessing.readTestDataFromExcelFile('App-Config', 'App-Config', 'CapabilityName', capabilityName, 'Way2_FeatureFlagsCombinationInUrl')
		//String Way3_ApplicationConfigJson = dataProcessing.readTestDataFromExcelFile('App-Config', 'App-Config', 'CapabilityName', 'newHamburgerMenu', 'Way3_ApplicationConfigJson')

		String masterProfile = RunConfiguration.executionProfile

		if (RunConfiguration.executionProfile.contains("_")) {
			masterProfile = RunConfiguration.executionProfile.substring(0, RunConfiguration.executionProfile.indexOf("_"))
		}

		if (profilesApplicable.contains(masterProfile)) {

			if ((Way1_CapabilityEnabledStatus.equalsIgnoreCase("Yes")) ||
			((GlobalVariable.URL).contains(Way2_FeatureFlagsCombinationInUrl))
			) {

				return true
			}
			else {

				return false
			}
		}
		else {

			return false
		}
	}

	@Keyword
	def String getCurrentAppNameFromProfileName () {

		String appToLaunch = RunConfiguration.executionProfile

		if (RunConfiguration.executionProfile.contains("_")) {
			appToLaunch = RunConfiguration.executionProfile.substring(0, RunConfiguration.executionProfile.indexOf("_"))
		}

		return appToLaunch
	}

	@Keyword
	def disableInfobarsForChromeIfApplicable(){

		/* To disable the info-bar from Chrome browser*/
		try {

			if((RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("browserType")).equals("CHROME_DRIVER")) {

				if (GlobalVariable.chromeDisableInfobars == true){

					RunConfiguration.setWebDriverPreferencesProperty('args',Arrays.asList('disable-infobars'))
				}
			}
			else {
				KeywordUtil.logInfo("Browser is other than Chrome. Thus, this function is not applicable.")
			}
		}
		catch (Exception e) {
			KeywordUtil.logInfo("Missing variable/other issue, while disabling the chrome info-bars. This step is not required for all automation projects.")
		}
	}

	@Keyword
	def nextTestCaseIdInTsSuiteFile (String fullSuiteName) {

		println RunConfiguration.getProjectDir()

		def TestSuiteEntity = new XmlParser().parse(new File (RunConfiguration.getProjectDir() + "/" + fullSuiteName + ".ts"))

		int testCaseCount = 0
		int currentTestCaseIndex = 0
		String currentTestCaseId = GlobalVariable.currentTestCaseID

		TestSuiteEntity.'testCaseLink'.each { testCase ->
			//println " testCaseId = " + testCase.testCaseId.text()
			//println " testCaseId = " + testCase.testCaseId.getClass()
			testCaseCount = testCaseCount + 1

			if (testCase.testCaseId.text().contains(currentTestCaseId)) {
				currentTestCaseIndex = testCaseCount - 1 //index starts with 0 for arrays, so subtracting 1
			}
		}

		//println " testCaseCount = " + testCaseCount
		//println " currentTestCaseIndex = " + currentTestCaseIndex
		//println " nextTestCase = " + TestSuiteEntity.testCaseLink.testCaseId[currentTestCaseIndex + 1].text()

		//if current test is not the last test in a suite, find next test case
		if (currentTestCaseIndex < (TestSuiteEntity.testCaseLink.testCaseId.size() - 1)) {

			GlobalVariable.nextTestCaseIdInTsSuiteFile = TestSuiteEntity.testCaseLink.testCaseId[currentTestCaseIndex + 1].text()
		}
	}

	@Keyword
	def int getTotalTestsForFreshRun (String fullSuiteName) {

		def TestSuiteEntity = new XmlParser().parse(new File (RunConfiguration.getProjectDir() + "/" + fullSuiteName + ".ts"))

		//def TestSuiteEntity = new XmlParser().parse(new File (RunConfiguration.getProjectDir() + "/Test Suites/CabinetsDotCom" + ".ts"))

		int testCaseCount = 0

		TestSuiteEntity.'testCaseLink'.each { testCase ->
			//println " testCaseId = " + testCase.isRun.text()

			if (testCase.isRun.text().contains("true")) {
				testCaseCount = testCaseCount + 1
			}
		}

		//println " testCaseCount = " + testCaseCount
		return testCaseCount
	}

	/**
	 * Validate name of tested version
	 * @param version to validate
	 */
	@Keyword
	def boolean validateTestedVersion(String version) {

		return GlobalVariable.version.equals(version) == true
	}

	@Keyword
	def terminateOpenWebDriversManually() {

		boolean safariDriverStatus = false

		try {
			safariDriverStatus = ((RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("browserType")).equals("SAFARI_DRIVER"))
		}
		catch (Exception e) {
			println "safari brower not applicable/open"
		}

		if (safariDriverStatus == false){
			try {
				DriverFactory.getWebDriver().close()
				DriverFactory.getWebDriver().quit()
			}
			catch (Exception e) {
				println "probably no brower session open"
			}
		}else{
			try {
				if (DriverFactory.getWebDriver().toString().contains("(null)") == false) {

					String webDriverClassName = DriverFactory.getWebDriver().getClass().toString()
					String browser = (webDriverClassName.substring(webDriverClassName.lastIndexOf(".") + 1).replaceAll("Driver", "")).toLowerCase()

					String browserProcess = ""
					String driverProcess = ""

					println "Browser type extracted from Katalon WebDriver : " + browser

					println "Previous/existing open browser session found, so closed it."

					if ((browser.equals("")==false) && (browser.contains("cie"))) {
						browserProcess = "iexplore"
						driverProcess = "IEDriverServer.exe"
					}
					else if ((browser.equals("")==false) && (browser.contains("cchrome"))) {
						browserProcess = "Google Chrome"
						driverProcess = "chromedriver.exe"
					}
					else if ((browser.equals("")==false) && (browser.contains("cgecko"))) {
						browserProcess = "Firefox"
						driverProcess = "geckodriver.exe"
					}
					else if ((browser.equals("")==false) && (browser.contains("csafari"))) {
						browserProcess = "Safari"
						driverProcess = "safaridriver.exe"
					}
					else if ((browser.equals("")==false) && (browser.contains("cedge"))) {
						browserProcess = "MicrosoftEdge"
						driverProcess = "MicrosoftWebDriver.exe"
					}

					try {
						if (System.getProperty("os.name").toLowerCase().trim().contains("win")) {

							if (browser.contains("cedge") == false) {
								DriverFactory.closeWebDriver()
							}
							//DriverFactory.closeWebDriver()

							println ("Terminating the leftover browser sessions and processes on Windows")
							Process procDriver = Runtime.getRuntime().exec("taskkill /F /T /IM "+ driverProcess)
							Process procIE = Runtime.getRuntime().exec("taskkill /F /T /IM "+ browserProcess + "*")
							procDriver.waitFor()
							procIE.waitFor()

							if (browser.contains("cedge") == true) {
								DriverFactory.closeWebDriver()
							}
						}
						else if (System.getProperty("os.name").toLowerCase().trim().contains("mac")) {

							if (browser.contains("cgecko") == false) {
								DriverFactory.closeWebDriver()
							}

							println ("Terminating the leftover browser sessions and processes on Mac")
							Process procDriver = Runtime.getRuntime().exec("killall -9 "+ driverProcess.replaceAll(".exe", ""))
							procDriver.waitFor()
							String[] cmds = [
								"pkill",
								"-f",
								browserProcess
							]
							Process procIE = Runtime.getRuntime().exec(cmds)
							procIE.waitFor()

							if (browser.contains("cgecko") == true) {
								DriverFactory.closeWebDriver()
							}
						}
					}
					catch (Exception exception) {
						//exception.printStackTrace()
						exception.getMessage()
					}
				}
			}
			catch (BrowserNotOpenedException) {
				println "No previous/existing open browser session found. Continuing with further test execution."
			}
		}
	}

	@Keyword
	def stopBrowserMobProxy() {

		try {

			bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).stop()
		}
		catch (Exception e) {
			println"Exception occoured while stoping bmpServer." + e.message
		}
	}

	@Keyword
	def removeBrowserMobProxyFromMap() {

		try {

			bmpServer.remove(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName"))
		}
		catch (Exception e) {
			println"Exception occoured while removing bmpServer from map." + e.message
		}
	}

	@Keyword
	def Proxy getBrowserMobProxy()  {

		//bmpServer.containsKey(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName"))

		stopBrowserMobProxy()

		if (bmpServer.containsKey(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")) == true) {

			bmpServer.replace(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName"), null)
			bmpServer.replace(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName"), new BrowserMobProxyServer())
		}
		else {

			bmpServer.put(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName"), new BrowserMobProxyServer())
		}

		try {
			bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).setTrustAllServers(true)
			//bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).enableHarCaptureTypes(CaptureType.REQUEST_CONTENT)
			bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT)
			bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).start()
		}
		catch (Exception e) {
			println"Exception occoured while setup the bmpServer" +e.message
		}

		int port = bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getPort()

		String specifyProxyIp = Inet4Address.getLocalHost().getHostAddress()

		// get the Selenium proxy object
		Proxy proxy  = ClientUtil.createSeleniumProxy(bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")));
		proxy.setHttpProxy(specifyProxyIp + ":" + port) //The port generated by server.start();
		proxy.setSslProxy(specifyProxyIp +  ":" + port)

		return proxy

	}
	@Keyword
	def setupForBrowserWithRespectToProxy(){

		disableProxySettingOnOS()

		if ((GlobalVariable.chromeBetaToUsedOnWindowsExecution.equals("true"))) {
			if (GlobalVariable.currentTestCaseCaptureNetworkTraffic.toString().equalsIgnoreCase("true")){
				if(GlobalVariable.anyWebDriverSessionOpen.toString().equalsIgnoreCase("false")){
					setupForBetaChromeWithProxy()
				}
			}else{
				if(GlobalVariable.anyWebDriverSessionOpen.toString().equalsIgnoreCase("false")){
					setupForBetaChromeWithoutProxy()
				}
			}
		}
		else{
			if (GlobalVariable.currentTestCaseCaptureNetworkTraffic.toString().equalsIgnoreCase("true")){
				if(GlobalVariable.anyWebDriverSessionOpen.toString().equalsIgnoreCase("false")){
					setupNormalBrowserWithProxy()
				}
			}
		}
	}

	def disableProxySettingOnOS() {

		if (GlobalVariable.envInfo.get("osName").contains("windows")){

			String [] disableCmd = [
				"wscript",
				"\"" + RunConfiguration.getProjectDir() + "/External Libraries/setProxy.wsf\"",
				"//job:disableProxyServer"
			]

			Runtime.getRuntime().exec(disableCmd)

		}
		else if (GlobalVariable.envInfo.get("osName").contains("mac")) {
			if ((RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("browserType")).equals("SAFARI_DRIVER")){

				String [] unsetWebProxy = [
					"/bin/bash",
					"-c",
					"echo " + macPassword + " | sudo -S networksetup -setwebproxystate Ethernet off"
				]
				String [] unsetSecureWebProxy = [
					"/bin/bash",
					"-c",
					"echo " + macPassword + " | sudo -S networksetup -setsecurewebproxystate Ethernet off"
				]
				executeCmdOnMacAndPrintMessage(unsetWebProxy)
				executeCmdOnMacAndPrintMessage(unsetSecureWebProxy)
			}
		}
		else {
			KeywordUtil.markErrorAndStop("OS Not supported for handling proxy related tests.")}

	}

	@Keyword
	def Map <String, String> setupForBetaChromeWithProxy () {

		println "Setting up the beta With Proxy"

		System.setProperty("webdriver.chrome.driver", RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("chromeDriverPath"))
		ChromeOptions optionsBeta = new ChromeOptions();
		optionsBeta.addArguments("--ignore-certificate-errors")
		optionsBeta.setCapability(CapabilityType.PROXY, getBrowserMobProxy())
		optionsBeta.setBinary(GlobalVariable.chromeBetaBinaryPathOnWindows);

		optionsBeta.addArguments("--disable-features=IntensiveWakeUpThrottling,CalculateNativeWinOcclusion")
		optionsBeta.addArguments("--enable-features=ReducedReferrerGranularity")

		DriverFactory.changeWebDriver(new ChromeDriver(optionsBeta))
		bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).newHar(GlobalVariable.currentTestCaseID + "_NetworkTraffic.har")
	}

	@Keyword
	def Map <String, String> setupForBetaChromeWithoutProxy () {

		println "Setting up the beta Without Proxy"

		System.setProperty("webdriver.chrome.driver", RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("chromeDriverPath"))
		ChromeOptions optionsBeta = new ChromeOptions();
		optionsBeta.setBinary(GlobalVariable.chromeBetaBinaryPathOnWindows);

		optionsBeta.addArguments("--disable-features=IntensiveWakeUpThrottling,CalculateNativeWinOcclusion")
		optionsBeta.addArguments("--enable-features=ReducedReferrerGranularity")

		DriverFactory.changeWebDriver(new ChromeDriver(optionsBeta))
	}

	@Keyword
	def setupNormalBrowserWithProxy() {

		String driverType = ""
		String remoteBrowserName = ""

		try {
			driverType = RunConfiguration.executionProperties.get("drivers").get("system").get("Remote").get("browserType")
			remoteBrowserName = RunConfiguration.executionProperties.get("drivers").get("preferences").get("Remote").get("browserName")
		}
		catch (Exception e) {
			//Some problem for fetching driver type
		}

		if (driverType.isEmpty()) {
			driverType = RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("browserType")
		}

		if (driverType.toLowerCase().contains("chrome") || remoteBrowserName.toLowerCase().contains("chrome")) {

			if (driverType.toLowerCase().contains("chrome")) {
				System.setProperty("webdriver.chrome.driver", RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("chromeDriverPath"))
			}
			RunConfiguration.executionProfile
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--ignore-certificate-errors")
			//options.addArguments("--proxy-server=localhost:" + port)
			//options.setCapability(CapabilityType.PROXY, proxy)
			options.setCapability(CapabilityType.PROXY, getBrowserMobProxy())

			options.addArguments("--disable-features=IntensiveWakeUpThrottling, CalculateNativeWinOcclusion")
			options.addArguments("--enable-features=ReducedReferrerGranularity")

			if (GlobalVariable.chromeBetaToUsedOnWindowsExecution.equals("true")) {

				options.setBinary(GlobalVariable.chromeBetaBinaryPathOnWindows);
			}

			if (driverType.toLowerCase().contains("remote")) {

				String remoteWebDriverUrl = RunConfiguration.executionProperties.get("drivers").get("system").get("Remote").get("remoteWebDriverUrl")

				WebDriver driver = new RemoteWebDriver(new URL(remoteWebDriverUrl), options)
				DriverFactory.changeWebDriver(driver)
			}
			else {
				WebDriver driver = new ChromeDriver(options)
				DriverFactory.changeWebDriver(driver)
			}
			//println (RunConfiguration.getExecutionGeneralProperties())

		} else if (driverType.toLowerCase().contains("firefox") || remoteBrowserName.toLowerCase().contains("firefox")) {

			if (driverType.toLowerCase().contains("firefox")) {
				System.setProperty("webdriver.gecko.driver", RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("geckoDriverPath"))
			}

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.PROXY, getBrowserMobProxy())

			FirefoxOptions opt = new FirefoxOptions();
			opt.merge(capabilities);

			if (driverType.toLowerCase().contains("remote")) {

				String remoteWebDriverUrl = RunConfiguration.executionProperties.get("drivers").get("system").get("Remote").get("remoteWebDriverUrl")

				WebDriver driver = new RemoteWebDriver(new URL(remoteWebDriverUrl), opt)
				DriverFactory.changeWebDriver(driver)
			}
			else {
				WebDriver driver = new FirefoxDriver(opt);
				DriverFactory.changeWebDriver(driver)
			}
			//println (RunConfiguration.getExecutionGeneralProperties())

		} else if (driverType.toLowerCase().contains("safari")) {

			Proxy proxy = getBrowserMobProxy()

			//println proxy.toString(); println proxy.getSslProxy()
			//println "ip - " + proxy.getSslProxy().substring(0, proxy.getSslProxy().indexOf(":"))
			//println "port - " + proxy.getSslProxy().substring(proxy.getSslProxy().indexOf(":") + 1)

			String ip = proxy.getSslProxy().substring(0, proxy.getSslProxy().indexOf(":"))
			String port = proxy.getSslProxy().substring(proxy.getSslProxy().indexOf(":") + 1)

			String [] increaseOpenFileLimit1 = [
				"/bin/bash",
				"-c",
				"echo " + macPassword + " | sudo -S sysctl -w kern.maxfiles=20480"
			]
			String [] increaseOpenFileLimit2 = [
				"/bin/bash",
				"-c",
				"echo " + macPassword + " | sudo -S sysctl -w kern.maxfilesperproc=18000"
			]
			String [] setupHttpProxy = [
				"/bin/bash",
				"-c",
				"echo " + macPassword + " | sudo -S networksetup -setwebproxy \"Ethernet\" " + ip + " " + port
			]
			String [] setupHttpsProxy = [
				"/bin/bash",
				"-c",
				"echo " + macPassword + " | sudo -S networksetup -setsecurewebproxy \"Ethernet\" " + ip + " " + port
			]
			String [] addCertificateToSysteRoot = [
				"/bin/bash",
				"-c",
				"echo " + macPassword + " | sudo -S security add-trusted-cert -k \"/Library/Keychains/System.keychain\" -d \"" + RunConfiguration.getProjectDir() + "/External Libraries/ca-certificate-rsa.cer\""
			]

			executeCmdOnMacAndPrintMessage(increaseOpenFileLimit1)
			executeCmdOnMacAndPrintMessage(increaseOpenFileLimit2)
			executeCmdOnMacAndPrintMessage(setupHttpProxy)
			executeCmdOnMacAndPrintMessage(setupHttpsProxy)
			executeCmdOnMacAndPrintMessage(addCertificateToSysteRoot)

			WebUI.openBrowser('')

			//			String [] revertOpenFileLimit1 = [
			//				"/bin/bash",
			//				"-c",
			//				"echo " + macPassword + " | sudo -S sysctl -w kern.maxfiles=12288"
			//			]
			//			String [] revertOpenFileLimit2 = [
			//				"/bin/bash",
			//				"-c",
			//				"echo " + macPassword + " | sudo -S sysctl -w kern.maxfilesperproc=10240"
			//			]
			//			String [] turnOffHttpProxy = [
			//				"/bin/bash",
			//				"-c",
			//				"echo " + macPassword + " | sudo -S networksetup -setwebproxystate \"Ethernet\" off"
			//			]
			//			String [] turnOffHttpsProxy = [
			//				"/bin/bash",
			//				"-c",
			//				"echo " + macPassword + " | sudo -S networksetup -setsecurewebproxystate \"Ethernet\" off"
			//			]
			//
			//			executeCmdOnMacAndPrintMessage(revertOpenFileLimit1)
			//			executeCmdOnMacAndPrintMessage(revertOpenFileLimit2)
			//			executeCmdOnMacAndPrintMessage(turnOffHttpProxy)
			//			executeCmdOnMacAndPrintMessage(turnOffHttpsProxy)

		} else if (driverType.toLowerCase().contains("edge")) {

			println ("In Edge config........")

			Proxy proxy = getBrowserMobProxy()

			String ip = proxy.getSslProxy().substring(0, proxy.getSslProxy().indexOf(":"))
			String port = proxy.getSslProxy().substring(proxy.getSslProxy().indexOf(":") + 1)

			String [] proxyEnableCmd = [
				"wscript",
				"\"" + RunConfiguration.getProjectDir() + "/External Libraries/setProxy.wsf\"",
				"//job:enableProxyServer",
				ip +":"+port
			]
			println "proxyEnableCmd-:"+proxyEnableCmd
			Runtime.getRuntime().exec(proxyEnableCmd)

			println System.getProperty("user.home")
			println RunConfiguration.getProjectDir() + "/External Libraries/ca-certificate-rsa.cer"

			File sourceFile = new File(RunConfiguration.getProjectDir() + "/External Libraries/ca-certificate-rsa.cer")
			File destinationFile = new File(System.getProperty("user.home") + "/ca-certificate-rsa.cer")
			FileUtils.copyFile(sourceFile,destinationFile)
			Thread.sleep(2000)

			String [] certInstallCmd = [
				"C:/Windows/System32/cmd.exe",
				"/c",
				"\"" + RunConfiguration.getProjectDir() + "/External Libraries/certInstall.bat.lnk\"",
				System.getProperty("user.home") + "/ca-certificate-rsa.cer",
			]
			println "certInstallCmd-:"+certInstallCmd
			Runtime.getRuntime().exec(certInstallCmd)

		} else {
			KeywordUtil.markError("Capturing Network Traffic for browsers other than Firefox, Chrome, Edge and Safari, is not supported yet.")
		}

		bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).newHar(GlobalVariable.currentTestCaseID + "_NetworkTraffic.har")

	}

	def executeCmdOnMacAndPrintMessage(String [] cmd) {

		String s = null

		Process p = Runtime.getRuntime().exec(cmd)
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// read the output from the command - message
		println("Here is the output of the command:\n");
		while ((s = stdInput.readLine()) != null) {
			System.out.println(s);
		}

		// read the output from the command - error
		println("Here is the output of the command:\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}

	}

	@Keyword
	def boolean verifyPresenceOfUrlInNetworkTraffic(String partialUrlThatIsConstant) {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		net.lightbody.bmp.core.har.Har har1 = bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean urlFound = false

		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {
				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				urlFound = true
			}
		}

		return urlFound
	}

	@Keyword
	def dumpNetworkLogsForCurrentTest() {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)
		net.lightbody.bmp.core.har.Har har1 = bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		println(har1)
		//bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).stop()

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		println("-----------------------------")
		println(har2.getLog().getCreator().getName())
		println(har2.getLog().getEntries().get(5).getRequest())
		println(har2.getLog().getEntries().get(5).getRequest().getUrl())

		println("-----------------------------")

		for (HarEntry e : har2.getLog().getEntries()) {
			//println (e.getRequest().getUrl())
		}
	}

	@Keyword
	def triggerChromeComponentsUpdate() {

		String browserVersion = GlobalVariable.envInfo.get("browserVersion")
		int browserMajorVersion = Integer.parseInt(browserVersion.substring(0, browserVersion.indexOf('.')))

		if (browserMajorVersion > 73) {

			DriverFactory.getWebDriver().get('chrome://components/')

			def components = DriverFactory.getWebDriver().findElements(By.className('button-check-update'))

			for (WebElement c : components) {

				try {
					c.click()
				}
				catch (Exception e) {
					println ("Exception occured while updating some component.")
				}
			}
		}

	}

	@Keyword
	def deleteBrowserData(String skipFileName){

		/*	if (GlobalVariable.envInfo.get("osName").contains("windows")){
		 String chromePath =  System.getProperty("user.home")+'//AppData//Local//Google//Chrome//User Data'
		 File userDataDir = new File(chromePath)
		 if (userDataDir.exists()){
		 this.utilities.removeDirectory(userDataDir,skipFileName)
		 }
		 }
		 else if (GlobalVariable.envInfo.get("osName").equals("mac")) {
		 // not implemented
		 }*/
	}

	@Keyword
	def downloadExecutionTracker() {

		println ("Downloading test execution tracker document")

		if (System.getProperty("os.name").toLowerCase().trim().contains("win")) {

			try {

				for (int downloadAttempts = 1; downloadAttempts <= 3; downloadAttempts++) {

					Process vbScriptProcess = Runtime.getRuntime().exec("taskkill /F /T /IM wscript.exe")
					vbScriptProcess.waitFor()

					print ("Download attempt " + downloadAttempts)

					File vbsOutput = new File(RunConfiguration.getProjectDir() + "/External Libraries/downloadExecutionTracker/output.txt")
					//println vbsOutput.exists()
					if (vbsOutput.exists()) {vbsOutput.delete()}

					File dir = new File(RunConfiguration.getProjectDir() + "/External Libraries/downloadExecutionTracker")
					String[] cmds = [
						"wscript",
						"DownloadTracker.vbs"
					]

					Process trackerDownloadProcess = Runtime.getRuntime().exec(cmds, null, dir)
					//trackerDownloadProcess.waitFor()

					for (int fileExistCheckAttempts = 1; fileExistCheckAttempts <= 20; fileExistCheckAttempts++) {

						if (vbsOutput.exists()) {break}
						else {sleep(1000)}
						print (".")
					}

					print ("\n")

					if (vbsOutput.exists()) {

						println ("Downloaded test execution tracker successfully")
						break
					}
				}

			}
			catch( IOException e ) {
				System.out.println(e.message)
			}

		}
		else if (System.getProperty("os.name").toLowerCase().trim().contains("mac")) {

		}

		println "end"

	}

	@Keyword
	def enforceProfileVariables () {

		DataProcessing dataProcessing = new DataProcessing()

		String currentEnv = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/testExecutionConfig.properties", "platformEnv")

		if (currentEnv != null) {

			if (currentEnv.isEmpty() == false) {

				GlobalVariable.platformEnv = currentEnv
			}
		}
	}

	@Keyword
	def setupTestExecutionPattern (String suiteName) {

		//File testExecutionPattern = new File("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties")

		//if(testExecutionPattern.exists()) {

		//	FileUtils.forceDelete(testExecutionPattern)
		//}

		//FileUtils.copyFile(new File("./External Libraries/testExecutionPattern.properties"), new File("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties"))

		DataProcessing dataProcessing = new DataProcessing()

		String startingTestPosition = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties", "startingTestPosition")
		String testExecutionOffset = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties", "testExecutionOffset")

		if (startingTestPosition.isEmpty() == false) { GlobalVariable.envInfo.put("startingTestPosition", startingTestPosition) }
		else { GlobalVariable.envInfo.put("startingTestPosition", "1") }

		println GlobalVariable.envInfo.get("startingTestPosition")

		if (testExecutionOffset.isEmpty() == false) { GlobalVariable.envInfo.put("testExecutionOffset", testExecutionOffset) }
		else { GlobalVariable.envInfo.put("testExecutionOffset", "1") }

		println GlobalVariable.envInfo.get("testExecutionOffset")
	}

	@Keyword
	def boolean executionCriterionSatisfied (HashMap <String, Object> workItemFields, ArrayList <String> executionScopes) {

		boolean toBeExecuted = false

		if ((executionScopes == null) || (executionScopes.isEmpty()==true)) {
			return true
		}

		for (String executionScope : executionScopes) {

			String workItemFieldValue = (workItemFields.get(executionScope.split("=")[0]).toString())
			String executionScopeValue = (executionScope.split("=")[1]).toString()

			if (workItemFieldValue.equals(executionScopeValue)) {
				toBeExecuted = true
				break
			}
		}

		return toBeExecuted
	}

	@Keyword
	def HashMap<String, String> getMapOfTcsToSkipFromTracker(String currentSuiteName){

		List<String>suiteName= GlobalVariable.associatedSuiteNames

		if(suiteName.contains(currentSuiteName)) {

			String sheetName = GlobalVariable.trackerDataSheetName
			String projectPath = RunConfiguration.getProjectDir()
			String dataSheetPath = projectPath + '/Data Files/Execution readiness tracker of all test cases.xlsx'
			//		String currentProfileName = RunConfiguration.executionProfile
			String currentBrowserName = GlobalVariable.envInfo.get('browserName')
			String environmentDetailsColumnName = (GlobalVariable.platformEnv + '-') + currentBrowserName
			//String automationStatusColumnName = "TFS Automation status"
			String fileNameWithFilePath = dataSheetPath
			Map<String, String> mapTcAndReason =  new HashMap<String, String>()

			new File(fileNameWithFilePath).withInputStream({ def inputStream ->
				Workbook workbook = new XSSFWorkbook(inputStream)

				Sheet sheet = workbook.getSheet(sheetName)

				if (sheet == null) {
					throw new IllegalArgumentException('Invalid sheet name')
				}

				int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum()

				int rowNumber = 0

				Row row = sheet.getRow(0)

				DataProcessing dataProcessing = new DataProcessing()

				int environmentToSelectColumnNumber = dataProcessing.findColumnIndex(row, environmentDetailsColumnName)

				//int automationStatusColumnNumber = dataProcessing.findColumnIndex(row, automationStatusColumnName)

				KeywordUtil.logInfo('ColumnNumber to be used found is :' + environmentToSelectColumnNumber)

				//KeywordUtil.logInfo('automationStatusColumnNumber found is :' + automationStatusColumnNumber)

				//				List<String> testIDsToBeSkipped = new ArrayList<String>()
				//				List<String> testIDsToBeSkippedWithReason = new ArrayList<String>()

				def cellValue

				for (int i = 1; i < (rowCount + 1); i++) {
					row = sheet.getRow(i)

					if (row != null) {
						Cell environment = row.getCell(environmentToSelectColumnNumber)

						//Cell automationStatus = row.getCell(automationStatusColumnNumber)

						Cell cell

						if (dataProcessing.isCellNonEmpty(environment)/*|| (dataProcessing.isCellNonEmpty(automationStatus) && (!(automationStatus.getStringCellValue().equalsIgnoreCase("Yes"))))*/) {

							String reason = ''

							if(dataProcessing.isCellNonEmpty(environment)){
								reason = ExcelKeywords.getCellValue(row.getCell(environmentToSelectColumnNumber))
								cellValue = ExcelKeywords.getCellValue(row.getCell(0))
							}
							//else if(!(automationStatus.getStringCellValue().equalsIgnoreCase("Yes"))){
							//	reason = ExcelKeywords.getCellValue(row.getCell(automationStatusColumnNumber))
							//	cellValue = ExcelKeywords.getCellValue(row.getCell(0))
							//}

							String testID
							testID = cellValue.toString().trim()
							mapTcAndReason.put(testID,reason)
							//						cellValue = ExcelKeywords.getCellValue(row.getCell(0))
							//						testIDsToBeSkipped.add(cellValue.toString().trim())
							//						testIDsToBeSkippedWithReason.add(cellValue.toString().trim()+'-'+reason)
						}
					}
				}
				println('Total number of test cases  to be skipped are :' + mapTcAndReason.size())
				KeywordUtil.logInfo('Total number of test cases  to be skipped are :' + mapTcAndReason.size())
				KeywordUtil.logInfo('List of test cases with reason to be skipped for suite  :'+currentSuiteName+" are :")
				println('List of test cases with reason to be skipped for suite  :'+currentSuiteName+" are :")
				for (Map.Entry<String,String> entry : mapTcAndReason.entrySet()){
					println("Key = " + entry.getKey() + ", Value = " + entry.getValue())
					KeywordUtil.logInfo("Key = " + entry.getKey() + ", Value = " + entry.getValue())
				}
				return mapTcAndReason
			})
		}
	}
}