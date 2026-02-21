#!/bin/bash

# Create build directories
mkdir -p build/migration
mkdir -p build/libs

# Compile Java file
javac -d build/migration SQLscripts/MigrateToSQL.java

# Create Manifest
echo "Main-Class: MigrateToSQL" > build/migration/Manifest.txt

# Create JAR
jar cfm build/libs/db-migration.jar build/migration/Manifest.txt -C build/migration .

# Cleanup Manifest
rm build/migration/Manifest.txt

echo "db-migration.jar created in build/libs/"
