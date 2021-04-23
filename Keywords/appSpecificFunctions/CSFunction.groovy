package appSpecificFunctions
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.eclipse.core.runtime.Assert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory

public class AdminPage {

	static WebDriver driver=DriverFactory.getWebDriver()

	@Keyword
	def assignOptionFromList(TestObject object ,String markets) {
		// Assign market to company
		driver.switchTo().frame(0)

		String xpathObj = object.findPropertyValue("id")
		String xpathObj1 = object.findPropertyValue("xpath")

		WebElement marketsForCompany = DriverFactory.getWebDriver().findElement(By.id(xpathObj))

		Thread.sleep(2000);

		List <WebElement> marketNameElements = marketsForCompany.findElements(By.xpath(xpathObj1));

		List<String> marketNameElementsText = Arrays.asList(marketsForCompany.getText().split("[\n]"));

		int marketSelectionCount = 0;


		markets = markets.replaceAll("\\s+","");
		int i = 0;
		for (String marketNameElementText: marketNameElementsText) {

			marketNameElementText = marketNameElementText.replaceAll("\\s+","");

			if(marketNameElementText.equals(markets)) {

				if (marketNameElements.get(i).getAttribute("checked")==null) {

					marketNameElements.get(i).click();
					marketSelectionCount = marketSelectionCount+1;
					Thread.sleep(1000);
				}
			}

			i++;
		}



		if (marketSelectionCount == 0) {
			KeywordUtil.logInfo("Market(s) (provided as input) not found.");
		}

		driver.switchTo().defaultContent()
	}
}

class PublisherPage{

	@Keyword
	def  selectEntryFromList (TestObject object, String entryName){
		//Select entry like market language//
		WebDriver driver=DriverFactory.getWebDriver();
		driver.switchTo().frame(0)

		String idObj = object.findPropertyValue("id")
		String xpathObj = object.findPropertyValue("xpath")

		WebElement allElement = driver.findElement(By.id(idObj))

		String nameofElemetprint = allElement.getText()

		List <WebElement> NameElements = driver.findElements(By.xpath(xpathObj))
		KeywordUtil.logInfo ('No. of Elements '+NameElements.size())

		List<String> NameElementsText = Arrays.asList(allElement.getText().split("[\\n]"))


		int selectionCount = 0

		int i = 0

		for (String NameElementText: NameElementsText) {

			NameElementText = NameElementText.replaceAll("\\s+","")
			entryName = entryName.replaceAll("\\s+","")

			if (NameElementText.equals(entryName)) {
				KeywordUtil.logInfo("....." + NameElementText)
				NameElements.get(i).click()
				selectionCount = selectionCount+1
				//println(selectionCount)
				Thread.sleep(1000)
			}

			i++
		}

		if (selectionCount == 0) {
			KeywordUtil.logInfo("Entry (provided as input) not found.")
		}
		DriverFactory.getWebDriver().switchTo().defaultContent()

	}

	@Keyword
	def  editSelectUnselectEntryFromList(TestObject object, String entryName){
		//Select entry like market language//
		WebDriver driver=DriverFactory.getWebDriver();
		driver.switchTo().frame(0)

		String idObj = object.findPropertyValue("id")
		String xpathObj = object.findPropertyValue("xpath")

		String specificCatalogListXpath = findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/3_Publisher/2_Edit_Catalog/div_specificListCatalogView').findPropertyValue('xpath')

		WebElement allElement = driver.findElement(By.xpath(specificCatalogListXpath))

		String demotext = allElement.getText()

		List <WebElement> NameElements = driver.findElements(By.xpath(xpathObj))
		KeywordUtil.logInfo ('No. of Elements '+NameElements.size())

		List<String> NameElementsText = Arrays.asList(allElement.getText().split("[\\n]"))

		//KeywordUtil.logInfo (NameElementsText.stream() )
		int selectionCount = 0

		int i = 0

		for (String NameElementText: NameElementsText) {

			NameElementText = NameElementText.replaceAll("\\s+","")
			entryName = entryName.replaceAll("\\s+","")

			if (NameElementText.equals(entryName)) {
				KeywordUtil.logInfo("....." + NameElementText)
				NameElements.get(i).click()
				//NameElements.get(i).clear()
				selectionCount = selectionCount+1
				//println(selectionCount)
				Thread.sleep(1000)
			}

			i++
		}

		if (selectionCount == 0) {
			KeywordUtil.logInfo("Entry (provided as input) not found.")
		}
		DriverFactory.getWebDriver().switchTo().defaultContent()

	}


