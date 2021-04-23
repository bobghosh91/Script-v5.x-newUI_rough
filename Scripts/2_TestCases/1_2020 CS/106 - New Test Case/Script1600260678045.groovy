import org.openqa.selenium.By
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
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import appSpecificFunctions.commonFunctions

println "open"

CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CompanyName')
//CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx","CS", 'CatalogName', 'Catalog'+commonFunctions.timeStamp )
CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx","CS" , GlobalVariable.currentTestCaseID, 'CatalogName', 'Catalog'+commonFunctions.timeStamp )

