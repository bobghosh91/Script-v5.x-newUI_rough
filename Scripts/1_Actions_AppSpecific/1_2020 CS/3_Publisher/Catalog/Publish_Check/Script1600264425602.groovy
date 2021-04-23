import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

def P=WebUI.getText(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/Publish Check'), FailureHandling.CONTINUE_ON_FAILURE)

int i=0
if (P=='Published') {
while (i<4){
	WebUI.waitForElementClickable(findTestObject('1_Actions_AppSpecific/1_2020 CS/3_Publishing/Catalog/button_Retire'),GlobalVariable.midWait)
	break;
			}	
	i++
}
	else{
		 //def PR=WebUI.getText(findTestObject('Object Repository/Publishing/td_Publish Failed'), FailureHandling.CONTINUE_ON_FAILURE)
		KeywordUtil.logInfo ("Catalog Publish Failed.............................."+P)
		
	}
	
println (CustomKeywords.'genericFunctions.DataProcessing.readTestDataFromExcelFile'(GlobalVariable.currentTestCaseID, 'CatalogName')+(" Catalog Publish Sucessfully......................."),FailureHandling.CONTINUE_ON_FAILURE)




