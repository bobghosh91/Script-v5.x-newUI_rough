import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.eclipse.persistence.internal.oxm.record.json.JSONParser.object_return
import org.openqa.selenium.By
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable
 

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/span_Select Catalog'))

WebUI.doubleClick(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/input_select2-search__field'))

WebUI.clearText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/input_select2-search__field'))

WebUI.setText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/input_select2-search__field'),catalogName)

WebUI.sendKeys(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/input_select2-search__field'), Keys.chord(Keys.ENTER))

WebUI.delay(GlobalVariable.minWait)






