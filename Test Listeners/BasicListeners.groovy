import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory

import genericFunctions.Utilities
import internal.GlobalVariable as GlobalVariable
import tfsIntegration.TfsWebServices as TfsWebServices

import org.apache.commons.io.FileUtils
import org.apache.poi.ss.usermodel.Cell as Cell
import org.apache.poi.ss.usermodel.Row as Row
import org.apache.poi.ss.usermodel.Sheet as Sheet
import org.apache.poi.ss.usermodel.Workbook as Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook as XSSFWorkbook
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import appSpecificFunctions.AEAPIConsumption
import appSpecificFunctions.RpdProcessing

import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.main.CustomKeywordDelegatingMetaClass
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint


class BasicListeners {
	
	//public static Map <String, Map <String, String>> testCasesListWithStatus = new HashMap <String, Map <String, String>> ()
	//public static String fullSuiteName = ""
	//public static String suiteName = ""
	//public static String currentTestCase = ""

	public TfsWebServices tfsWebServices = new TfsWebServices()
	public Map <String, Map <String, String>> testCasesListWithStatus = new HashMap <String, Map <String, String>> ()
	public String fullSuiteName = ""
	public String suiteName = ""
	public String currentTestCase = ""
	HashMap<String, Object> mapTcProps = new HashMap <String, Object> ()
	HashMap<String, String> mapTcAndReason = new HashMap <String, Object> ()
	Map<String, Integer> subErrorEntity = new HashMap<String, Integer>()
	List <String> testCasesList = new ArrayList <String> ()
	public int currentTestCasePosition = 0
	public int previousExecutedTestCasePosition = 0
		
