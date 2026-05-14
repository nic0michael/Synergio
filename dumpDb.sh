#!/bin/bash
DATE=$(date +%Y-%m-%d)
FOLDER="data/"
SQL_FILE=$(mktemp)

cat > "$SQL_FILE" <<EOF
CALL CSVWRITE('${FOLDER}SERVICE_RECORD-${DATE}.csv', 'SELECT * FROM SERVICE_RECORD');
CALL CSVWRITE('${FOLDER}CUSTOMER-${DATE}.csv', 'SELECT * FROM CUSTOMER');
CALL CSVWRITE('${FOLDER}CUSTOMER_VEHICLE-${DATE}.csv', 'SELECT * FROM CUSTOMER_VEHICLE');
EOF

java -cp h2.jar org.h2.tools.RunScript \
  -url "jdbc:h2:file:./data/database;AUTO_SERVER=TRUE" \
  -user sa \
  -script "$SQL_FILE"

rm "$SQL_FILE"

echo "## Database dump created in: ${FOLDER}"
echo "  SERVICE_RECORD-${DATE}.csv"
echo "  CUSTOMER-${DATE}.csv"
echo "  CUSTOMER_VEHICLE-${DATE}.csv"
