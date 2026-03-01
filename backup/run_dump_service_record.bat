@echo off
rem Run the SQL script using h2.jar found in the current directory or classpath
java -cp h2.jar org.h2.tools.RunScript -url "jdbc:h2:file:./data/database;AUTO_SERVER=TRUE" -user sa -script backup/export_service_record.sql
echo Database dump created: data/DB_DUMP_SERVICE_RECORD.csv