# Requirements Document: Synergeio Vehicle Service Station

## 1. Existing Requirements

### 1.1 Project Overview

**Project Name:** Synergeio Vehicle Service Station  
**Project Type:** Java Spring Boot Microservice  
**Primary Purpose:** Manage vehicle service records with CSV-based persistence  
**Architecture:** Model-View-Controller (MVC) with layered architecture

**Business Context:**
A lightweight web application for a vehicle service station to track customer vehicles, service appointments, materials, labor, and costs. The system provides urgency-based views to prioritize upcoming services.

Status:
DONE
---

### 1.2 Technical Stack

```
Framework:           Spring Boot 4.0.0-SNAPSHOT
Build Tool:          Gradle 8.x
Java Version:        JDK 21 (with Java 17 compatibility)
View Engine:         Thymeleaf
Web Framework:       Spring Web MVC
Development Tools:   Spring Boot DevTools, Lombok
Logging:             SLF4J with Logback
Testing:             JUnit 5, Spring Boot Test, Thymeleaf Test, WebMVC Test
Data Persistence:    CSV files (no database)
```

**Dependencies (from build.gradle):**
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

Status:
DONE
---

### 1.3 Domain Model Requirements

#### 1.3.1 ServiceRecord Entity

**Package:** `za.co.synergio.georgiou.model`  
**Purpose:** Represents a vehicle service record with customer, vehicle, and service details

**Field Specifications:**

| Field Name | Data Type | Annotation | Validation | Description |
|------------|-----------|------------|------------|-------------|
| `index` | `int` | None | Auto-generated unique ID | Unique identifier for record |
| `date` | `LocalDate` | `@DateTimeFormat(iso = ISO.DATE)` | None | Transaction/entry date (UTC) |
| `serviceDate` | `LocalDate` | `@DateTimeFormat(iso = ISO.DATE)` | None | Scheduled service date |
| `recurringDate` | `LocalDate` | `@DateTimeFormat(iso = ISO.DATE)` | None | Next recurring service date (auto-calculated) |
| `customerName` | `String` | None | Required (validated in controller) | Customer full name |
| `cellphone` | `String` | None | Optional | Customer contact number |
| `customerAddress` | `String` | None | Optional | Customer physical address |
| `vehicleMakeAnModel` | `String` | None | Optional | Vehicle make and model |
| `vehicleRegNumber` | `String` | None | Optional | Vehicle registration number |
| `colour` | `String` | None | Optional | Vehicle color |
| `odometerReading` | `String` | None | Optional | Current odometer reading |
| `vinNumber` | `String` | None | Optional | Vehicle identification number |
| `documentType` | `String` | None | Optional | Type of service document |
| `requirementCategory` | `String` | None | Optional | Category of service |
| `interval` | `int` | None | Default: 0 | Service interval in days |
| `daysLeft` | `int` | None | Auto-calculated | Days until service due (can be negative for overdue) |
| `materialsRequired` | `String` | None | Optional | Materials needed for service |
| `labourHours` | `double` | None | Default: 0.0 | Estimated labor hours |
| `amount` | `BigDecimal` | None | Default: 0 | Service cost amount |
| `breakdown` | `String` | None | Optional | Cost breakdown details |
| `jobsToDo` | `List<String>` | None | Optional | List of jobs/tasks to perform |
| `state` | `int` | None | Default: 0 | Record state: 0=active, 1=completed, 3=deleted |

**Business Rules:**
- Commas in text fields (`customerName`, `customerAddress`, `vehicleMakeAnModel`, `vinNumber`) are automatically replaced with semicolons to preserve CSV integrity
- `daysLeft` is calculated as: `ChronoUnit.DAYS.between(today, serviceDate)`
- `recurringDate` is calculated as: `date + interval` (when interval > 0)
- `amount` defaults to `BigDecimal.ZERO` if not provided
- Date calculations use UTC timezone (`ZoneOffset.UTC`)

**Methods:**
- Complete set of getters and setters
- `toString()` - Comprehensive string representation of all fields
- `replaceCommaWithSemicolon(String)` - Static utility method for CSV safety

Status:
DONE
---

### 1.4 Controller Layer Requirements

#### 1.4.1 MvcController

**Package:** `za.co.synergio.georgiou.controller`  
**Annotation:** `@Controller`  
**Dependencies:** `CsvStorage` (autowired via constructor injection)  
**Logging:** SLF4J Logger instance

**Endpoint Specifications:**

