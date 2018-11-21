FOR %%F IN (.\target\*.war) DO (
 set filename=%%F
 goto run
)
:run
java -jar -Dspring.profiles.active=dev %filename% --logging.config=logback.xml
