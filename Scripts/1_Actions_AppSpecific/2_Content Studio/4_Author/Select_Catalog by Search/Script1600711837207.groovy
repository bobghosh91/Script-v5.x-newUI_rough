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
 

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_Select Catalog'))

WebUI.doubleClick(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_select catalog by search'))

WebUI.clearText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_select catalog by search'))

WebUI.setText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_select catalog by search'),catalogName)

WebUI.sendKeys(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/4_Author/span_select catalog by search'), Keys.chord(Keys.ENTER))

WebUI.delay(GlobalVariable.minWait)






