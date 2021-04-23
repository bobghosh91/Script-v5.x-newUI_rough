package genericFunctions

import static com.kms.katalon.core.testdata.TestDataFactory.findTestData

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Map
import java.util.regex.Pattern
import java.io.FileOutputStream

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.testdata.ExcelData
import internal.GlobalVariable

import org.apache.poi.common.usermodel.Hyperlink
import org.apache.poi.hssf.usermodel.DVConstraint
import org.apache.poi.hssf.usermodel.HSSFDataValidation
//import org.apache.poi.hssf.util.CellRangeAddressList
import org.apache.poi.ss.util.CellRangeAddressList
import org.apache.poi.hssf.util.CellReference
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell as Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.DataValidation
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row as Row
import org.apache.poi.ss.usermodel.Sheet as Sheet
import org.apache.poi.ss.usermodel.Workbook as Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFDataValidation
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFHyperlink
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.common.usermodel.Hyperlink
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook as XSSFWorkbook
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil

public class DataProcessing {

	LinkedHashMap <String, Object> reportHeaderValueEntries = new LinkedHashMap <String, Object> ()
	InetAddress networkAddrs = InetAddress.getLocalHost();

	@Keyword
	def boolean isCellNonEmpty(Cell cell) {
		return !((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK))
	}

	@Keyword
	def Object findColumnIndex(Row row, String columnName) {
		int cellIndex = 0

		for (Cell cell : row) {
			if ((cell != null) && columnName.equalsIgnoreCase(cell.getStringCellValue())) {
				return cellIndex
			}
			++cellIndex
		}
		KeywordUtil.markErrorAndStop(('No matching column with title: ' + columnName) + ' is found in expected sheet')
	}

	@Keyword
	def String readTestDataFromExcelFile (String dataObject, String sheetName, String referenceColumn, String identifierString, String paramName) {

		/*
		 Identifying index of Test Case ID column
		 */

		ExcelData temp = (ExcelData) findTestData(dataObject)
		temp.changeSheet(sheetName)

		def columnNameList = temp.getColumnNames()

		def indexOfReferenceColumn = null

		for (int i=0; i < columnNameList.size(); i++) {

			println (columnNameList[i])

			if (columnNameList[i] == referenceColumn) {
				indexOfReferenceColumn = i
				break
			}
		}

		/*
		 Creating  a hashmap of {test data hashmap} per testcase
		 */
		def testDataList = temp.getAllData()
		def testDataMapsForTestCases = [:]

		for (Object row : testDataList) {

			def testDataMapForTestCase = [:]

			for (int i=0; i < columnNameList.size(); i++) {

				testDataMapForTestCase.put(columnNameList[i], row[i]);
				//println(columnNameList[i] + " : " + row[i])
			}

			testDataMapsForTestCases.put(row[indexOfReferenceColumn], testDataMapForTestCase);
			//println (row)
		}

		testDataMapsForTestCases = testDataMapsForTestCases

		String testInputParam = testDataMapsForTestCases.get(identifierString).get(paramName)

		if (testInputParam == null) {

			testInputParam = ""
		}

		//KeywordUtil.logInfo(testInputParam)
		return (testInputParam)
	}

	@Keyword
	def String readTestDataFromExcelFile (String sheetName, String identifierString, String paramName) {

		/*
		 Identifying index of Test Case ID column
		 */

		ExcelData temp = (ExcelData) findTestData('Test-Input')
		temp.changeSheet(sheetName)

		def columnNameList = temp.getColumnNames()

		def indexOfReferenceColumn = null

		for (int i=0; i < columnNameList.size(); i++) {

			println (columnNameList[i])

			if (columnNameList[i] == 'TC-ID') {
				indexOfReferenceColumn = i
				break
			}
		}

		/*
		 Creating  a hashmap of {test data hashmap} per testcase
		 */
		def testDataList = temp.getAllData()
		def testDataMapsForTestCases = [:]

		for (Object row : testDataList) {

			def testDataMapForTestCase = [:]

			for (int i=0; i < columnNameList.size(); i++) {

				testDataMapForTestCase.put(columnNameList[i], row[i]);
				//println(columnNameList[i] + " : " + row[i])
			}

			testDataMapsForTestCases.put(row[indexOfReferenceColumn], testDataMapForTestCase);
			//println (row)
		}

		testDataMapsForTestCases = testDataMapsForTestCases

		String testInputParam = testDataMapsForTestCases.get(identifierString).get(paramName)

		if (testInputParam == null) {

			testInputParam = ""
		}

		if ((testInputParam).contains("<browserNameInLowerCase>")) {

			println GlobalVariable.envInfo.get('browserName')

			if ((GlobalVariable.envInfo.get('browserName')).contains("edge")) {

				testInputParam = testInputParam.replace("<browserNameInLowerCase>", "chrome")
			}
			else {

				testInputParam = testInputParam.replace("<browserNameInLowerCase>", GlobalVariable.envInfo.get('browserName'))
			}
		}
		if ((testInputParam).contains("<osNameInLowerCase>")) {

			testInputParam = testInputParam.replace("<osNameInLowerCase>", GlobalVariable.envInfo.get("osName").substring(0, GlobalVariable.envInfo.get("osName").indexOf(" ")))
		}
		if ((testInputParam).contains("<platformEnv>")) {

			testInputParam = testInputParam.replace("<platformEnv>", GlobalVariable.platformEnv)
		}
		if ((testInputParam).contains("<profile>")) {

			String masterProfile = RunConfiguration.executionProfile

			if (RunConfiguration.executionProfile.contains("_")) {
				masterProfile = RunConfiguration.executionProfile.substring(0, RunConfiguration.executionProfile.indexOf("_"))
			}

			testInputParam = testInputParam.replace("<profile>", masterProfile)
		}
		if ((testInputParam).contains("<timestamp>")) {

			def date = new Date()
			def sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")

			testInputParam = testInputParam.replace("<timestamp>", sdf.format(date).toString())
		}

		//KeywordUtil.logInfo(testInputParam)
		return (testInputParam)
	}