	@Keyword
	def  libraryselection (TestObject object, String libraryNames){
		//Library selection while catalog creation//
		DriverFactory.getWebDriver().switchTo().frame(0)

		String xpathObj = object.findPropertyValue("id")
		String xpathObj1 = object.findPropertyValue("xpath")

		WebElement librariesForCatalog = DriverFactory.getWebDriver().findElement(By.id(xpathObj))

		List <WebElement> libraryNameElements = librariesForCatalog.findElements(By.xpath(xpathObj1))
		KeywordUtil.logInfo ('No. of libraries '+libraryNameElements.size())

		List<String> libraryNameElementsText = Arrays.asList(librariesForCatalog.getText().split("[\\n]"))
		//println (libraryNameElementsText)

		int librarySelectionCount = 0

		int i = 0

		for (String libraryNameElementText: libraryNameElementsText) {

			libraryNameElementText = libraryNameElementText.replaceAll("\\s+","")
			libraryNames = libraryNames.replaceAll("\\s+","")
			//println(libraryNames)

			int indexOfBracket = libraryNameElementText.indexOf('(')
			libraryNameElementText = libraryNameElementText.substring(0, indexOfBracket)

			if (libraryNameElementText.equals(libraryNames)) {
				//KeywordUtil.logInfo("....." + libraryNameElementText)
				libraryNameElements.get(i).click()
				librarySelectionCount = librarySelectionCount+1
				//println(librarySelectionCount)
				Thread.sleep(1000)
			}

			i++
		}

		if (librarySelectionCount == 0) {
			KeywordUtil.logInfo("Model library(s) (provided as input) not found.")
		}
		DriverFactory.getWebDriver().switchTo().defaultContent()

	}
}
class AuthorPage{

	static WebDriver driver=DriverFactory.getWebDriver()


	@Keyword
	def  exportData (TestObject object){

		DriverFactory.getWebDriver().switchTo().frame(0)
		String xpathObj = object.findPropertyValue('xpath')
		String xpathObj1 = object.findPropertyValue('id')

		String buttontext =DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).getText()

		//KeywordUtil.logInfo(str)

		if (buttontext == 'Prepare') {
			DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)).click()
		} else {
			DriverFactory.getWebDriver().findElement(By.id(xpathObj1)).click()
		}

		DriverFactory.getWebDriver().switchTo().defaultContent()

	}

	@Keyword
	def selectCatalog(TestObject object, String catalogName, String market, String company){
		//Select catalog in Authoring company
		DriverFactory.getWebDriver().switchTo().frame(0)
		String xpathObj = object.findPropertyValue('xpath')
		//String xpathObj1 = object.findPropertyValue('id')
		Select sel
		try {

			sel = new Select(DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)))

		}
		catch (Exception e) {

			sel = new Select(DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)))
		}
		Thread.sleep(1000)
		List<WebElement> SelectedOptions = sel.getOptions();
		int counter = 0; int index=0;
		for (WebElement SelectedOption :SelectedOptions)
		{
			if((SelectedOption.getText()).contains(catalogName+" | "+market+" | "+company)) {
				index = counter;
				Thread.sleep(1000)
				sel.selectByIndex(index)


			}
			counter++;
		}
		if (index == 0) {
			KeywordUtil.logInfo("Catalog(s) (provided as input) not found.")
		}
		DriverFactory.getWebDriver().switchTo().defaultContent()

	}

	@Keyword
	def selectLibrary(TestObject object, String LibraryName){

		DriverFactory.getWebDriver().switchTo().frame(0)
		String xpathObj = object.findPropertyValue('xpath')
		//String xpathObj1 = object.findPropertyValue('id')

		Select sel
		try {
			sel = new Select(DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)))
		}
		catch (Exception e) {
			KeywordUtil.logInfo(e)
			sel = new Select(DriverFactory.getWebDriver().findElement(By.xpath(xpathObj)))
		}

		List<WebElement> SelectedOptions = sel.getOptions();
		int counter = 0; int index=0;
		for (WebElement SelectedOption :SelectedOptions)
		{
			if((SelectedOption.getText()).contains(LibraryName)) {
				index = counter;
				Thread.sleep(1000)
				sel.selectByIndex(index)

			}
			counter++

		}
		if (index == 0) {
			KeywordUtil.logInfo("Model library(s) (provided as input) not found.")
		}

		DriverFactory.getWebDriver().switchTo().defaultContent()
	}

}


class fileUpload {
	// Upload multiple file like CSV, AXS, Jpg, zip
	static String browser=DriverFactory.getExecutedBrowser().name.toLowerCase().replace("_driver","")

	def fileUploadUtilityPath="External Libraries\\FileUploadSe.exe"

