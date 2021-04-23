set "_path=%~dp0"
for %%I in ("%_path%\..") do set "batchparent=%%~fI"
pushd "%batchparent%\External Libraries\reportsFolder\"
for /F "delims=" %%i in ('dir /b') do (rmdir "%%i" /s/q || del "%%i" /s/q)

