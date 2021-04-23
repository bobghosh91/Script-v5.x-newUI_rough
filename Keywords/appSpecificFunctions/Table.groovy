package appSpecificFunctions
import java.util.regex.Pattern.Loop

import javax.validation.metadata.ReturnValueDescriptor

import org.eclipse.persistence.internal.oxm.record.json.JSONParser.object_return
import org.eclipse.persistence.internal.oxm.record.json.JSONParser.pair_return
import org.junit.After
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable



class Tables {
	WebDriver driver=DriverFactory.getWebDriver()
	WebElement tableBodyElement
	String tableBodyElementId
	WebElement tablePanel
	WebElement rowEntry
	public int attempsLimit = 2;
	List <WebElement> tableRows = null
	List <WebElement> tableColumns = null
	WebElement tableRowFirstElement = null
	String tdata  = null;
	String celltext =null
	String celltext1 =null

	def APTables (TestObject object) {
		DriverFactory.getWebDriver().switchTo().frame(0)
		String xpathObj = object.findPropertyValue('xpath')
		WebElement tableBodyElement = DriverFactory.getWebDriver().findElement(By.xpath(xpathObj))
		tablePanel = tableBodyElement.findElement(By.xpath(".."))
		int i=1
		while (!(tablePanel.getAttribute("class").toString().contentEquals("panel-body")) && i<=10){
			tablePanel = tablePanel.findElement(By.xpath(".."));
			i=i+1;
		}
		KeywordUtil.logInfo("::::::::::::" + tablePanel.getAttribute("class"))
		setTableBodyElement(tableBodyElement)
	}

	/*def void reinitiateBasicTableElements(WebElement tableBodyElement) throws InterruptedException{
	 boolean staleElementExceptionCaught = true;
	 int i=0;
	 while (staleElementExceptionCaught == true)  {
	 try {
	 tableBodyElement = driver.findElement(By.id(tableBodyElementId));
	 tableBodyElementId = tableBodyElement.getAttribute("id");
	 tableRows = tableBodyElement.findElements(By.xpath(".//tr"));
	 tableColumns = tableBodyElement.findElements(By.xpath(".//tr[1]//td"));
	 tableRowFirstElement = tableBodyElement.findElements(By.xpath(".//tr")).get(0).findElement(By.xpath(".//td"));
	 tableRowFirstElement.getAttribute("class").equals("dataTables_empty");
	 staleElementExceptionCaught = false;
	 }
	 catch (Exception e) {
	 staleElementExceptionCaught = true;
	 KeywordUtil.logInfo("Table Class: Stale Element found, and is being handeled.");
	 Thread.sleep(1000);
	 i=i+1;
	 }
	 }
	 }*/


	def boolean goToNextPage () throws InterruptedException {
		WebElement NextPageButton;
		//Finding the pagination section
		WebElement pagination = tablePanel.findElement(By.xpath(".//div[contains(@id, '_paginate')]"));
		KeywordUtil.logInfo("Pagination section found");
		Thread.sleep(2000);

		try {
			NextPageButton = pagination.findElement(By.xpath(".//a[contains(@id, 'next')]")); //Finding the "Next" page
		} catch (NoSuchElementException e) {
			KeywordUtil.logInfo("<Next> Page Button not found");
			return false;
		}

		if (NextPageButton.getAttribute("class").contains("disabled") == false) {
			KeywordUtil.logInfo("Next button enabled?: " + NextPageButton.isEnabled());
			NextPageButton.click();
			WebUI.delay(2)
			pagination.findElement(By.xpath(".//span//a[contains(@class,'current')]")).isDisplayed()
			return true;
		}
		else {
			KeywordUtil.logInfo("<Next> Page Button not clickable");
			return false;
		}
	}

	def int getPageCount() throws InterruptedException {
		KeywordUtil.logInfo("Returning page count.");

		for (int attempt=0; attempt < attempsLimit; attempt ++) {

			try {
				WebElement LastPage;
				WebElement pagination = tablePanel.findElement(By.xpath(".//div[contains(@id, '_paginate')]"));

				List <WebElement> paginationPages = pagination.findElements(By.xpath(".//span//a"));

				if (paginationPages.size() > 1) {
					LastPage = paginationPages.get(paginationPages.size()-1);
					LastPage.getText();
					KeywordUtil.logInfo("Last Page: " + LastPage.getText());
					int Count=Integer.parseInt(LastPage.getText());

					KeywordUtil.logInfo("More than one pages found.");
					//WebUI.delay(GlobalVariable.midWait)

					return Count
				}
				else {
					KeywordUtil.logInfo("Only One page found.");
					//WebUI.delay(GlobalVariable.midWait);
					return 1;
				}


			}
			catch (Exception e) {
				KeywordUtil.logInfo("\"StaleElementReferenceException\" Found. Re-attempting the operation.")
			}

		}

		throw new Exception("Cannot Return an Integer.");
	}
	def Object[] getTableHeaders (){

		tableBodyElement=getTableBodyElement()

		List <WebElement> tableHeaderSection = tablePanel.findElements(By.xpath(".//thead//tr//th"))
		List <String> header = new ArrayList<String>()

		for (WebElement headerEntry: tableHeaderSection) {
			header.add(headerEntry.getText());
		}

		return header
	}
	@Keyword
	def clickTableEntry(TestObject object,String columnName, String tablevalue){
		APTables(object)
		int counter = 0; int index=0;
		Loop:for (String element1:getTableHeaders()) {

			if ( element1.contains(columnName)) {
				index=counter+1
				int pageCount= getPageCount()
				for(int i=0;i<pageCount;i++){
					try {
						List < WebElement > tableRowSection = tableBodyElement.findElements(By.xpath("//tbody//tr//td["+index+"]"))

						for (WebElement rowEntry: tableRowSection) {

							if ((rowEntry.getText()).contains(tablevalue)) {

								if (rowEntry.findElement(By.xpath("..")).getAttribute("class").contains("_selected selected")){
									KeywordUtil.logInfo("Selected");
									break Loop
								}else{
									rowEntry.click()
									KeywordUtil.logInfo("Entry found on table " + tablevalue);
									break Loop
								}
							}
						}
					}
					catch(Exception e) {
						println(e)
					}
					goToNextPage()
					tableBodyElement.isDisplayed()
				}

			}else{
				KeywordUtil.logInfo("Table header not found on table " + columnName);
			}
			counter++
		}
		DriverFactory.getWebDriver().switchTo().defaultContent()
	}

	@Keyword
	def TableEntryVerify(TestObject object,String VerifycolumnName, String Verifytablevalue){

		APTables(object)
		tableBodyElement=getTableBodyElement()
		int counter = 0; int index=0
		Loop:for (String element1:getTableHeaders()) {

			if ( element1.contains(VerifycolumnName)) {
				index=counter+1
				List < WebElement > tableRowSection = tableBodyElement.findElements(By.xpath("//tbody//tr//td["+index+"]"))
				for (WebElement rowEntry: tableRowSection) {
				
					if (rowEntry.findElement(By.xpath("..")).getAttribute("class")){
						print rowEntry.getText()==Verifytablevalue
						if ((rowEntry.getText()==Verifytablevalue)==true){
							KeywordUtil.logInfo(rowEntry.getText());
							break Loop
						}
						
					}
				}


			}
			counter++
		}
     DriverFactory.getWebDriver().switchTo().defaultContent()
	}
}