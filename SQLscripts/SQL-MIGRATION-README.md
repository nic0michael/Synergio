# SQL Migration Guide

This guide explains how to migrate data from CSV files to the H2 database using the provided Java utility.

## Prerequisites

- Java Development Kit (JDK) installed.
- The `MigrateToSQL.java` file located in the `SQLscripts` folder.
- The CSV data files located in the `data` folder at the project root.

## 1. Generate SQL Scripts

The migration utility reads the CSV files and generates two SQL scripts: `schema.sql` (for table creation) and `data.sql` (for data insertion).

### Step 1: Compile the Utility

Open a terminal or command prompt in the **project root directory** and run the following command:

```bash
javac SQLscripts/MigrateToSQL.java
```

### Step 2: Run the Utility

Execute the utility from the **project root directory** (this is important so it can find the `./data/` folder):

```bash
java -cp SQLscripts MigrateToSQL
```

Upon successful execution, you will see a message indicating that `schema.sql` and `data.sql` have been generated in the `SQLscripts` folder.

## 2. Execute SQL Scripts on H2 Database

You can execute the generated scripts using the H2 Console or the H2 command-line tool.

### Option A: Using H2 Console (Web Interface)

1.  Start your Spring Boot application or the H2 Console directly.
2.  Open the H2 Console in your browser (usually at `http://localhost:8080/h2-console` if running Spring Boot).
3.  **JDBC URL**: Ensure this matches your application properties (e.g., `jdbc:h2:file:./data/database`).
4.  **User Name** and **Password**: Enter credentials as configured in your `application.properties`.
5.  **Run Schema Script**:
    - Copy the contents of `SQLscripts/schema.sql`.
    - Paste them into the SQL query area and click **Run**.
6.  **Run Data Script**:
    - Copy the contents of `SQLscripts/data.sql`.
    - Paste them into the SQL query area and click **Run**.

### Option B: Using H2 Command Line (Shell)

If you have the H2 jar file available, you can use the Shell tool:

```bash
java -cp h2.jar org.h2.tools.Shell -url jdbc:h2:file:./data/database -user sa -password "" -script SQLscripts/schema.sql
java -cp h2.jar org.h2.tools.Shell -url jdbc:h2:file:./data/database -user sa -password "" -script SQLscripts/data.sql
```
*(Adjust the jar path, URL, user, and password as necessary)*
