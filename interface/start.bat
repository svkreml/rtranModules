@echo off
SetLocal EnableDelayedExpansion
set JAVA=C:\jdk1.8.0_91\jre\bin\java.exe
rem set DUMP=-Dcom.sun.xml.ws.transport.http.HttpAdapter.dump=true
rem -Dlogback.configurationFile=/path/to/config.xml

%JAVA% -jar target/single.jar
pause
