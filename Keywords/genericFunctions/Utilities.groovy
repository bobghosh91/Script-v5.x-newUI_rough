package genericFunctions

import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.*
import java.awt.Color
import java.awt.Graphics
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage as BufferedImage
import javax.imageio.ImageIO as ImageIO
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern as regPattern

import javax.imageio.ImageIO as ImageIO
import javax.swing.JOptionPane

import org.apache.commons.io.FileUtils
import org.openqa.selenium.By as By
import org.openqa.selenium.Dimension as Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.Point
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.html5.LocalStorage as LocalStorage
import org.openqa.selenium.html5.WebStorage as WebStorage
import org.openqa.selenium.interactions.Actions
import org.sikuli.basics.Settings
import org.sikuli.script.Button
import org.sikuli.script.Finder as Finder
import org.sikuli.script.Match as Match
import org.sikuli.script.Pattern as Pattern
import org.sikuli.script.Screen as Screen
import org.testng.Assert

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.transform.Synchronized

import org.openqa.selenium.Point
import org.openqa.selenium.TakesScreenshot

import internal.GlobalVariable
import ru.yandex.qatools.ashot.AShot as AShot
import ru.yandex.qatools.ashot.Screenshot as Screenshot
import ru.yandex.qatools.ashot.comparison.ImageDiff as ImageDiff
import ru.yandex.qatools.ashot.comparison.ImageDiffer as ImageDiffer
import ru.yandex.qatools.ashot.shooting.ShootingStrategies as ShootingStrategies
import org.openqa.selenium.Dimension as Dimension
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper

import javax.swing.JOptionPane

// ------- R&D
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO

import sun.misc.BASE64Decoder;
import tfsIntegration.TfsWebServices

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

public class Utilities {

	Map <String, String> programmaticGlobalMap = new HashMap <String, HashMap> ()
	static def currentScreen = 0

	/**
	 * Wait for object to be visible
	 * @param waitObject object to track
	 */
	@Keyword
	def waitForObjectToBeVisible(TestObject object) {

		def elementVisibility = false

		def attempt = 1

		while ((elementVisibility == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Attempt " + attempt + " - Katalon's visibility check for Test Object \"" + object.getObjectId() + "\".")

			try {

				elementVisibility = WebUI.waitForElementVisible(object, GlobalVariable.midWait)
				attempt++
			} catch (Exception e) {
				KeywordUtil.logInfo(e.getMessage())
				attempt++
			}
		}

		if (elementVisibility != true) {
			KeywordUtil.markErrorAndStop("Error occured while waiting for following UI Object to be visible \"" + object.getObjectId() + "\"")
		}
	}

	/**
	 * Wait for element to be not visible
	 * @param waitObject object to track
	 */ // waitTillTestObjectDisappers
	@Keyword
	def waitTillTestObjectDisappers(TestObject waitObject, int waitAttempts = GlobalVariable.waitAttempts) {
		def disappear = false
		def attempt = 0

		if (WebUI.waitForElementPresent(waitObject,10)== true)
		{
			try {
				while ((disappear == false) && (attempt < waitAttempts)) {
					disappear = WebUI.waitForElementNotPresent(waitObject, GlobalVariable.maxWait)
					attempt++
				}
			} catch (WebElementNotFoundException e) {
				KeywordUtil.markFailed("Element found : " + waitObject)
			}
		}
	}

	/**
	 * Wait for object to be clickable and click
	 * @param waitObject object to track
	 */
	@Keyword
	def waitForObjectToBeClickableAndClick(TestObject object) {

		def elementClickabilityCheck = false

		def attempt = 1

		while ((elementClickabilityCheck == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Clickability Attempt " + attempt + " - Katalon's clickability check for Test Object \"" + object.getObjectId() + "\".")

			try {
				elementClickabilityCheck = WebUI.waitForElementClickable(object, GlobalVariable.midWait)
				attempt++
			} catch (Exception e) {
				KeywordUtil.logInfo(e.getMessage())
				attempt++
			}
		}

		if (elementClickabilityCheck != true) {
			KeywordUtil.markErrorAndStop("Error occured while waiting for object to be clickable - \"" + object.getObjectId() + "\"")
		}

		KeywordUtil.logInfo("------------------------------")

		def elementClickCheck = false

		attempt = 1

		while ((elementClickCheck == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Actual Click Attempt " + attempt + " - Katalon's click attempt for Test Object \"" + object.getObjectId() + "\".")

			int i = 1

			while (i <= GlobalVariable.midWait) {

				try {
					WebUI.click(object,FailureHandling.STOP_ON_FAILURE)
					elementClickCheck = true
					break
				} catch (Exception e) {
					//KeywordUtil.logInfo(e.getMessage())
					KeywordUtil.logInfo("Click sub-attempt no. " + i)
					i++
					WebUI.delay(1)
				}
			}
			attempt++
		}

		if (elementClickCheck != true) {
			KeywordUtil.markErrorAndStop("Error occured while attempting to click the object - \"" + object.getObjectId() + "\"")
		}
	}

	/**
	 * Wait for object to be clickable
	 * @param waitObject object to track
	 */
	@Keyword
	def waitForObjectToBeClickable(TestObject object) {

		def elementClickabilityCheck = false

		def attempt = 1

		while ((elementClickabilityCheck == false) && (attempt <= GlobalVariable.waitAttempts)) {

			KeywordUtil.logInfo("Clickability Attempt " + attempt + " - Katalon's clickability check for Test Object \"" + object.getObjectId() + "\".")

			try {
				elementClickabilityCheck = WebUI.waitForElementClickable(object, GlobalVariable.midWait)
				attempt++
			} catch (Exception e) {
				KeywordUtil.logInfo(e.getMessage())
				attempt++
			}
		}

		if (elementClickabilityCheck != true) {
			KeywordUtil.markErrorAndStop("Error occured while waiting for object to be clickable - \"" + object.getObjectId() + "\"")
		}
	}

	/**
	 * Wait for test object text to become an expected one
	 * @param to object to track
	 * @param expectedText text to expect
	 * @param waitAttempts optional
	 */
	@Keyword
	def void waitTillTestObjectTextBecomes(TestObject to, String expectedText, int waitAttempts = GlobalVariable.waitAttempts){

		boolean textSeen = false
		def attempt = 1
		def actualMessage = ''

		while ((attempt <= waitAttempts) && (textSeen == false)){
			println 'attempt : ' +attempt
			int counter = 0
			while(counter < GlobalVariable.midWait) {
				actualMessage = WebUI.getText(to)
				if(actualMessage.equals(expectedText)) {
					textSeen = true
					break
				}else{
					WebUI.delay(1)
					counter = counter + 1
				}
				println '------------------visibility sub-attempt'
			}
			attempt++
		}

		if (textSeen == false) {
			KeywordUtil.markErrorAndStop("\n\n" + "Error occured while waiting for text of Test Object to become expected one." + "\n\n" + "Expected Text : " + expectedText + "\n\n" + "Actual Text : " + actualMessage + "\n\n")
		}
	}

	@Keyword
	def public String getProgrammaticGlobalMap () {

		return programmaticGlobalMap;
	}

	@Keyword
	def String executeJavaScript () {

		WebDriver driver = DriverFactory.getWebDriver()
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert('Welcome to Test site');");
	}

	/**
	 * Get default project name.
	 * @param defaultProjectName Default project name
	 */
	@Keyword
	String getdefaultProjectName() {

		EnvironmentSetup environmentSetup = new EnvironmentSetup()
		return 'Project_'+ environmentSetup.getCurrentTimeStamp()
	}

	@Keyword
	def String takeElementScreenshot (TestObject objectWithXpath, String screenshotPartialName) {

		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.scaling(GlobalVariable.scalingFactorWin10)).takeScreenshot(DriverFactory.getWebDriver(), 		DriverFactory.getWebDriver().findElement(By.xpath(objectWithXpath.findPropertyValue("xpath"))))

		File screenshotsFolder = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (screenshotsFolder.exists() == false) {
			screenshotsFolder.mkdir()
		}

		String imagePath = screenshotsFolder.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID  + '_' + screenshotPartialName + '.png'
		println("--------------" + imagePath)
		ImageIO.write(screenshot.getImage(), 'PNG', new File(imagePath))

		return imagePath
	}

	def ArrayList<String> takeFullScreenshotForImgFindIssuesUsingSikuli(String referenceImagePath, String screenNoToBeDumped=null, Match m=null) {

		Screen screen = new Screen()
		ArrayList<String> writtenScreenshots = new ArrayList<String> ()

		def screensToBeAnalyzed = screen.getNumberScreens()

		File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (folderToBeWritten.exists() == false) {
			folderToBeWritten.mkdir()
		}

		if (m == null) {

			if (screenNoToBeDumped==null) {

				for (int i=0; i<screensToBeAnalyzed; i++) {

					String screenshotName = (new File(referenceImagePath)).getName().replace(".png", "").replace(".PNG", "")
					File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '_ActualScreen_' + i.toString() + '.png')
					ImageIO.write(screen.getScreen(i).capture().getImage(), "PNG", screenshotToBeWritten)
					writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
				}
			}
			else {

				String screenshotName = (new File(referenceImagePath)).getName().replace(".png", "").replace(".PNG", "")
				File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '_ActualScreen.png')
				ImageIO.write(screen.getScreen(Integer.parseInt(screenNoToBeDumped)).capture().getImage(), "PNG", screenshotToBeWritten)
				writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
			}
		}
		else {
			String screenshotName = (new File(referenceImagePath)).getName().replace(".png", "").replace(".PNG", "")
			File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '_ActualScreenPortion.png')
			ImageIO.write(screen.getScreen(currentScreen).capture(m.getX(), m.getY(), m.getW(), m.getH()).getImage(), "PNG", screenshotToBeWritten)
			writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
		}

