set "_path=%~dp0"
cd /D %_path%
java -jar tfsIntegration.jar tfsIntegration.Tests -transferResultsFromMultipleReportsToTestSuites -excludeSkippedTests=false


Timeout 5
pause