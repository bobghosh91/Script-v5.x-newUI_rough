import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable


WebUI.callTestCase(findTestCase('Test Cases/1_Actions_AppSpecific/2_Content Studio/4_Author/Click_Import-Export'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.delay(1)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/input_csvFiles'))

CustomKeywords.'appSpecificFunctions.fileUpload.dataImport'("Data Files\\ReferenceData\\" + GlobalVariable.currentTestCaseID, 'csv')

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/button_Import CSVsXLS'))

//WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/button_Import CSVsXLS'))
//CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeVisible'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/div_uploadProgressBarValue'))
CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/button_Release Notes'))