##### View Endpoints (Return HTML Pages)

| Method | Path | Parameters | Returns | Purpose |
|--------|------|------------|---------|---------|
| GET | `/` | None | `form.html` | Display new service record form with current UTC date |
| GET | `/editRecord` | `index` (int) | `edit.html` | Load existing record for editing by index |
| GET | `/records` | None | `records.html` | Display all service records |
| GET | `/40DAYS` | None | `40days.html` | Display records due within 40 days (1-40 days left) |
| GET | `/10DAYS` | None | `10days.html` | Display records due within 10 days (1-10 days left) |
| GET | `/today` | None | `today.html` | Display records due today or overdue by up to 3 days (-3 to 0 days left) |
| GET | `/help` | None | `help.html` | Display help/documentation page |

##### Action Endpoints (State Management)

| Method | Path | Parameters | Action | Redirect |
|--------|------|------------|--------|----------|
| POST | `/submit` | `ServiceRecord` (form data) | Create new record, save to CSV | `/` (form page) |
| POST | `/update` | `ServiceRecord` (form data) | Update existing record in CSV | `/` (form page) |
| GET | `/activateRecord` | `index` (int) | Set record state to 0 (active) | `/records` |
| GET | `/completeRecord` | `index` (int) | Set record state to 1 (completed) | `/records` |
| GET | `/deleteRecord` | `index` (int) | Set record state to 3 (soft-deleted) | `/records` |
| GET | `/publish` | None | Export current records to Published.csv | `/` |

##### REST API Endpoint

| Method | Path | Parameters | Returns | Purpose |
|--------|------|------------|---------|---------|
| GET | `/api/records` | None | JSON array | Return all service records as JSON |

**Business Logic in Controller:**

1. **Form Initialization (`/` endpoint):**
   - Creates new empty `ServiceRecord`
   - Sets `date` field to current UTC date
   - Adds to model and returns form view

2. **Edit Record Loading (`/editRecord` endpoint):**
   - Reads all records from CSV
   - Filters by index using stream
   - If found: returns existing record with index preserved
   - If not found: creates new record with current UTC date

3. **Record Submission (`/submit` endpoint):**
   - Validates: `date` must not be null, `customerName` must not be blank
   - Defaults `amount` to `BigDecimal.ZERO` if null
   - Calculates `daysLeft` from `serviceDate` if provided
   - Calculates `recurringDate` from `date + interval` if interval > 0
   - Saves record via `CsvStorage.save()`
   - Redirects to form page

4. **Record Update (`/update` endpoint):**
   - Same validation and calculation logic as submit
   - Updates existing record via `CsvStorage.update()`
   - Redirects to form page

5. **State Management (activate/complete/delete endpoints):**
   - Reads all records from CSV
   - Finds record by index
   - Updates state field (0=active, 1=completed, 3=deleted)
   - Saves via `CsvStorage.update()`
   - Redirects to records page

6. **Filtered Views:**
   - **40 Days:** `daysLeft < 41 AND daysLeft > 0`
   - **10 Days:** `daysLeft < 11 AND daysLeft > 0`
   - **Today:** `daysLeft < 1 AND daysLeft > -4`
   - Uses Java streams for filtering

7. **Publish:**
   - Calls `CsvStorage.publish()` to export to Published.csv
   - Redirects to form page

**Logging Requirements:**
- Log at INFO level for all endpoint method invocations
- Log record details for state changes (activate/complete/delete)
- Log full record toString() for edit and update operations

Status:
DONE
---

### 1.5 Data Persistence Layer Requirements

#### 1.5.1 CsvStorage Interface

**Package:** `za.co.synergio.georgiou.storage`

**Methods:**
```java
List<ServiceRecord> readAll() throws IOException;
void save(ServiceRecord record) throws IOException;
void update(ServiceRecord record) throws IOException;
List<ServiceRecord> readAllDueIn40Days() throws IOException;
void publish() throws IOException;
```

#### 1.5.2 CsvStorageImpl Implementation

**Package:** `za.co.synergio.georgiou.storage`  
**Annotation:** `@Component`  
**Thread Safety:** All read/write methods must be `synchronized`

**Configuration Properties:**
```properties
synergeio.folder.path      # Base folder for all CSV files
synergeio.csv.file         # Main CSV file name (service_records.csv)
synergeio.back.file        # Backup file name (service_records.backup)
synergeio.published.file   # Published export file name (Published.csv)
synergeio.index.file       # Index counter file name (index.counter)
synergeio.customers.file   # Customer file (future use)
synergeio.customer_vehicles.file  # Vehicle file (future use)
```

