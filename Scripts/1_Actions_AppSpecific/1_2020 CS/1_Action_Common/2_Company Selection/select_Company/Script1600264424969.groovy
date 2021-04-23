import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.junit.After
import org.openqa.selenium.By

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

CustomKeywords.'genericFunctions.Assertions.assertTestObjectIsClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/2_Company Selection/select_company'))
WebUI.delay(1)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/2_Company Selection/select_company'), 'Test2020 New', false)

WebUI.delay(5)


