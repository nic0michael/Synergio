#!/bin/bash
DATE=$(date +%Y-%m-%d)
FOLDER="data/"
SQL_FILE=$(mktemp)

# Locate h2.jar: local copy first, then Gradle cache, then Maven cache
H2_JAR="h2.jar"
if [ ! -f "$H2_JAR" ]; then
    H2_JAR=$(find ~/.gradle/caches -name "h2-*.jar" 2>/dev/null | head -1)
fi
if [ -z "$H2_JAR" ] || [ ! -f "$H2_JAR" ]; then
    H2_JAR=$(find ~/.m2/repository/com/h2database -name "h2-*.jar" 2>/dev/null | head -1)
fi
if [ -z "$H2_JAR" ] || [ ! -f "$H2_JAR" ]; then
    echo "ERROR: h2.jar not found. Run: ./gradlew copyH2JarToBackup"
    exit 1
fi

cat > "$SQL_FILE" <<EOF
CALL CSVWRITE('${FOLDER}SERVICE_RECORD-${DATE}.csv', 'SELECT * FROM SERVICE_RECORD');
CALL CSVWRITE('${FOLDER}CUSTOMER-${DATE}.csv', 'SELECT * FROM CUSTOMER');
CALL CSVWRITE('${FOLDER}CUSTOMER_VEHICLE-${DATE}.csv', 'SELECT * FROM CUSTOMER_VEHICLE');
EOF

java -cp "$H2_JAR" org.h2.tools.RunScript \
  -url "jdbc:h2:file:./data/database;AUTO_SERVER=TRUE" \
  -user sa \
  -script "$SQL_FILE"

rm "$SQL_FILE"

echo "## Database dump created in: ${FOLDER}"
echo "  SERVICE_RECORD-${DATE}.csv"
echo "  CUSTOMER-${DATE}.csv"
echo "  CUSTOMER_VEHICLE-${DATE}.csv"