**CSV File Format:**
```csv
index,transactionDate,serviceDate,recurringDate,customerName,cellphone,customerAddress,vehicleMakeAndModel,vehicleRegNumber,colour,odometerReading,vinNumber,documentType,requirementCategory,interval,daysLeft,materialsRequired,labourHours,amount,breakdown,jobsToDo,state
```

**Implementation Requirements:**

1. **Constructor Initialization:**
   - Inject configuration properties via `@Value` annotations
   - Construct file paths using `Path.of(folderPath + fileName)`
   - Create directories if they don't exist (`Files.createDirectories()`)
   - Create CSV file with header if missing
   - Create backup file if missing

2. **save() Method:**
   - Synchronized operation
   - Generate next index via `getNextIndex()`
   - Append record to CSV file using `StandardOpenOption.APPEND`
   - Write record using `toCsvSave()` serialization
   - Add newline after record

3. **update() Method:**
   - Synchronized operation
   - Read all existing records
   - Find record by index and replace with updated version
   - Preserve all other records unchanged
   - Create backup before writing (`Files.copy()` with `REPLACE_EXISTING`)
   - Rewrite entire CSV file with header + all records
   - Use `StandardOpenOption.TRUNCATE_EXISTING`

4. **readAll() Method:**
   - Synchronized operation
   - Return empty list if file doesn't exist
   - Read all lines, skip header row
   - Parse each line using `fromCsvRead()`
   - Recalculate `daysLeft` for each record on read
   - Filter out null records (parse failures)

5. **publish() Method:**
   - Read all current records
   - Write to `publishedPath` with full header and all records
   - Use `StandardOpenOption.TRUNCATE_EXISTING`

6. **getNextIndex() Method:**
   - Read current index from `index.counter` file
   - If file doesn't exist, create with value "1" and return 1
   - Parse current value, increment by 1
   - Write incremented value back to file
   - Return incremented value for use

**CSV Serialization Logic:**

- **Quote Handling:** Fields containing commas, quotes, or newlines must be quoted
- **Escape Handling:** Double quotes inside fields must be doubled (`"` becomes `""`)
- **Newline Handling:** Replace `\n` with `\\n` in field values
- **List Serialization:** `jobsToDo` list is joined with semicolons (`;`)
- **Date Format:** ISO date format (`YYYY-MM-DD`)
- **Null Handling:** Null values serialize as empty strings

**CSV Parsing Logic:**

- **Custom Parser:** Implement `parseCsvLine()` to handle quoted fields correctly
- **Quote Awareness:** Track `inQuotes` state to handle commas inside quoted fields
- **Field Extraction:** Split on commas only when not inside quotes
- **Type Conversion:** 
  - `parseInt()` with fallback to 0
  - `parseDouble()` with fallback to 0.0
  - `parseBigDecimal()` with fallback to BigDecimal.ZERO
  - `parseDate()` using `LocalDate.parse()`
- **List Parsing:** Split `jobsToDo` field on semicolons
- **Days Left Recalculation:** Always recalculate on read using `ChronoUnit.DAYS.between(today, serviceDate)`

Status:
DONE
---

### 1.6 User Interface Requirements

#### 1.6.1 Page Structure

**Templates Location:** `src/main/resources/templates/`  
**Template Engine:** Thymeleaf  
**Static Resources:** `src/main/resources/static/`

**Pages Required:**

1. **form.html** - New service record entry form
2. **edit.html** - Edit existing service record form
3. **records.html** - View all service records table
4. **40days.html** - View records due within 40 days
5. **10days.html** - View records due within 10 days
6. **today.html** - View records due today/overdue
7. **calendar.html** - Calendar view (optional)
8. **help.html** - Help and documentation page

#### 1.6.2 Form Requirements (form.html & edit.html)

**Form Fields:**
- Date (LocalDate picker, ISO format)
- Service Date (LocalDate picker)
- Customer Name (text, required)
- Cellphone (text)
- Customer Address (textarea)
- Vehicle Make and Model (text)
- Vehicle Registration Number (text)
- Vehicle Colour (text)
- Odometer Reading (text)
- VIN Number (text)
- Document Type (text or select)
- Requirement Category (text or select)
- Interval (number, days)
- Materials Required (textarea)
- Labour Hours (number, decimal)
- Amount (number, decimal, currency)
- Breakdown (textarea)
- Jobs To Do (multiple input or textarea)