	@Keyword
	def String readTestDataFromExcelFile (String identifierString, String paramName) {


		/*
		 Iterate through number of test data sheet names
		 */
		ExcelData temp = (ExcelData) findTestData('Test-Input')
		def testDataMapsForTestCases = [:]
		boolean foundTestData=false

		List<String> allSheetNames = GlobalVariable.testDataSheetName

		for(int iterSheetName =0 ; iterSheetName<allSheetNames.size() && foundTestData==false ;iterSheetName++ ){

			String currentSheetName = allSheetNames[iterSheetName]
			temp.changeSheet(currentSheetName)
			println '--' +currentSheetName
			//temp.changeSheet(GlobalVariable.testDataSheetName)

			/*
			 Identifying index of Test Case ID column
			 */
			def columnNameList = temp.getColumnNames()
			def indexOfReferenceColumn = null

			for (int i=0; i < columnNameList.size(); i++) {
				//	println (columnNameList[i])
				if (columnNameList[i] == 'TC-ID') {
					indexOfReferenceColumn = i
					break
				}
			}

			/*
			 Creating  a hashmap of {test data hashmap} per testcase
			 */
			def testDataList = temp.getAllData()

			for (Object row : testDataList) {

				def testDataMapForTestCase = [:]

				for (int i=0; i < columnNameList.size(); i++) {
					testDataMapForTestCase.put(columnNameList[i], row[i]);
					//println(columnNameList[i] + " : " + row[i])
				}

				testDataMapsForTestCases.put(row[indexOfReferenceColumn], testDataMapForTestCase);
				if(identifierString.equals(row[indexOfReferenceColumn])){
					foundTestData= true
					break
				}
				//println (row)
			}
			//testDataMapsForTestCases = testDataMapsForTestCases
		}//End of for loop

		String testInputParam = testDataMapsForTestCases.get(identifierString).get(paramName)

		if (testInputParam == null) {

			testInputParam = ""
		}

		if ((testInputParam).contains("<browserNameInLowerCase>")) {

			println GlobalVariable.envInfo.get('browserName')

			if ((GlobalVariable.envInfo.get('browserName')).contains("edge")) {

				testInputParam = testInputParam.replace("<browserNameInLowerCase>", "chrome")
			}
			else {

				testInputParam = testInputParam.replace("<browserNameInLowerCase>", GlobalVariable.envInfo.get('browserName'))
			}
		}
		if ((testInputParam).contains("<osNameInLowerCase>")) {

			testInputParam = testInputParam.replace("<osNameInLowerCase>", GlobalVariable.envInfo.get("osName").substring(0, GlobalVariable.envInfo.get("osName").indexOf(" ")))
		}
		if ((testInputParam).contains("<platformEnv>")) {

			testInputParam = testInputParam.replace("<platformEnv>", GlobalVariable.platformEnv)
		}
		if ((testInputParam).contains("<profile>")) {

			String masterProfile = RunConfiguration.executionProfile

			if (RunConfiguration.executionProfile.contains("_")) {
				masterProfile = RunConfiguration.executionProfile.substring(0, RunConfiguration.executionProfile.indexOf("_"))
			}

			testInputParam = testInputParam.replace("<profile>", masterProfile)
		}
		if ((testInputParam).contains("<timestamp>")) {

			def date = new Date()
			def sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")

			testInputParam = testInputParam.replace("<timestamp>", sdf.format(date).toString())
		}

		//KeywordUtil.logInfo(testInputParam)
		return (testInputParam)
	}

	@Keyword
	def String getTestInputValueFromExcelDataJsonString (String excelDataColumnName, String key) {

		DataProcessing dataProcessing = new DataProcessing()

		def slurper = new groovy.json.JsonSlurper()
		def map

		try{

			map = slurper.parseText(dataProcessing.readTestDataFromExcelFile(GlobalVariable.currentTestCaseID, excelDataColumnName))
		}catch (Exception e){

			if ((excelDataColumnName.equals("FlagsForNonSupportedEnvJson") && key.equals("Combination(s)")) == false) {

				KeywordUtil.markErrorAndStop("Error in processing JsonString. Please check the input Json string.")
			}
		}

		String matchKey = ""
		String matchValue = ""

		map.each { k, v ->

			//println "$k : $v"
			matchKey = "$k"
			//println "CCC " + matchKey

			if (matchKey.equals(key)) {
				matchValue = "$v"
				//println "CCC " + matchValue
			}
		}

		if (matchValue.isEmpty()) {

			if ((excelDataColumnName.equals("FlagsForNonSupportedEnvJson") && key.equals("Combination(s)")) == false) {

				KeywordUtil.markErrorAndStop("Expected Value for a Key not found. Please check the input Json string.")
			}
		}

		if(matchValue.equals('<nil>')){
			matchValue = ""
		}

		KeywordUtil.logInfo(matchValue)
		return matchValue

	}


	@Keyword
	def static void writeToSpecificCellIntoExcel(String fileNameWithFilePath, String sheetName, String rowName, String excelDataColumnName, String dataToWrite) throws IOException {

		new File(fileNameWithFilePath).withInputStream { inputStream ->

			Workbook workbook = new XSSFWorkbook(inputStream)

			Sheet sheet = workbook.getSheet(sheetName)
			if (sheet == null) {
				throw new IllegalArgumentException("Invalid sheet name")
			}

			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum()

			int columnNumber = 0

			int rowNumber = 0

			boolean columnFound = false
			boolean rowFound = false

			for (int i = 0; i < (rowCount + 1); i++) {
				Row row = sheet.getRow(i)

				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j)

					if (cell == null) {
						continue
					}

					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:

						// DON"T COMPARE INTEGER WITH STRING

						//							if (rowFound== false && row.getCell(0).getNumericCellValue() == Integer.parseInt(GlobalVariable.currentTestCaseID) ) {
							if (rowFound== false && row.getCell(0).getNumericCellValue() == Integer.parseInt(rowName)) {
								rowNumber = i
								rowFound = true
							}
							break

						case Cell.CELL_TYPE_STRING:

							if (rowFound== false && row.getCell(0).getCellType()!=Cell.CELL_TYPE_NUMERIC){

								if(row.getCell(0).getStringCellValue() == rowName) {
									rowNumber = i
									rowFound = true
								}
							}

							if (columnFound== false && excelDataColumnName.equalsIgnoreCase(row.getCell(j).getStringCellValue())) {
								columnNumber = j
								columnFound = true
							}

							break
					}

					if (rowFound && columnFound) {
						break
					}
				}

