set "_path=%~dp0"
cd /D %_path%
java -jar tfsIntegration.jar tfsIntegration.Tests -combineResultsFiles "./reportsFolder" "xlsx" ":"

Timeout 5

java -jar "statsGenerator.jar"

Timeout 10

taskkill /F /T /IM EXCEL.EXE

Timeout 5


