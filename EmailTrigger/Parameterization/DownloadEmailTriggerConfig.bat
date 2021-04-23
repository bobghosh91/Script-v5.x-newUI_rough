cd\
d:
cd "QA Automations\Email macros\Parameterization"

taskkill /F /T /IM EXCEL.EXE
del "Email Trigger Config.xlsx" /f /q
del "output.txt" /f /q

Timeout 10

DownloadEmailTriggerConfig.vbs
rem pause