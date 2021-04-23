import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.By as By
import org.openqa.selenium.JavascriptExecutor as JavascriptExecutor
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import internal.GlobalVariable as GlobalVariable

import appSpecificFunctions.commonFunctions


//WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'genericFunctions.DataProcessing.writeToSpecificCellIntoExcel'("Data Files\\Test-Input.xlsx", "CS", "201", 'CatalogName', 'Catalog'+commonFunctions.timeStamp)

//CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Company", "Magna")

//CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Roles", "ContentPublisher")

//CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Roles", "ContentAuthor")
