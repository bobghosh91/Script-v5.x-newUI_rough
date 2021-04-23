package genericFunctions
import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebElement
import org.testng.Assert as Assert

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import ch.qos.logback.core.util.FileUtil

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import internal.GlobalVariable as GlobalVariable

import java.io.File
import java.text.DecimalFormat

import javax.imageio.ImageIO as ImageIO
import ru.yandex.qatools.ashot.AShot as AShot
import ru.yandex.qatools.ashot.comparison.ImageDiff as ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer as ImageDiffer
import ru.yandex.qatools.ashot.Screenshot as Screenshot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies as ShootingStrategies
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider as WebDriverCoordsProvider
import ru.yandex.qatools.ashot.cropper.indent.IndentFilter as IndentFilter
import ru.yandex.qatools.ashot.cropper.indent.BlurFilter as BlurFilter
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper as IndentCropper
import java.awt.image.BufferedImage as BufferedImage
import org.sikuli.script.Screen as Screen
import org.sikuli.script.Pattern as Pattern
import org.sikuli.script.Region
import org.sikuli.basics.Settings
import org.sikuli.script.Finder as Finder
import org.sikuli.script.Match as Match

import de.sstoehr.harreader.HarReader
import de.sstoehr.harreader.model.Har
import de.sstoehr.harreader.model.HarEntry
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

class Assertions {

	DataProcessing dataProcessing = new DataProcessing ()
	Utilities utilities = new Utilities()
	DecimalFormat df = new DecimalFormat("##.##")
	int imgCheckCounter
	String result

	/**
	 * Verify web element with text is not visible on page.
	 * @param textToBeSearched string to be search on page.
	 */
	@Keyword
	def assertWebElementsWithTextNotVisibleOnUI(String textToBeSearched){
		List <WebElement> elements = DriverFactory.getWebDriver().findElements(By.xpath("//*[contains(text(), '" + textToBeSearched + "')]"))
		for (WebElement e : elements) {
			if (e.displayed) {
				Assert.assertFalse(e.displayed, '\n\n' + 'Some Web Elements with text, expected to be invisible, are actually visible on UI.' + '\n\n' + 'The provided text is : \"' + textToBeSearched + '\"\n\n' + 'The tag name of first such element found is : ' + e.getTagName() + '\n\n')
			}
		}
	}

	/**
	 * Verify text is present on page.
	 * @param expectedText string to be search on page.
	 */
	@Keyword
	def assertTextIsPresentInPageSource(String expectedText){
		boolean textPresent = DriverFactory.getWebDriver().getPageSource().contains(expectedText)
		println 'Expected text->'+ expectedText + '--textPresent->'+ textPresent
		Assert.assertTrue(textPresent, '\n\n' + 'The expected text is not present in page source.' + '\n\n' + 'Expected Text : \"' + expectedText + '\"\n\n')
	}

	/**
	 * Verify text is not present on page.
	 * @param textToBeAbsent string to be search absence on page.
	 */
	@Keyword
	def assertTextIsNotPresentInPageSource(String textToBeAbsent){
		boolean textPresent = DriverFactory.getWebDriver().getPageSource().contains(textToBeAbsent)
		println 'Text to Be Absent->'+ textToBeAbsent + '--textPresent->'+ textPresent
		Assert.assertFalse(textPresent, '\n\n' + 'The text to be absent is actually present in page source.' + '\n\n' + 'Text to be absent : \"' + textToBeAbsent + '\"\n\n')
	}

	/**
	 * Verify object is visible on page.
	 * @param object object to be search on page.
	 */
	@Keyword
	def assertTestObjectIsVisible(TestObject object, String objectDisplayName="default"){
		String errorMessage
		if((objectDisplayName.equals("default"))){
			errorMessage = '\n\n' + 'Expected TestObject is not found on UI.' + '\n\n' + 'Expected TestObject : ' + object.getObjectId() + "\n\n"
		}
		else {
			errorMessage = 'TestObject :'+objectDisplayName+' is expected to be visible on UI, actually it is not visible on UI.\n\n'
		}
		Assert.assertTrue(WebUI.waitForElementVisible(object, GlobalVariable.midWait,FailureHandling.STOP_ON_FAILURE), errorMessage )
	}

	/**
	 * Verify object is not visible on page.
	 * @param object object to be search absence on page.
	 * @param objectDisplayName is display name of object which should be expected to be not visible 
	 */
	@Keyword
	def assertTestObjectIsNotVisible(TestObject object, String objectDisplayName="default"){

		boolean elementPresent
		try{
			elementPresent = WebUI.verifyElementPresent(object,GlobalVariable.minuteDelay)
		}
		catch(Exception e){
			elementPresent = false
		}
		if(elementPresent == true) {
			boolean elementNotVisible
			try{
				elementNotVisible = WebUI.verifyElementNotVisible(object)
			}
			catch(Exception e){
				elementNotVisible = false
			}
			if (elementNotVisible == false) {
				if((objectDisplayName.equals("default"))){
					KeywordUtil.markFailedAndStop('TestObject expected to be invisible, actually found on UI.' + '\n\n' + 'TestObject expected to be invisible : ' + object.getObjectId() + "\n\n")
				}
				else {
					KeywordUtil.markFailedAndStop('TestObject :'+objectDisplayName+' is expected to be invisible, actually found on UI.\n\n')
				}
			}
		}
	}

	/**
	 * Verify test object with expected text is visible.
	 * @param textToBePresent string to be search on page.
	 */
	@Keyword
	def assertTestObjectTextIsPresentOnUi(TestObject object, String textToBePresent){

		String actualText = ""

		try {
			actualText = WebUI.getText(object)
		}
		catch (Exception e) {
			println "*******************" + e.message
			actualText = WebUI.getText(findTestObject(object.getObjectId()))
		}

		Assert.assertTrue(WebUI.getText(object).trim().equals(textToBePresent.trim()), '\n\n' + 'Text Appearing on UI for this test object, is not matching with the expected value.' + '\n\n' + 'TestObject : ' + object.getObjectId() + '\n\n' + 'Expected Text is : ' + textToBePresent + '\n\n' + 'Actual Text is : ' + WebUI.getText(object) + "\n\n")
	}

	/**
	 * Verify object is clickable on page.
	 * @param object object to be searched on page.
	 */
	@Keyword
	def assertTestObjectIsClickable(TestObject object){

		if (WebUI.verifyElementClickable(object) == false) {
			KeywordUtil.markFailedAndStop('\n\n' + 'Expected TestObject is not clickable on UI.' + '\n\n' + 'Expected TestObject is : ' + object.getObjectId() + "\n\n")
		}
	}