				if (rowFound && columnFound) {
					break
				}
			}

			if (rowFound && columnFound) {
				new File(fileNameWithFilePath).withOutputStream { outputStream ->
					Row row = sheet.getRow(rowNumber)
					Cell cell = row.getCell(columnNumber)
					if (cell == null) {
						cell = row.createCell(columnNumber)
					}
					cell.setCellValue(dataToWrite)
					workbook.write(outputStream)
				}
			} else {
				// ERROR, NO MATCHING CELL FOUND
				KeywordUtil.markErrorAndStop("Error, NO MATCHING CELL FOUND, where expected row name is :"+rowName+" and expected column name is : "+excelDataColumnName)
			}
			// Current POI jar in katalon is old version and hence it doesn't support workbook.close() method. If want to use, try replacing the jar with newer version
			//			workbook.close()
		}
	}

	@Keyword
	/**
	 * @param testName is the test title received from listener
	 *  @param testCaseContext is the object received from listener
	 */
	def customReportGeneration(Map <String, Object> mapTcProps, String suiteName, String testName, TestCaseContext testCaseContext, String skipReason, Map<String, Integer> subErrorEntity){

		//Pushing current TC execution result to report file - start

		DataProcessing dataProcessing = new DataProcessing()

		File TestCaseExecutionResultFolder = new File((RunConfiguration.getReportFolder() + "/customReport").replaceAll("\\\\", "/"))

		println TestCaseExecutionResultFolder.exists()

		if (TestCaseExecutionResultFolder.exists() == false) {

			TestCaseExecutionResultFolder.mkdir()
		}

		String mappedToManualQaSpreadsheet = ""

		try {
			mappedToManualQaSpreadsheet = dataProcessing.readTestDataFromExcelFile(GlobalVariable.currentTestCaseID, 'MappedToManualQaSpreadsheet')
		}
		catch (Exception e) {
			//Do nothing if no mapping status found.
		}

		String initialDeveloper = ""

		try {
			initialDeveloper = dataProcessing.readTestDataFromExcelFile(GlobalVariable.currentTestCaseID, 'Initial Developer')
		}
		catch (Exception e) {
			//Do nothing if no initial developer found.
		}

		String fullMessage = testCaseContext.getMessage()
		String shortError = ''

		if (testCaseContext.getTestCaseStatus().toString().contains('PASSED') == false) {

			try {

				if (fullMessage.contains('Caused by: ')) {

					shortError = fullMessage.substring(fullMessage.lastIndexOf('Caused by: '))
					shortError = shortError.substring(11, shortError.indexOf('\n\tat '))
					shortError = shortError.replaceAll('\"', '""')
				}
				else {

					shortError = fullMessage.substring(fullMessage.indexOf('Reason'))
					shortError = shortError.substring(shortError.indexOf('\n') + 1, shortError.indexOf('\n\tat '))
					shortError = shortError.replaceAll('\"', '""')
				}
			}
			catch (Exception e) {

				shortError = 'See HTML report for more technical details.'
			}
		}

		if(GlobalVariable.currentTestCaseSkippedStatus.equals('true'))
		{
			shortError = skipReason
		}

		//Error Category operation - Start
		String errorCategory=''
		String subErrorEntityToWrite = ''
		String subErrorEntityCount = ''
		String imagePath = ""
		String localPCName = networkAddrs.getCanonicalHostName()

		if (testCaseContext.getTestCaseStatus().toString() in ['FAILED', 'ERROR']== true){
			if(shortError.contains('com.kms.katalon.core.exception.StepErrorException')){
				errorCategory = 'StepErrorException'
			}else if(shortError.contains('com.kms.katalon.core.webui.exception.WebElementNotFoundException')){
				errorCategory = 'WebElementNotFoundException'
			}else if(shortError.contains('com.kms.katalon.core.exception.StepFailedException')){
				errorCategory = 'StepFailedException'
			}else if(shortError.contains('org.openqa.selenium.NoSuchElementException')){
				errorCategory = 'NoSuchElementException'
			}else if(shortError.contains('java.lang.AssertionError:')){
				errorCategory = 'AssertionError'
			}else{
				errorCategory = 'Other'
			}
			subErrorEntityToWrite  = updateErrorEntityList(shortError,subErrorEntity)

			if(!(subErrorEntityToWrite.isEmpty())){
				subErrorEntityCount = subErrorEntity.get(subErrorEntityToWrite)
			}

			imagePath = RunConfiguration.getReportFolder() + '/TestCaseDumps/' + GlobalVariable.currentTestCaseID + '_ActualScreen.png'

			if(imagePath.toLowerCase().contains('local builds') || imagePath.toLowerCase().contains('agent')){
				imagePath = '\\\\' + imagePath.replaceFirst('([a-zA-Z]:)?', localPCName)
			}
		}

		//Error Category operation - End

		String testStatusForReport=testCaseContext.getTestCaseStatus().toString()

		//Force ERRORs to be logged as FAILURES
		if (testStatusForReport.equalsIgnoreCase("ERROR")) {
			testStatusForReport = "FAILED"
		}

		if(fullMessage == ""){

			String infoFilePath = RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID+"/imgCheckInfo.txt"
			Path path = Paths.get(infoFilePath)

			if(Files.exists(path)){

				shortError = new String(Files.readAllBytes(path))

				imagePath = (shortError.substring(0, shortError.indexOf('\n'))).trim()
				shortError = (shortError.substring(shortError.indexOf('\n'))).trim()

				if ((shortError.contains("DIFF") || shortError.contains("NOTFOUND")) && (shortError.contains("Verify Manually") == false)) {

					testStatusForReport = "VERIFY"
					errorCategory = "VerifyImageCheckFailure"
					GlobalVariable.currentTestCaseImgCheckSuccess = 'false'
				} else if (((shortError.contains("DIFF") || shortError.contains("NOTFOUND")) == false) && shortError.contains("Verify Manually")) {

					testStatusForReport = "VERIFY"
					errorCategory = "VerifySemiAutomationResults"
					//(new Utilities()).mergeScreenshotsTaken(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID, GlobalVariable.currentTestCaseID + "_CombinedImgReport")
					GlobalVariable.currentTestCaseImgCheckSuccess = 'false'
				} else if ((shortError.contains("DIFF") || shortError.contains("NOTFOUND")) && shortError.contains("Verify Manually")) {

					testStatusForReport = "VERIFY"
					errorCategory = "VerifyImageCheckFailure, VerifySemiAutomationResults"
					GlobalVariable.currentTestCaseImgCheckSuccess = 'false'
				} else {
					GlobalVariable.currentTestCaseImgCheckSuccess = 'true'
				}

				(new Utilities()).mergeScreenshotsTaken(RunConfiguration.getReportFolder() + "/ImgReport/" +GlobalVariable.currentTestCaseID, GlobalVariable.currentTestCaseID + "_CombinedImgReport")
			}
			else if (suiteName.contains("DS")) {

				//imagePath = RunConfiguration.getProjectDir() + "\\External Libraries\\reportsFolder\\" + GlobalVariable.platformEnv + "_ DS_ FE\\DS-" + suiteName + "-" + GlobalVariable.platformEnv + "\\" + GlobalVariable.currentTestCaseID + "-" + GlobalVariable.envInfo.get('testCaseCategory')

				//if(imagePath.toLowerCase().contains('local builds') || imagePath.toLowerCase().contains('agent')){
				//	imagePath = '\\\\' + imagePath.replaceFirst('([a-zA-Z]:)?', localPCName)
				//}

				testStatusForReport = "VERIFY"
				errorCategory = "VerifySemiAutomationResults"
			}
		}

		if(GlobalVariable.currentTestCaseSkippedStatus.equals('true')){
			//testStatusForReport = 'SKIPPED'
			testStatusForReport = 'NOT EXECUTED'
		}

		//Prepare report file with details with header

		reportHeaderValueEntries = getReportHeaderValueEntriesMap()
		reportHeaderValueEntries.replace("TC-ID", GlobalVariable.currentTestCaseID)
		//reportHeaderValueEntries.replace("MappedToManualQaSpreadsheet", mappedToManualQaSpreadsheet)
		reportHeaderValueEntries.replace("Initial Developer", initialDeveloper)
		reportHeaderValueEntries.replace("Script Title", testName.substring(testName.indexOf("-") + 1).trim())
		reportHeaderValueEntries.replace("Execution Status", testStatusForReport)
		reportHeaderValueEntries.replace("Issue category", errorCategory)
		reportHeaderValueEntries.replace("Issue Entity", subErrorEntityToWrite)
		reportHeaderValueEntries.replace("Issue Entity Recurrence", "")
		reportHeaderValueEntries.replace("Issue Details", shortError)
		reportHeaderValueEntries.replace("Image Path", imagePath)

		if ((testStatusForReport.equalsIgnoreCase("PASSED") == false) && (testStatusForReport.equalsIgnoreCase("NOT EXECUTED") == false)) {
			reportHeaderValueEntries.replace("Analysis Status", "Pending")
		}
		else {
			reportHeaderValueEntries.replace("Analysis Status", "")
		}

		if (mapTcProps.containsKey("trackerReasonNeedsUpdate")) {
			reportHeaderValueEntries.replace("Action Decided/Taken", mapTcProps.get("trackerReasonNeedsUpdate"))
		}
		else {
			reportHeaderValueEntries.replace("Action Decided/Taken", "")
		}

		reportHeaderValueEntries.replace("VSTS TC Priority", mapTcProps.get("Microsoft.VSTS.Common.Priority"))
		reportHeaderValueEntries.replace("VSTS Automation Status", mapTcProps.get("Technologies2020.Common.Automation"))
		reportHeaderValueEntries.replace("Automation Suite", suiteName)

		try {
			if(GlobalVariable.envInfo.get("fetchURL").toString().isEmpty() == false) { //if fetchURL entry exists, then use it
				reportHeaderValueEntries.replace("URL", GlobalVariable.envInfo.get("fetchURL").toString())
			}
			else if (GlobalVariable.URL.toString().isEmpty() == false) {
				reportHeaderValueEntries.replace("URL", GlobalVariable.URL.toString())
			}
			else {
				reportHeaderValueEntries.replace("URL", "Couldn't retrieve (Site Load Issue/Browser Terminated/Unknown Issue)")
			}
		}
		catch (Exception e) {
			reportHeaderValueEntries.replace("URL", "Couldn't retrieve (Site Load Issue/Browser Terminated/Unknown Issue)")
		}

		if ((GlobalVariable.envInfo.get("browserName")).toString().isEmpty() || (GlobalVariable.envInfo.get("browserName") == null)) {
			reportHeaderValueEntries.replace("Browser Details", "Couldn't retrieve (Site Load Issue/Browser Terminated/Unknown Issue)")
		}
		else {
			reportHeaderValueEntries.replace("Browser Details", "[" + GlobalVariable.envInfo.get("browserName") + ": " + GlobalVariable.envInfo.get("browserVersion") + "]")
		}

		reportHeaderValueEntries.replace("Catalog Details", GlobalVariable.envInfo.get("Catalog Details"))
		reportHeaderValueEntries.replace("App Details", GlobalVariable.envInfo.get("App Details"))

		//Find position for "Issue Entity" in linkedHashMap and write subErrorEntityCount using excel formula

		int columnIndexIssueEntity = 0

		for (String key : reportHeaderValueEntries.keySet()) {

			if (key.equals("Issue Entity")) {

				String columnNameIssueEntity = CellReference.convertNumToColString(columnIndexIssueEntity)
				subErrorEntityCount = "COUNTIF(" + columnNameIssueEntity + ":" + columnNameIssueEntity + ",OFFSET(INDIRECT(ADDRESS(ROW(), COLUMN())),0,-1))"
				break
			}
			columnIndexIssueEntity = columnIndexIssueEntity + 1
		}

		reportHeaderValueEntries.replace("Issue Entity Recurrence", subErrorEntityCount)

		//Report file
		
		if(suiteName.equals("PSL")){
			suiteName = suiteName + RunConfiguration.executionProfile
		}
		String reportPath = TestCaseExecutionResultFolder.getAbsolutePath() + '/'
		String reportNameWithExtension = suiteName + ' auto_' + GlobalVariable.envInfo.get("osName") + '_' + GlobalVariable.envInfo.get("browserName") + '_' + GlobalVariable.platformEnv + '.xlsx'

		File TestCaseExecutionResult = new File(reportPath + reportNameWithExtension)

		//Create report file with header, if file not present already

		int rowIndex = 0
		int columnIndex = 0

		if (TestCaseExecutionResult.exists() == false) {

			XSSFWorkbook workbook = new XSSFWorkbook()
			XSSFSheet sheet = workbook.createSheet("report")

			Row row = sheet.createRow(rowIndex)

			for (Object headerEntry : reportHeaderValueEntries.keySet()) {

				Cell cell = row.createCell(columnIndex++)
				cell.setCellValue(headerEntry.toString())

			}

			FileOutputStream outputStreamHeader = new FileOutputStream(reportPath + reportNameWithExtension)
			workbook.write(outputStreamHeader)
			outputStreamHeader.close()
		}

		//Write content in the report file

		if (TestCaseExecutionResult.exists() == true) {

			FileInputStream inputStream = new FileInputStream(new File(reportPath + reportNameWithExtension));
			XSSFWorkbook workbook = WorkbookFactory.create(inputStream)
			//FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator()
			//workbook.getCreationHelper().createFormulaEvaluator().evaluateAll()
			XSSFSheet sheet = workbook.getSheet("report")
			inputStream.close()

			rowIndex = workbook.getSheetAt(0).getLastRowNum() + 1
			columnIndex = 0
			Row row = sheet.createRow(rowIndex)

			for (Map.Entry <String, Object> entry : reportHeaderValueEntries.entrySet()) {

				Cell cell = row.createCell(columnIndex)

				if ((entry.getKey().toString().equals("TC-ID"))) {

					cell.setCellValue(entry.getValue().toString())

					final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL)

					String tfsEndpoint = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsEndpoint")
					String tfsCollection = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsCollection")
					String tfsProject = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsProject")

					href.setAddress(tfsEndpoint + "/" + tfsCollection + "/" + tfsProject + "/_workitems/edit/" + GlobalVariable.currentTestCaseID)
					cell.setHyperlink(href)

					XSSFCellStyle hlinkstyle = workbook.createCellStyle()
					XSSFFont hlinkfont = workbook.createFont()
					hlinkfont.setUnderline(XSSFFont.U_SINGLE)
					hlinkfont.setColor(new HSSFColor.BLUE().getIndex())
					hlinkstyle.setFont(hlinkfont)

					cell.setCellStyle(hlinkstyle)
				}
				else if ((entry.getKey().toString().equals("Execution Status"))) {

					XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet)
					XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint("PASSED-ERROR-FAILED-NOT EXECUTED-VERIFY-BLOCKED".split("-"))
					CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex)
					XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList)
					validation.setShowErrorBox(true)
					sheet.addValidationData(validation)

					validation.setSuppressDropDownArrow(false)

					//Find position for "Analysis Status" in linkedHashMap and write execution status formula

					int columnIndexAnalysisStatus = 0
					String executionStatusFormula = ''

					for (String key : reportHeaderValueEntries.keySet()) {

						if (key.equals("Analysis Status")) {

							String columnNameAnalysisStatus = CellReference.convertNumToColString(columnIndexAnalysisStatus)
							executionStatusFormula = 'IF(ISNUMBER(SEARCH("passed",INDIRECT("' + columnNameAnalysisStatus + '"&ROW()))),"PASSED",' +
									'IF(ISNUMBER(SEARCH("defect",INDIRECT("' + columnNameAnalysisStatus + '"&ROW()))),"FAILED",' +
									'IF(ISNUMBER(SEARCH("blocked",INDIRECT("' + columnNameAnalysisStatus + '"&ROW()))),"BLOCKED","' +
									entry.getValue().toString() + '")))'
							break
						}
						columnIndexAnalysisStatus = columnIndexAnalysisStatus + 1
					}

					cell.setCellFormula(executionStatusFormula)
					//cell.setCellValue(entry.getValue().toString())
				}
				else if ((entry.getKey().toString().equals("Analysis Status")) && (rowIndex != 0)) {

					XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet)
					XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint("Passed (by Re-Run):Passed (by Image Verification):Passed (by Manual Verification):Defect:Blocked:Pending".split(":"))
					CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex)
					XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList)
					validation.setShowErrorBox(true)
					sheet.addValidationData(validation)

					validation.setSuppressDropDownArrow(false)

					cell.setCellValue(entry.getValue().toString())
				}
				else if ((entry.getKey().toString().equals("Issue Entity Recurrence"))) {

					String column = CellReference.convertNumToColString(cell.getColumnIndex());
					cell.setCellFormula(entry.getValue())
				}
				else if ((entry.getKey().toString().equals("Image Path"))) {

					if (entry.getValue().toString().isEmpty() == false) {

						final Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.FILE)

						File imageFile = new File (entry.getValue().toString().trim())

						if (imageFile.exists()) {

							cell.setCellValue("Image Path")

							href.setAddress(imageFile.toURI().toString());
							cell.setHyperlink(href);

							XSSFCellStyle hlinkstyle = workbook.createCellStyle()
							XSSFFont hlinkfont = workbook.createFont()
							hlinkfont.setUnderline(XSSFFont.U_SINGLE)
							hlinkfont.setColor(new HSSFColor.BLUE().getIndex())
							hlinkstyle.setFont(hlinkfont)

							cell.setCellStyle(hlinkstyle)

							//println (cell.getHyperlink().getAddress())
						}
					}
				}
				else {

					if (entry.getValue().toString().isEmpty() == false) {

						if (entry.getValue().toString().length() > 32767) {
							cell.setCellValue((entry.getValue().toString()).substring(0, 32766))
						} else {
							cell.setCellValue(entry.getValue().toString())
						}
					}
				}

				columnIndex = columnIndex + 1

			}

			//evaluator.evaluateAll();
			//FileOutputStream outputStreamTestResultRow = new FileOutputStream(reportPath + reportNameWithExtension)

			//spreadsheet report and tfs integration - do not put redundant "Not Executed" entries
			EnvironmentSetup environmentSetup = new EnvironmentSetup ()

			if (shortError.contains("Execution criterion not satisfied") || shortError.contains("VSTS TC Automation Status") || skipReason.contains("VSTS TC State - Closed") || (skipReason.toLowerCase().startsWith("<") && skipReason.toLowerCase().endsWith(">"))) {
				println "Not writing the result in spreadsheet."
			}
			else {
				FileOutputStream outputStreamTestResultRow = new FileOutputStream(reportPath + reportNameWithExtension)
				workbook.write(outputStreamTestResultRow)
				outputStreamTestResultRow.close()
			}
		}
		//Pushing current TC execution result to report file - end
	}

	def LinkedHashMap <String, Object> getReportHeaderValueEntriesMap () {

		reportHeaderValueEntries.clear()

		reportHeaderValueEntries.put("TC-ID", "")
		//reportHeaderValueEntries.put("MappedToManualQaSpreadsheet", "")
		reportHeaderValueEntries.put("Initial Developer", "")
		reportHeaderValueEntries.put("Script Title", "")
		reportHeaderValueEntries.put("Execution Status", "")
		reportHeaderValueEntries.put("Issue category", "")
		reportHeaderValueEntries.put("Issue Entity", "")
		reportHeaderValueEntries.put("Issue Entity Recurrence", "")
		reportHeaderValueEntries.put("Issue Details", "")
		reportHeaderValueEntries.put("Image Path", "")
		reportHeaderValueEntries.put("Analysis Status", "")
		reportHeaderValueEntries.put("Action Decided/Taken", "")
		reportHeaderValueEntries.put("VSTS TC Priority", "")
		reportHeaderValueEntries.put("VSTS Automation Status", "")
		reportHeaderValueEntries.put("Automation Suite", "")
		reportHeaderValueEntries.put("URL", "")
		reportHeaderValueEntries.put("Browser Details", "")
		reportHeaderValueEntries.put("Catalog Details", "")
		reportHeaderValueEntries.put("App Details", "")

		return reportHeaderValueEntries
	}

	@Keyword
	def String getPropertyValueFromPropertiesFile(String propertiesFilePath, String property) {

		try {
			Properties propertiesFile = new Properties()

			File f = new File(propertiesFilePath)

			propertiesFile.load(new FileInputStream(f.getAbsolutePath()))

			return propertiesFile.getProperty(property)
		}
		catch (Exception e) {

			return ""
		}
	}

	@Keyword
	def cleanTheTestExecutionReportPropertiesFile(String suiteName) {

		Properties reportPropertiesFile = new Properties()

		File testExecutionCustomReport = new File("./External Libraries/testExecutionReportMetadata/testExecutionReport_" + suiteName + ".properties")

		if (testExecutionCustomReport.exists() == false) {

			testExecutionCustomReport << "testExecutionCustomReportPath="
		}

		reportPropertiesFile.load(new FileInputStream(testExecutionCustomReport.getAbsolutePath()))

		for (Map.Entry<Object, Object> entry : reportPropertiesFile.entrySet())
		{
			println (entry.getKey() + "/" + entry.getValue());
			reportPropertiesFile.setProperty(entry.getKey().toString(), "")
		}

		FileOutputStream fos = new FileOutputStream(testExecutionCustomReport.getAbsolutePath());
		reportPropertiesFile.store(fos, "")
		fos.close()
	}

	@Keyword
	def restoreTestExecutionPatternPropertiesFile(String suiteName) {

		Properties propertiesFile = new Properties()

		File testExecutionPattern = new File("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties")

		if (testExecutionPattern.exists() == false) {

			testExecutionPattern << "testExecutionOffset=1\nstartingTestPosition=1"
		}

		propertiesFile.load(new FileInputStream(testExecutionPattern.getAbsolutePath()))

		for (Map.Entry<Object, Object> entry : propertiesFile.entrySet())
		{
			println (entry.getKey() + "/" + entry.getValue());
			propertiesFile.setProperty(entry.getKey().toString(), "1")
		}

		FileOutputStream fos = new FileOutputStream(testExecutionPattern.getAbsolutePath());
		propertiesFile.store(fos, "")
		fos.close()
	}

	@Keyword
	def setTheTestExecutionReportPath(String suiteName, String value){

		Properties reportPropertiesFile = new Properties()

		String key = 'testExecutionCustomReportPath'

		reportPropertiesFile.load(new FileInputStream("./External Libraries/testExecutionReportMetadata/testExecutionReport_" + suiteName + ".properties"))

		if (value == null) {
			reportPropertiesFile.setProperty(key, "")
		}
		else {
			reportPropertiesFile.setProperty(key, value)
		}

		FileOutputStream fos = new FileOutputStream("./External Libraries/testExecutionReportMetadata/testExecutionReport_" + suiteName + ".properties")
		reportPropertiesFile.store(fos, "")
		fos.close()
	}

	@Keyword
	def setTheTestExecutionPatternPath(String suiteName, String value){

		Properties reportPropertiesFile = new Properties()

		String key = 'testExecutionPatternPath'

		reportPropertiesFile.load(new FileInputStream("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties"))

		if (value == null) {
			reportPropertiesFile.setProperty(key, "")
		}
		else {
			reportPropertiesFile.setProperty(key, value)
		}

		FileOutputStream fos = new FileOutputStream("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties")
		reportPropertiesFile.store(fos, "")
		fos.close()
	}

	@Keyword
	def String getTheTestExecutionReportPath(String suiteName){

		Properties reportPropertiesFile = new Properties()

		String key = 'testExecutionCustomReportPath'

		File testExecutionCustomReport = new File("./External Libraries/testExecutionReportMetadata/testExecutionReport_" + suiteName + ".properties")

		reportPropertiesFile.load(new FileInputStream(testExecutionCustomReport.getAbsolutePath()))

		return (reportPropertiesFile.getProperty(key))
	}

	@Keyword
	def String getTheTestExecutionPatternPath(String suiteName){

		Properties reportPropertiesFile = new Properties()

		String key = 'testExecutionPatternPath'

		File testExecutionCustomReport = new File("./External Libraries/testExecutionPatternMetadata/testExecutionPattern_" + suiteName + ".properties")

		reportPropertiesFile.load(new FileInputStream(testExecutionCustomReport.getAbsolutePath()))

		return (reportPropertiesFile.getProperty(key))
	}

	@Keyword
	/**
	 * @param testName is the test title received from listener
	 *  @param pathOfPreviousTestCaseExecutionResult is custom report path of previous test execution
	 *  @param pathOfCurrentTestCaseExecutionResult is custom report path of current test execution
	 *  @param pathOfCombinedTestCaseExecutionResult is custom report path of combined result
	 */
	def mergeTheCustomReports (String pathOfPreviousTestCaseExecutionResult, String pathOfCurrentTestCaseExecutionResult, String pathOfCombinedTestCaseExecutionResult){

		//Reading existing reports

		File previousTestCaseExecutionResult = new File (pathOfPreviousTestCaseExecutionResult)
		File currentTestCaseExecutionResult = new File (pathOfCurrentTestCaseExecutionResult)

		FileInputStream inputStreamPreviousContent = new FileInputStream(previousTestCaseExecutionResult)
		FileInputStream inputStreamCurrentContent = new FileInputStream(currentTestCaseExecutionResult)

		XSSFWorkbook workbookPreviousContent = WorkbookFactory.create(inputStreamPreviousContent)
		XSSFWorkbook workbookCurrentContent = WorkbookFactory.create(inputStreamCurrentContent)

		XSSFSheet sheetPreviousContent = workbookPreviousContent.getSheet("report")
		XSSFSheet sheetCurrentContent = workbookCurrentContent.getSheet("report")

		inputStreamPreviousContent.close()
		inputStreamCurrentContent.close()

		//Combined report preparation
		File combinedTestCaseExecutionResult = new File (pathOfCombinedTestCaseExecutionResult)
		FileOutputStream outputStreamCombinedContent = new FileOutputStream(combinedTestCaseExecutionResult)
		XSSFWorkbook workbookCombinedContent = new XSSFWorkbook()
		XSSFSheet sheetCombinedContent = workbookCombinedContent.createSheet("report")
		int rowNo = 0

		Iterator<Row> previousRowContentIterator = sheetPreviousContent.iterator();

		while (previousRowContentIterator.hasNext()) {

			Row previousContentRow = previousRowContentIterator.next();
			println "\tPrevious : ${previousContentRow.getCell(0)}"

			String tcIdFromPreviousFile = previousContentRow.getCell(0)

			boolean lineMatchFound = false

			Iterator<Row> currentRowContentIterator = sheetCurrentContent.iterator();

			while (currentRowContentIterator.hasNext()) {

				Row currentContentRow = currentRowContentIterator.next();
				println "\tCurrent : ${currentContentRow.getCell(0)}"

				try {

					String tcIdFromCurrentFile = currentContentRow.getCell(0)

					if (tcIdFromCurrentFile.equals(tcIdFromPreviousFile)) {

						println '\t\tAdding current line to combined result'

						sheetCombinedContent = copyRowContentsToWorkbook(currentContentRow, workbookCombinedContent, "report", rowNo)

						lineMatchFound = true
						break
					}
				}
				catch (Exception e) {

					println '\tException occurred while fetching/comparing test from current report.'
				}
			}

			if (lineMatchFound == false) {

				println 'Adding previous line to combined result'
				sheetCombinedContent = copyRowContentsToWorkbook(previousContentRow, workbookCombinedContent, "report", rowNo)
			}

			rowNo = rowNo + 1
		}

		workbookCombinedContent.write(outputStreamCombinedContent)
		outputStreamCombinedContent.close()
	}

	def XSSFSheet copyRowContentsToWorkbook (Row sourceRow, XSSFWorkbook targetWorkbook, String targetSheetName, int rowIndex) {

		DataProcessing dataProcessing = new DataProcessing()

		XSSFSheet targetSheet = targetWorkbook.getSheet(targetSheetName)

		Row targetRow = targetSheet.createRow(rowIndex)

		Iterator <Cell> sourceCellIterator = sourceRow.cellIterator()

		int columnIndex = 0

		reportHeaderValueEntries = getReportHeaderValueEntriesMap()

		println (reportHeaderValueEntries)

		for (String key : reportHeaderValueEntries.keySet()) {

			//println (key)
			//println (getCellValue(sourceRow.getCell(columnIndex)).toString())

			if (key.contains("Image Path")) {

				try {
					reportHeaderValueEntries.replace(key, sourceRow.getCell(columnIndex).getHyperlink().getAddress())
				} catch (Exception e) {
					reportHeaderValueEntries.replace(key, getCellValue(sourceRow.getCell(columnIndex)).toString())
				}
			}
			else {
				reportHeaderValueEntries.replace(key, getCellValue(sourceRow.getCell(columnIndex)).toString())
			}

			println (key)
			println (getCellValue(sourceRow.getCell(columnIndex)).toString())

			columnIndex = columnIndex + 1
		}

		columnIndex = 0

		for (Map.Entry <String, Object> entry : reportHeaderValueEntries.entrySet()) {

			Cell cell = targetRow.createCell(columnIndex)

			if ((entry.getKey().toString().equals("TC-ID")) && (rowIndex != 0)) {

				cell.setCellValue(entry.getValue().toString())

				final Hyperlink href = targetWorkbook.getCreationHelper().createHyperlink(HyperlinkType.URL)

				String tfsEndpoint = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsEndpoint")
				String tfsCollection = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsCollection")
				String tfsProject = dataProcessing.getPropertyValueFromPropertiesFile(RunConfiguration.getProjectDir() + "/External Libraries/tfsIntegration.properties", "tfs.tfsProject")

				href.setAddress(tfsEndpoint + "/" + tfsCollection + "/" + tfsProject + "/_workitems/edit/" + entry.getValue().toString())
				cell.setHyperlink(href)

				XSSFCellStyle hlinkstyle = targetWorkbook.createCellStyle()
				XSSFFont hlinkfont = targetWorkbook.createFont()
				hlinkfont.setUnderline(XSSFFont.U_SINGLE)
				hlinkfont.setColor(new HSSFColor.BLUE().getIndex())
				hlinkstyle.setFont(hlinkfont)

				cell.setCellStyle(hlinkstyle)
			}
			else if ((entry.getKey().toString().equals("Execution Status")) && (rowIndex != 0)) {

				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(targetSheet)
				XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint("PASSED:ERROR:FAILED:NOT EXECUTED:VERIFY:BLOCKED".split(":"))
				CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex)
				XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList)
				validation.setShowErrorBox(true)
				targetSheet.addValidationData(validation)

				validation.setSuppressDropDownArrow(false)

				try { cell.setCellFormula(entry.getValue().toString()) }
				catch (Exception e) {
					println "found"
					throw e
				}
				//cell.setCellValue(entry.getValue().toString())
			}
			else if ((entry.getKey().toString().equals("Analysis Status")) && (rowIndex != 0)) {

				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(targetSheet)
				XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint("Passed (by Re-Run):Passed (by Image Verification):Passed (by Manual Verification):Defect:Blocked:Pending".split(":"))
				CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex)
				XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(dvConstraint, addressList)
				validation.setShowErrorBox(true)
				targetSheet.addValidationData(validation)

				validation.setSuppressDropDownArrow(false)

				cell.setCellValue(entry.getValue().toString())
			}
			else if ((entry.getKey().toString().equals("Issue Entity Recurrence")) && (rowIndex != 0)) {

				cell.setCellFormula(entry.getValue())
			}
			else if ((entry.getKey().toString().equals("Image Path")) && (rowIndex != 0)) {

				if (entry.getValue().toString().isEmpty() == false) {

					cell.setCellValue("Image Path")

					final Hyperlink href = targetWorkbook.getCreationHelper().createHyperlink(HyperlinkType.FILE)

					//File imageFile = new File (entry.getValue().toString().trim())

					//href.setAddress(imageFile.toURI().toString())
					href.setAddress(entry.getValue().toString().trim())
					cell.setHyperlink(href);

					XSSFCellStyle hlinkstyle = targetWorkbook.createCellStyle()
					XSSFFont hlinkfont = targetWorkbook.createFont()
					hlinkfont.setUnderline(XSSFFont.U_SINGLE)
					hlinkfont.setColor(new HSSFColor.BLUE().getIndex())
					hlinkstyle.setFont(hlinkfont)

					cell.setCellStyle(hlinkstyle)
				}
			}
			else {

				if (entry.getValue().toString().isEmpty() == false) {

					if (entry.getValue().toString().length() > 32767) {
						cell.setCellValue(entry.getValue().toString().substring(0, 32766))
					} else {
						cell.setCellValue(entry.getValue().toString())
					}
				}
			}

			columnIndex = columnIndex + 1

		}

		println("")
		return targetSheet
	}

	def Object getCellValue (Cell cell) {

		Object cellValue = ''

		if (cell!=null) {

			switch (cell.getCellType()) {

				case Cell.CELL_TYPE_NUMERIC:
					cellValue = cell.getNumericCellValue()
					break

				case Cell.CELL_TYPE_STRING:
					cellValue = cell.getStringCellValue()
					break

				case Cell.CELL_TYPE_FORMULA:
					cellValue = cell.getCellFormula()
					break

				case Cell.CELL_TYPE_BLANK:
					break

				case Cell.CELL_TYPE_BOOLEAN:
					cellValue = cell.getBooleanCellValue()
					break

				case Cell.CELL_TYPE_ERROR:
					cellValue = cell.getErrorCellValue()
					break

				default:
					break
			}
		}
		return cellValue
	}

	def String updateErrorEntityList(String errorValue,Map<String, Integer> subErrorEntity){
		if(errorValue.contains('com.kms.katalon.core.exception.StepErrorException')||
		errorValue.contains('com.kms.katalon.core.webui.exception.WebElementNotFoundException')||
		errorValue.contains('com.kms.katalon.core.exception.StepFailedException')){

			try{

				Map<String, String> imgErrorMsgMap = new HashMap<String, String>()
				imgErrorMsgMap.put('Expected Image found in UI, but not satisfying the comparison tolerance','Image present with less tolerance')
				imgErrorMsgMap.put('Expected Image NOT found in UI','Image absent')
				imgErrorMsgMap.put('Image expected to be absent, was actually found in UI. The image absence check failed','Image present, It should be absent')
				for(Object imgErrorListItem : imgErrorMsgMap ){
					if(errorValue.contains(imgErrorListItem.getKey())){
						if(subErrorEntity.containsKey(imgErrorListItem.getValue())){
							int cnt = subErrorEntity.get(imgErrorListItem.getValue())
							cnt++
							subErrorEntity.replace(imgErrorListItem.getValue(), cnt)
							return imgErrorListItem.getValue()
						}else{
							subErrorEntity.put(imgErrorListItem.getValue(),1)
							return imgErrorListItem.getValue()
						}
					}
				}
				String errorEntity = errorValue.split('Object Repository/')[1]
				errorEntity = errorEntity.replaceAll('"','')
				if(subErrorEntity.containsKey(errorEntity)){
					int cnt = subErrorEntity.get(errorEntity)
					cnt++
					subErrorEntity.replace(errorEntity, cnt)
					return errorEntity
				}else{
					subErrorEntity.put(errorEntity,1)
					return errorEntity
				}
			}catch(Exception e){
				return ''
			}
		}else if(errorValue.contains('org.openqa.selenium.NoSuchElementException')){
			try{
				String errorEntity = errorValue.split('Element info: ')[1]
				errorEntity = errorEntity.replaceAll('"','')
				if(subErrorEntity.containsKey(errorEntity)){
					int cnt = subErrorEntity.get(errorEntity)
					cnt++
					subErrorEntity.replace(errorEntity, cnt)
					return errorEntity
				}else{
					subErrorEntity.put(errorEntity,1)
					return errorEntity
				}
			}catch(Exception e){
				return ''
			}
		}else if(errorValue.contains('java.lang.AssertionError:')){
			return ''
		}else{
			return ''
		}
	}


	@Keyword
	def int recordImageInformationToFile(String checkResult, String checkResultValue){

		int recordCounter =1
		/*List<String> pcNameList = new ArrayList<>()
		 pcNameList.add('pc-auto1')
		 pcNameList.add('pc-auto2')
		 pcNameList.add('pc-auto3')
		 pcNameList.add('pc-auto4')
		 pcNameList.add('pc-vedprakashb')
		 pcNameList.add('pc-vahid')*/

		String imgReportPath

		String infoFilePath = RunConfiguration.getReportFolder() + "/ImgReport/" + GlobalVariable.currentTestCaseID+"/imgCheckInfo.txt"
		String localFilePath = RunConfiguration.getReportFolder() + "/ImgReport/" + GlobalVariable.currentTestCaseID

		String localPCName = networkAddrs.getCanonicalHostName()
		imgReportPath = localFilePath

		//	for(String pcName : pcNameList){
		//if(pcName.toLowerCase().contains(localPCName.toLowerCase()) &&
		if(localFilePath.toLowerCase().contains('local builds') || localFilePath.toLowerCase().contains('agent')){
			imgReportPath = '\\\\'+localFilePath.replaceFirst('([a-zA-Z]:)?', localPCName)
		}
		//		}

		File parentFolder = new File(RunConfiguration.getReportFolder() + "/ImgReport/" + GlobalVariable.currentTestCaseID)

		if(parentFolder.exists()==false){
			parentFolder.mkdirs()
		}

		Path path = Paths.get(infoFilePath)

		File file = new File(infoFilePath)
		Writer output;

		try{
			if(file.exists()==false){
				file.createNewFile()
				output = new BufferedWriter(new FileWriter(file, true))
				output.write(imgReportPath)
				output.newLine()
				output.write(recordCounter+' : '+checkResult+' : '+checkResultValue)
				output.close()

			}
			else{
				List<String> allLines = Files.readAllLines(path)
				recordCounter = allLines.size()
				output = new BufferedWriter(new FileWriter(file, true))
				output.newLine()
				output.append(recordCounter+' : '+checkResult+' : '+checkResultValue)}
			output.close()
		}
		catch(IOException e){
		}
		return recordCounter
	}

	@Keyword
	def Object getValueOfKeyFromJson(Object obj, String keyToGetValue){
		boolean valueFound = false;
		def slurper = new groovy.json.JsonSlurper()
		def map
		try{

			map = slurper.parseText(obj)
		}catch (Exception e){
			println '\n\nException to convert response string to JSON object\n\n'+e.printStackTrace()
		}
		String companyReturnKeysValue
		for (def object : map) {
			for (entry in object) {
				//println entry.key
				//println entry.value
				if ((entry.key.toString()).equals(keyToGetValue)) {
					companyReturnKeysValue=entry.getValue().toString()
					valueFound = true
					break;
				}
			}

		}
		if (valueFound == false) {
			KeywordUtil.markFailedAndStop("\n\n" + "Expected value of provided key "+keyToGetValue+" is not found in the JSON file." + "\n\n")
		}
		return companyReturnKeysValue
	}

}