import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/*WebUI.delay(1)

def Imp=WebUI.getText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/td_Import Completed'), FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(Imp, 'Import Completed', true)

println(Imp)
*/

def Imp= WebUI.getText(findTestObject('1_Actions_AppSpecific/1_2020 CS/4_Author/Catalog Authring/Versions/td_Import Completed'), FailureHandling.CONTINUE_ON_FAILURE)


if (Imp=='Import Completed') {
	
	KeywordUtil.logInfo("Import Completed"+Imp)
	}
	
	else{
			 
			KeywordUtil.logInfo("Import Failed"+Imp)
			
		}