	def List <String> flagProcessing (List <String> listContainingFlags) {
		
		if (listContainingFlags.contains("<testSuite>")) {
			int index = listContainingFlags.indexOf("<testSuite>")
			listContainingFlags.set(index, this.suiteName)
		}
		if (listContainingFlags.contains("<platformEnv>")) {
			int index = listContainingFlags.indexOf("<platformEnv>")
			listContainingFlags.set(index, GlobalVariable.platformEnv)
		} 
		if (listContainingFlags.contains("<os>")) {
			int index = listContainingFlags.indexOf("<os>")
			listContainingFlags.set(index, GlobalVariable.envInfo.get("osName"))
		}
		if (listContainingFlags.contains("<browser>")) {
			int index = listContainingFlags.indexOf("<browser>")
			listContainingFlags.set(index, GlobalVariable.envInfo.get("browserName"))
		}
		if (listContainingFlags.contains("<browserVersion>")) {
			int index = listContainingFlags.indexOf("<browserVersion>")
			listContainingFlags.set(index, GlobalVariable.envInfo.get("browserVersion"))
		}
		if (listContainingFlags.contains("<profile>")) {
			int index = listContainingFlags.indexOf("<profile>")
			listContainingFlags.set(index, RunConfiguration.executionProfile.trim()) //replaceAll("\\s+", ""))
		}
		if (listContainingFlags.contains("<initialTimeStamp>")) {
			int index = listContainingFlags.indexOf("<initialTimeStamp>")
			listContainingFlags.set(index, GlobalVariable.envInfo.get("initialTimeStamp"))
		}
		
		return listContainingFlags
		
	}
	
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def sampleBeforeTestSuite(TestSuiteContext testSuiteContext) {
		
		this.testCasesListWithStatus.clear()
		this.fullSuiteName = testSuiteContext.getTestSuiteId();
		this.suiteName = this.fullSuiteName.substring(this.fullSuiteName.lastIndexOf('/') + 1)	
		GlobalVariable.envInfo.put("currentSuiteName", this.suiteName)
		
		CustomKeywords.'genericFunctions.EnvironmentSetup.enforceProfileVariables'()
		CustomKeywords.'genericFunctions.EnvironmentSetup.setupTestExecutionPattern'( this.suiteName)
		//CustomKeywords.'genericFunctions.EnvironmentSetup.downloadExecutionTracker'()
		CustomKeywords.'genericFunctions.EnvironmentSetup.disableInfobarsForChromeIfApplicable'()
		CustomKeywords.'genericFunctions.EnvironmentSetup.setUpPlatformProfileBase'()
		
		if (GlobalVariable.updateReferenceImages == true) {
//			Utilities utilities = new Utilities()
//			utilities.infoBox("Reference Image Update Mode is turned ON. Press Ok to continue test execution. If not, terminate the execution from within Katalon App/Cmd.", "QA Test Automation Custom Message")
			CustomKeywords.'genericFunctions.Utilities.infoBox'("Reference Image Update Mode is turned ON. Press Ok to continue test execution. If not, terminate the execution from within Katalon App/Cmd.", "QA Test Automation Custom Message")
		}
		
		mapTcAndReason = CustomKeywords.'genericFunctions.EnvironmentSetup.getMapOfTcsToSkipFromTracker'(this.suiteName)	
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def sampleAfterTestSuite(TestSuiteContext testSuiteContext) {
		
		CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
		
		//List <String> testCasesList = new ArrayList <String> ()
		Map <String, String> testCaseResult =  new HashMap <String, String> ()
		Map <String, String> allTestCaseVerifyFlags =  new HashMap <String, String> ()
		 
		String appName = RunConfiguration.executionProfile.replaceAll("\\s+", "") 
		
		//testCasesList.addAll(this.testCasesListWithStatus.keySet())
		if(suiteName.equals("PSL")){
			suiteName = suiteName + RunConfiguration.executionProfile
		}
		
		tfsWebServices.setTfsIntegrationPropertiesFilePath("./External Libraries/tfsIntegration.properties")
		tfsWebServices.setTfsIntegrationTempPropertiesFilePath("./External Libraries/tfsIntegrationTemp/tfsIntegrationTemp_" + suiteName + ".properties")
		
		List <String> testSuiteHierarchyList = flagProcessing(Arrays.asList(GlobalVariable.tfsSuiteHierarchy.toString().split(",")))
		
		boolean testSuiteReRunPossibility = false
		int totalTestsForFreshRun = CustomKeywords.'genericFunctions.EnvironmentSetup.getTotalTestsForFreshRun'(this.fullSuiteName)
		
		if ((GlobalVariable.tfsSuiteUpdateMode == true) && (this.testCasesListWithStatus.keySet().size() < totalTestsForFreshRun)) {
			
			testSuiteReRunPossibility = true
			
			//Delete the previous execution report in the outside folder
			File previousReportFile = new File(CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName))
			CustomKeywords.'genericFunctions.Utilities.deleteSpecificFile'("./External Libraries/reportsFolder/" + previousReportFile.getName())
			
			//Delete the previous test execution dumps + img reports in the outside folder
			CustomKeywords.'genericFunctions.Utilities.removeDirectory'(new File("./External Libraries/reportsFolder/" + previousReportFile.getName().substring(0, previousReportFile.getName().indexOf('.xlsx')) + "-TestCaseDumps"), "")
			//CustomKeywords.'genericFunctions.Utilities.removeDirectory'(new File("./External Libraries/reportsFolder/" + previousReportFile.getName().substring(0, previousReportFile.getName().indexOf('.xlsx')) + "-ImgReport"), "")
						
			String pathOfPreviousTestCaseExecutionResult = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName))
			String pathOfCurrentTestCaseExecutionResult = (RunConfiguration.getReportFolder() + "/customReport" + '/' + this.suiteName + ' auto_' + GlobalVariable.envInfo.get("osName") + '_' + GlobalVariable.envInfo.get("browserName") + '_' + GlobalVariable.platformEnv + '.xlsx').replaceAll("\\\\","/")
			String pathOfCombinedTestCaseExecutionResult = (RunConfiguration.getReportFolder() + "/customReport" + '/' + this.suiteName + ' auto_' + GlobalVariable.envInfo.get("osName") + '_' + GlobalVariable.envInfo.get("browserName") + '_' + GlobalVariable.platformEnv + '_Merged.xlsx').replaceAll("\\\\","/")
			
			CustomKeywords.'genericFunctions.DataProcessing.mergeTheCustomReports'(pathOfPreviousTestCaseExecutionResult, pathOfCurrentTestCaseExecutionResult, pathOfCombinedTestCaseExecutionResult)
			CustomKeywords.'genericFunctions.DataProcessing.cleanTheTestExecutionReportPropertiesFile'(this.suiteName)
			CustomKeywords.'genericFunctions.DataProcessing.setTheTestExecutionReportPath'(this.suiteName, pathOfCombinedTestCaseExecutionResult)
			