**Form Behavior:**
- Client-side validation using `form.js`
- Thymeleaf binding to `ServiceRecord` model
- Submit button posts to `/submit` (new) or `/update` (edit)
- Display validation errors from `BindingResult`

#### 1.6.3 Records View Requirements (records.html, 40days.html, 10days.html, today.html)

**Table Columns:**
- Index
- State (Active/Completed/Deleted)
- Transaction Date
- Service Date
- Days Left
- Customer Name
- Cellphone
- Vehicle Registration
- Vehicle Make/Model
- Amount
- Actions (Edit, Activate, Complete, Delete buttons)

**Table Behavior:**
- Display records in table format
- Color coding by urgency (CSS based on daysLeft)
- Action buttons with query parameters (e.g., `/editRecord?index=123`)
- Responsive table layout

**Navigation:**
- Links to all views (All Records, 40 Days, 10 Days, Today)
- Link to add new record (form page)
- Link to help page

#### 1.6.4 Static Resources

**CSS (static/css/style.css):**
- Global styles for application
- Form styling
- Table styling with alternating rows
- Responsive design breakpoints
- Color coding for urgency levels

**JavaScript (static/js/):**
- **form.js:** Client-side form validation, date pickers
- **calendar.js:** Calendar view interactions (if implemented)

Status:
DONE
---

### 1.7 Configuration Requirements

#### 1.7.1 Application Properties

**File:** `src/main/resources/application.properties`

**Required Properties:**
```properties
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=/path/to/data/folder/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.published.file=Published.csv
synergeio.index.file=index.counter
synergeio.customers.file=customers.csv
synergeio.customer_vehicles.file=customer_vehicles.csv
```

#### 1.7.2 Platform-Specific Paths

**Windows:**
```properties
synergeio.folder.path=C:/Users/username/Documents/Synergeio/
# OR with backslashes (escaped):
synergeio.folder.path=C:\\Users\\username\\Documents\\Synergeio\\
```

**Linux/macOS:**
```properties
synergeio.folder.path=/home/username/Documents/Synergeio/
```

**Path Handling Requirements:**
- Support forward slashes on all platforms
- Support backslashes (escaped) on Windows
- Create parent directories automatically if missing
- Use `Path.of()` for platform-independent path construction

#### 1.7.3 External Configuration Support

**Deployment Requirement:**
- Support external `application.properties` in same directory as JAR
- External config takes precedence over bundled config
- Allows environment-specific configuration without rebuilding

Status:
DONE
---

### 1.8 Build and Deployment Requirements

#### 1.8.1 Gradle Configuration

**File:** `build.gradle`

**Key Settings:**
```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.0.0-SNAPSHOT'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = 'https://repo.spring.io/snapshot' }
}
```

**Build Commands:**
```bash
# Development mode (with hot reload)
./gradlew bootRun

# Build JAR
./gradlew build

# Clean and build
./gradlew clean build

# Run tests
./gradlew test
```

#### 1.8.2 JAR Packaging

**Output Location:** `build/libs/synergeio-0.0.1-SNAPSHOT.jar`

**Packaging Requirements:**
- Executable JAR with embedded Tomcat
- Include all dependencies
- Bundle default `application.properties`
- Support external configuration override

**Build Scripts (for Linux/Mac):**
- `jar-make.sh` - Builds the JAR file
- `jar-copy.sh` - Copies JAR to deployment location and renames to `synergeio.jar`

#### 1.8.3 Deployment Instructions

**Windows:**
```powershell
# Create application folder
mkdir C:\Users\username\Documents\Synergeio

# Copy JAR and application.properties
# Edit application.properties with correct paths

# Run application
cd C:\Users\username\Documents\Synergeio
java -jar synergeio.jar
```

**Linux/macOS:**
```bash
# Create application folder
mkdir -p /home/username/Synergeio

# Copy JAR and application.properties
# Edit application.properties with correct paths

# Run application
cd /home/username/Synergeio
java -jar synergeio.jar
```

**Access Application:**
```
http://localhost:8088/
```

#### 1.8.4 Runtime Requirements

- Java Runtime Environment 17 or higher (JDK 21 recommended)
- Minimum 512MB RAM
- File system write permissions for data directory
- Port 8088 available (or configure alternative port)

Status:
DONE
---

### 1.9 Business Rules and Calculations

#### 1.9.1 Date Calculations

