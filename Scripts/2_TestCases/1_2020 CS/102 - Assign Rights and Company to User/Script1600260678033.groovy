import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.junit.After

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

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/LoginToCS'), [("userEmail"):"john08@dispostable.com"], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Click_Site-AdminPage'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Click_Users'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/Search_User'), [("userEmail"):"qaguiautomationtest11@2020spaces.com"], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/button_Edit-User'))

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/button_Ok_EditUser'))

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/select_Role'), [('role') : 'AdminPage'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/select_Role'), [('role') : 'ContentPublisher'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/select_Role'), [('role') : 'ContentAuthor'], FailureHandling.STOP_ON_FAILURE)

CustomKeywords.'appSpecificFunctions.AdminPage.assignOptionFromList'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/companyForUser-list-select'), "Test2020 New")

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickableAndClick'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/button_Ok_EditUser'))

WebUI.delay(5)

CustomKeywords.'genericFunctions.Utilities.waitForObjectToBeClickable'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/button_Edit-User'))

CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Company", "Test2020 New")

CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Roles", "ContentPublisher")

CustomKeywords.'appSpecificFunctions.Tables.TableEntryVerify'(findTestObject('1_Actions_AppSpecific/1_2020 CS/2_AdminPage/Site AdminPage/table_user list'), "Roles", "ContentAuthor")



WebUI.callTestCase(findTestCase('1_Actions_AppSpecific/1_2020 CS/1_Action_Common/1_Login-Page/Logout'), [:], FailureHandling.STOP_ON_FAILURE)

