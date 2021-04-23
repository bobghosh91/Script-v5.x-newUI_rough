import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.junit.After
import org.openqa.selenium.By

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_company'))

CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_company'))

WebUI.delay(GlobalVariable.minWait)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/2_Content Studio/1_Action_Common/2_Company Selection/select_company'), CompanyName, true)