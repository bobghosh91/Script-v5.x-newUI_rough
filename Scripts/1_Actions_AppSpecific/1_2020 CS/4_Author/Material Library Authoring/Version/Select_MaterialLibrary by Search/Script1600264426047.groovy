import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable


WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/span_Select Material Library'))

WebUI.doubleClick(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/input_Select Material Library_'))

/*WebUI.setText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Model Library Authoring/Models/input_library_select2-search'),CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(
			GlobalVariable.currentTestCaseID, 'LibraryName'))
*/
WebUI.setText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/input_Select Material Library_'),MatlibraryName)
	
WebUI.sendKeys(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/input_Select Material Library_'), Keys.chord(Keys.ENTER))

WebUI.delay(GlobalVariable.minWait)