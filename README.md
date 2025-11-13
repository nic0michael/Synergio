# Synergeio Vehicle Service Station

A lightweight **Spring Boot (Gradle)** microservice for managing vehicle service records in a CSV file with a simple **Thymeleaf frontend**.

---

## 🧑‍💻 Developer Instructions

### 0. Install Java JDK 21

**[Windows Java JDK 21](https://download.oracle.com/java/25/latest/jdk-25_windows-x64_bin.msi)**

**[Linux Java JDK 21](https://www.oracle.com/africa/java/technologies/downloads/#jdk25-linux)**

**[Mac Java JDK 21](https://www.oracle.com/africa/java/technologies/downloads/#jdk25-mac)**

#### Install Git Bash
**[Git for Windows/x64 Setup](https://github.com/git-for-windows/git/releases/download/v2.51.2.windows.1/Git-2.51.2-64-bit.exe)**

### 1. Project Structure
```
SynergeioApplication.java            # Main Spring Boot entry point
src/main/java/za/co/synergio/georgiou
├── controller/MvcController.java    # Handles HTML and API routes
├── model/ServiceRecord.java         # POJO for CSV record mapping
├── storage/CsvStorage.java          # Interface for CSV operations
├── storage/CsvStorageImpl.java      # Implementation of CSV read/write

src/main/resources
├── templates/                       # Thymeleaf HTML templates
│   ├── form.html
│   ├── records.html
│   └── help.html
├── static/css/style.css             # Global styles
├── static/js/form.js                # Client-side validation
├── static/js/records.js            # Dynamic calendar view
└── application.properties           # Spring config

service_records.csv                  # Generated CSV persistence file
```
### 2. Setting the Path to write the CSV file
1. In the folder: /src/main/resources you will find a file: application.properties
2. Change this line: synergeio.csv.path=/home/nickm/Documents/MyRequirement/service_records.csv
3. Change this line: synergeio.back.path=/home/nickm/Documents/MyRequirement/service_records.backup

**Currenly they are set to save on my Linux or Mac machines for windows use:**

1. synergeio.csv.path=C:\\Users\\nickm\\Documents\\MyRequirement\\service_records.csv
2. synergeio.back.path=C:\\Users\\nickm\\Documents\\MyRequirement\\service_records.backup
Like this:
synergeio.csv.path=C:\\Users\\nico\\Documents\\NikisDocs\\service_records.csv
synergeio.back.path=C:\Users\\nico\\Documents\\NikisDocs\\service_records.backup

### 3. Creating and copying the JAR file (Using Linux / Mac Shell script)
For Windows users run the commands manually in Power Shell
#### Do not build if you have not changed the paths as per previous instruction

**We have created two shell scripts :**
1. jar-make.sh This makes the JAR file in build/libs folder as: Synergeío-0.0.1-SNAPSHOT.jar
2. jar-copy.sh This copied the above JAR file as: synergio.jar


### 4. Running the JAR file you just built
**Run this command:**
```sh
java -jar synergio.jar
```
### 5. Dependencies Installation
```bash
./gradlew build
```
> **Optional:** If front-end assets use any npm packages, run `npm install` inside the project root.

### 6. CSV Persistence
- All records are stored in a simple CSV file: `service_records.csv`.
- The file is auto-created in the working directory if missing.
- Each row corresponds to one customer record (Date, Customer Name, Vehicle Info, etc.).
- The `CsvStorageImpl` class handles reading, writing, and appending records.

### 7. Run the Application Locally
```bash
./gradlew bootRun
```
Then open your browser at:
```
http://localhost:8080/
```
---

## 🚀 Deployment Instructions

### Local JAR Deployment (Or use the developers shell scripts)
1. Build the executable JAR:
   ```bash
   ./gradlew clean build
   ```
2. Run the JAR file:
   ```bash
   java -jar build/libs/synergeio-0.0.1-SNAPSHOT.jar
   ```
3. Access the web interface at `http://localhost:8080`.

### Windows or macOS
- Ensure **Java 21+** is installed and available in your system PATH.
- On **Windows**, use PowerShell:
  ```powershell
  java -jar build\libs\synergeio-0.0.1-SNAPSHOT.jar
  ```
- On **macOS/Linux**, use:
  ```bash
  java -jar build/libs/synergeio-0.0.1-SNAPSHOT.jar
  ```

### Environment Setup
No database is required. The application stores everything in a CSV file. Ensure write permissions are available in the working directory.

---

## 👨‍🏭 User Instructions

### Accessing the Application
1. Visit `http://localhost:8080` after running the app.
2. Use the navigation bar to switch between **Form**, **Calendar**, and **Help** pages.

### Submitting Customer Requirements
- Fill in all required fields on the **Form** page.
- Select the **Document Type** (Quotation or Invoice) and **Category** (Oil Change, Service, etc.).
- Click **Save Record** to store the entry.
- A new line will be appended to `service_records.csv`.

### Viewing Records 
- Go to the **View Records** tab.
- The newest records appear on top

### CSV File Updates
- Each submission immediately updates `service_records.csv`.
- The file can be opened manually in Excel or any text editor.

---

**© 2025 Synergeio Vehicle Service Station**
