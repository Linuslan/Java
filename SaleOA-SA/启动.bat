@echo on 
set p=%~dp0
echo %p%;
set path=%p%jre/bin;
set CLASSPATH=.;%p%jre/lib;
start javaw -classpath jre/lib/*.jar;lib/*.jar -jar -agentlib:lib/decrypt lib/json_encrypt.jar