**Days Left Calculation:**
- Formula: `ChronoUnit.DAYS.between(LocalDate.now(ZoneOffset.UTC), serviceDate)`
- Calculated in controller before save/update
- Recalculated on every CSV read
- Positive value = future date
- Zero = due today
- Negative value = overdue

**Recurring Date Calculation:**
- Formula: `date + interval` (in days)
- Only calculated if `interval > 0`
- Applied during submit and update

**Timezone:**
- All date operations use UTC (`ZoneOffset.UTC`)
- Consistent across all date handling

#### 1.9.2 State Management

**State Values:**
- `0` = Active (default for new records)
- `1` = Completed (service finished)
- `3` = Deleted (soft-deleted, retained in CSV)

**State Transitions:**
- Any state → Active (0) via `/activateRecord`
- Any state → Completed (1) via `/completeRecord`
- Any state → Deleted (3) via `/deleteRecord`

**Display Rules:**
- All states visible in main records view
- Deleted records may be hidden or styled differently (implementation choice)

#### 1.9.3 Filtering Logic

**40-Day View:**
- Include records where: `1 <= daysLeft <= 40`
- Excludes overdue and records due beyond 40 days

**10-Day View:**
- Include records where: `1 <= daysLeft <= 10`
- High urgency view

**Today View:**
- Include records where: `-3 <= daysLeft <= 0`
- Includes today and up to 3 days overdue

#### 1.9.4 Data Validation

**Required Fields:**
- `date` (transaction date)
- `customerName`

**Default Values:**
- `amount`: `BigDecimal.ZERO` if null
- `state`: 0 (active) for new records
- `daysLeft`: recalculated on read

**Field Sanitization:**
- Commas replaced with semicolons in: `customerName`, `customerAddress`, `vehicleMakeAnModel`, `vinNumber`
- Prevents CSV parsing errors

Status:
DONE
---

### 1.10 Non-Functional Requirements

#### 1.10.1 Performance

- CSV read operations: < 500ms for files up to 10,000 records
- Form submission: < 1 second response time
- Page load: < 2 seconds
- Support up to 10 concurrent users

#### 1.10.2 Scalability

- Maximum 100,000 records per CSV file
- CSV file size limit: 50MB
- Backup retention: minimum 1 backup file
- Consider database migration if scale exceeds these limits

#### 1.10.3 Reliability

- Atomic write operations (synchronized methods)
- Backup before destructive operations
- Index counter persistence
- Graceful handling of file I/O errors

#### 1.10.4 Usability

- Intuitive navigation between views
- Clear urgency indicators (color coding)
- Responsive form validation with error messages
- Help documentation available

#### 1.10.5 Maintainability

- Clean code principles (per constitution)
- Self-documenting code
- SLF4J logging for troubleshooting
- Layered architecture for separation of concerns

#### 1.10.6 Logging Standards

**Framework:** SLF4J with Logback (Spring Boot default)

**Logging Requirements:**
- Log at INFO level for all controller method invocations
- Log method entry with method name
- Log successful operations with relevant details
- Log errors before throwing exceptions
- Include record details in logs for state changes

**Example Log Statements:**
```java
log.info("form method called");
log.info("Found and update record " + index + "\n" + serviceRecord);
log.info("submit method called");
```

Status:
DONE
---

### 1.11 Testing Requirements

#### 1.11.1 Test Framework

**Test Dependencies:**
- Spring Boot Test Starter
- Thymeleaf Test Support
- WebMVC Test Support
- JUnit Platform Launcher

**Test Location:** `src/test/java/za/co/synergio/georgiou/`

#### 1.11.2 Test Strategy

**Per Constitution:**
- Follow Test-Driven Development (TDD)
- Write failing tests before implementation
- Use real stub classes instead of Mockito
- Keep tests simple and focused

**Test Types:**

**Unit Tests:**
- Model validation and business logic
- CSV serialization/deserialization
- Date calculations
- State transitions

**Integration Tests:**
- Controller endpoint behavior
- CSV storage operations
- Form submission workflows

**Web MVC Tests:**
- HTTP request/response handling
- Model attribute binding
- View resolution
- Redirect behavior

Status:
DONE
---

### 1.12 Project Structure

