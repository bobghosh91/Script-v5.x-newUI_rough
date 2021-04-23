For /R "..\Test Suites\" %%a IN (*.ts*) do (
  rem Echo %%~na
  del %%a
  COPY "testExecutionPattern.properties" "testExecutionPatternMetadata\testExecutionPattern_%%~na.properties" 
)