	/**
	 * Verify image is absent on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsAbsent (String pathOfImageToBeAbsent) {

		//assertImageIsAbsentUsingSikuli(pathOfImageToBeAbsent)
		assertImageIsAbsentUsingSelenium(pathOfImageToBeAbsent)
	}

	/**
	 * Verify image is absent on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsAbsentUsingSelenium (String pathOfImageToBeAbsent) {

		WebUI.delay(GlobalVariable.midWait)

		pathOfImageToBeAbsent = RunConfiguration.getProjectDir() + pathOfImageToBeAbsent

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeAbsent)

		TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)

		Pattern patternToBeFound = new Pattern(pathOfImageToBeAbsent)

		Finder findInUi = new Finder(ImageIO.read(SrcFile))

		findInUi.find(patternToBeFound)


		def didMatchFound = findInUi.hasNext()
		Match m = findInUi.next()

		KeywordUtil.logInfo('Was image match found ? : ' + didMatchFound)
		Double matchScore = 0.0

		if (didMatchFound) {

			result= 'IMG-ABSENCE_FAIL'
			imgCheckCounter = dataProcessing.recordImageInformationToFile(result, 'NA')

			ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeAbsent, m)
			utilities.reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeAbsent,1.0, imgCheckCounter, result)
			KeywordUtil.logInfo('\n\n' + 'Image expected to be absent, was actually found in UI. The image absence check failed.')
			KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
		}
		else{
			result = 'IMG-ABSENCE_PASS'
			imgCheckCounter = dataProcessing.recordImageInformationToFile(result, 'NA')

			ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeAbsent, m)
			utilities.reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeAbsent,0.0, imgCheckCounter, result)
			KeywordUtil.logInfo('\n\n' + 'Image expected to be absent, and it is not found in UI. The image absence check passed.')
			KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
		}
	}

	/**
	 * Verify image is absent on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsAbsentUsingSikuli (String pathOfImageToBeAbsent) {

		Screen screen = new Screen()

		pathOfImageToBeAbsent = RunConfiguration.getProjectDir() + pathOfImageToBeAbsent

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeAbsent)

		def screensToBeAnalyzed = screen.getNumberScreens()
		def currentScreen = 0

		KeywordUtil.logInfo("Screens To Be Analyzed : " + screensToBeAnalyzed)

		def attempt = 1
		def imageFound = false

		while ((imageFound == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Confirmation " + attempt + ": Asserting if image is absent in screen.")

			currentScreen = 0

			while (currentScreen < screensToBeAnalyzed) {

				Pattern patternToBeFound = new Pattern(pathOfImageToBeAbsent)
				Pattern uiPattern = new Pattern(screen.getScreen(currentScreen).capture().getImage())

				Finder findInUi = new Finder(uiPattern.getImage())

				findInUi.find(patternToBeFound)
				KeywordUtil.logInfo('=======================')

				def didMatchFound = findInUi.hasNext()

				KeywordUtil.logInfo('Was image match found in screen ' + currentScreen + '? ' + didMatchFound)
				Double matchScore = 0.0

				if (didMatchFound) {

					Match m = findInUi.next()

					Utilities utilities = new Utilities()
					ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImageIdentificationIssues(pathOfImageToBeAbsent, currentScreen.toString(), m)
					utilities.reportUnexpectedImagePresenceIssues(writtenScreenshots.get(0), pathOfImageToBeAbsent, m)
					KeywordUtil.markFailedAndStop('\n\n' + 'Image expected to be absent, was actually found in UI. The image absence check failed.' + '\n\n' + 'See following report for comparison : ' + RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_unexpectedImageFoundIssues.html' + "\n")
				}
				currentScreen = currentScreen + 1
			}

			attempt++
			WebUI.delay(GlobalVariable.minuteDelay)
		}
	}

	/**
	 * Verify image is present on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsPresent (String pathOfImageToBeFound, int tolerance, String instructionMessage="") {

		//assertImageIsPresentUsingSikuli(pathOfImageToBeFound, tolerance)
		assertImageIsPresentUsingSelenium(pathOfImageToBeFound, tolerance, instructionMessage)
	}

	/**
	 * Verify image is present on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsPresentUsingSelenium (String pathOfImageToBeFound, int tolerance, String instructionMessage) {

		if (GlobalVariable.updateReferenceImages == true) {

			Utilities utilities = new Utilities()
			utilities.takePartialScreenshotUsingSeleniumBasedOnMetaData(pathOfImageToBeFound)
		}

		pathOfImageToBeFound = RunConfiguration.getProjectDir() + pathOfImageToBeFound

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeFound)

		def attempt = 1
		def imageFound = false

		DataProcessing dataProcessing = new DataProcessing()

		while ((imageFound == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Attempt " + attempt + ": Asserting if image is present in screen.")

			TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)

			Pattern patternToBeFound = new Pattern(pathOfImageToBeFound)
			Finder findInUi = new Finder(ImageIO.read(SrcFile))

			findInUi.find(patternToBeFound)
			KeywordUtil.logInfo('=======================')

			def didMatchFound = findInUi.hasNext()
			Match m = findInUi.next()

			KeywordUtil.logInfo('Was image match found in screen ? : ' + didMatchFound)
			Double matchScore = 0.0

			if (didMatchFound) {

				KeywordUtil.logInfo(('Match Found with ' + (m.getScore() * 100)) + ' % (Threshold provided is ' + tolerance + '%)')
				matchScore = m.getScore() * 100


				if (matchScore > tolerance) {
					//Case- Above matching %
					result = 'IMG-PRESENCE_PASS'
					imageFound = true
					imgCheckCounter = dataProcessing.recordImageInformationToFile(result, String.valueOf(df.format(matchScore))+'/'+String.valueOf(tolerance))

					ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeFound, m)
					utilities.reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeFound, m.getScore(), imgCheckCounter, result, instructionMessage)
					utilities.captureImagePatternOnScreenLocation(pathOfImageToBeFound, m)

					KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
					break
				}
				else {
					//Case- below matching %
					result = 'IMG-PRESENCE_FAIL-DIFF'
					imageFound = true
					imgCheckCounter = dataProcessing.recordImageInformationToFile(result, String.valueOf(df.format(matchScore))+'/'+String.valueOf(tolerance))

					ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeFound, m)
					utilities.reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeFound, m.getScore(), imgCheckCounter, result, instructionMessage)
					KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
					break
				}

				findInUi.destroy()
			}
			attempt++
			WebUI.delay(GlobalVariable.minuteDelay)
		}

		if (imageFound == false) {
			result = 'IMG-PRESENCE_FAIL-NOTFOUND'
			imgCheckCounter = dataProcessing.recordImageInformationToFile(result, 'NA')

			Utilities utilities = new Utilities()
			ArrayList<String> writtenScreenshots = utilities.takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeFound)
			utilities.reportImageFindingIssues(writtenScreenshots, pathOfImageToBeFound, imgCheckCounter, result, instructionMessage)
			KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
		}
	}

	/**
	 * Verify image is present on UI.
	 * @param image path to be searched, and tolerance in int.
	 */
	@Keyword
	def assertImageIsPresentUsingSikuli (String pathOfImageToBeFound, int tolerance) {

		if (GlobalVariable.updateReferenceImages == true) {

			Utilities utilities = new Utilities()
			utilities.takePartialScreenshotUsingSikuliScreenBasedOnMetaData(pathOfImageToBeFound)
		}

		Screen screen = new Screen()
		pathOfImageToBeFound = RunConfiguration.getProjectDir() + pathOfImageToBeFound

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeFound)
		//KeywordUtil.logInfo("Reference Image Path : " + abcd)