		return writtenScreenshots

	}

	def ArrayList<String> takeFullScreenshotForImgFindIssuesUsingSelenium(String referenceImagePath, Match m=null) {

		ArrayList<String> writtenScreenshots = new ArrayList<String> ()

		File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (folderToBeWritten.exists() == false) {
			folderToBeWritten.mkdir()
		}

		TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)

		if(referenceImagePath.isEmpty() == false){

			if (m == null) {

				String screenshotName = (new File(referenceImagePath)).getName().replace(".png", "").replace(".PNG", "")
				File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '_ActualScreenPortion.png')

				ImageIO.write(ImageIO.read(SrcFile), "PNG", screenshotToBeWritten)
				writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
			}
			else {
				String screenshotName = (new File(referenceImagePath)).getName().replace(".png", "").replace(".PNG", "")
				File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '_ActualScreenPortion.png')

				BufferedImage bi = ImageIO.read(SrcFile)
				BufferedImage biSub = bi.getSubimage(m.getX(), m.getY(), m.getW(), m.getH())

				ImageIO.write(biSub, "PNG", screenshotToBeWritten)
				writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
			}
		}
		else {

			String screenshotName = "Screenshot_" + (new EnvironmentSetup()).getCurrentTimeStamp()
			File screenshotToBeWritten = new File (folderToBeWritten.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + screenshotName + '.png')

			ImageIO.write(ImageIO.read(SrcFile), "PNG", screenshotToBeWritten)
			writtenScreenshots.add(screenshotToBeWritten.getAbsolutePath())
		}

		return writtenScreenshots
	}

	def String captureImagePatternOnScreenLocation (String referenceImagePath, Match m) {

		referenceImagePath = referenceImagePath.replaceAll("\\\\", "/")

		String folderPath = referenceImagePath.substring(0, referenceImagePath.lastIndexOf("/"))
		String locationFileName = (referenceImagePath.substring(referenceImagePath.lastIndexOf("/") + 1)).replace(".png", ".txt").replace(".PNG", ".txt")

		File folder = new File(folderPath)
		File locationFile = new File(folderPath + "/" + locationFileName)

		String locationInfo = m.getX() + "," + m.getY() + "," + m.getW() + "," + m.getH()

		if (locationFile.exists() == false) {

			locationFile << locationInfo
		}
		else {

			def position = (locationFile.text).split(",")

			int x = Integer.parseInt(position[0])
			int y = Integer.parseInt(position[1])
			int w = Integer.parseInt(position[2])
			int h = Integer.parseInt(position[3])

			if(x==0 || y == 0){
				//Division by zero (0) handling
				locationFile.delete()
				locationFile << locationInfo

			}else{

				def xDiffPercent = ((Math.abs(m.getX() - x) * 100) * (1 / x)).intValue()
				def yDiffPercent = ((Math.abs(m.getY() - y) * 100) * (1 / y)).intValue()
				def wDiffPercent = ((Math.abs(m.getW() - w) * 100) * (1 / w)).intValue()
				def hDiffPercent = ((Math.abs(m.getH() - h) * 100) * (1 / h)).intValue()
				if ((xDiffPercent > 5) || (yDiffPercent > 5) || (wDiffPercent > 5) || (hDiffPercent > 5)) {

					locationFile.delete()
					locationFile << locationInfo
				}
			}

		}

		return (locationFile.getCanonicalPath())

	}

	def String takePartialScreenshotUsingSeleniumBasedOnMetaData (String referenceImagePath) {

		if (GlobalVariable.updateReferenceImages == true) {

			referenceImagePath = RunConfiguration.getProjectDir() + referenceImagePath.replaceAll("\\\\", "/")

			String folderPath = referenceImagePath.substring(0, referenceImagePath.lastIndexOf("/"))
			String locationFileName = (referenceImagePath.substring(referenceImagePath.lastIndexOf("/") + 1)).replace(".png", ".txt").replace(".PNG", ".txt")
			String fileExtension = (referenceImagePath.substring(referenceImagePath.lastIndexOf(".") + 1))

			File locationFile = new File(folderPath + "/" + locationFileName)

			if (locationFile.exists() == false) {
				KeywordUtil.markErrorAndStop("\n\n" + "The screenshot information file with co-ordinates, is missing. Screenshot cannot be auto-updated."
						+ "\n\n" + "Expected File : " + locationFileName
						+ "\n\n" + "Expected folder for above file : " + folderPath
						+ "\n\n")
			}

			def position = (locationFile.text).split(",")

			int dx = Integer.parseInt(position[0])
			int dy = Integer.parseInt(position[1])
			int dw = Integer.parseInt(position[2])
			int dh = Integer.parseInt(position[3])

			File screenshotToBeWritten = new File (referenceImagePath)

			TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)

			BufferedImage bi = ImageIO.read(SrcFile)
			BufferedImage biSub = bi.getSubimage(dx, dy, dw, dh)

			ImageIO.write(biSub, fileExtension, screenshotToBeWritten)
		}

	}

	def String takePartialScreenshotUsingSikuliScreenBasedOnMetaData (String referenceImagePath) {

		if (GlobalVariable.updateReferenceImages == true) {

			referenceImagePath = RunConfiguration.getProjectDir() + referenceImagePath.replaceAll("\\\\", "/")

			String folderPath = referenceImagePath.substring(0, referenceImagePath.lastIndexOf("/"))
			String locationFileName = (referenceImagePath.substring(referenceImagePath.lastIndexOf("/") + 1)).replace(".png", ".txt").replace(".PNG", ".txt")
			String fileExtension = (referenceImagePath.substring(referenceImagePath.lastIndexOf(".") + 1))

			File locationFile = new File(folderPath + "/" + locationFileName)

			if (locationFile.exists() == false) {
				KeywordUtil.markErrorAndStop("\n\n" + "The screenshot information file with co-ordinates, is missing. Screenshot cannot be auto-updated."
						+ "\n\n" + "Expected File : " + locationFileName
						+ "\n\n" + "Expected folder for above file : " + folderPath
						+ "\n\n")
			}

			def position = (locationFile.text).split(",")

			int dx = Integer.parseInt(position[0])
			int dy = Integer.parseInt(position[1])
			int dw = Integer.parseInt(position[2])
			int dh = Integer.parseInt(position[3])

			File screenshotToBeWritten = new File (referenceImagePath)

			Screen screen = new Screen()
			ImageIO.write(screen.getScreen(currentScreen).capture(dx, dy, dw, dh).getImage(), fileExtension, screenshotToBeWritten)
		}

	}

	def takeFullScreenshot() {

		File dir = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (dir.exists() == false) {
			dir.mkdir()
		}

		File[] imageFiles = dir.listFiles({ File file ->
			file.name.startsWith(GlobalVariable.currentTestCaseID) &&
					file.name.endsWith(".png")
		} as FileFilter)

		boolean useSikuli = false

		if (useSikuli == true) {

			Screen screen = new Screen()

			def screensToBeAnalyzed = screen.getNumberScreens()

			if (imageFiles.size() == 0) {

				for (int i=0; i<screensToBeAnalyzed; i++) {

					File screenshotToBeWritten = new File (dir.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + "_" + 'ActualScreen_' + i.toString() + '.png')

					ImageIO.write(screen.getScreen(i).capture().getImage(), "PNG", screenshotToBeWritten)
					//KeywordUtil.logInfo('Test Case Not Passed. Actual full screen image(s) saved at following location for debugging : ' + RunConfiguration.getReportFolder() + '/TestCaseDumps/')
				}

			}
		}
		else {

			try {
				if (DriverFactory.getWebDriver().toString().contains("(null)") == false) {

					if (imageFiles.size() == 0) {

						File screenshotToBeWritten = new File (dir.getAbsolutePath() + '/' + GlobalVariable.currentTestCaseID + '_ActualScreen.png')
						TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
						File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)
						ImageIO.write(ImageIO.read(SrcFile), "PNG", screenshotToBeWritten)
					}
				}
			}
			catch (Exception e) {
				KeywordUtil.logInfo("No browser session was open. Probably the session got abnormally closed or had not launched itself in the beginning.")
			}
		}
	}

	@Keyword
	def String base64ConversionOfImages (String imagePath) {

		def inputFile1 = new File(imagePath)
		String encoded1 = inputFile1.getBytes().encodeBase64().toString()
		println encoded1
		return encoded1
	}

	@Keyword
	def String reportScreenShotTaken (String pathOfActualImageFound, String reportName, int imgCheckCounter, String instructionMessage="") {

		File styleTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "style.css")
		File sliderScriptTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderScript.js")
		File sliderProcessingTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderProcessing.js")

		String styleContentReadFromFile = styleTemplate.getText()
		String sliderScriptContentReadFromFile = sliderScriptTemplate.getText()
		String sliderProcessingScriptContentReadFromFile = sliderProcessingTemplate.getText()

		String actualImageFolder = RunConfiguration.getReportFolder() + "/TestCaseDumps"

		println actualImageFolder

		String instructionInHtml = ""

		if (instructionMessage.isEmpty()==false) {

			//instructionInHtml = "<p> <b> Instruction Message : </p> <p>" + instructionMessage + "</b> </p>"
			instructionInHtml = "<b> <p>Instruction Message : </p> <p style='white-space: pre;'>" + instructionMessage + "</p></b>"
		}
		
		String html =

				"<html>" +
				"<head>" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
				"<!-- BeerSlider opensource style definition https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<style>" + styleContentReadFromFile + "</style>" +
				"<!-- BeerSlider opensource script https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<script>" + sliderScriptContentReadFromFile + "</script>" +
				"</head>" +
				"<body>" +

				"<h1>Verify below image</h1>" +

				"<div id=\"comparison\">" +

				"<p>Test Case : " + GlobalVariable.currentTestCaseID + " : " + getDetailsFromWorkItemIdList([GlobalVariable.currentTestCaseID]).get(GlobalVariable.currentTestCaseID).get('WI_System.Title') + "</p>" +
				//"<p>Actual Image Path : " + pathOfActualImageFound + "</p>" +
				instructionInHtml +
				"<div id=\"slider\" class=\"beer-slider beer-slider-wlabels\" data-beer-label=\"Actual\">" +
				"<img src=\"data:image/png;base64," + base64ConversionOfImages(pathOfActualImageFound) + "\" alt=\"Actual\" width=\"100%\" height=\"auto\">" +
				"<div class=\"beer-reveal\" data-beer-label=\"Reference/Expected\">" +
				"<img src=\"data:image/png;base64,"+ "" +"\" alt=\"Reference/Expected\" width=\"100%\" height=\"auto\">" +
				"</div>" +
				"</div>" +
				"<p></p><p>----------------------------------------------------------------</p><p></p>" +
				"</div>" +

				"<script>" + sliderProcessingScriptContentReadFromFile + "</script>" +

				"</body>" +
				"</html>"

		//def report = new File(RunConfiguration.getReportFolder() + "/TestCaseDumps/" + GlobalVariable.currentTestCaseID + "_imageCompareIssues.html")

		File imgDir = new File(RunConfiguration.getReportFolder() +"/ImgReport/"+GlobalVariable.currentTestCaseID+"/")
		if(!imgDir.exists()){
			imgDir.mkdirs()
		}

		reportName = imgCheckCounter + "_" + reportName
		def report = new File(RunConfiguration.getReportFolder() + "/ImgReport/" + GlobalVariable.currentTestCaseID + "/" + reportName + ".html")
		report << html

		return (imgDir.getCanonicalPath())
	}

	@Keyword
	def mergeScreenshotsTaken(String pathOfResult, String combinedReportName) {

		File combinedImgReportStyle = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "combinedImgReportStyle.css")
		File combinedImgReportScript = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "combinedImgReportScript.js")
		File style = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "style.css")
		File sliderScript = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderScript.js")
		File sliderProcessing = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderProcessing.js")

		String combinedImgReportStyle_ReadFromFile = combinedImgReportStyle.getText()
		String combinedImgReportScript_ReadFromFile = combinedImgReportScript.getText()
		String style_ReadFromFile = style.getText()
		String sliderScript_ReadFromFile = sliderScript.getText()
		String sliderProcessing_ReadFromFile = sliderProcessing.getText()

		File reportsFolder = new File(pathOfResult);
		String reportFilePaths = "";

		ArrayList<File> filesInDirectory = new File(reportsFolder.getCanonicalPath()).listFiles()

		LinkedHashMap <String, File> htmlFilesInDirectory = new LinkedHashMap <String, File> ()

		for(File f : filesInDirectory){

			String fileExtenstion = f.getName().substring(f.getName().lastIndexOf(".") + 1,f.getName().length())

			if(fileExtenstion.equals("html")){

				String fileIndex = f.getName().substring(0, f.getName().indexOf("_"))
				htmlFilesInDirectory.put(fileIndex, f)
			}
		}

		int counter = 1
		String pageContents = ""
		String showPage = ""

		if (htmlFilesInDirectory.size() > 1) {

			for(counter = 1; counter <= htmlFilesInDirectory.size(); counter++){

				String filePath = htmlFilesInDirectory.get(counter.toString()).getCanonicalPath()
				String fileName = htmlFilesInDirectory.get(counter.toString()).getName()

				try {
					String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());
					reportFilePaths = reportFilePaths + filePath + ";";

					try {
						File myObj = new File(filePath);
						Document html = Jsoup.parse(myObj, "UTF-8", "")
						html.outputSettings(new Document.OutputSettings().prettyPrint(false)); //preserve linebreaks and spacing

						String htmlBodyContent = ""

						if (fileName.contains("NOTFOUND")) {
							htmlBodyContent = html.body().toString().replace("id=\"slider1\"", "id=\"slider" + counter + "\"")
						}
						else {
							htmlBodyContent = html.body().getElementsByTag("h1").toString() + '\n' +
									html.getElementById("comparison").toString().replace("id=\"slider\"", "id=\"slider" + counter + "\"")
						}

						if (counter ==1) {

							pageContents = pageContents + '<div id="page' + counter + '" class="page" style="">\n' +
									'<p>Image Report : ' + fileName + '</p>' + '\n' +
									htmlBodyContent +
									'\n</div>\n'
						} else {

							pageContents = pageContents + '<div id="page' + counter + '" class="page" style="display:none">\n' +
									'<p>Image Report : ' + fileName + '</p>' + '\n' +
									htmlBodyContent +
									'\n</div>\n'
						}

						showPage = showPage + "<button onclick=\"show('page" + counter + "');\" style=\"width:25px;line-height:1.6;margin:5px 2px;text-align:left\">" + counter + "</button> -\n"

						//System.out.println("\n" + data);
						myReader.close();
					} catch (FileNotFoundException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
				}
				catch (Exception e) {

				}
			}

			showPage = showPage.substring(0, showPage.lastIndexOf("-"))

			String html =
					"<html>\n" +
					"<head>\n" +
					"<style>\n" + combinedImgReportStyle_ReadFromFile + "\n</style>\n" +
					"<script>\n" + combinedImgReportScript_ReadFromFile + "\n</script>\n" +
					"<style>\n" + style_ReadFromFile + "\n</style>\n" +
					"<script>\n" + sliderScript_ReadFromFile + "\n</script>\n" +
					"</head>\n" +
					"<body>\n<p>\n" +
					"Show report : \n </p>\n<p>\n" +
					showPage +
					"\n</p>\n" +
					pageContents +
					"\n<script>\n" + sliderProcessing_ReadFromFile + "\n</script>" +
					"\n</body>\n" +
					"</html>"

			def report = new File(pathOfResult + "/" + combinedReportName + ".html")
			report << html
		}
	}

	@Keyword
	def reportImageMatchPercentageIssues (String pathOfActualImageFound, String pathOfReferenceImageToBeFound, double m, int imgCounter, String result, String instructionMessage="") {

		File styleTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "style.css")
		File sliderScriptTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderScript.js")
		File sliderProcessingTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderProcessing.js")

		String styleContentReadFromFile = styleTemplate.getText()
		String sliderScriptContentReadFromFile = sliderScriptTemplate.getText()
		String sliderProcessingScriptContentReadFromFile = sliderProcessingTemplate.getText()

		String osBasedMessage = ""
		String commandLineSyntax = ""

		String referenceImageFolder = pathOfReferenceImageToBeFound.substring(0, pathOfReferenceImageToBeFound.lastIndexOf("/"))
		String actualImageFolder = RunConfiguration.getReportFolder() + "/TestCaseDumps"

		println referenceImageFolder
		println actualImageFolder

		if (GlobalVariable.envInfo.get("osName").toLowerCase().contains("win")) {
			osBasedMessage = "Windows CMD syntax to replace files : "

			commandLineSyntax = "xcopy \"" + pathOfActualImageFound + "\" \"" + referenceImageFolder + "\" /Y" + "\n" + "&&" + "\n" +
					"del \"" + pathOfReferenceImageToBeFound.replaceAll("/", "\\\\") + "\"" + "\n" + "&&" + "\n" +
					"ren " + "\"" + referenceImageFolder.replaceAll("/", "\\\\") + "\\" + pathOfActualImageFound.substring(pathOfActualImageFound.lastIndexOf("\\") + 1) + "\" " + "\"" + pathOfReferenceImageToBeFound.substring(pathOfReferenceImageToBeFound.lastIndexOf("/") + 1) + "\""

		}
		else if (GlobalVariable.envInfo.get("osName").toLowerCase().contains("mac")) {

			println GlobalVariable.envInfo.get("osName").toLowerCase()
			osBasedMessage = "Mac terminal syntax to replace files : "

			commandLineSyntax = "cp \"" + pathOfActualImageFound + "\" \"" + referenceImageFolder + "\" " + "\n" + "&&" + "\n" +
					"rm \"" + pathOfReferenceImageToBeFound.replaceAll("\\\\", "/") + "\"" + "\n" + "&&" + "\n" +
					"mv " + "\"" + referenceImageFolder.replaceAll("\\\\", "/") + "/" + pathOfActualImageFound.substring(pathOfActualImageFound.lastIndexOf("/") + 1) + "\" " + "\"" + referenceImageFolder.replaceAll("\\\\", "/") + "/" + pathOfReferenceImageToBeFound.substring(pathOfReferenceImageToBeFound.lastIndexOf("/") + 1) + "\""

		}
		else {
			osBasedMessage = "OS cannot be known to find screenshot replace syntex."
		}

		String instructionInHtml = ""

		if (instructionMessage.isEmpty()==false) {

			//instructionInHtml = "<p> <b> Instruction Message : </p> <p>" + instructionMessage + "</b> </p>"
			instructionInHtml = "<b> <p>Instruction Message : </p> <p style='white-space: pre;'>" + instructionMessage + "</p></b>"

		}

		String html =

				"<html>" +
				"<head>" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
				"<!-- BeerSlider opensource style definition https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<style>" + styleContentReadFromFile + "</style>" +
				"<!-- BeerSlider opensource script https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<script>" + sliderScriptContentReadFromFile + "</script>" +
				"</head>" +
				"<body>" +

				"<h1>Manually Compare Below Images for Actual vs Expected Result</h1>" +

				"<div id=\"comparison\">" +

				"<p>Test Case : " + GlobalVariable.currentTestCaseID + " : " + getDetailsFromWorkItemIdList([GlobalVariable.currentTestCaseID]).get(GlobalVariable.currentTestCaseID).get('WI_System.Title') + "</p>" +
				"<p>Reference/Expected Image Path : " + pathOfReferenceImageToBeFound + "</p>" +
				"<p>Actual Image Path : " + pathOfActualImageFound + "</p>" +
				"<p>Matching Percentage : " + m * 100 + "</p>" +
				instructionInHtml +
				"<p>Use the slider to compare two images (If you feel the actual image is acceptable, you may replace the expected image with actual one (This can be done manually using image paths mentioned above - just ensure to rename the copied image appropriately, OR you can use the CMD command at the end of this doc). :</p>" +

				"<div id=\"slider\" class=\"beer-slider beer-slider-wlabels\" data-beer-label=\"Actual\">" +
				"<img src=\"data:image/png;base64," + base64ConversionOfImages(pathOfActualImageFound) + "\" alt=\"Actual\" width=\"100%\" height=\"auto\">" +
				"<div class=\"beer-reveal\" data-beer-label=\"Reference/Expected\">" +
				"<img src=\"data:image/png;base64,"+ base64ConversionOfImages(pathOfReferenceImageToBeFound) +"\" alt=\"Reference/Expected\" width=\"100%\" height=\"auto\">" +
				"</div>" +
				"</div>" +
				"<p>" + osBasedMessage + "</p>" +
				"<p>" + commandLineSyntax + "</p>" +
				"<p></p><p>----------------------------------------------------------------</p><p></p>" +
				"</div>" +

				"<script>" + sliderProcessingScriptContentReadFromFile + "</script>" +

				"</body>" +
				"</html>"

		//def report = new File(RunConfiguration.getReportFolder() + "/TestCaseDumps/" + GlobalVariable.currentTestCaseID + "_imageCompareIssues.html")

		File imgDir = new File(RunConfiguration.getReportFolder() +"/ImgReport/"+GlobalVariable.currentTestCaseID+"/")
		if(!imgDir.exists()){
			imgDir.mkdirs()
		}

		def report = new File(RunConfiguration.getReportFolder() +"/ImgReport/"+GlobalVariable.currentTestCaseID+"/"+imgCounter+"_"+result+".html")
		report << html

	}

	@Keyword
	def reportUnexpectedImagePresenceIssues (String pathOfActualImageFound, String pathOfImageToBeAbsent, Match m) {

		File styleTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "style.css")
		File sliderScriptTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderScript.js")
		File sliderProcessingTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderProcessing.js")

		String styleContentReadFromFile = styleTemplate.getText()
		String sliderScriptContentReadFromFile = sliderScriptTemplate.getText()
		String sliderProcessingScriptContentReadFromFile = sliderProcessingTemplate.getText()

		String html =

				"<html>" +
				"<head>" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
				"<!-- BeerSlider opensource style definition https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<style>" + styleContentReadFromFile + "</style>" +
				"<!-- BeerSlider opensource script https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<script>" + sliderScriptContentReadFromFile + "</script>" +
				"</head>" +
				"<body>" +

				"<h1>Manually Compare Below Images for Actual vs Expected Result</h1>" +

				"<div id=\"comparison\">" +

				"<p>Test Case : " + GlobalVariable.currentTestCaseID + " : " + getDetailsFromWorkItemIdList([GlobalVariable.currentTestCaseID]).get(GlobalVariable.currentTestCaseID).get('WI_System.Title') + "</p>" +
				"<p>Image which should not be present in screen : " + pathOfImageToBeAbsent + "</p>" +
				"<p>Image actually found in screen : " + pathOfActualImageFound + "</p>" +
				"<p>Matching Percentage : " + m.getScore() * 100 + "</p>" +
				"<p>Use the slider to see where in actual UI, the image that is not expected to be present, actually found:</p>" +

				"<div id=\"slider\" class=\"beer-slider beer-slider-wlabels\" data-beer-label=\"Actual\">" +
				"<img src=\"data:image/png;base64," + base64ConversionOfImages(pathOfActualImageFound) + "\" alt=\"Actual\" width=\"100%\" height=\"auto\">" +
				"<div class=\"beer-reveal\" data-beer-label=\"Reference/Expected\">" +
				"<img src=\"data:image/png;base64,"+ base64ConversionOfImages(pathOfImageToBeAbsent) +"\" alt=\"Reference/Expected\" width=\"100%\" height=\"auto\">" +
				"</div>" +
				"</div>" +
				"<p></p>" +
				"<p></p><p>----------------------------------------------------------------</p><p></p>" +
				"</div>" +

				"<script>" + sliderProcessingScriptContentReadFromFile + "</script>" +

				"</body>" +
				"</html>"

		def report = new File(RunConfiguration.getReportFolder() + "/TestCaseDumps/" + GlobalVariable.currentTestCaseID + "_unexpectedImageFoundIssues.html")
		report << html

	}

	@Keyword
	def reportImageFindingIssues (ArrayList<String> pathsOfActualImageFound, String pathOfReferenceImageToBeFound, int imgCounter, String result, String instructionMessage="") {

		File styleTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "style.css")
		File sliderScriptTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderScript.js")
		File sliderProcessingTemplate = new File (RunConfiguration.getProjectDir() + "/Data Files/ReferenceData/" + "sliderProcessing.js")

		String styleContentReadFromFile = styleTemplate.getText()
		String sliderScriptContentReadFromFile = sliderScriptTemplate.getText()
		String sliderProcessingScriptContentReadFromFile = sliderProcessingTemplate.getText()

		BufferedImage screenSize = ImageIO.read(new File(pathsOfActualImageFound.get(0)))
		BufferedImage overlay = ImageIO.read(new File(pathOfReferenceImageToBeFound))

		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(screenSize.getWidth(), overlay.getWidth())
		int h = Math.max(screenSize.getHeight(), overlay.getHeight())
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics()
		//g.drawImage(image, 0, 0, null)
		g.setColor(Color.WHITE)

		//Integer startPointx = (screenSize.getWidth()/4) //- (overlay.getWidth()/2)
		//Integer startPointy = (screenSize.getHeight()/2) - (overlay.getHeight()/2)

		g.drawImage(overlay, 0, 0/*startPointx.intValue(), startPointy.intValue()*/, null)

		File dir = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')
		if (dir.exists() == false) {dir.mkdir()}

		ImageIO.write(combined, "PNG", new File(RunConfiguration.getReportFolder() + "/TestCaseDumps/combined.PNG"));

		String subHtml = ""
		String actualImagePaths = ""

		for (int screenCounter = 0; screenCounter < pathsOfActualImageFound.size(); screenCounter++) {

			subHtml = subHtml +

					"<div id=\"comparison" + (screenCounter + 1) + "\">" +
					"<div id=\"slider" + (screenCounter + 1) + "\" class=\"beer-slider beer-slider-wlabels\" data-beer-label=\"Actual (Full Screen) " + screenCounter + "\">" +
					"<img src=\"data:image/png;base64," + base64ConversionOfImages(pathsOfActualImageFound.get(screenCounter)) + "\" alt=\"Actual\" width=\"100%\" height=\"auto\">" +
					"<div class=\"beer-reveal\" data-beer-label=\"Reference/Expected Image portion to find\">" +
					"<img src=\"data:image/png;base64,"+ base64ConversionOfImages(RunConfiguration.getReportFolder() + "/TestCaseDumps/combined.PNG") +"\" alt=\"Reference/Expected\" width=\"auto\" height=\"auto\">" +
					"</div>" +
					"</div>" +
					"<p></p>"

			actualImagePaths = actualImagePaths + pathsOfActualImageFound.get(screenCounter) + ",   "
		}

		(new File(RunConfiguration.getReportFolder() + "/TestCaseDumps/combined.PNG")).delete()

		String instructionInHtml = ""

		if (instructionMessage.isEmpty()==false) {

			//instructionInHtml = "<p> <b> Instruction Message : </p> <p>" + instructionMessage + "</b> </p>"
			instructionInHtml = "<b> <p>Instruction Message : </p> <p style='white-space: pre;'>" + instructionMessage + "</p></b>"
		}

		String html =

				"<html>" +
				"<head>" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
				"<!-- BeerSlider opensource style definition https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<style>" + styleContentReadFromFile + "</style>" +
				"<!-- BeerSlider opensource script https://pepsized.com/beer-slider-responsive-accessible-before-after-slider/ -->" +
				"<script>" + sliderScriptContentReadFromFile + "</script>" +
				"</head>" +
				"<body>" +

				"<h1>Expected or Reference Image not found in UI</h1>" +

				"<p>Test Case : " + GlobalVariable.currentTestCaseID + " : " + getDetailsFromWorkItemIdList([GlobalVariable.currentTestCaseID]).get(GlobalVariable.currentTestCaseID).get('WI_System.Title') + "</p>" +
				"<p>Reference/Expected Image Path : " + pathOfReferenceImageToBeFound + "</p>" +
				"<p>Full screen images (per screen) at the time of failure are collected at : <br>" + actualImagePaths.substring(0, actualImagePaths.lastIndexOf(",")) + "</p>" +
				instructionInHtml +
				"<p>Use the slider to see which \"Expected or Reference\" image the automation script was trying to identify in screen. :</p>" +

				subHtml +

				"<p></p><p>----------------------------------------------------------------</p><p></p>" +
				"</div>" +

				"<script>" + sliderProcessingScriptContentReadFromFile + "</script>" +

				"</body>" +
				"</html>"

		File imgDir = new File(RunConfiguration.getReportFolder() +"/ImgReport/"+GlobalVariable.currentTestCaseID+"/")
		if(!imgDir.exists()){
			imgDir.mkdirs()
		}

		def report = new File(RunConfiguration.getReportFolder() +"/ImgReport/"+GlobalVariable.currentTestCaseID+"/"+imgCounter+"_"+result+".html")
		report << html

	}

	@Keyword
	def double compareImages (String pathOfActualImageToBeFound, String pathOfReferenceImageToBeFound) {

		//Screenshot Comparison using Sikuli

		Screen screen = new Screen()

		//pathOfActualImageToBeFound = RunConfiguration.getProjectDir() + pathOfActualImageToBeFound
		//pathOfReferenceImageToBeFound = RunConfiguration.getProjectDir() + pathOfReferenceImageToBeFound

		Pattern actualPattern = new Pattern(pathOfActualImageToBeFound)
		Pattern refPattern = new Pattern(pathOfReferenceImageToBeFound)

		Finder findInReference = new Finder(refPattern.getImage())

		findInReference.find(actualPattern)

		Double matchScore = 0.0

		if (findInReference.hasNext()) {

			Match m = findInReference.next()

			println(('Match Found with ' + (m.getScore() * 100)) + ' %')
			matchScore = m.getScore() * 100

			findInReference.destroy()

		}

		//Image Difference Calculation using Ashot

		File screenshotsFolder = new File(RunConfiguration.getReportFolder() + '/TestCaseDumps')

		if (screenshotsFolder.exists() == false) {
			screenshotsFolder.mkdir()
		}

		File fileReference = new File(pathOfReferenceImageToBeFound)
		File fileActual = new File(pathOfActualImageToBeFound)

		ImageDiff diff = new ImageDiffer().makeDiff(ImageIO.read(fileReference), ImageIO.read(fileActual))

		BufferedImage diffImage = diff.getMarkedImage() // comparison result with marked differences

		println('Image Difference Exists : ' + diff.hasDiff())

		ImageIO.write(diffImage, 'PNG', new File(RunConfiguration.getReportFolder() + '/TestCaseDumps/' + fileActual.getName().replace(".png", "") + '_diff.png'))

		ImageIO.write(diff.getTransparentMarkedImage(), 'PNG', new File(RunConfiguration.getReportFolder() + '/TestCaseDumps/' + fileActual.getName().replace(".png", "") + 		'_diffTransparent.png'))

		//Return Sikuli Match score
		return matchScore
	}

	@Keyword
	def findImagePatternAndClick (String pathOfImageToBeFound, int tolerance=GlobalVariable.imageComparisonMidTolerance) {

		//findImagePatternAndClickUsingSikuliScreen(pathOfImageToBeFound, tolerance=GlobalVariable.imageComparisonMidTolerance)

		findImagePatternAndClickUsingSeleniumScreenshot(pathOfImageToBeFound, tolerance=GlobalVariable.imageComparisonMidTolerance)

	}

	def findImagePatternAndClickUsingSeleniumScreenshot (String pathOfImageToBeFound, int tolerance=GlobalVariable.imageComparisonMidTolerance) {

		if (GlobalVariable.updateReferenceImages == true) {

			takePartialScreenshotUsingSeleniumBasedOnMetaData(pathOfImageToBeFound)
		}

		pathOfImageToBeFound = RunConfiguration.getProjectDir() + pathOfImageToBeFound

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeFound)

		def attempt = 0
		def imageFound = false

		while ((imageFound == false) && (attempt <= GlobalVariable.waitAttempts)) {

			if ((attempt==GlobalVariable.waitAttempts) && (imageFound == false)) {

				ArrayList<String> writtenScreenshots = takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeFound)
				reportImageFindingIssues(writtenScreenshots, pathOfImageToBeFound)
				KeywordUtil.markErrorAndStop('\n\n' + 'Expected Image NOT found in UI.' + '\n\n' + 'Expected Image portion which the automation script was trying to find in UI, can be compared with actual screen using following compariosn html : ' + RunConfiguration.getReportFolder() + '/TestCaseDumps/'  + GlobalVariable.currentTestCaseID + '_imageFindingIssues.html' + "\n")
			}

			TakesScreenshot scrShot = ((TakesScreenshot) DriverFactory.getWebDriver())
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE)

			Pattern patternToBeFound = new Pattern(pathOfImageToBeFound)
			//Pattern uiPattern = new Pattern(scrShot.getScreenshotAs(OutputType.FILE))

			Finder findInUi = new Finder(ImageIO.read(SrcFile))

			findInUi.find(patternToBeFound)

			KeywordUtil.logInfo('=======================')

			def didMatchFound = findInUi.hasNext()
			//println didMatchFound
			Match m = findInUi.next()
			//println m
			KeywordUtil.logInfo('Was image match found ? : ' + didMatchFound)
			Double matchScore = 0.0

			if (didMatchFound) {

				//Match m = findInUi.next()

				KeywordUtil.logInfo(('Match Found with ' + (m.getScore() * 100)) + ' % (Threshold provided is ' + tolerance + '%)')
				matchScore = m.getScore() * 100

				if (matchScore > tolerance) {

					captureImagePatternOnScreenLocation(pathOfImageToBeFound, m)
					seleniumClickUsingSikuliMatchObjectLocation(m)
				}
				else {
					ArrayList<String> writtenScreenshots = takeFullScreenshotForImgFindIssuesUsingSelenium(pathOfImageToBeFound, m)
					reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeFound, m)
					KeywordUtil.markFailedAndStop('\n\n' + 'Expected Image found in UI, but not satisfying the comparison tolerance.' + '\n\n' + 'Actual matching percentage found : ' + matchScore + '\n\n' + 'Specified tolerance : ' + tolerance + '\n\n' + 'Actual vs Expected Image can be compared using following compariosn html :\n' + RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_imageCompareIssues.html' + '\n')

				}

				findInUi.destroy()
				imageFound = true
				break
			}
			attempt++
			WebUI.delay(GlobalVariable.minWait)
		}
		WebUI.delay(GlobalVariable.minuteDelay)
	}

	def findImagePatternAndClickUsingSikuliScreen (String pathOfImageToBeFound, int tolerance=GlobalVariable.imageComparisonMidTolerance) {

		if (GlobalVariable.updateReferenceImages == true) {

			takePartialScreenshotUsingSikuliScreenBasedOnMetaData(pathOfImageToBeFound)
		}

		Screen screen = new Screen()

		pathOfImageToBeFound = RunConfiguration.getProjectDir() + pathOfImageToBeFound

		KeywordUtil.logInfo("Reference Image Path : " + pathOfImageToBeFound)

		def screensToBeAnalyzed = screen.getNumberScreens()
		def currentScreen = 0

		def attempt = 0
		def imageFound = false

		while ((imageFound == false) && (attempt <= GlobalVariable.waitAttempts)) {

			if ((attempt==GlobalVariable.waitAttempts) && (imageFound == false)) {

				ArrayList<String> writtenScreenshots = takeFullScreenshotForImgFindIssuesUsingSikuli(pathOfImageToBeFound)
				reportImageFindingIssues(writtenScreenshots, pathOfImageToBeFound)
				KeywordUtil.markErrorAndStop('\n\n' + 'Expected Image NOT found in UI.' + '\n\n' + 'Expected Image portion which the automation script was trying to find in UI, can be compared with actual screen using following compariosn html : ' + RunConfiguration.getReportFolder() + '/TestCaseDumps/'  + GlobalVariable.currentTestCaseID + '_imageFindingIssues.html' + "\n")
			}
			while (currentScreen < screensToBeAnalyzed) {

				Pattern patternToBeFound = new Pattern(pathOfImageToBeFound)
				Pattern uiPattern = new Pattern(screen.getScreen(currentScreen).capture().getImage())

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

						captureImagePatternOnScreenLocation(pathOfImageToBeFound, m)
						screen.getScreen(currentScreen).click(pathOfImageToBeFound)
					}
					else {

						ArrayList<String> writtenScreenshots = takeFullScreenshotForImgFindIssuesUsingSikuli(pathOfImageToBeFound, currentScreen.toString(), m)
						reportImageMatchPercentageIssues(writtenScreenshots.get(0), pathOfImageToBeFound, m)
						KeywordUtil.markFailedAndStop('\n\n' + 'Expected Image found in UI, but not satisfying the comparison tolerance.' + '\n\n' + 'Actual matching percentage found : ' + matchScore + '\n\n' + 'Specified tolerance : ' + tolerance + '\n\n' + 'Actual vs Expected Image can be compared using following compariosn html :\n' + RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_imageCompareIssues.html' + '\n')
					}

					findInUi.destroy()
					imageFound = true
					break
				}
				currentScreen = currentScreen + 1
			}

			attempt++
			currentScreen = 0
			WebUI.delay(GlobalVariable.minWait)
		}
		WebUI.delay(GlobalVariable.minuteDelay)

	}

	/**
	 * takeScreenShotOfPageForVerification
	 * @param instructionMessage instructionMessage to verify screenshot
	 */
	@Keyword
	def String takeScreenShotOfPageForVerification (String instructionMessage="") {

		ArrayList<String> writtenScreenshots = takeFullScreenshotForImgFindIssuesUsingSelenium("", null)

		String screenshotName = (new File(writtenScreenshots.get(0))).getName().replace(".png", "").replace(".PNG", "")
		screenshotName = screenshotName.substring(screenshotName.indexOf("_") + 1)

		int imgCheckCounter = (new DataProcessing()).recordImageInformationToFile(screenshotName, "Verify Manually")
		String imgDir = reportScreenShotTaken(writtenScreenshots.get(0), screenshotName, imgCheckCounter, instructionMessage)
		return imgDir
	}

	@Keyword
	def seleniumClickUsingSikuliMatchObjectLocation (Match m) {

		Actions action = new Actions(DriverFactory.getWebDriver())

		//Loop to iterate through all possible web-elements to which selenium actions class can utilize, and locate
		//In in each attempt, there is an attempt to locate the element and go back to browser screen's left top
		//Loop is immediately broken when these activities are completed for an element

		for (Object element : DriverFactory.getWebDriver().findElements(By.xpath("(//*)"))) {

			try {

				int elementOffsetX = element.getLocation().getX() + (int) (element.getSize().getWidth()/2)
				int elementOffsetY = element.getLocation().getY() + (int) (element.getSize().getHeight()/2)

				action.moveToElement(element).build().perform()
				action.moveByOffset(-elementOffsetX, -elementOffsetY).build().perform()

				break
			}
			catch (Exception e) {
				//println ("Trying with next element")
			}
		}

		int xOffset = (int) (m.getX() + (m.getW()/2))
		int yOffset = (int) (m.getY() + (m.getH()/2))

		action.moveByOffset(xOffset, yOffset).build().perform()
		action.click().build().perform()
	}

	@Keyword
	def mouseDragAndRelease (int xOffset, int yOffset) {

		//sikuliMouseDragAndRelease(xOffset, yOffset)
		seleniumMouseDragAndRelease(xOffset, yOffset)
	}

	@Keyword
	def moveMouseAwayAndClick (int xOffset, int yOffset) {

		//sikuliMoveMouseAwayAndClick (xOffset, yOffset)
		seleniumMoveMouseAwayAndClick(xOffset, yOffset)
	}

	@Keyword
	def seleniumMouseDragAndRelease (int xOffset, int yOffset) {

		KeywordUtil.logInfo("Selenium Mouse Click, then Drag followed by Mouse release (all using LMB)")

		Actions action = new Actions(DriverFactory.getWebDriver())

		int xOffsetStep1 = (int) (xOffset / 2)
		int yOffsetStep1 = (int) (yOffset / 2)
		int xOffsetStep2 = xOffset - xOffsetStep1
		int yOffsetStep2 = yOffset - yOffsetStep1

		action.clickAndHold().build().perform()
		WebUI.delay(2)

		//action.moveByOffset(xOffset, yOffset).build().perform()
		//WebUI.delay(2)

		action.moveByOffset(xOffsetStep1, yOffsetStep1).build().perform()
		WebUI.delay(2)
		action.moveByOffset(xOffsetStep2, yOffsetStep2).build().perform()
		WebUI.delay(2)

		action.release().build().perform()
	}

	@Keyword
	def seleniumMoveMouseAwayAndClick (int xOffset, int yOffset) {

		KeywordUtil.logInfo("Selenium Mouse Move away and click. Offset : " + xOffset + ", " + yOffset)

		Actions action = new Actions(DriverFactory.getWebDriver())

		action.moveByOffset(xOffset, yOffset).build().perform()
		WebUI.delay(1)
		action.click().build().perform()
		WebUI.delay(1)
	}

	@Keyword
	def sikuliMouseDragAndRelease (int xOffset, int yOffset) {

		KeywordUtil.logInfo("Sikuli Mouse Click, then Drag followed by Mouse release (all using LMB)")
		Screen screen = new Screen()
		screen.getScreen(currentScreen).mouseDown(Button.LEFT)
		WebUI.delay(1)
		screen.getScreen(currentScreen).mouseMove(xOffset, yOffset)
		WebUI.delay(1)
		screen.getScreen(currentScreen).mouseUp(Button.LEFT)
	}

	@Keyword
	def sikuliMoveMouseAwayAndClick (int xOffset, int yOffset) {

		KeywordUtil.logInfo("Sikuli Mouse Move away and click. Offset : " + xOffset + ", " + yOffset)
		Screen screen = new Screen()
		screen.getScreen(currentScreen).getScreen(currentScreen)
		screen.getScreen(currentScreen).mouseMove(xOffset, yOffset)
		screen.getScreen(currentScreen).click()
		WebUI.delay(1)
	}

	@Keyword
	def int getHeightOfUiObject(TestObject objectWithXpath) {

		String xpathOfProjectItem = objectWithXpath.findPropertyValue("xpath")
		WebElement element = DriverFactory.getWebDriver().findElement(By.xpath(xpathOfProjectItem))
		int height = element.getSize().getHeight()
		return height
	}

	@Keyword
	def int getWidthOfUiObject(TestObject objectWithXpath) {

		String xpathOfProjectItem = objectWithXpath.findPropertyValue("xpath")
		WebElement element = DriverFactory.getWebDriver().findElement(By.xpath(xpathOfProjectItem))
		int width = element.getSize().getWidth()
		return width
	}

	@Keyword
	def Object getValueOfKeyFromLocalStorage(String givenKey){

		//		if ((RunConfiguration.executionProperties.get("drivers").get("system").get("WebUI").get("browserType")).equals("SAFARI_DRIVER") == false) {
		//
		//			WebDriver driver = DriverFactory.getWebDriver()
		//			WebStorage webStorage = ((driver) as WebStorage)
		//			LocalStorage localStorage = webStorage.getLocalStorage()
		//
		//
		//			for (String key : localStorage.keySet()) {
		//				if (key.equalsIgnoreCase(givenKey)) {
		//					KeywordUtil.logInfo("Value for ("+ givenKey +") key is :" +localStorage.getItem(key))
		//					def slurper = new groovy.json.JsonSlurper()
		//					def localStorageJson = slurper.parseText(localStorage.getItem(key))
		//					return localStorageJson
		//					break
		//				}
		//			}
		//		}
		//		else {

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()

		String getLocalStorageJsStmt = 'var callback = arguments[arguments.length - 1];' + 'callback(window.localStorage)'

		def response = js.executeAsyncScript(getLocalStorageJsStmt)

		//println response.getClass(); //println "Local Storage information : " + response; //println response.get(0).getClass()

		for (String key : response.keySet()) {
			if (key.equalsIgnoreCase(givenKey)) {
				KeywordUtil.logInfo("Value for ("+ givenKey +") key is :" +response.get(key))
				def slurper = new groovy.json.JsonSlurper()
				def localStorageJson = slurper.parseText(response.get(key))
				return localStorageJson
				break
			}
		}

	}
	//	}

	@Keyword
	def String getValueOfTextBoxObjectUsingXpath(TestObject object){
		String xpathObj = object.findPropertyValue("xpath")
		String textValue = DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).getAttribute("value");
		return textValue;
	}

	@Keyword
	def pressDeleteKeyRepeatedlyFewTimes (int numberOfTimesTheKeyPressNeeded) {

		Actions action = new Actions (DriverFactory.getWebDriver())

		for (int i = 0; i < numberOfTimesTheKeyPressNeeded; i++) {
			action.sendKeys(Keys.DELETE)
			action.build().perform()
		}
	}

	@Keyword
	def pressBackSpaceKeyRepeatedlyFewTimes (int numberOfTimesTheKeyPressNeeded) {

		Actions action = new Actions (DriverFactory.getWebDriver())

		for (int i = 0; i < numberOfTimesTheKeyPressNeeded; i++) {
			action.sendKeys(Keys.BACK_SPACE)
			action.build().perform()
		}
	}

	@Keyword
	def setValueOfTextBoxObjectUsingXpath(TestObject object,String value, int numberOfTimesDeleteAndBackspacePressNeeded=40){
		String xpathObj = object.findPropertyValue("xpath")

		if (System.getProperty("os.name").toLowerCase().trim().contains("windows")) {

			DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).sendKeys(Keys.CONTROL + "a")
			WebUI.delay(GlobalVariable.minuteDelay)
			//DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).sendKeys(Keys.DELETE)

		}
		else if (System.getProperty("os.name").toLowerCase().trim().contains("mac")) {

			DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).click()
			pressDeleteKeyRepeatedlyFewTimes(numberOfTimesDeleteAndBackspacePressNeeded)
			pressBackSpaceKeyRepeatedlyFewTimes(numberOfTimesDeleteAndBackspacePressNeeded)
		}

		WebUI.delay(GlobalVariable.minuteDelay)
		DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).sendKeys(value)
		DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).sendKeys(Keys.ENTER)
	}

	@Keyword
	@Synchronized
	def setValueOfTextBoxUsingClipboard(TestObject object,String value, int numberOfTimesDeleteAndBackspacePressNeeded=40){
		WebUI.click(object)
		if (System.getProperty("os.name").toLowerCase().trim().contains("windows")) {
			Actions action = new Actions(DriverFactory.getWebDriver())
			StringSelection stringSelection = new StringSelection(value);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
			String result = (String) clipboard.getData(DataFlavor.stringFlavor);

			action.keyDown(Keys.CONTROL).build().perform()
			action.sendKeys("v").build().perform()
			action.keyUp(Keys.CONTROL).build().perform()
		}
	}

	@Keyword
	def resizeBrowser(int xoffset, int yoffset){
		Dimension d = new Dimension(xoffset, yoffset)
		DriverFactory.getWebDriver().manage().window().setSize(d)
	}

	@Keyword
	def deletAllFilesAtLocationWithExtension(String locationPath,String fileExtension){
		try{
			File folder = new File(locationPath);
			File[] fList = folder.listFiles();
			for (int i = 0; i < fList.length; i++) {
				String pes = fList[i];
				if (pes.endsWith(fileExtension)) {
					// and deletes
					boolean success = fList[i].delete()
				}
			}
		}catch (Exception e)
		{
			KeywordUtil.markErrorAndStop(e.getMessage())
		}
	}

	@Keyword
	def deleteSpecificFile(String filePath){

		try{
			File file = new File(filePath);
			file.delete()
		}catch (Exception e)
		{
			KeywordUtil.markErrorAndStop(e.getMessage())
		}
	}

	@Keyword
	def waitForChromeFileDownloadOnWindows(String locationPath,String fileExtension,int waitTime)
	{
		File folder = new File(locationPath);
		try{
			int counter = 0
			while(counter < waitTime)
			{
				File[] fList = folder.listFiles();
				for (int i = 0; i < fList.length; i++) {
					String pes = fList[i];
					if (pes.endsWith(fileExtension)) {
						counter = waitTime
						break
					}
				}
				fList = null
				WebUI.delay(1)
				counter = counter + 1
			}
		}catch (Exception e)
		{
			KeywordUtil.markFailedAndStop(e.getMessage())
		}
	}

	// -------------------------------------------------------------------------------
	// R&D

	/**
	 * Select a cabinet layout.
	 * @param layoutNumber
	 */
	@Keyword
	def clickOnThumbnailOption(thumbnailNumber) {

		String thumbnailXpath = '//*[@id="page-container"]//div[' + thumbnailNumber + '][contains(@class, "owl-item active")]'

		// Create dynamic object to receive xpath
		TestObject layoutThumbnailButton = new TestObject("layoutButton")
		layoutThumbnailButton.addProperty("xpath", ConditionType.EQUALS, thumbnailXpath)

		WebUI.click(layoutThumbnailButton)
	}

	/**
	 * Select a step in QSF.
	 * @param stepNumber
	 */
	@Keyword
	def selectStepInFlow(stepNumber) {

		String stepXpath = '//*[@id="milestone-bubbles-container"]/ul/div[' + stepNumber + ']'

		// Create dynamic object to receive xpath
		TestObject stepButton = new TestObject("stepButton")
		stepButton.addProperty("xpath", ConditionType.EQUALS, stepXpath)

		WebUI.click(stepButton)
	}

	/**
	 * Wait for element to be visible
	 * @param waitObject object to track
	 */
	@Keyword
	def waitForElementToBeVisible(String waitObject) {
		try {

			def loadingCompleted = false

			def attempt = 0

			while ((loadingCompleted == false) && (attempt < 3)) {

				loadingCompleted = WebUI.waitForElementVisible(findTestObject(waitObject), 20)

				attempt++
			}
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found : " + waitObject)
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element : " + waitObject)
		}
	}

	/**
	 * create dynamic object from an xpath
	 * @param buttonName of object
	 * @param xpath of object
	 */
	@Keyword
	def TestObject createDynamicObjectFromXpath(TestObject myObject, String xpath, String objectName) {

		myObject = new TestObject(objectName)

		//myObject.addProperty("name", ConditionType.EQUALS, objectName)

		println(myObject.toString())

		myObject.addProperty("xpath", ConditionType.EQUALS, xpath)

		return myObject
	}

	/**
	 * Wait for dynamic object/element to be visible
	 * @param waitObject object to track
	 */
	@Keyword
	def waitForDynamicElementToBeVisible(Object waitObject) {
		try {

			def loadingCompleted = false

			def attempt = 0

			while ((loadingCompleted == false) && (attempt < 3)) {

				loadingCompleted = WebUI.waitForElementVisible(waitObject, 20)

				attempt++
			}
		} catch (WebElementNotFoundException e) {
			KeywordUtil.markFailed("Element not found : " + waitObject)
		} catch (Exception e) {
			KeywordUtil.markFailed("Fail to click on element : " + waitObject)
		}


	}



	/**
	 * TEST
	 * @param waitObject object to track
	 */
	@Keyword
	def callFunction(String URL) {

		WebUI.callTestCase(findTestCase('0_Actions_General/goToUrl'), [('specifiedURL') : URL], FailureHandling.STOP_ON_FAILURE)
	}

	@Keyword
	def clickOnUiObjectUsingJavaScript(TestObject to){

		try {
			WebElement element = WebUiCommonHelper.findWebElement(to, GlobalVariable.midWait)
			WebUI.executeJavaScript("arguments[0].click();", Arrays.asList(element))
		}
		catch (Exception e) {
			KeywordUtil.markErrorAndStop("Error occured while clicking object using JavaScript. : " + e.getMessage())
		}
	}

	@Keyword
	def String getValueFromUiObjectUsingJSXpth(TestObject to){

		try {
			String xpath =	to.findPropertyValue("xpath")
			String jsscript = "return (document.evaluate(\""+xpath+"\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue).value"

			JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
			String value = js.executeScript(jsscript).toString()

			return value
		}
		catch (Exception e) {
			KeywordUtil.markErrorAndStop("Error occured while clicking object using JavaScript. : " + e.getMessage())
		}
	}

	@Keyword
	def int getXValueOfTestObject(TestObject to){
		Point elementLocation = WebUiCommonHelper.findWebElement(to,GlobalVariable.midWait).getLocation()
		println('elementXposFromTopLeft ' + elementLocation.getX())
		return  elementLocation.getX()
	}

	@Keyword
	def int getYValueOfTestObject(TestObject to){
		Point elementLocation = WebUiCommonHelper.findWebElement(to,GlobalVariable.midWait).getLocation()
		println('elementYposFromTopLeft ' + elementLocation.getY())
		return elementLocation.getY()
	}

	@Keyword
	def HashMap <String, Integer> getXYValueOfTestObjectPoint(TestObject testObject, String pointType) {

		Point elementLocation = WebUiCommonHelper.findWebElement(testObject,GlobalVariable.midWait).getLocation()
		HashMap <String, Integer> pointLocationMap = new HashMap <String, Integer>()

		int elementWidth = WebUiCommonHelper.findWebElement(testObject,GlobalVariable.midWait).getRect().getWidth()
		int elementHeight = WebUiCommonHelper.findWebElement(testObject,GlobalVariable.midWait).getRect().getHeight()

		switch (pointType) {

			case("TOP-LEFT"):

				pointLocationMap.put("x", elementLocation.getX())
				pointLocationMap.put("y", elementLocation.getY())
				break

			case("BOTTOM-RIGHT"):

				pointLocationMap.put("x", elementLocation.getX() + elementWidth)
				pointLocationMap.put("y", elementLocation.getY() + elementHeight)
				break

			case("MIDDLE"):

				pointLocationMap.put("x", (elementLocation.getX() + elementWidth)/2)
				pointLocationMap.put("y", (elementLocation.getY() + elementHeight)/2)
				break

			default:

				pointLocationMap.put("x", elementLocation.getX())
				pointLocationMap.put("y", elementLocation.getY())
				break

		}

		return pointLocationMap
	}


	@Keyword
	def boolean compareTextFiles(String fileNameWithPathToCompare,String refFileNameWithPath){
		try{
			BufferedReader reader1 = new BufferedReader(new FileReader(fileNameWithPathToCompare))
			BufferedReader reader2 = new BufferedReader(new FileReader(refFileNameWithPath))
			String line1 = reader1.readLine();
			String line2 = reader2.readLine();

			boolean areEqual = true;
			int lineNum = 1;
			while (line1 != null || line2 != null)
			{
				if(line1 == null || line2 == null)
				{
					areEqual = false;
					break;
				}
				else if(! line1.equalsIgnoreCase(line2))
				{
					areEqual = false;
					break;
				}
				line1 = reader1.readLine()
				line2 = reader2.readLine()
				lineNum++;
			}

			if(areEqual)
			{
				println("Two files have same content.")
			}
			else
			{
				println("Two files have different content. They differ at line "+lineNum)
				println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum)
			}
			reader1.close()
			reader2.close()
			return areEqual

		} catch (Exception e) {
			KeywordUtil.markFailed("Failed to compare two files "+e.message)
		}
	}

	@Keyword
	def void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	@Keyword
	def String downloadFileToTemp(String imgURL,String fileToSave){

		InputStream is = new URL(imgURL).openStream()

		if (fileToSave.contains(':')){
			Files.copy(is, Paths.get(fileToSave))
			return fileToSave
		}
		else{

			File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/temp')

			if (folderToBeWritten.exists() == false) {
				folderToBeWritten.mkdir()
			}

			String filePath = folderToBeWritten.getAbsolutePath().replaceAll("\\\\", "/") + "/" + fileToSave /*"/TestCaseDumps/" + GlobalVariable.currentTestCaseID */
			Files.copy(is, Paths.get(filePath))
			return filePath
		}
	}

	@Keyword
	def String convertBase64StringToImageAndGetPath(String base64Str,String fileToSave){

		// tokenize the data
		def parts = base64Str.tokenize(",");
		def imageString = parts[1];

		// create a buffered image
		BufferedImage image = null;
		byte[] imageByte;

		BASE64Decoder decoder = new BASE64Decoder();
		imageByte = decoder.decodeBuffer(imageString);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		image = ImageIO.read(bis);
		bis.close();

		// write the image to a file

		File folderToBeWritten = new File(RunConfiguration.getReportFolder() + '/temp')

		if (folderToBeWritten.exists() == false) {
			folderToBeWritten.mkdir()
		}

		String filePath = folderToBeWritten.getAbsolutePath().replaceAll("\\\\", "/") + "/" + fileToSave

		File outputfile = new File(filePath);
		ImageIO.write(image, "png", outputfile);
		return filePath
	}

	@Keyword
	def String findTestObjectWithLinkAndReturnLinkTitle(String link){

		String link_Xpath = '//*[@href="'+link+'"]'
		TestObject link_TestObject = new TestObject()
		link_TestObject.addProperty("xpath", ConditionType.EQUALS,link_Xpath)
		WebUI.click(link_TestObject)
		WebUI.delay(GlobalVariable.minWait)
		WebUI.switchToWindowIndex(1)
		String title = WebUI.getWindowTitle()
		String url = WebUI.getUrl()
		WebUI.closeWindowUrl(url)
		WebUI.delay(GlobalVariable.minuteDelay)
		WebUI.switchToWindowIndex(0)
		return title
	}

	@Keyword
	def boolean verifyStringWithRegularExpression(String str, String patternStr){
		java.util.regex.Pattern patternObj = regPattern.compile(patternStr)
		Matcher matcherObj = patternObj.matcher(str)
		Assert.assertTrue(matcherObj.matches(), '\n\n' + 'The provided string '+ str + " Not matching with pattern " + patternStr + '\n\n')
		return true
	}

	@Keyword
	def checkEndOfURLWithString(String stringWithWhichURLShouldEnd) {
		WebUI.waitForPageLoad(GlobalVariable.midWait)
		if (!(WebUI.getUrl().endsWith(stringWithWhichURLShouldEnd))) {
			KeywordUtil.markErrorAndStop(('Wrong page is displayed. URL should end with :' + stringWithWhichURLShouldEnd) + '. \n It seems wrong URL is launched.')
		}
	}

	@Keyword
	def removeDirectory(File dir,String skipfile) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File aFile : files) {
					String temp = aFile.getName()

					if (!skipfile.equalsIgnoreCase(aFile.getName())){
						removeDirectory(aFile,skipfile)
					}
				}
			}
			dir.delete()
		} else {
			dir.delete()
		}
	}

	@Keyword
	def copyFileFromOneLocationToAnother(String sourcePath, String destinationPath) {

		File sourceFile = new File(sourcePath)
		File destinationFile = new File(destinationPath + "/" + sourceFile.getName())
		FileUtils.copyFile(sourceFile, destinationFile)

		//Path src = Paths.get(sourcePath);
		//Path dest = Paths.get(destinationPath);
		//Files.copy(src.toFile(), dest.toFile());
	}

	@Keyword
	def copyDirectoryFromOneLocationToAnother(String sourceDir, String destinationDir) {

		try {

			File srcDir = new File(sourceDir)
			File destDir = new File(destinationDir)
			FileUtils.copyDirectory(srcDir, destDir)

		} catch (IOException e) {

			println (e.getMessage())
		}
	}

	@Keyword
	/**
	 *  @param filePath is path of file you want to upload. This should be absolute path to reach till that location
	 * 	Currently this function is handled only for Chrome.
	 */
	def uploadFileWithDesktopBasedPopup(String filePath){
		WebUI.delay(GlobalVariable.minuteDelay)
		String finalFinalPath = filePath.replace('/', "\\")
		StringSelection stringSelection = new StringSelection(finalFinalPath)
		println stringSelection
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
		clipboard.setContents(stringSelection, null)
		WebUI.delay(1)
		Robot robot = null;
		try {
			robot = new Robot()
		}
		catch (AWTException e) {
			e.printStackTrace()
		}
		robot.keyPress(KeyEvent.VK_ENTER)
		robot.keyRelease(KeyEvent.VK_ENTER)
		WebUI.delay(1)
		robot.keyPress(KeyEvent.VK_CONTROL)
		robot.keyPress(KeyEvent.VK_V)
		WebUI.delay(1)
		robot.keyRelease(KeyEvent.VK_V)
		robot.keyRelease(KeyEvent.VK_CONTROL)
		WebUI.delay(GlobalVariable.minuteDelay)
		robot.keyPress(KeyEvent.VK_ENTER)
		robot.keyRelease(KeyEvent.VK_ENTER)
		WebUI.delay(1)
	}


	/**
	 *  Check test object checked / unchecked value
	 *  @param tObj is test object to verify check functionality
	 */
	@Keyword
	def boolean isTestObjectChecked(TestObject tObj){
		try {
			boolean flag = WebUI.verifyElementChecked(tObj, 0)
			return flag;
		}catch(Exception e){
			println'Exception at verifying element checked state.'
			return false;
		}
	}

	/**
	 *  Drag test object from source to destination
	 *  @param tSourceObj is test object to drag
	 *  @param tDestinationObj is test object to drop source object 
	 */
	@Keyword
	def testObjDragAndDropTo(TestObject tSourceObj,TestObject tDestinationObj){
		String xpathOfSourceTO = tSourceObj.findPropertyValue("xpath")
		String xpathOfDestinationTO = tDestinationObj.findPropertyValue("xpath")
		WebElement element = DriverFactory.getWebDriver().findElement(By.xpath(xpathOfSourceTO))
		WebElement element2 = DriverFactory.getWebDriver().findElement(By.xpath(xpathOfDestinationTO))

		final String java_script =
				"var src=arguments[0],tgt=arguments[1];var dataTransfer={dropEffe" +
				"ct:'',effectAllowed:'all',files:[],items:{},types:[],setData:fun" +
				"ction(format,data){this.items[format]=data;this.types.append(for" +
				"mat);},getData:function(format){return this.items[format];},clea" +
				"rData:function(format){}};var emit=function(event,target){var ev" +
				"t=document.createEvent('Event');evt.initEvent(event,true,false);" +
				"evt.dataTransfer=dataTransfer;target.dispatchEvent(evt);};emit('" +
				"dragstart',src);emit('dragenter',tgt);emit('dragover',tgt);emit(" +
				"'drop',tgt);emit('dragend',src);";

		JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getWebDriver()
		js.executeScript(java_script, element, element2);
	}


	/**
	 *  Mouse wheel movement on 2D/3D player using java script
	 *  @param webElementID is ID representing DOM element 
	 *  @param scrollIncrement is constant value +120 for Zoom in/ -120 for Zoom out(1 mouse wheel rotation)
	 */
	@Keyword
	def mouseWheelMovementUsingJavaScript(String webElementID, int scrollIncrement){

		String script = "var evt = document.createEvent('MouseEvents');"+
				"evt.initEvent('wheel', true, true);"+
				"evt.deltaY = arguments[0];"+
				"document.getElementById('"+webElementID+"').dispatchEvent(evt)"

		((JavascriptExecutor) DriverFactory.getWebDriver()).executeScript(script, scrollIncrement);

	}
	@Keyword
	@Synchronized
	def double convertNumStringToDouble(String value){
		try{
			return  Double.parseDouble(value)
		}catch(Exception e){
			println e.printStackTrace();
		}
	}

	@Keyword
	def extractNumbersFromString (int numberLength, String stringToExtractTheNumbersFrom) {

		final java.util.regex.Pattern p = java.util.regex.Pattern.compile( "(\\d{" + numberLength +"})" )

		final java.util.regex.Matcher m = p.matcher( stringToExtractTheNumbersFrom )

		ArrayList<String> allMatches = new ArrayList<String>()

		while (m.find()) {
			allMatches.add(m.group())
		}

		return allMatches
	}


	/**
	 * Get the text is present in Image portion in expected UI cell.
	 * @param numberOfRowsToSplitScreen Specify the no. of rows in which screen is to be divided
	 * @param numberOfColumnsToSplitScreen Specify the no. of columns in which screen is to be divided
	 * @param expectedCellRowNo Specify the expected row no. of cell, in which point is to be verified
	 * @param expectedCellColumnNo Specify the expected row no. of cell, in which point is to be verified
	 */
	@Keyword
	def String getTextInImageInUiSection (int numberOfRowsToSplitScreen, int numberOfColumnsToSplitScreen, int expectedCellRowNo, int expectedCellColumnNo ) {

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

		return textExtractedByOCR
	}

	/**
	 * Get the VSTS work item details for id list provided.
	 * @param workItemIdList list of ids
	 */
	@Keyword
	def HashMap<String, Object> getDetailsFromWorkItemIdList (ArrayList<String> workItemIdList) {

		TfsWebServices tfsWebServicesLocal = new TfsWebServices()

		tfsWebServicesLocal.setTfsIntegrationPropertiesFilePath('./External Libraries/tfsIntegration.properties')

		tfsWebServicesLocal.setTfsIntegrationTempPropertiesFilePath(('./External Libraries/tfsIntegrationTemp/tfsIntegrationTemp_' + GlobalVariable.envInfo.get("currentSuiteName")) + '.properties')

		HashMap<String, Object> mapWorkItemProps = new HashMap <String, Object> (
				HashMap<String, Object> props = new HashMap <String, Object> ())

		if (workItemIdList.size() != 0) {

			for (String workItemId : workItemIdList) {

				sleep(2000)

				String bugDetails = tfsWebServicesLocal.getWorkItemDetails(workItemId)

				//If bug details are null, try to fetch them again
				if (bugDetails == null) {

					sleep(3000)
					bugDetails = tfsWebServicesLocal.getWorkItemDetails(workItemId)
				}

				props.put('WI_' + 'System.State', tfsWebServicesLocal.retrieveWorkItemFieldValue('System.State', bugDetails))
				props.put('WI_' + 'System.Reason', tfsWebServicesLocal.retrieveWorkItemFieldValue('System.Reason', bugDetails))
				props.put('WI_' + 'System.WorkItemType', tfsWebServicesLocal.retrieveWorkItemFieldValue('System.WorkItemType', bugDetails))
				props.put('WI_' + 'System.Title', tfsWebServicesLocal.retrieveWorkItemFieldValue('System.Title', bugDetails))

				//Formed the URL manually instead of fetching from API response
				
				//DataProcessing dataProcessing = new DataProcessing()
				
				//String testCaseHref = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsEndpoint") + "/" +
				//					  dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsCollection") + "/" +
				//					  dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsProject") +
				//					  "/_workitems/edit/" + GlobalVariable.currentTestCaseID
				
				//props.put('WI_' + 'URL', testCaseHref)
									  
				mapWorkItemProps.put(workItemId, props)
			}
		}

		return mapWorkItemProps
	}
}