```
Synergio/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── za/co/synergio/georgiou/
│   │   │       ├── SynergeioApplication.java          # Main Spring Boot entry point
│   │   │       ├── controller/
│   │   │       │   └── MvcController.java             # Web + REST controller
│   │   │       ├── model/
│   │   │       │   ├── ServiceRecord.java             # Main domain entity
│   │   │       │   ├── Customer.java                  # Placeholder for future
│   │   │       │   └── CustomerVehicle.java           # Placeholder for future
│   │   │       └── storage/
│   │   │           ├── CsvStorage.java                # Storage interface
│   │   │           └── CsvStorageImpl.java            # CSV implementation
│   │   └── resources/
│   │       ├── application.properties                 # Configuration
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                      # Global styles
│   │       │   ├── image/                             # Images directory
│   │       │   └── js/
│   │       │       ├── calendar.js                    # Calendar functionality
│   │       │       └── form.js                        # Form validation
│   │       └── templates/
│   │           ├── form.html                          # New record form
│   │           ├── edit.html                          # Edit record form
│   │           ├── records.html                       # All records view
│   │           ├── 40days.html                        # 40-day urgency view
│   │           ├── 10days.html                        # 10-day urgency view
│   │           ├── today.html                         # Today/overdue view
│   │           ├── calendar.html                      # Calendar view
│   │           └── help.html                          # Help page
│   └── test/
│       └── java/
│           └── za/co/synergio/georgiou/
│               └── SynergeioApplicationTests.java     # Test suite
├── data/                                              # Runtime data directory
│   └── records.csv                                    # Generated CSV file
├── build.gradle                                       # Gradle build configuration
├── settings.gradle                                    # Gradle settings
├── gradlew                                            # Gradle wrapper (Unix)
├── gradlew.bat                                        # Gradle wrapper (Windows)
├── jar-make.sh                                        # Build script
├── jar-copy.sh                                        # Deployment script
├── README.md                                          # Documentation
├── CorporatePolicyV2_0.md                            # Corporate policies
└── .specify/                                          # Spec-Kit configuration
    └── memory/
        └── constitution.md                            # Project constitution
```

Status:
DONE
---

### 1.13 Success Criteria

#### 1.13.1 Functional Success Criteria

- ✅ Users can create, read, update service records via web forms
- ✅ CSV persistence maintains data across application restarts
- ✅ Days-left calculations update dynamically on every read
- ✅ Filtering views (40-day, 10-day, today) display correct subsets
- ✅ State management (activate, complete, delete) works correctly
- ✅ Backup mechanism prevents data loss
- ✅ Application runs on Windows, Linux, and macOS
- ✅ External configuration works for deployment flexibility

#### 1.13.2 Non-Functional Success Criteria

- ✅ Application starts within 10 seconds
- ✅ JAR file under 50MB
- ✅ Zero external database dependencies
- ✅ Responsive UI works in Chrome, Firefox, Edge
- ✅ Code follows constitution principles (clean code, KISS, TDD)
- ✅ SLF4J logging provides adequate debugging information
- ✅ Thread-safe CSV operations via synchronized methods

Status:
DONE
---

### 1.14 Implementation Guidelines

**Constitution Compliance:**

This project must be implemented following the principles defined in the project constitution:

1. **Logging Standards (MANDATORY):**
   - Log errors before throwing exceptions
   - Log info on successful method completion
   - Use SLF4J framework consistently

2. **KISS Philosophy (CRITICAL):**
   - Avoid over-engineering
   - Keep solutions simple and maintainable
   - Justify any complexity

3. **Test-Driven Development (NON-NEGOTIABLE):**
   - Write failing tests first
   - Use real stub classes (not Mockito)
   - Follow Red-Green-Refactor cycle

4. **Clean Code (HIGHEST PRIORITY):**
   - Small, focused methods
   - Meaningful names
   - No code duplication
   - Only Javadoc comments allowed
   - No inline comments

5. **Self-Documenting Code (MANDATORY):**
   - Clear, descriptive class/method/variable names
   - Code readable without comments

Status:
DONE
---

### 1.15 Glossary

- **CSV**: Comma-Separated Values file format for data persistence
- **ServiceRecord**: Primary domain entity representing a vehicle service appointment
- **State**: Record lifecycle status (0=active, 1=completed, 3=deleted)
- **Days Left**: Calculated field showing days until service due date (negative = overdue)
- **Recurring Date**: Automatically calculated next service date based on interval
- **Index**: Unique identifier for each service record (auto-incremented from counter file)
- **Soft Delete**: Marking record as deleted (state=3) without physical removal from CSV
- **UTC**: Coordinated Universal Time used for all date calculations
- **MVC**: Model-View-Controller architectural pattern
- **Thymeleaf**: Java-based template engine for server-side HTML rendering
- **SLF4J**: Simple Logging Facade for Java
- **Gradle**: Build automation tool for Java projects
- **KISS**: Keep It Simple Stupid - principle of simplicity in design
- **TDD**: Test-Driven Development methodology