		def screensToBeAnalyzed = screen.getNumberScreens()
		def currentScreen = 0

		KeywordUtil.logInfo("Screens To Be Analyzed : " + screensToBeAnalyzed)

		def attempt = 1
		def imageFound = false

		while ((imageFound == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Attempt " + attempt + ": Asserting if image is present in screen.")

			currentScreen = 0


			while (currentScreen < screensToBeAnalyzed) {

				KeywordUtil.logInfo("Looking in screen " + currentScreen + ".")

				Pattern patternToBeFound = new Pattern(pathOfImageToBeFound)
				Pattern uiPattern = new Pattern(screen.getScreen(currentScreen).capture().getImage())
				BufferedImage bI = screen.getScreen(currentScreen).capture().getImage()

				Finder findInUi = new Finder(uiPattern.getImage())

				findInUi.find(patternToBeFound)
				KeywordUtil.logInfo('=======================')

				def didMatchFound = findInUi.hasNext()

				KeywordUtil.logInfo('Was image match found in screen ' + currentScreen + '? ' + didMatchFound)
				Double matchScore = 0.0

				if (didMatchFound) {

					Match m = findInUi.next()

					KeywordUtil.logInfo(('Match Found with ' + (m.getScore() * 100)) + ' % (Threshold provided is ' + tolerance + '%)')
					matchScore = m.getScore() * 100

					if (matchScore > tolerance) {

						KeywordUtil.logInfo("Expected image found in UI. Image presence check passed.")
						imageFound = true
						KeywordUtil.logInfo("Image found in UI in current screen.")

						Utilities utilities = new Utilities()
						utilities.captureImagePatternOnScreenLocation(pathOfImageToBeFound, m)

						break
					}
					else {

						Utilities utilities = new Utilities()
						ArrayList<String> writtenScreenshots = utilities.findImagePatternAndClickUsingSikuliScreen(pathOfImageToBeFound, currentScreen.toString(), m)
						utilities.reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeFound, m.getScore())
						//KeywordUtil.markFailedAndStop('\n\n' + 'Expected Image found in UI, but not satisfying the comparison tolerance.' + '\n\n' + 'Actual matching percentage found : ' + matchScore + '\n\n' + 'Specified tolerance : ' + tolerance + '\n\n' + 'Actual vs Expected Image can be compared using following compariosn html :\n' + RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_imageCompareIssues.html' + '\n')
						KeywordUtil.markFailedAndStop('\n\n' + 'Expected Image found in UI, but not satisfying the comparison tolerance.' + '\n\n' + 'Actual matching percentage found : ' + matchScore + '\n\n' + 'Specified tolerance : ' + tolerance + '\n\n' + 'Actual vs Expected Image can be compared using following compariosn html :\n' + RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_imageCompareIssues.html' + '\n')
					}

					findInUi.destroy()
				}
				currentScreen = currentScreen + 1
			}

			attempt++
			WebUI.delay(GlobalVariable.minuteDelay)
		}

		if (imageFound == false) {

			Utilities utilities = new Utilities()
			ArrayList<String> writtenScreenshots = utilities.findImagePatternAndClickUsingSikuliScreen(pathOfImageToBeFound)
			utilities.reportImageFindingIssues(writtenScreenshots, pathOfImageToBeFound)
			KeywordUtil.markFailedAndStop('\n\n' + 'Expected Image NOT found in UI.' + '\n\n' + 'Expected Image portion which the automation script was trying to find in UI, can be compared with actual screen using following compariosn html : ' + RunConfiguration.getReportFolder() + '/TestCaseDumps/'  + GlobalVariable.currentTestCaseID + '_imageFindingIssues.html' + "\n")
		}
	}

	@Keyword
	def assertImagesEqualityUsingSikuli (String pathOfReferenceImageToBeFound, String pathOfActualImageToBeFound, int tolerance=100, String instructionMessage="") {

		if(!pathOfReferenceImageToBeFound.contains(':')){
			pathOfReferenceImageToBeFound = RunConfiguration.getProjectDir() + pathOfReferenceImageToBeFound
		}

		if(!pathOfActualImageToBeFound.contains(':')){
			pathOfActualImageToBeFound = RunConfiguration.getProjectDir() + pathOfActualImageToBeFound
		}

		KeywordUtil.logInfo("Reference Image Path : " + pathOfReferenceImageToBeFound)

		println("Actual Image Path : " + pathOfActualImageToBeFound)

		Pattern referencePatternToBeFound = new Pattern(pathOfReferenceImageToBeFound)
		Pattern actualPattern = new Pattern(pathOfActualImageToBeFound)

		Finder findInActualImage = new Finder(actualPattern.getImage())

		findInActualImage.find(referencePatternToBeFound)

		println('=======================')

		def didMatchFound = findInActualImage.hasNext()

		println('Was image match found? ' + didMatchFound)

		Double matchScore = 0.0

		if (didMatchFound) {

			Match m = findInActualImage.next()

			println(('Match Found with ' + (m.getScore() * 100)) + ' % (Threshold provided is ' + tolerance + '%)')
			matchScore = m.getScore() * 100

			if (matchScore >= tolerance) {

				println('\n\n' + 'Reference and actual images are matching. ' + 'Actual matching percentage found : ' + matchScore + '\n\n' + 'Specified tolerance : ' + tolerance + '\n\n')
			}
			else {
				Utilities utilities = new Utilities()

				result = 'IMG-PRESENCE_FAIL-DIFF'
				imgCheckCounter = dataProcessing.recordImageInformationToFile(result, String.valueOf(df.format(matchScore))+'/'+String.valueOf(tolerance))

				utilities.reportImageMatchPercentageIssues(pathOfActualImageToBeFound, pathOfReferenceImageToBeFound, m.getScore(), imgCheckCounter, result, instructionMessage)
				KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
			}

			findInActualImage.destroy()

		}
		else {


			result = 'IMG-PRESENCE_FAIL-NOTFOUND'
			imgCheckCounter = dataProcessing.recordImageInformationToFile(result, 'NA')

			Utilities utilities = new Utilities()
			ArrayList<String> writtenScreenshots = new ArrayList<String>()
			writtenScreenshots.add(pathOfActualImageToBeFound)
			utilities.reportImageFindingIssues(writtenScreenshots, pathOfReferenceImageToBeFound, imgCheckCounter, result, instructionMessage)
			KeywordUtil.logInfo(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/"+imgCheckCounter+"_"+result+".html")
		}

	}

	@Keyword
	def String assertPresenceOfPartialUrlInNetworkTrafficAndGetIt(String partialUrlThatIsConstant) {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		EnvironmentSetup environmentSetup = new EnvironmentSetup()

		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean urlFound = false

		String url = ""

		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {
				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				urlFound = true
				url = e.getRequest().getUrl()
			}
		}

		if (urlFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "Expected partial URL NOT found in the recorded network traffic." + "\n\n" + "Expected partial URL : " + partialUrlThatIsConstant + "\n")
		}

