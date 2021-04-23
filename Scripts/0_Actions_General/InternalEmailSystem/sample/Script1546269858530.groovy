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

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/launchOutlookWebApp'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/signInToOutlookWebApp'), [('userName') : 'qaguiautomationtest3@2020spaces.com'
        , ('password') : 'auto123#'], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/clickOnInbox'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/ensureEmailListShowingRecentAtTop'), [:], FailureHandling.STOP_ON_FAILURE)

not_run: WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/deleteAllExistingEmails'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/selectEmailAndReturnItsBody'), [('emailSubject') : 'Trial Message 2'], 
    FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('0_Actions_General/InternalEmailSystem/logoutFromOutlookWebApp'), [('userName') : 'qaguiautomationtest3'], 
    FailureHandling.STOP_ON_FAILURE)

