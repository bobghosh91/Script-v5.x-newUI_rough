import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as log
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import groovy.time.TimeCategory as TimeCategory
import java.text.SimpleDateFormat as SimpleDateFormat
import com.kms.katalon.core.testobject.*
import java.util.Date as Date
import java.text.ParseException as ParseException

//try {
//	a = 10/0
//	println a
//}
//catch (Exception e){
//	println e.getMessage();
//	println(e.toString());
////	println(e.getStackTrace()); 
//	log.markFailed('failed step')
//}
//println RunConfiguration.getProjectDir() + '/Data Files/ReferenceData/' + GlobalVariable.currentTestCaseID + ".zip"
//WebUI.delay(300)

SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
Date date = new Date();
String currentDate = formatter.format(date)

String strnote = WebUI.getText(findTestObject('Object Repository/1_Actions_AppSpecific/2_Content Studio/6_Import/Import_logs Window'))

//println strnote
//for(item in s){
//	println item
//}

String[] s = strnote.split('\\n')

println 'Length is '+s.length
def catchphrase = s[s.length-1]
println catchphrase

if (catchphrase.contains('Current job is Failed')){
	
	println "found the phrase"
	String[] s1 = catchphrase.split(' ')
	println 'Length is '+s1.length
	println 'Class is '+s1.getClass()
	ActualText =  s1[s1.length-4]+' '+s1[s1.length-3]+' '+s1[s1.length-2]+' '+s1[s1.length-1]
	CustomKeywords.'genericFunctions.Assertions.valueEqualityAssertion'(ActualText, 'Current job is Failed', 'Expected and actual Strings in import log not equal')
	
}

print ' '

String[] s2 = catchphrase.split('Current job is Failed')
println "New phrase is $s2[0]"
if (s2[0].contains(currentDate)){
	println "found the dateStamp"
}

CustomKeywords.'genericFunctions.Assertions.assertValuesMatchingWithToleranceUnit'(currentDate, 'Current job is Success', 'not equal')