		return url
	}


	/** Following Keyword is related to get Request Payload from network traffic, Here we  are passing partialUrl with textToSearch which represents unique name from Post Data.
	 Here If we get more than one similar Url's(i.e. Design Summary) that time we are returning last Url requestPayload */
	@Keyword
	def String assertPresenceOfRequestPayloadInNetworkTrafficAndGetIt(String partialUrlThatIsConstant,String textToSearch) {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		EnvironmentSetup environmentSetup = new EnvironmentSetup()

		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean requestPayloadFound = false

		String requestPayload = ""

		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {
				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				if(e.getRequest().getPostData().getText() != null && e.getRequest().getPostData().getText().contains(textToSearch)){
					requestPayloadFound = true
					requestPayload = e.getRequest().getPostData().getText()
					println "requestPayload :- "+requestPayload
				}
			}
		}

		if (requestPayloadFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "Expected request Payload NOT found in the recorded network traffic." + "\n\n" + "Expected payload must contains : " + textToSearch + "\n")
		}

		return requestPayload
	}

	/** Following Keyword is related to get Request Payload from network traffic, Here we  are passing partialUrl with textToSearch which represents unique name from Post Data.
	 Here If we get more than one similar Url's(i.e. Design Summary), to identify correct Network call we are passing one more
	 parameter validateString which is unique word in Network Call and returning network call related to that unique validateString*/
	@Keyword
	def String assertPresenceOfRequestPayloadInNetworkTrafiWithUniqueValidateString(String partialUrlThatIsConstant,String textToSearch,String validateString) {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		EnvironmentSetup environmentSetup = new EnvironmentSetup()

		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean requestPayloadFound = false

		String requestPayload = ""

		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {
				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				if(e.getRequest().getPostData().getText() != null && e.getRequest().getPostData().getText().contains(textToSearch)){
					requestPayloadFound = true
					requestPayload = e.getRequest().getPostData().getText()
					println "requestPayload :- "+requestPayload

					if(requestPayload.contains(validateString)){
						break
					}
				}
			}
		}

		if (requestPayloadFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "Expected request Payload NOT found in the recorded network traffic." + "\n\n" + "Expected payload must contains : " + textToSearch + "\n")
		}

		return requestPayload
	}

	@Keyword
	def String assertPresenceOfStringsInUrlFromNetworkTrafficAndGetIt(List <String> expectedStrings) {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		EnvironmentSetup environmentSetup = new EnvironmentSetup()

		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean urlFound = false

		String url = ""

		for (HarEntry e : har2.getLog().getEntries()) {

			int count = 0

			for (String expectedString : expectedStrings) {

				if (e.getRequest().getUrl().contains(expectedString)) {

					//println ("Expected string : " + expectedString)
					//println ("URL found : " + e.getRequest().getUrl())

					count = count + 1
				}
				else {
					break
				}
			}

			if (count == expectedStrings.size()) {
				urlFound = true
				url = e.getRequest().getUrl()
				println ("------------------------------------")
				println ("Final URL : " + url)
				break
			}

			if (count > 0)
			{
				println ("------------------------------------")
			}

		}

		if (urlFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "One or all of the expected strings NOT found in any of the URLs, within the recorded network traffic." + "\n\n" + "Expected Strings : " + expectedStrings + "\n")
		}

		return url
	}

	@Keyword
	def Object assertPresenceOfPartialUrlAndGetResponse (String partialUrlThatIsConstant, String responseContentType = 'application/json') {

		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"

		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)

		EnvironmentSetup environmentSetup = new EnvironmentSetup()

		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)

		HarReader harReader = new HarReader()
		Har har2;

		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))

		boolean urlFound = false

		def response = ""
		int i = 0
		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {

				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				urlFound = true
				response = e.getResponse()

				try {
					if (response.content.mimeType.contains(responseContentType)) {
						//println (response.content.text)
						break
					}
				}
				catch (Exception ex) {

				}
			}

			i = i+ 1
		}

		if (urlFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "Expected partial URL NOT found in the recorded network traffic." + "\n\n" + "Expected partial URL : " + partialUrlThatIsConstant + "\n")
		}

		return response.content.text

	}

	@Keyword
	def assertAbsenceOfPartialUrlInNetworkTraffic(String partialUrlThatIsConstant) {
		String networkTrafficCollectionPath = RunConfiguration.getReportFolder() + "/" + GlobalVariable.currentTestCaseID + "_NetworkTraffic.har"
		println ("Network Traffic Collected at : " + networkTrafficCollectionPath)
		EnvironmentSetup environmentSetup = new EnvironmentSetup()
		net.lightbody.bmp.core.har.Har har1 = environmentSetup.bmpServer.get(GlobalVariable.currentTestCaseID + GlobalVariable.envInfo.get("browserName")).getHar()
		File harFile = new File(networkTrafficCollectionPath)
		har1.writeTo(harFile)
		HarReader harReader = new HarReader()
		Har har2;
		har2 = harReader.readFromFile(new File(networkTrafficCollectionPath))
		boolean urlFound = false
		String url = ""
		for (HarEntry e : har2.getLog().getEntries()) {

			//println (e.getRequest().getUrl())
			if (e.getRequest().getUrl().contains(partialUrlThatIsConstant)) {
				println ("Expected partial URL : " + partialUrlThatIsConstant)
				println ("Expected partial URL found in the recorded network traffic : " + e.getRequest().getUrl())

				urlFound = true
				url = e.getRequest().getUrl()
			}
		}

		if (urlFound == true) {
			KeywordUtil.markFailedAndStop('\n\n' + "Partial URL to be absent is actually found in the recorded network traffic." + '\n\n' + 'Partial URL to be absent : ' + partialUrlThatIsConstant + "\n\n" + "Actual URL found : " + url + "\n")
		}
	}
	@Keyword
	def String assertPresenceOfQueryParamInNetworkUrlAndGetTheValue (String url, String queryParam) {

		ArrayList<Integer> queryParamStartPositions = new ArrayList <Integer> ()

		for (int i = -1; (i = url.indexOf(queryParam, i + 1)) != -1; i++) {
			System.out.println(i);
			queryParamStartPositions.add(Integer.parseInt(i.toString()))
		}

		//println("AAAAAAAAAAAA- " + queryParamStartPositions.get(0))
		Assert.assertTrue(queryParamStartPositions.size() == 1, '\n\n' + queryParamStartPositions.size() + " occurances of expected query parameter(s) found in suppiled URL. Please check the network call manually." + "\n\n" + "Supplied URL : " + url + "\n\n" + "Expected query param : " + queryParam + "\n\n")

		String temp = url.substring(queryParamStartPositions.get(0))
		//println("AAAAAAAAAAAA- " + temp)

		String queryParamAndValue = temp

		try {
			queryParamAndValue = temp.substring(0, temp.indexOf("&"))
		}
		catch (Exception e) {
			println ("No \"&\" found, meaning that no multiple query params.")
		}
		//println("AAAAAAAAAAAA- " + queryParamAndValue)

		String [] queryParamAndValueSplitted = queryParamAndValue.split("=");

		//println("AAAAAAAAAAAA- " + queryParamAndValueSplitted[1])
		return queryParamAndValueSplitted[1]
	}

	@Keyword
	def String assertOnScreenLocationOfTestObject (TestObject testObject, int expectedXpixelLocationFromTopLeft, int expectedYpixelLocationFromTopLeft, int tolerance) {

		Point elementLocation = WebUiCommonHelper.findWebElement(testObject,GlobalVariable.midWait).getLocation()
		println('elementXposFromTopLeft ' + elementLocation.getX())
		println('elementYposFromTopLeft ' + elementLocation.getY())
		println('browserScreenWidth ' + DriverFactory.getWebDriver().manage().window().getSize().getWidth())
		println('browserScreenHeight ' + DriverFactory.getWebDriver().manage().window().getSize().getHeight())
		def xDiffPercentage = (Math.abs(elementLocation.getX() - expectedXpixelLocationFromTopLeft) * 100) * (1 / expectedXpixelLocationFromTopLeft)
		def yDiffPercentage = (Math.abs(elementLocation.getY() - expectedYpixelLocationFromTopLeft) * 100) * (1 / expectedYpixelLocationFromTopLeft)
		println(xDiffPercentage.getClass())
		println(yDiffPercentage.getClass())
		println(xDiffPercentage)
		println(yDiffPercentage)
		Assert.assertTrue(xDiffPercentage < tolerance, '\n\n' + 'Position difference ' + xDiffPercentage + '% - Actual X-pixel location of web element is ' + elementLocation.getX() + ', while expected one is ' + expectedXpixelLocationFromTopLeft + '\n\n')
		Assert.assertTrue(yDiffPercentage < tolerance, '\n\n' + 'Position difference ' + yDiffPercentage + '% - Actual Y-pixel location of web element is ' + elementLocation.getY() + ', while expected one is ' + expectedYpixelLocationFromTopLeft + '\n\n')

	}

	/**
	 * Verify specified point is in expected UI cell.
	 * @param testObject The object of which one of the corner point needs to be verified
	 * @param pointType For Test Object, can be "TOP-LEFT", "BOTTOM-RIGHT", "MIDDLE"; otherwise specify empty string i.e. ""
	 * @param numberOfRowsToSplitScreen Specify the no. of rows in which screen is to be divided
	 * @param numberOfColumnsToSplitScreen Specify the no. of columns in which screen is to be divided
	 * @param expectedCellRowNo Specify the expected row no. of cell, in which point is to be verified
	 * @param expectedCellColumnNo Specify the expected row no. of cell, in which point is to be verified
	 */
	@Keyword
	def String assertOnScreenLocationOfTestObjectInUiSection (TestObject testObject, String pointType, int numberOfRowsToSplitScreen, int numberOfColumnsToSplitScreen, int expectedCellRowNo, int expectedCellColumnNo) {

		//Point details

		HashMap <String, Integer> pointLocationMap = (new Utilities()).getXYValueOfTestObjectPoint(testObject, pointType)

		assertOnScreenLocationOfPointInUiSection(pointLocationMap, pointType, numberOfRowsToSplitScreen, numberOfColumnsToSplitScreen, expectedCellRowNo, expectedCellColumnNo)

	}

	/**
	 * Verify specified point is in expected UI cell.
	 * @param pointLocationMap A map, with expected keys as "x" and "y", with integer values respectively
	 * @param pointType For Test Object, can be "TOP-LEFT", "BOTTOM-RIGHT", "MIDDLE"; otherwise specify empty string i.e. ""
	 * @param numberOfRowsToSplitScreen Specify the no. of rows in which screen is to be divided 
	 * @param numberOfColumnsToSplitScreen Specify the no. of columns in which screen is to be divided
	 * @param expectedCellRowNo Specify the expected row no. of cell, in which point is to be verified
	 * @param expectedCellColumnNo Specify the expected row no. of cell, in which point is to be verified
	 */	
	@Keyword
	def String assertOnScreenLocationOfPointInUiSection (HashMap <String, Integer> pointLocationMap, String pointType = "", int numberOfRowsToSplitScreen, int numberOfColumnsToSplitScreen, int expectedCellRowNo, int expectedCellColumnNo) {

		//Screen division management and verifications

		Assert.assertTrue ((numberOfRowsToSplitScreen >= 2 && numberOfRowsToSplitScreen <= 10), "\n\n" + "The rows for screen division has to be in the range 2 to 10. Specified value is : " + numberOfRowsToSplitScreen + "\n\n")
		Assert.assertTrue ((numberOfColumnsToSplitScreen >= 2 && numberOfColumnsToSplitScreen <= 10), "\n\n" + "The columns for screen division has to be in the range 2 to 10. Specified value is : " + numberOfColumnsToSplitScreen + "\n\n")

		Assert.assertTrue ((expectedCellRowNo >= 1 && expectedCellRowNo <= 10), "\n\n" + "The expected cell row number (where the test object is expected) has to be in the range 1 to " + numberOfRowsToSplitScreen + ". Specified value is : " + expectedCellRowNo + "\n\n")
		Assert.assertTrue ((expectedCellColumnNo >= 1 && expectedCellColumnNo <= 10), "\n\n" + "The expected cell column number (where the test object is expected) has to be in the range 1 to " + numberOfColumnsToSplitScreen + ". Specified value is : " + expectedCellColumnNo + "\n\n")

		int browserWidth = DriverFactory.getWebDriver().manage().window().getSize().getWidth()
		int browserHeight = DriverFactory.getWebDriver().manage().window().getSize().getHeight()
		KeywordUtil.logInfo("browserWidth : " + browserWidth)
		KeywordUtil.logInfo("browserHeight : " + browserHeight)

		//Calculate ranges and position for presence of element in correct column

		HashMap <Integer, Range<Integer>> cellLimitsXdir = new HashMap <Integer, Range<Integer>>()
		int xStartLocal = 0; int xEndLocal = 0; cellLimitsXdir.put(0,xStartLocal)
		String actualColumnNoForElement = ""

		for (int i=1; i <= numberOfColumnsToSplitScreen; i++)	{

			xEndLocal = xStartLocal + browserWidth/numberOfColumnsToSplitScreen
			cellLimitsXdir.put(i, xStartLocal..xEndLocal)
			xStartLocal = xEndLocal
			KeywordUtil.logInfo("cell" + i + " Column Limits (Xdir) : " + cellLimitsXdir.get(i).getFrom() + " - " + cellLimitsXdir.get(i).getTo())

			if (cellLimitsXdir.get(i).contains(pointLocationMap.get("x"))) {
				actualColumnNoForElement = i.toString()
			}
		}

		//Calculate ranges and position for presence of element in correct row

		HashMap <Integer, Range<Integer>> cellLimitsYdir = new HashMap <Integer, Range<Integer>>()
		int yStartLocal = 0; int yEndLocal = 0; cellLimitsYdir.put(0,yStartLocal)
		String actualRowNoForElement = ""

		for (int i=1; i <= numberOfRowsToSplitScreen; i++)	{

			yEndLocal = yStartLocal + browserHeight/numberOfRowsToSplitScreen
			cellLimitsYdir.put(i, yStartLocal..yEndLocal)
			yStartLocal = yEndLocal
			KeywordUtil.logInfo("cell" + i + " Row Limits (Ydir) : " + cellLimitsYdir.get(i).getFrom() + " - " + cellLimitsYdir.get(i).getTo())

			if (cellLimitsYdir.get(i).contains(pointLocationMap.get("y"))) {
				actualRowNoForElement = i.toString()
			}
		}

		boolean pointFallsInExpectedCell = (cellLimitsXdir.get(expectedCellColumnNo).contains(pointLocationMap.get("x"))) && (cellLimitsYdir.get(expectedCellRowNo).contains(pointLocationMap.get("y")))

		String message = ""

		if (pointType.equals("")){

			message = "Test point (x=" + pointLocationMap.get("x") + ", y=" + pointLocationMap.get("y") + ") is outside the expected cell."
		}
		else {
			message = "Test object's \"" + pointType + "\" point (x=" + pointLocationMap.get("x") + ", y=" + pointLocationMap.get("y") + ") is outside the expected cell."
		}

		Assert.assertTrue(pointFallsInExpectedCell, "\n\n" + message +
				"\n\n" + "Expected Cell : row=" + expectedCellRowNo     + ", column=" + expectedCellColumnNo     +
				"\n\n" + "Actual Cell : row="   + actualRowNoForElement + ", column=" + actualColumnNoForElement +
				"\n\n")
	}

	/**
	 * Verify that the expected text is present in Image portion in expected UI cell.
	 * @param expectedText Expected text to be present in Image portion in expected UI cell
	 * @param numberOfRowsToSplitScreen Specify the no. of rows in which screen is to be divided
	 * @param numberOfColumnsToSplitScreen Specify the no. of columns in which screen is to be divided
	 * @param expectedCellRowNo Specify the expected row no. of cell, in which point is to be verified
	 * @param expectedCellColumnNo Specify the expected row no. of cell, in which point is to be verified
	 */
	@Keyword
	def String assertTextInImageInUiSection (String expectedText, int numberOfRowsToSplitScreen, int numberOfColumnsToSplitScreen, int expectedCellRowNo, int expectedCellColumnNo, boolean exactMatch=false ) {

		//Screen division management and verifications

		Assert.assertTrue ((numberOfRowsToSplitScreen >= 2 && numberOfRowsToSplitScreen <= 10), "\n\n" + "The rows for screen division has to be in the range 2 to 10. Specified value is : " + numberOfRowsToSplitScreen + "\n\n")
		Assert.assertTrue ((numberOfColumnsToSplitScreen >= 2 && numberOfColumnsToSplitScreen <= 10), "\n\n" + "The columns for screen division has to be in the range 2 to 10. Specified value is : " + numberOfColumnsToSplitScreen + "\n\n")

		Assert.assertTrue ((expectedCellRowNo >= 1 && expectedCellRowNo <= 10), "\n\n" + "The expected cell row number (where the test object is expected) has to be in the range 1 to " + numberOfRowsToSplitScreen + ". Specified value is : " + expectedCellRowNo + "\n\n")
		Assert.assertTrue ((expectedCellColumnNo >= 1 && expectedCellColumnNo <= 10), "\n\n" + "The expected cell column number (where the test object is expected) has to be in the range 1 to " + numberOfColumnsToSplitScreen + ". Specified value is : " + expectedCellColumnNo + "\n\n")

		int browserWidth = DriverFactory.getWebDriver().manage().window().getSize().getWidth()
		int browserHeight = DriverFactory.getWebDriver().manage().window().getSize().getHeight()
		KeywordUtil.logInfo("browserWidth : " + browserWidth)
		KeywordUtil.logInfo("browserHeight : " + browserHeight)

		//Calculate ranges for columns

		HashMap <Integer, Range<Integer>> cellLimitsXdir = new HashMap <Integer, Range<Integer>>()
		int xStartLocal = 0; int xEndLocal = 0; cellLimitsXdir.put(0,xStartLocal)
		String actualColumnNoForElement = ""

		for (int i=1; i <= numberOfColumnsToSplitScreen; i++)	{

			xEndLocal = xStartLocal + browserWidth/numberOfColumnsToSplitScreen
			cellLimitsXdir.put(i, xStartLocal..xEndLocal)

			KeywordUtil.logInfo("cell" + i + " Column Limits (Xdir) : " + cellLimitsXdir.get(i).getFrom() + " - " + cellLimitsXdir.get(i).getTo())

			if (expectedCellColumnNo == i) {break}

			xStartLocal = xEndLocal
		}

		//Calculate ranges for rows

		HashMap <Integer, Range<Integer>> cellLimitsYdir = new HashMap <Integer, Range<Integer>>()
		int yStartLocal = 0; int yEndLocal = 0; cellLimitsYdir.put(0,yStartLocal)
		String actualRowNoForElement = ""

		for (int i=1; i <= numberOfRowsToSplitScreen; i++)	{

			yEndLocal = yStartLocal + browserHeight/numberOfRowsToSplitScreen
			cellLimitsYdir.put(i, yStartLocal..yEndLocal)

			KeywordUtil.logInfo("cell" + i + " Row Limits (Ydir) : " + cellLimitsYdir.get(i).getFrom() + " - " + cellLimitsYdir.get(i).getTo())

			if (expectedCellRowNo == i) {break}

			yStartLocal = yEndLocal
		}

		TakesScreenshot scrShot =((TakesScreenshot) DriverFactory.getWebDriver())
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE)

		//FileUtils.copyFile(SrcFile, new File("D:/ScreenCapture.PNG"))

		BufferedImage bi = ImageIO.read(SrcFile)

		//println ("xStartLocal " + xStartLocal); println ("yStartLocal " + yStartLocal); println ("xEndLocal " + xEndLocal); println ("yEndLocal " + yEndLocal)

		//Below Delta's are to reduce with and/or height of cropped portion, as 'getSubimage' api cannot handle boundary values

		int deltaX, deltaY = 0

		if (numberOfRowsToSplitScreen == expectedCellRowNo) {

			deltaY = 135
		}
		if (numberOfColumnsToSplitScreen == expectedCellColumnNo) {

			deltaX = 20
		}

		BufferedImage biSub = bi.getSubimage(xStartLocal, yStartLocal , (xEndLocal - xStartLocal - deltaX), (yEndLocal - yStartLocal - deltaY))

		//BufferedImage biSub = bi.getSubimage(xStartLocal, yStartLocal , (xEndLocal - xStartLocal - deltaX), (200))

		File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (folderToBeWritten.exists() == false) {
			folderToBeWritten.mkdir()
		}

		ImageIO.write(biSub, "png", new File(RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_ImageForOCR.PNG'))

		Settings.OcrTextSearch = true

		Settings.OcrTextRead = true

		//Pattern patternToBeFound = new Pattern("D:/ScreenCaptureSubImage.PNG")
		Pattern patternToBeFound = new Pattern(biSub)

		String textExtractedByOCR = patternToBeFound.getImage().text()

		println (patternToBeFound.getImage().text())

		//	Assert.assertTrue((textExtractedByOCR.replaceAll("\\s","")).contains(expectedText.replaceAll("\\s","")), "\n\n" + "The text comparison ignoring spaces, is failed. " + "\n\n" + "Expected Text w/o spaces : " + expectedText.replaceAll("\\s","") + "\n\n" + "Actual Text w/o spaces : " + textExtractedByOCR.replaceAll("\\s","") + "\n\n")

		boolean flag = false

		if(exactMatch == false){
			if((textExtractedByOCR.replaceAll("\\s","")).contains(expectedText.replaceAll("\\s",""))){
				flag = true
			}
		}
		else{
			if((textExtractedByOCR.replaceAll("\\s","")).equals(expectedText.replaceAll("\\s",""))){
				flag = true
			}
		}

		if(flag==false){
			KeywordUtil.markErrorAndStop("\n\n" + "The text comparison ignoring spaces, is failed. " + "\n\n" + "Expected Text w/o spaces : " + expectedText.replaceAll("\\s","") + "\n\n" + "Actual Text w/o spaces : " + textExtractedByOCR.replaceAll("\\s","") + "\n\n")
		}

		KeywordUtil.logInfo('=======================')
	}


	@Keyword
	def String assertTextAbsentInImageInUiSection (String expectedText, int numberOfRowsToSplitScreen, int numberOfColumnsToSplitScreen, int expectedCellRowNo, int expectedCellColumnNo, boolean exactMatch=false ) {

		//Screen division management and verifications

		Assert.assertTrue ((numberOfRowsToSplitScreen >= 2 && numberOfRowsToSplitScreen <= 10), "\n\n" + "The rows for screen division has to be in the range 2 to 10. Specified value is : " + numberOfRowsToSplitScreen + "\n\n")
		Assert.assertTrue ((numberOfColumnsToSplitScreen >= 2 && numberOfColumnsToSplitScreen <= 10), "\n\n" + "The columns for screen division has to be in the range 2 to 10. Specified value is : " + numberOfColumnsToSplitScreen + "\n\n")

		Assert.assertTrue ((expectedCellRowNo >= 1 && expectedCellRowNo <= 10), "\n\n" + "The expected cell row number (where the test object is expected) has to be in the range 1 to " + numberOfRowsToSplitScreen + ". Specified value is : " + expectedCellRowNo + "\n\n")
		Assert.assertTrue ((expectedCellColumnNo >= 1 && expectedCellColumnNo <= 10), "\n\n" + "The expected cell column number (where the test object is expected) has to be in the range 1 to " + numberOfColumnsToSplitScreen + ". Specified value is : " + expectedCellColumnNo + "\n\n")

		int browserWidth = DriverFactory.getWebDriver().manage().window().getSize().getWidth()
		int browserHeight = DriverFactory.getWebDriver().manage().window().getSize().getHeight()
		KeywordUtil.logInfo("browserWidth : " + browserWidth)
		KeywordUtil.logInfo("browserHeight : " + browserHeight)

		//Calculate ranges for columns

		HashMap <Integer, Range<Integer>> cellLimitsXdir = new HashMap <Integer, Range<Integer>>()
		int xStartLocal = 0; int xEndLocal = 0; cellLimitsXdir.put(0,xStartLocal)
		String actualColumnNoForElement = ""

		for (int i=1; i <= numberOfColumnsToSplitScreen; i++)	{

			xEndLocal = xStartLocal + browserWidth/numberOfColumnsToSplitScreen
			cellLimitsXdir.put(i, xStartLocal..xEndLocal)

			if (expectedCellColumnNo == i) {break}

			xStartLocal = xEndLocal
			KeywordUtil.logInfo("cell" + i + " Column Limits (Xdir) : " + cellLimitsXdir.get(i).getFrom() + " - " + cellLimitsXdir.get(i).getTo())
		}

		//Calculate ranges for rows

		HashMap <Integer, Range<Integer>> cellLimitsYdir = new HashMap <Integer, Range<Integer>>()
		int yStartLocal = 0; int yEndLocal = 0; cellLimitsYdir.put(0,yStartLocal)
		String actualRowNoForElement = ""

		for (int i=1; i <= numberOfRowsToSplitScreen; i++)	{

			yEndLocal = yStartLocal + browserHeight/numberOfRowsToSplitScreen
			cellLimitsYdir.put(i, yStartLocal..yEndLocal)

			if (expectedCellRowNo == i) {break}

			yStartLocal = yEndLocal
			KeywordUtil.logInfo("cell" + i + " Row Limits (Ydir) : " + cellLimitsYdir.get(i).getFrom() + " - " + cellLimitsYdir.get(i).getTo())
		}

		TakesScreenshot scrShot =((TakesScreenshot) DriverFactory.getWebDriver())
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE)

		//FileUtils.copyFile(SrcFile, new File("D:/ScreenCapture.PNG"))

		BufferedImage bi = ImageIO.read(SrcFile)

		//println ("xStartLocal " + xStartLocal); println ("yStartLocal " + yStartLocal); println ("xEndLocal " + xEndLocal); println ("yEndLocal " + yEndLocal)

		BufferedImage biSub = bi.getSubimage(xStartLocal, yStartLocal, (xEndLocal - xStartLocal), (yEndLocal - yStartLocal))

		File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (folderToBeWritten.exists() == false) {
			folderToBeWritten.mkdir()
		}

		ImageIO.write(biSub, "png", new File(RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_ImageForOCR.PNG'))

		Settings.OcrTextSearch = true

		Settings.OcrTextRead = true

		//Pattern patternToBeFound = new Pattern("D:/ScreenCaptureSubImage.PNG")
		Pattern patternToBeFound = new Pattern(biSub)

		String textExtractedByOCR = patternToBeFound.getImage().text()

		println (patternToBeFound.getImage().text())

		//	Assert.assertTrue((textExtractedByOCR.replaceAll("\\s","")).contains(expectedText.replaceAll("\\s","")), "\n\n" + "The text comparison ignoring spaces, is failed. " + "\n\n" + "Expected Text w/o spaces : " + expectedText.replaceAll("\\s","") + "\n\n" + "Actual Text w/o spaces : " + textExtractedByOCR.replaceAll("\\s","") + "\n\n")

		boolean flag = false

		if(exactMatch == false){
			if((textExtractedByOCR.replaceAll("\\s","")).contains(expectedText.replaceAll("\\s",""))){
				flag = true
			}
		}
		else{
			if((textExtractedByOCR.replaceAll("\\s","")).equals(expectedText.replaceAll("\\s",""))){
				flag = true
			}
		}

		if(flag==true){
			KeywordUtil.markErrorAndStop("\n\n" + "The text absence check comparison ignoring spaces, is failed. " + "\n\n" + "Not expected Text w/o spaces : " + expectedText.replaceAll("\\s","") + "\n\n" + "Found Text w/o spaces : " + textExtractedByOCR.replaceAll("\\s","") + "\n\n")
		}

		KeywordUtil.logInfo('=======================')
	}

	@Keyword
	def  assertOnScreenLocationOfPointOnUI (int actualRPDElementX, int actualRPDElementY, int expectedX,int expectedY, int tolerance) {
		def xDiffPercentage = (Math.abs(actualRPDElementX - expectedX) * 100) * (1 / expectedX)
		def yDiffPercentage = (Math.abs(actualRPDElementY - expectedY) * 100) * (1 / expectedY)
		println(xDiffPercentage.getClass())
		println(yDiffPercentage.getClass())
		println(xDiffPercentage)
		println(yDiffPercentage)
		Assert.assertTrue(xDiffPercentage < tolerance, '\n\n' + 'Position difference ' + xDiffPercentage + '% - Actual X-pixel location of web element is ' + actualRPDElementX + ', while expected one is ' + expectedX + '\n\n')
		Assert.assertTrue(yDiffPercentage < tolerance, '\n\n' + 'Position difference ' + yDiffPercentage + '% - Actual Y-pixel location of web element is ' + actualRPDElementY + ', while expected one is ' + expectedY + '\n\n')
	}


	@Keyword
	def assertFileIsPresentAtLocationWithExtension(String locationPath,String fileExtension){
		int numberOfFiles = 0;
		try{
			File folder = new File(locationPath);
			File[] fList = folder.listFiles();
			for (int i = 0; i < fList.length; i++) {
				String pes = fList[i];
				if (pes.endsWith(fileExtension)) {
					numberOfFiles += numberOfFiles + 1
				}
			}
		}catch (Exception e)
		{
			KeywordUtil.markFailedAndStop(e.getMessage())
		}
		if(numberOfFiles > 1 || numberOfFiles < 1){
			KeywordUtil.markFailedAndStop('\n\n' + 'Either no file downloaded or More than one files downloaded/present at following location : \"' + locationPath + '\"\n\n' + 'and with extension : ' + fileExtension + '\n')
		}
	}

	@Keyword
	def valueGreaterThanAssertion(def valueToBeGreater, def valueToBeLower, String customMessage) {
		Assert.assertTrue(valueToBeGreater.getClass().equals(valueToBeGreater.getClass()) == true, "Type not matching for values to be compared. Please check inputs.")
		Assert.assertTrue(valueToBeGreater > valueToBeLower, "\n\n" + customMessage + ".\nValue expected to be greater : "+valueToBeGreater + ", whereas actual value is : "+ valueToBeLower + "\n\n")
	}

	@Keyword
	def valueLesserThanAssertion(def valueToBeLower, def valueToBeGreater, String customMessage) {
		Assert.assertTrue(valueToBeLower.getClass().equals(valueToBeGreater.getClass()) == true , "Type not matching for values to be compared. Please check inputs.")
		Assert.assertTrue(valueToBeLower < valueToBeGreater, "\n\n" + customMessage + ".\nValue expected to be lower : "+valueToBeLower + ", whereas actual value is : "+ valueToBeGreater + "\n\n")
	}

	@Keyword
	def valueEqualityAssertion(def firstValue, def secondValue, String customMessage) {
		Assert.assertTrue(firstValue.getClass().equals(secondValue.getClass()) == true , "Type not matching for values to be compared. Please check inputs.")
		Assert.assertTrue(firstValue == secondValue, "\n\n" + customMessage + ".\nValues expected to be equal are as follows : First value = "+firstValue + ", Second value = "+ secondValue + "\n\n")
	}

	@Keyword
	def valueUnequalityAssertion(def firstValue, def secondValue, String customMessage) {
		Assert.assertTrue(firstValue.getClass().equals(secondValue.getClass()) == true , "Type not matching for values to be compared. Please check inputs.")
		Assert.assertFalse(firstValue == secondValue, "\n\n" + customMessage + ".\nValues expected to be un-equal are as follows : First value = "+firstValue + ", Second value = "+ secondValue + "\n\n")
	}

	@Keyword
	def valueInIntegerRangeAssertion(int actualvalue, int expectedValue, int toleranceInPercentNo, String customMessage) {

		Assert.assertTrue(actualvalue.getClass().equals(expectedValue.getClass()) == true , "Type not matching for range boundaries. Please check inputs.")

		int delta = (int) ((expectedValue * toleranceInPercentNo)/100)
		int rangeStartNo = expectedValue - delta
		int rangeEndNo = expectedValue + delta

		Range<Integer> rangeForCurrentCase = rangeStartNo..rangeEndNo

		Assert.assertTrue(rangeForCurrentCase.contains(actualvalue) == true , "\n\n" + customMessage + ".\nActual value " + actualvalue + " not falling into the expected range based on tolerance. Range is [" + rangeStartNo + ", " + rangeEndNo + "]." + "\n\n")
	}
	@Keyword
	def assertValuesMatchingWithToleranceUnit(double value1, double value2, int tolerance, String customMessage =""){
		Assert.assertTrue((Math.abs(value1 - value2) <= tolerance), '\n\n'+customMessage+'\n\nvalue1 '+value1+'\nvalue2 '+value2+'\ntolerance '+tolerance+'\n\n')
	}
	@Keyword
	def assertValuesMatchingWithTolerancePercentage(double value1, double value2, int tolerancePercentage, String customMessage =""){
		int diffPercentage = (Math.abs(value1 - value2) * 100) * (1 / value2)
		println '------------>>'+diffPercentage
		Assert.assertTrue(diffPercentage < tolerancePercentage, "\n\n" + customMessage + "\n\nvalue1 "+value1+"\nvalue2 "+value2+"\ntolerance "+tolerancePercentage+"\n\n")
	}
}