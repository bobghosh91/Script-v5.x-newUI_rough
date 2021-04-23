echo %cd%
set arg=%1

CERTUTIL -addstore -enterprise -f -v root %arg%