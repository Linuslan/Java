set p=%~dp0
set path=%p%encrypt.LIB;
java -Djava.library.path=. -cp . Encrypt -src jardemo.jar