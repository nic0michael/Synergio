#!/bin/bash
# Run the SQL script using h2.jar found in the current directory or classpath


java -cp h2.jar org.h2.tools.RunScript -url "jdbc:h2:file:../data/database;AUTO_SERVER=TRUE" -user sa -script ./export_customer_vehicle.sql

echo "Database dump created: ./data/DB_DUMP_CUSTOMER_VEHICLE.csv"