Status:
DONE
---

## 2. New Requirements

### 2.1 make powershell scripts
1. Use the Linux/Mac shell scripts:
 - jar-make.sh
 - jar-copy.sh
2. Make equilalent Power Shell Scripts with similar names
Status:
DONE
---
### 2.2 add REST methods to MvcController.java
#### 2.2.2 Customer fields
1. add /createcustomer restmethod
 - This should direct request to cretecustomer.html
 - This form should submit to an empty restmethod saveCustomer
 - Dont create unit tests yet
2. add /editcustomer restmethod
 - This should direct request to editcustomer.html
 - This form should submit to an empty restmethod saveCustomer
 - Dont create unit tests yet
Status:
DONE
---

#### 2.2.2 CustomerVehicle fields
1. add /createvehicle restmethod
 - This should direct request to cretevehicle.html
 - This form should submit to an empty restmethod saveCustomer
 - Dont create unit tests yet
2. add /editvehicle restmethod
 - This should direct request to editvehicle.html
 - This form should submit to an empty restmethod saveCustomer
 - Dont create unit tests yet
Status:
DONE
---

### 2.2 Add CSV_HEADER strings in CsvStorageImpl
add header strings like this one:
private static final String CSV_HEADER = String.join(",",

#### 2.2.1 for Customer
- call this CUST_CSV_HEADER

#### 2.2.2 for CustomerVehicle
- call this VEH_CSV_HEADER

Status:
DONE
---

### 2.3 Add CSV save methods to CsvStorageImpl
add methods similar to:
private String toCsvSave(ServiceRecord r) {

#### 2.3.1 for Customer
- call this toCsvSaveCustomer(Customer c)

#### 2.3.2 for CustomerVehicle
- call this toCsvSaveVehicle(CustomerVehicle v) 
Status:
DONE
---

### 2.4 add save methods
add methods similat to:
public synchronized void save(ServiceRecord record) throws IOException {

#### 2.4.1 for Customer
- call this saveCustomer(Customer c)

#### 2.4.2 for CustomerVehicle
- call this saveVehicle(CustomerVehicle v) 

### 2.4.3 Add these methods to the Interface CsvStorage
Status:
DONE
---


### 2.5 add save methods
add methods similat to:
public synchronized void update(ServiceRecord record) throws IOException {

#### 2.5.1 for Customer
- call this updateCustomer(Customer c)

#### 2.5.2 for CustomerVehicle
- call this updateVehicle(CustomerVehicle v) 

### 2.5.3 Add these methods to the Interface CsvStorage
Status:
DONE
---


### 2.6 add listAll methods to MvcController
add methods similat to:
public String records(Model model) throws IOException {

#### 2.6.1 for Customer
- call this customerRecords(Model model) throws IOException {
   1. that calls this method in CsvStorageImpl:
   public synchronized List<Customer> readAllCustomers() throws IOException {
   2. that then calls HTML page named: customers.html


#### 2.6.2 for CustomerVehicle
- call this vehicleRecords(Model model) throws IOException {
   1. that calls this method in CsvStorageImpl:
   public synchronized List<CustomerVehicle> readAllVehicles() throws IOException {
   2. that then calls HTML page named: vehicles.html

Status:
DONE
---


### 2.7 rename vehicle and customer records HTML pages
add methods similat to:
public String records(Model model) throws IOException {

#### 2.6.1 for Customer
- Rename: customers.html to recordsOfCustomers


#### 2.6.2 for CustomerVehicle
- Rename: vehicles.html to recordsOfVehicles

Status:
DONE
---

### 2.8 add Select methods to MvcController

#### 2.8.1 for Customer
- The method should return an array with options and values
   1. the options should be Customer Names: customerName
   2. the values should be index numbers : index
   3. the method name should be listCustomerOptions

#### 2.8.2 for CustomerVehicle
- The method should return an array with options and values
   String vehicleOption = customerName+" "+vehicleRegNumber+" "+vehicleMakeAnModel+" "+colour;
   1. the options should be CustomerVehicles: vehicleOption
   2. the values should be index numbers:  index
   3. the method name should be listVehicleOptions

Status:
DONE
---
### 2.9 Add to help.html
#### Add 
- add all html pages categorized to help.html
- short explanation onle line what they do
- add a link to each page

Status:
DONE
---
### 2.10 for getting searched records in MvcController

#### 2.10.1 for searching for a Customer
- add a method searcForCustomer(int index) to MvcController
- this returns Customer
- this calls CsvStorageImpl method  List<Customer> readAllCustomers() to get a list of Customers
-  this iterates through the list to find record with matching index and returns that record

#### 2.10.2 for searching for a CustomerVehicle record
- add a method searchForCustomerVehicle(int index) to MvcController
- this returns Customer
- this calls CsvStorageImpl method  List<CustomerVehicle> readAllVehicles() to get a list of CustomerVehicles
- then this iterates through the list to find record with matching index and returns that record

Status:
DONE
---

### 2.11 add list option dropdowns to Edit pages

#### 2.11.1 for Customer createcustomer.html
- add a dropdown to select a existing customer
- this is generated by calling MvcController method:
  public List<Map<String, String>> listCustomerOptions() 

- Using the index from this to find record call MvcController method: Customer searchForCustomer(@RequestParam("index") int index)
- use this record to replace the customerName input 
- use this record to replace the customerAddress input 
- use this record to replace the customeCellphone input
- next to this dropdown add a button with label "Add New Vehicle" linked to /createcustomer


#### 2.11.2 for CustomerVehicle createvehicle.html

#### 2.11.2.1 for Vehicle fields
- add a dropdown to select a existing customer
- this is generated by calling MvcController method:CustomerVehicle searchForCustomerVehicle(@RequestParam("index") int index
  public List<Map<String, String>> listCustomerOptions() 

- Using the index from this to find record call MvcController method:
- use this record to replace the vehicleRegNumber input 
- use this record to replace the vehicleMakeAnModel input 
- use this record to replace the colour input
- use this record to replace the vinNumber input
- next to this dropdown add a button with label "Add New Vehicle" linked to /createvehicle


#### 2.11.2.2 for Customer fields
- add a dropdown to select a existing customer
- this is generated by calling MvcController method:
  public List<Map<String, String>> listCustomerOptions()   

- Using the index from this to find record  call MvcController method: Customer searchForCustomer(@RequestParam("index") int index)
- use this record to replace the customerName input 
- use this record to replace the customerAddress input 
- use this record to replace the customeCellphone input
- next to this dropdown add a button with label "Add New Vehicle" linked to /createcustomer

Status:
DONE
---

### 2.12 add Profile page
- in the MvcController create a new REST method /profile
- use a  @GetMapping
- it must redirect the user to a html page profile.html
- generate the page profile.html
- it must have button links to
  * "/createcustomer" with lable Add New Customer
  * "/customerRecords" with label List Customers
  * "/createvehicle" with lable Add New Vehicle
  * "/vehicleRecords" with label List Vehicles


#### 2.12.2 add Profile page 
- in the help.html page add the profile on the top 
Status:
DONE
---

## 3. Latest Requirements
### 3.1 we need to show the vehicles of a customer
#### 3.1.1 Retrieve List of Customers Cars from CsvStorageImpl
In class CsvStorageImpl create a method to retrieve the vechiles of a Customer
List<CustomerVehicle> getCustomerVehicles(int customerId)
Status:
TO DO
#### 3.1.2 Retrieve List of Customers Cars from MvcController
In class MvcController create a method to retrieve the vechiles of a Customer
List<CustomerVehicle> getCustomerVehicles(int customerId)
This must be a REST method ("/vehicleRecords with a @RequestParam("CustomerId") int CustomerId)  
this method should call CsvStorageImpl method: getCustomerVehicles and pass CustomerId 
Status:
TO DO

#### 3.1.3 Retrieve List of Customers Cars from Webpage 
Create a new HTML template: getcustvehicles.html
This page should call  MvcController and get a Dropdown list of customers with message Select Customer
it should then call MvcController method: getCustomerVehicles and pass the customers Id 
the MVC controller should redirect to a template displaycustvehicles

create a new HTML template: displaycustvehicles.html
it then should display a table with these fields:
    private String vehicleRegNumber;
    private String vehicleMakeAnModel;
    private String colour;
It should display:  private String customerName; on top of this table
Status:
TO DO

**Document Version:** 1.0  
**Created:** December 4, 2025  
**Purpose:** Complete technical specification to rebuild Synergeio Vehicle Service Station  
**Compliance:** Adheres to CorporatePolicyV2_0.md and project constitution

Status:
DONE
---