			//Copy and put the execution report in the outside folder
			CustomKeywords.'genericFunctions.Utilities.copyFileFromOneLocationToAnother'(CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName), "./External Libraries/reportsFolder")
			
			//Various params for copying current test case dumps + img reports
			int indexForCustomReportParent = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).indexOf('customReport')
			int indexTillCustomReport = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).lastIndexOf('/') + 1
			
			String sourceDir = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(0,indexForCustomReportParent) 
			String destinationDir = "./External Libraries/reportsFolder/"
			String destinationDirDsResults = destinationDir + GlobalVariable.platformEnv + "_ DS_ FE/"
			
			String sourceDirFolderName_TestCaseDumps = "TestCaseDumps"
			String sourceDirFolderName_ImgReports = "ImgReport"
			String sourceDirFolderName_DsResults = "/DS-" + RunConfiguration.executionProfile + "-" + GlobalVariable.platformEnv
			
			String destDirFolderNam_TestCaseDumps = ((CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(indexTillCustomReport)).replaceAll('.xlsx', '') + "-TestCaseDumps"
			String destDirFolderNam_ImgReports = ((CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(indexTillCustomReport)).replaceAll('.xlsx', '') + "-ImgReport"
			String destDirFolderName_DsResults = "/DS-" + this.suiteName + "-" + GlobalVariable.platformEnv
									
			//Copy and put the test case dumps + img reports in the outside folder
			//CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(sourceDir + sourceDirFolderName_TestCaseDumps, destinationDir + destDirFolderNam_TestCaseDumps)
			//CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(sourceDir + sourceDirFolderName_ImgReports, destinationDir + destDirFolderNam_ImgReports)
			if (fullSuiteName.contains("DS") == true) {
				
				def dlist = []
				new File(sourceDir + sourceDirFolderName_DsResults).eachDir {dlist << it }
				println dlist
				
				for (File subFolder : dlist) {
						
					String subFolderName = subFolder.getName()
					
					File destinationFolder = new File(destinationDirDsResults + destDirFolderName_DsResults + "/" + subFolderName)
					File destinationFolderReRun = new File(destinationDirDsResults + destDirFolderName_DsResults + "/" + subFolderName + "_ReRun")
					
					if (destinationFolder.exists()) {FileUtils.deleteDirectory(destinationFolder)}
					destinationFolderReRun.mkdir()
					
					CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(subFolder.getCanonicalPath(), destinationFolderReRun.getCanonicalPath())
				}
			}
		}
		
		if (testSuiteReRunPossibility == false) {
			
			CustomKeywords.'genericFunctions.DataProcessing.cleanTheTestExecutionReportPropertiesFile'(this.suiteName)
			CustomKeywords.'genericFunctions.DataProcessing.restoreTestExecutionPatternPropertiesFile'(this.suiteName)
			
			String pathOfCurrentTestCaseExecutionResult = RunConfiguration.getReportFolder() + "/customReport" + '/' + this.suiteName + ' auto_' + GlobalVariable.envInfo.get("osName") + '_' + GlobalVariable.envInfo.get("browserName") + '_' + GlobalVariable.platformEnv + '.xlsx'
			
			pathOfCurrentTestCaseExecutionResult = pathOfCurrentTestCaseExecutionResult.replaceAll("\\\\","/")
			
			CustomKeywords.'genericFunctions.DataProcessing.setTheTestExecutionReportPath'(this.suiteName, pathOfCurrentTestCaseExecutionResult)
			
			int testsSkippedCount = 0 
			
			for (String tc : testCasesListWithStatus.keySet()) {
				
				if (testCasesListWithStatus.get(tc).get("testCaseSkipped").equals("true"))
				{
					testsSkippedCount = testsSkippedCount + 1
				}
			}
				
			//Push the result to VSTS only if not all tests are skipped.
			if (testsSkippedCount < testCasesListWithStatus.size()) {
					
				if (fullSuiteName.contains("DS") == false) {
				
					if (GlobalVariable.tfsSuiteName.toString().equals("<defaultPattern>") || (GlobalVariable.tfsSuiteName.toString().isEmpty() == true)) {
						tfsWebServices.setupTfsTestSuiteHierarchy("", GlobalVariable.envInfo.get("initialTimeStamp"), GlobalVariable.envInfo.get("osName"), GlobalVariable.envInfo.get("browserName"), GlobalVariable.envInfo.get("browserVersion"), ' environment_' + GlobalVariable.platformEnv + ' app_' + appName, testSuiteHierarchyList, testCasesList);
					}
					else {
						tfsWebServices.setupTfsTestSuiteHierarchy("", GlobalVariable.envInfo.get("initialTimeStamp"), "", "", "", GlobalVariable.tfsSuiteName, testSuiteHierarchyList, testCasesList);
					}
				}	
			}
			
			//Copy and put the custom report in the outside folder
			CustomKeywords.'genericFunctions.Utilities.copyFileFromOneLocationToAnother'(CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName), "./External Libraries/reportsFolder")
			
			//Various params for copying current test case dumps + img reports
			int indexForCustomReportParent = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).indexOf('customReport')
			int indexTillCustomReport = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).lastIndexOf('/') + 1
			
			String sourceDir = (CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(0,indexForCustomReportParent) 
			String destinationDir = "./External Libraries/reportsFolder/"
			String destinationDirDsResults = destinationDir + GlobalVariable.platformEnv + "_ DS_ FE/"
			
			String sourceDirFolderName_TestCaseDumps = "TestCaseDumps"
			String sourceDirFolderName_ImgReports = "ImgReport"
			String sourceDirFolderName_DsResults = "/DS-" + RunConfiguration.executionProfile + "-" + GlobalVariable.platformEnv
						
			String destDirFolderNam_TestCaseDumps = ((CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(indexTillCustomReport)).replaceAll('.xlsx', '') + "-TestCaseDumps"
			String destDirFolderNam_ImgReports = ((CustomKeywords.'genericFunctions.DataProcessing.getTheTestExecutionReportPath'(this.suiteName)).substring(indexTillCustomReport)).replaceAll('.xlsx', '') + "-ImgReport"
			String destDirFolderName_DsResults = "/DS-" + this.suiteName + "-" + GlobalVariable.platformEnv
						
			//Copy and put the test case dumps + img reports in the outside folder
			//CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(sourceDir + sourceDirFolderName_TestCaseDumps, destinationDir + destDirFolderNam_TestCaseDumps)
			//CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(sourceDir + sourceDirFolderName_ImgReports, destinationDir + destDirFolderNam_ImgReports)
			if (fullSuiteName.contains("DS") == true) {
				
				File dsResultsFolder = new File(destinationDirDsResults + destDirFolderName_DsResults)
				if (dsResultsFolder.exists()) {FileUtils.deleteDirectory(dsResultsFolder)}
				dsResultsFolder.mkdir()
				
				CustomKeywords.'genericFunctions.Utilities.copyDirectoryFromOneLocationToAnother'(sourceDir + sourceDirFolderName_DsResults, destinationDirDsResults + destDirFolderName_DsResults)
			}
		}
				
		for (Map.Entry<String, Map<String, String>> e : this.testCasesListWithStatus.entrySet()) {
			
			println(e.getKey() + " : " + e.getValue().get("status") + " : " + e.getValue().get("manualVerificationNeeded"));
			
			String status = e.getValue().get("status")
			
			/*if (status.equalsIgnoreCase("SKIPPED")) {
				status = "notExecuted"
			}*/
			
			if (e.getValue().get("testCaseSkipped").equals("false")) {
				
				if (fullSuiteName.contains("DS") == false) {
			
					if (testSuiteReRunPossibility == true) {
						tfsWebServices.performTfsTestCaseStatusUpdateForNonPassedTests(e.getKey(), status, e.getValue().get("manualVerificationNeeded"));
					}
					else {
						tfsWebServices.performTfsTestCaseStatusUpdate(e.getKey(), status, e.getValue().get("manualVerificationNeeded"));
					}
				}
			}
			
			testCaseResult.put(e.getKey(), e.getValue().get("status"))
			allTestCaseVerifyFlags.put(e.getKey(), e.getValue().get("manualVerificationNeeded"))
		}
		
		tfsWebServices.archiveOldTfsTestSuitesWithinHierarchy();		
	}
	
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def sampleBeforeTestCase(TestCaseContext testCaseContext) {
		
		currentTestCasePosition = currentTestCasePosition + 1
		
		mapTcProps.clear()

		if(RunConfiguration.executionSource.contains(".tc")){

			CustomKeywords.'genericFunctions.EnvironmentSetup.enforceProfileVariables'()
			CustomKeywords.'genericFunctions.EnvironmentSetup.disableInfobarsForChromeIfApplicable'()
			CustomKeywords.'genericFunctions.EnvironmentSetup.setUpPlatformProfileBase'()
			CustomKeywords.'genericFunctions.EnvironmentSetup.deleteBrowserData'('Bookmarks')// skip provided file

			if (GlobalVariable.updateReferenceImages == true) {
				Utilities utilities = new Utilities()
				utilities.infoBox("Reference Image Update Mode is turned ON. Press Ok to continue test execution. If not, terminate the execution from within Katalon.", "QA Test Automation Custom Message")
			}
		}

		GlobalVariable.currentTestCaseSkippedStatus = null
		GlobalVariable.currentTestCaseCaptureNetworkTraffic = null
		GlobalVariable.anyWebDriverSessionOpen = null
		GlobalVariable.currentTestCaseImgCheckSuccess = null
		GlobalVariable.envInfo.put("fetchURL", "") //restore fetchURL value to empty before every TC
		GlobalVariable.envInfo.put('privacyPolicyMessageResponded', false) //restore privacyPolicyMessageResponded value to false before every TC

		String fullTestName = testCaseContext.getTestCaseId()

		if (fullTestName.contains("0_EnvInfo") == false) {
			String testName = fullTestName.substring(fullTestName.lastIndexOf('/') + 1)
			currentTestCase = testName.substring(0, testName.indexOf("-"))
			currentTestCase = currentTestCase.replaceAll(" ", "")
			GlobalVariable.currentTestCaseID = currentTestCase
		}

		try {
			if (DriverFactory.getWebDriver().toString().contains("(null)") == false) {
				GlobalVariable.anyWebDriverSessionOpen = "true"

				if (GlobalVariable.nextTestCaseIdInTsSuiteFile.contains(GlobalVariable.currentTestCaseID) == false) {

					//println "GlobalVariable.nextTestCaseIdInTsSuiteFile : " + GlobalVariable.nextTestCaseIdInTsSuiteFile
					CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
					GlobalVariable.anyWebDriverSessionOpen = "false"
				}

			}
			else {
				GlobalVariable.anyWebDriverSessionOpen = "false"
			}
		}
		catch (BrowserNotOpenedException) {
			GlobalVariable.anyWebDriverSessionOpen = "false"
		}

		if (testCaseContext.getTestCaseVariables().containsKey("captureNetworkTraffic")) {
			try {
				String captureNetworkTraffic = testCaseContext.getTestCaseVariables().get("captureNetworkTraffic")
				println "captureNetworkTraffic : " + captureNetworkTraffic
				if (captureNetworkTraffic != null) {
					if (captureNetworkTraffic.equalsIgnoreCase("true")) {
						GlobalVariable.currentTestCaseCaptureNetworkTraffic = "true"
					}
				}
			}
			catch (Exception e) {
				println("Exception Occured while processing captureNetworkTraffic variable." + e.message)
			}
		}

		boolean testCaseToBeSkipped = false

		//Handling execution scope. If test case is not under execution scope, skip the test case from the execution.

		tfsWebServices.setTfsIntegrationPropertiesFilePath("./External Libraries/tfsIntegration.properties")
		tfsWebServices.setTfsIntegrationTempPropertiesFilePath("./External Libraries/tfsIntegrationTemp/tfsIntegrationTemp_" + suiteName + ".properties")

		String workItemDetails = tfsWebServices.getWorkItemDetails(GlobalVariable.currentTestCaseID)

		//If work item details are null, try to fetch them again
		if (workItemDetails==null) {

			sleep(3000)
			workItemDetails = tfsWebServices.getWorkItemDetails(GlobalVariable.currentTestCaseID)
		}

		if (workItemDetails!=null) {

			mapTcProps.put("Microsoft.VSTS.Common.Priority", tfsWebServices.retrieveWorkItemFieldValue("Microsoft.VSTS.Common.Priority", workItemDetails))
			mapTcProps.put("Technologies2020.Common.Automation", tfsWebServices.retrieveWorkItemFieldValue("Technologies2020.Common.Automation", workItemDetails))
			mapTcProps.put("System.State", tfsWebServices.retrieveWorkItemFieldValue("System.State", workItemDetails))
		}
		else {
			mapTcProps.put("Microsoft.VSTS.Common.Priority", "Couldn't retrieve (VSTS token may be missing OR Temporary issue)")
			mapTcProps.put("Technologies2020.Common.Automation", "Couldn't retrieve (VSTS token may be missing OR Temporary issue)")
			mapTcProps.put("System.State", "Couldn't retrieve (VSTS token may be missing OR Temporary issue)")
		}

		if(RunConfiguration.executionSource.contains(".tc") == false){

			if ((testCaseToBeSkipped == false) && (workItemDetails!=null)) {

				if (fullSuiteName.contains("DS") == false) {

					if ((tfsWebServices.getTfsIntegrationPropertiesFile().getProperty("tfs.appsForExecutionScope")).contains(CustomKeywords.'genericFunctions.EnvironmentSetup.getCurrentAppNameFromProfileName'())) {

						String workItemFieldsString = tfsWebServices.getTfsIntegrationPropertiesFile().getProperty("tfs.workItemFields")
						String executionScopeString = tfsWebServices.getTfsIntegrationPropertiesFile().getProperty("tfs.executionScope." + GlobalVariable.platformEnv)

						HashMap <String, Object> workItemFields = new HashMap <String, Object> ()
						ArrayList <String> executionScopes = new ArrayList <String> ()

						for (String field : workItemFieldsString.split(";")) {

							workItemFields.put(field, tfsWebServices.retrieveWorkItemFieldValue(field, workItemDetails))
						}

						if (executionScopeString != null) {

							for (String field : executionScopeString.split(";")) {
								executionScopes.add(field)
							}
						}

						if (CustomKeywords.'genericFunctions.EnvironmentSetup.executionCriterionSatisfied'(workItemFields, executionScopes) == false) {
							testCaseToBeSkipped = true
							mapTcAndReason.put(GlobalVariable.currentTestCaseID, "Execution criterion not satisfied for current environment (Priority/Type/Other).")
						}
					}
				}
			}

			//Handling non supported tests.
			//If test case automation status is other than yes, skip it.
			//Else test case fits under execution scope but is not supported due to bug/other reason, skip the test case from the execution.

			if (testCaseToBeSkipped == false) {

				//println (mapTcProps.get("System.State"))

				if (mapTcProps.get("System.State").equals("Closed")) {

					//"put", here, is serving as a first time pair add, or the replace
					mapTcAndReason.put(GlobalVariable.currentTestCaseID, "VSTS TC State - Closed (Test is no longer Active)")
					testCaseToBeSkipped = true
				}
				else if ((mapTcProps.get("Technologies2020.Common.Automation").equals("Yes") == false) && (mapTcProps.get("Technologies2020.Common.Automation").contains("Couldn't retrieve") == false)) {

					//"put", here, is serving as a first time pair add, or the replace
					mapTcAndReason.put(GlobalVariable.currentTestCaseID, "VSTS TC Automation Status - " + mapTcProps.get("Technologies2020.Common.Automation"))
					testCaseToBeSkipped = true
				}
				else {
					testCaseToBeSkipped = mapTcAndReason.containsKey(GlobalVariable.currentTestCaseID)

					if (mapTcAndReason.containsKey(GlobalVariable.currentTestCaseID)) {

						ArrayList <String> workItemIdList = CustomKeywords.'genericFunctions.Utilities.extractNumbersFromString'(6, mapTcAndReason.get(GlobalVariable.currentTestCaseID))

						if (workItemIdList.size() != 0) {

							String modifiedReason = 'Script included in run, please review the tracker comment \"' + mapTcAndReason.get(GlobalVariable.currentTestCaseID) + '\"\n'
							int closedDefectCountWithMatchingCriterion = 0

							for (String bugId : workItemIdList) {

								sleep(2000)

								String bugDetails = tfsWebServices.getWorkItemDetails(bugId)

								//If bug details are null, try to fetch them again
								if (bugDetails==null) {

									sleep(3000)
									bugDetails = tfsWebServices.getWorkItemDetails(bugId)
								}

								mapTcProps.put("WI_" + "id", bugId)
								mapTcProps.put("WI_" + "System.State", tfsWebServices.retrieveWorkItemFieldValue("System.State", bugDetails))
								mapTcProps.put("WI_" + "System.Reason", tfsWebServices.retrieveWorkItemFieldValue("System.Reason", bugDetails))
								mapTcProps.put("WI_" + "System.WorkItemType", tfsWebServices.retrieveWorkItemFieldValue("System.WorkItemType", bugDetails))
								mapTcProps.put("WI_" + "System.Title", tfsWebServices.retrieveWorkItemFieldValue("System.Title", bugDetails))

								modifiedReason = modifiedReason +
										'[' +
										'WI ID : '  + mapTcProps.get("WI_" + "id") + ', ' +
										'Type : '   + mapTcProps.get("WI_" + "System.WorkItemType") + ', ' +
										'State : '  + mapTcProps.get("WI_" + "System.State")        + ', ' +
										'Reason : ' + mapTcProps.get("WI_" + "System.Reason") + ']' + '\n'

								if (((mapTcProps.get("WI_" + "System.WorkItemType")).toString().equalsIgnoreCase("Bug")) &&
								(mapTcProps.get("WI_" + "System.State").equals("Closed")) /*&&
								 (mapTcProps.get("WI_" + "System.Reason") in ['Cannot Reproduce','Fixed','Verified','Duplicate'])*/) {

									closedDefectCountWithMatchingCriterion = closedDefectCountWithMatchingCriterion + 1

								}
							}

							if (closedDefectCountWithMatchingCriterion == workItemIdList.size()) {

								testCaseToBeSkipped = false
								mapTcProps.put("trackerReasonNeedsUpdate", modifiedReason)
								mapTcAndReason.replace(GlobalVariable.currentTestCaseID, modifiedReason)
							}
						}
					}
				}
			}

			if(testCaseToBeSkipped == true){

				//This test case is to be skipped. Please see csv reports for details.

				GlobalVariable.currentTestCaseSkippedStatus = "true"
				println("Skipped this test case -->:"+GlobalVariable.currentTestCaseID)
				testCaseContext.skipThisTestCase()
			}
		}

		String	nonSupportedEnv_ = CustomKeywords.'genericFunctions.DataProcessing.getTestInputValueFromExcelDataJsonString'('FlagsForNonSupportedEnvJson','Combination(s)')

		/* Commented this as the skip mechanism is driven by tracker.
		 if (nonSupportedEnv_ != null && nonSupportedEnv_.isEmpty() == false && nonSupportedEnv_.length() > 0) {
		 boolean toBeSkipped = CustomKeywords.'genericFunctions.EnvironmentSetup.testCaseSupportedOnCurrentEnvironment'(nonSupportedEnv_)
		 if (toBeSkipped == true) {
		 GlobalVariable.currentTestCaseSkippedStatus = "true"
		 String errorMessage = CustomKeywords.'genericFunctions.DataProcessing.getTestInputValueFromExcelDataJsonString'('FlagsForNonSupportedEnvJson','Message(s)')
		 testCaseContext.skipThisTestCase()
		 KeywordUtil.markErrorAndStop("\n\nTest Case Skipped : " + errorMessage + "\n\n")
		 }
		 }
		 else {
		 KeywordUtil.logInfo("No information for non-supported environments found. Running the test...")
		 }
		 */
		boolean doesCurrenttestCaseSatisfyExecutionPattern = ((currentTestCasePosition) == Integer.parseInt(GlobalVariable.envInfo.get("startingTestPosition"))) ||
				((currentTestCasePosition - previousExecutedTestCasePosition) == Integer.parseInt(GlobalVariable.envInfo.get("testExecutionOffset")))

		if (doesCurrenttestCaseSatisfyExecutionPattern == true) { 
			
			previousExecutedTestCasePosition = currentTestCasePosition
		}
		else {
			
			if(testCaseToBeSkipped == true) {
				
				String skipReason = mapTcAndReason.get(GlobalVariable.currentTestCaseID)
				
				if (skipReason.contains("Execution criterion not satisfied") || skipReason.contains("VSTS TC Automation Status") || skipReason.contains("VSTS TC State - Closed") || (skipReason.toLowerCase().startsWith("<") && skipReason.toLowerCase().endsWith(">"))) {
					println "Don't collect the TC with pattern reason."
				}
				else {
					mapTcAndReason.put(GlobalVariable.currentTestCaseID, "TC Skipped Due to Execution Pattern")
				}
			}
			else {			
				
				GlobalVariable.currentTestCaseSkippedStatus = "true"
				mapTcAndReason.put(GlobalVariable.currentTestCaseID, "TC Skipped Due to Execution Pattern")
				//println mapTcAndReason.get(GlobalVariable.currentTestCaseID)
				//println("Skipped this test case -->:"+GlobalVariable.currentTestCaseID)
				testCaseContext.skipThisTestCase()
			}
		}
	}
	
	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def sampleAfterTestCase(TestCaseContext testCaseContext) {
		
		AEAPIConsumption aEAPIConsumption = new AEAPIConsumption()
		aEAPIConsumption.setCatalogCodeAndVersionInGlobalVariables()
		aEAPIConsumption.setAppDetailsInGlobalVariables()
		
		String fullTestName = testCaseContext.getTestCaseId();
		String testName = fullTestName.substring(fullTestName.lastIndexOf('/') + 1)
				
		if (fullTestName.contains("0_EnvInfo") == false) {
			
			currentTestCase = testName.substring(0, testName.indexOf("-"))
			currentTestCase = currentTestCase.replaceAll(" ", "")
			
			GlobalVariable.currentTestCaseID = currentTestCase
				
			Map <String, String> localMap = new HashMap<String, String> ()
			localMap.clear()
			localMap.put("status", testCaseContext.getTestCaseStatus().toString())
			
			try {
				String manualVerificationNeeded = testCaseContext.getTestCaseVariables().get("manualVerificationNeeded")
				println "manualVerificationNeeded : " + manualVerificationNeeded
					
				if (manualVerificationNeeded.equalsIgnoreCase("true")) {
					localMap.put("manualVerificationNeeded", "true")
				}
				else {
					localMap.put("manualVerificationNeeded", "false")
				}
			}
			catch (Exception e) {
				localMap.put("manualVerificationNeeded", "false")
			}
			
			//if (testCaseContext.isSkipped()) {
			if(GlobalVariable.currentTestCaseSkippedStatus.equals('true')){
				localMap.put("testCaseSkipped", "true")
			}
			else {
				localMap.put("testCaseSkipped", "false")
			}
				
			this.testCasesListWithStatus.put(currentTestCase, localMap)
			
			//if(GlobalVariable.currentTestCaseSkippedStatus.equals('true') == false){
			
			//spreadsheet report and tfs integration - do not put redundant "Not Executed" entries
			String skipReason = ""
				
			if (GlobalVariable.currentTestCaseSkippedStatus.equals("true")) {
					
				skipReason = mapTcAndReason.get(GlobalVariable.currentTestCaseID) 
					
				if (skipReason.contains("Execution criterion not satisfied") || skipReason.contains("VSTS TC Automation Status") || skipReason.contains("VSTS TC State - Closed") || (skipReason.toLowerCase().startsWith("<") && skipReason.toLowerCase().endsWith(">"))) {
					println "Don't collect the TC list."
				}
				else {
					testCasesList.add(GlobalVariable.currentTestCaseID)
				}
			}
			else {
				testCasesList.add(GlobalVariable.currentTestCaseID)
			}
		}
				
		if(testCaseContext.isSkipped() == false){
		
			if (testCaseContext.getTestCaseStatus().equalsIgnoreCase("passed") == false) {
				
				File imageCompareIssueFile = new File (RunConfiguration.getReportFolder() + "/TestCaseDumps/" + GlobalVariable.currentTestCaseID + "_imageCompareIssues.html")
				
				if (imageCompareIssueFile.exists() == false) {
					Utilities utilities = new Utilities()
					utilities.takeFullScreenshot()
				}
			}
			
			if (testCaseContext.getTestCaseVariables().containsKey("captureNetworkTraffic")) {
				
				try {
					String captureNetworkTraffic = testCaseContext.getTestCaseVariables().get("captureNetworkTraffic")
					println "captureNetworkTraffic : " + captureNetworkTraffic
				
					if (captureNetworkTraffic != null) {
						if (captureNetworkTraffic.equalsIgnoreCase("true")) {
							CustomKeywords.'genericFunctions.EnvironmentSetup.dumpNetworkLogsForCurrentTest'()
						}
					}
				}
				catch (Exception e) {
					println("Exception Occured while processing captureNetworkTraffic variable." + e.message)
				}
				
				CustomKeywords.'genericFunctions.EnvironmentSetup.stopBrowserMobProxy'()
				CustomKeywords.'genericFunctions.EnvironmentSetup.removeBrowserMobProxyFromMap'()
					
			}
			
			if ( ((GlobalVariable.enforceNewBrowserSessionForEachTestCase).toString().equals("false"))  && (testCaseContext.getTestCaseVariables().containsKey("useBrowserSessionOfThisTestForNextTest"))) {
			
				try {
					String useBrowserSessionOfThisTestForNextTest = testCaseContext.getTestCaseVariables().get("useBrowserSessionOfThisTestForNextTest")
					println "useBrowserSessionOfThisTestForNextTest : " + useBrowserSessionOfThisTestForNextTest
				
					if (useBrowserSessionOfThisTestForNextTest != null) {
						if (useBrowserSessionOfThisTestForNextTest.equalsIgnoreCase("true")) {
							println ("----------Browser session to be used for next test.")
							//println ("----------Resetting the APP state to homepage.")
							//WebUI.callTestCase(findTestCase('Action/1_Action_Common/2_Home-Page/clickOnLogout'), [:], FailureHandling.STOP_ON_FAILURE)
							//WebUI.callTestCase(findTestCase('Action/1_Action_Common/6_Hamburger-Menu/clickOnMenuManagerHomeButton'), [:], FailureHandling.STOP_ON_FAILURE)
						}
						else {
							CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
						}
					}
				}
				catch (Exception e) {
					CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
				}			
				
				if (testCaseContext.getTestCaseStatus().equalsIgnoreCase("passed") == false) {
					
					CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
				}
						
			}
			
			else {
				CustomKeywords.'genericFunctions.EnvironmentSetup.terminateOpenWebDriversManually'()
			}
			
			if(RunConfiguration.executionSource.contains(".tc") == false) {
				
				CustomKeywords.'genericFunctions.EnvironmentSetup.nextTestCaseIdInTsSuiteFile'(this.fullSuiteName)
			}
		}
		
		//Pushing current TC execution result to custom report file - start
		
		String skipReason = ""
		
		if (GlobalVariable.currentTestCaseSkippedStatus.equals("true")) {
		
			skipReason = mapTcAndReason.get(GlobalVariable.currentTestCaseID)
		}
		
		CustomKeywords.'genericFunctions.DataProcessing.customReportGeneration'(mapTcProps, this.suiteName, testName, testCaseContext, skipReason , subErrorEntity)

		//Pushing current TC execution result to custom report file - end
	}
}