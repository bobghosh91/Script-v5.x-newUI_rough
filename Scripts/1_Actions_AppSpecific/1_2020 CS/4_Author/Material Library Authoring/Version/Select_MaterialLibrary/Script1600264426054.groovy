import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

String MatlibraryName = CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, "MaterialLibraryName")

println MatlibraryName

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Versions/select_Material Library'))

WebUI.delay(2)

WebUI.selectOptionByLabel(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Versions/select_Material Library'), MatlibraryName, false)

int index =WebUI.getNumberOfSelectedOption(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Versions/select_Material Library'))

if (index == 0) {
	KeywordUtil.logInfo("Material library(s) (provided as input) not found.")
}
WebUI.delay(GlobalVariable.minWait)