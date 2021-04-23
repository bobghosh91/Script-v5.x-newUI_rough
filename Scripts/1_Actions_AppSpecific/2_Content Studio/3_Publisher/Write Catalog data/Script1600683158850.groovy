import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import genericFunctions.CICMethod
import genericFunctions.DataProcessing
import genericFunctions.EnvironmentSetup


def a=EnvironmentSetup.timeStamp

//CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(2&3, 'CatalogName', ma+a)

/*CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(2 , 'CatalogName', 'MajorSwitchCat'+a)

CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(3 , 'CatalogName', 'MajorSwitchCat'+a)

CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(2, 'CatalogCode', 'CCMA'+a)


CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(4, 'CatalogName', 'MinorSwitchCat'+a)

CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(5, 'CatalogName', 'MinorSwitchCat'+a)

CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(4, 'CatalogCode', 'CCMI'+a)


CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(6, 'CatalogName', 'NonGraphic'+a)

CustomKeywords.'genericFunctions.writeDataNew.writeDataToExelFile'(6, 'CatalogCode', 'CCNG'+a)*/


CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx",GlobalVariable.testDataSheetName , GlobalVariable.testDataSheetName, "CatalogName", "Test"+a)
CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx",GlobalVariable.testDataSheetName , GlobalVariable.currentTestCaseID, 'CatalogCode', 'Test')