	@Keyword
	def dataImport (String dataPath, String importType) throws InterruptedException {

		/*if (assetType.equals("Graphical")) { hasGraphicYesRadioButton.click() }
		 else { hasGraphicNoRadioButton.click() }*/
		dataPath = RunConfiguration.getProjectDir().replaceAll("/","\\\\")+'\\'+dataPath

		File f = new File(dataPath);
		if (f.exists() && f.isDirectory()) {

			KeywordUtil.logInfo("Path Exists");

			String exePath; String fileNamesToUpload; int fileCountToUpload;
			String thumbnailFileExt1 = ".jpg"; String thumbnailFileExt2 = ".jpg"
			String thumbnailFileExt3 = ".axs"; String thumbnailFileExt4 = ".axs"
			String thumbnailFileExt5 = ".csv"; String thumbnailFileExt6 = ".csv"
			String thumbnailFileExt7 = ".zip"; String thumbnailFileExt8 = ".zip"

			switch (importType) {

				case "zip":
					fileNamesToUpload = getfileNames(thumbnailFileExt7, thumbnailFileExt8,dataPath)
					fileCountToUpload = getfilesCount(thumbnailFileExt7, thumbnailFileExt8, dataPath)
					Assert.isTrue(fileCountToUpload!=0,"No ZIP for upload.")
					exePath = "\"" + (fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break;

				case "csv":
					fileNamesToUpload = getfileNames(thumbnailFileExt5, thumbnailFileExt6,dataPath)
					fileCountToUpload = getfilesCount(thumbnailFileExt5, thumbnailFileExt6, dataPath)
					Assert.isTrue(fileCountToUpload!=0,"No CSV for upload.")
					exePath = "\"" + (fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break

				case "axs":
					fileNamesToUpload = getfileNames(thumbnailFileExt3, thumbnailFileExt4,dataPath + "\\AXS")
					fileCountToUpload = getfilesCount(thumbnailFileExt3, thumbnailFileExt4, dataPath + "\\AXS")
					Assert.isTrue(fileCountToUpload!=0,"No AXS for upload.")
					exePath = "\"" + (fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\\AXS" + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break

				case "small":
					fileNamesToUpload = getfileNames(thumbnailFileExt1, thumbnailFileExt2,dataPath + "\\SMALL")
					fileCountToUpload = getfilesCount(thumbnailFileExt1, thumbnailFileExt2, dataPath+ "\\SMALL")
					Assert.isTrue(fileCountToUpload!=0,"No SMALL thumbnail for upload.")
					exePath = "\"" +(fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\\SMALL" + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break

				case "medium":
					fileNamesToUpload = getfileNames(thumbnailFileExt1, thumbnailFileExt2,dataPath + "\\MEDIUM")
					fileCountToUpload = getfilesCount(thumbnailFileExt1, thumbnailFileExt2, dataPath + "\\MEDIUM")
					Assert.isTrue(fileCountToUpload!=0,"No MEDIUM thumbnail for upload.")
					exePath = "\"" +(fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\\MEDIUM" + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break

				case "large":
					fileNamesToUpload = getfileNames(thumbnailFileExt1, thumbnailFileExt2,dataPath + "\\LARGE")
					fileCountToUpload = getfilesCount(thumbnailFileExt1, thumbnailFileExt2, dataPath + "\\LARGE")
					Assert.isTrue(fileCountToUpload!=0,"No LARGE thumbnail for upload.")
					exePath = "\"" +(fileUploadUtilityPath) + "\""+  " \"" + dataPath + "\\LARGE" + "\" " + "\" "+ fileNamesToUpload + "\" " + "\"" + browser+"\""
					Upload(exePath)
					break
			}
			Thread.sleep(15000)
		}
		else{
			KeywordUtil.logInfo("Path NOT Exists_"+dataPath)

		}
	}

	public static String getfileNames (final String extension1, final String extension2,String Path){
		String ext1 = extension1

		File file = new File(Path)

		File[] files = file.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						if(name.toLowerCase().endsWith(extension1) || name.toLowerCase().endsWith(extension2)){
							return true;
						} else {
							return false;
						}
					}
				});

		String fileNamesToUpload = ""

		for(File f:files){

			fileNamesToUpload = fileNamesToUpload + "\"\"" + f.getName() + "\"\" "
			KeywordUtil.logInfo (f.getName())
		}

		return fileNamesToUpload
	}
	def static int getfilesCount (final String extension1, final String extension2, String path) {

		String ext1 = extension1

		File file = new File(path)

		File[] files = file.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						if(name.toLowerCase().endsWith(extension1) || name.toLowerCase().endsWith(extension2)){
							return true
						} else {
							return false
						}
					}
				});

		int fileCountToUpload = 0

		for(File f:files){
			//KeywordUtil.logInfo(fileCountToUpload)
			fileCountToUpload = fileCountToUpload + 1
		}

		return fileCountToUpload
	}

	public def Upload (String exePath) {

		try {
			Thread.sleep(2000)
			Runtime.getRuntime().exec(exePath)
		} catch (IOException | InterruptedException e) {
			e.printStackTrace()
		}
	}

}