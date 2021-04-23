package appSpecificFunctions

import org.testng.Assert

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


import internal.GlobalVariable

public class Assertions {

	@Keyword
	def assertPresenceOfFOEntryInDefaultFOState(String expectedString ){
		boolean elementVisibility = false
		def attempt = 1
		while ((elementVisibility == false) && (attempt <= GlobalVariable.midWait)) {
			String defaultFOStateText = (new RpdProcessing()).getDefaultFOStateFromCatalogsJsonAsString()
			if(defaultFOStateText.contains(expectedString)) {
				elementVisibility = true
				break
			}
			WebUI.delay(1)
			attempt++
		}
		if(elementVisibility == false){
			KeywordUtil.markFailedAndStop("\nActual :The FOEntry("+expectedString+") is not present in DefaultFOState.\nExpected : The FOEntry should be present in  DefaultFoState.")
		}
	}

	@Keyword
	def assertAbsenceOfFOEntryInDefaultFOState(String expectedString ){
		boolean elementVisibility = false
		def attempt = 1
		while ((elementVisibility == false) && (attempt <= GlobalVariable.minWait)) {
			String defaultFOStateText = (new RpdProcessing()).getDefaultFOStateFromCatalogsJsonAsString()
			if(defaultFOStateText.contains(expectedString)) {
				elementVisibility = true
				break
			}
			WebUI.delay(1)
			attempt++
		}
		if(elementVisibility == true){
			KeywordUtil.markFailedAndStop("\nActual :The FOEntry("+expectedString+") is present in DefaultFOState.\nExpected : The FOEntry should not be present in  DefaultFoState.")
		}
	}

	@Keyword
	def assertDefaultFOStateMatchWithReference(String expectedFOState, String actualFOState, boolean ensureCountMatchForEntries = false){

		expectedFOState = expectedFOState.replace('{',"")
		expectedFOState = expectedFOState.replaceAll("}","")

		actualFOState = actualFOState.replace('{',"")
		actualFOState = actualFOState.replaceAll("}","")

		List<String> expectedFOStateInList = expectedFOState.split(",")
		List<String> actualFOStateInList = actualFOState.split(",")

		int expectedFOCombinationsSize  = expectedFOStateInList.size()
		int actualFOCombinationsSize = actualFOStateInList.size()

		if(ensureCountMatchForEntries){
			Assert.assertTrue(expectedFOCombinationsSize==actualFOCombinationsSize,"Number of feature-option combination to be present in RPD is :"+ expectedFOCombinationsSize+ " .\"n Here found" + actualFOCombinationsSize +" combinations.")
		}

		for(String combination : expectedFOStateInList){
			//	KeywordUtil.logInfo(combination)

			Assert.assertTrue(actualFOState.contains(combination), "Styles are not applied correctly in design page, as that of selected while building room! \"n Reason: "+combination+ "expected to be present in RPD, but it is not.")
		}
	}
}

