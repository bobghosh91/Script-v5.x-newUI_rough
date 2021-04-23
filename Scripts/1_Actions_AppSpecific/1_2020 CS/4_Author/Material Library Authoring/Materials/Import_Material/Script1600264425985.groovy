import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable


WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/Click_Import'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/input_Upload CSV file_material'))

CustomKeywords.'appSpecificFunctions.fileUpload.dataImport'("Data Files\\ReferenceData\\" + GlobalVariable.currentTestCaseID, 'csv')

//WebUI.uploadFile(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/input_Upload CSV file_material'), RunConfiguration.getProjectDir().replaceAll("/","\\\\")+'\\'+CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'DataPath'))

WebUI.delay(15)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/4_Author/Material Library Authoring/Materials/Click_Start Import Material'), [:], FailureHandling.STOP_ON_FAILURE)


