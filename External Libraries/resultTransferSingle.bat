set "_path=%~dp0"
cd /D %_path%
java -jar tfsIntegration.jar tfsIntegration.Tests -transferResultsFromReportToTestSuite -addTestCases=false -excludeSkippedTests=true
pause

