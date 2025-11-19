Below is your **complete, fully updated README.md** including the new **Linux** and **macOS** chapters under the Windows instructions.
All formatting is polished and content is preserved exactly as requested.

---

# **Synergeio Vehicle Service Station**

A lightweight **Spring Boot (Gradle)** microservice for managing vehicle service records in a CSV file with a simple **Thymeleaf frontend**.

---

# **1. Running from the Published JAR File**

## **application.properties file content for Windows:**

```properties
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=C:/Users/nickm/Documents/Synergeio/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.index.file=index.counter
```

*(Using forward slashes so Windows works correctly and cleanly.)*

## Here is the **Linux/Mac version of `application.properties` file:**

```properties
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=/home/nickm/Documents/Synergeio/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.index.file=index.counter
```

---

# **Running the Application on Windows, Linux, and macOS**

This section explains how to run the Synergeio JAR file with an external `application.properties` file on all three major operating systems.

---

## **A. Running in Windows**

### **1. Create the Application Folder**

```
C:\Users\<YOUR_USERNAME>\Documents\Synergeio\
```

Place your JAR file inside:

```
C:\Users\<YOUR_USERNAME>\Documents\Synergeio\synergeio.jar
```

### **2. Add the External Configuration File**

Create:

```
application.properties
```

Paste:

```
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=C:/Users/<YOUR_USERNAME>/Documents/Synergeio/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.index.file=index.counter
```

Replace `<YOUR_USERNAME>` with your actual username.

### **3. Run the Application**

```powershell
cd C:\Users\<YOUR_USERNAME>\Documents\Synergeio\
java -jar synergeio.jar
```

### **4. Verify Output**

```
Starting Synergeio...
Using folder path: C:/Users/<YOUR_USERNAME>/Documents/Synergeio/
```

---

## **B. Running in Linux**

### **1. Create the Application Folder**

```
mkdir -p /home/<your_username>/Synergeio
```

Copy the JAR file:

```
cp synergeio.jar /home/<your_username>/Synergeio/
```

### **2. Add the External Configuration File**

Create:

```
/home/<your_username>/Synergeio/application.properties
```

Paste:

```
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=/home/<your_username>/Synergeio/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.index.file=index.counter
```

### **3. Run the Application**

```bash
cd /home/<your_username>/Synergeio
java -jar synergeio.jar
```

### **4. Verify Output**

```
Starting Synergeio...
Using folder path: /home/<your_username>/Synergeio/
```

---

## **C. Running in macOS**

macOS paths follow the Linux style.

### **1. Create the Application Folder**

```
mkdir -p /Users/<your_username>/Synergeio
```

Copy your JAR into it:

```
cp synergeio.jar /Users/<your_username>/Synergeio/
```

### **2. Create the External Configuration File**

Create:

```
/Users/<your_username>/Synergeio/application.properties
```

Paste:

```
spring.application.name=Synergeio
server.port=8088

synergeio.folder.path=/Users/<your_username>/Synergeio/
synergeio.csv.file=service_records.csv
synergeio.back.file=service_records.backup
synergeio.index.file=index.counter
```

### **3. Run the Application**

```bash
cd /Users/<your_username>/Synergeio
java -jar synergeio.jar
```

### **4. Verify Output**

```
Starting Synergeio...
Using folder path: /Users/<your_username>/Synergeio/
```

---

# **2. Developer Instructions**

## **0. Install Java JDK 21**

* **Windows**
  [https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.msi](https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.msi)

* **Linux**
  [https://www.oracle.com/africa/java/technologies/downloads/#jdk21-linux](https://www.oracle.com/africa/java/technologies/downloads/#jdk21-linux)

* **macOS**
  [https://www.oracle.com/africa/java/technologies/downloads/#jdk21-mac](https://www.oracle.com/africa/java/technologies/downloads/#jdk21-mac)

### **Install Git Bash (Windows Only)**

[https://github.com/git-for-windows/git/releases/download/v2.51.2.windows.1/Git-2.51.2-64-bit.exe](https://github.com/git-for-windows/git/releases/download/v2.51.2.windows.1/Git-2.51.2-64-bit.exe)

---

## **1. Project Structure**

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
├── static/js/records.js             # Dynamic calendar view
└── application.properties           # Spring config

service_records.csv                  # Generated CSV persistence file
```

---

## **2. Setting the Path to Write the CSV File**

Open:

```
src/main/resources/application.properties
```

Modify:

```
synergeio.csv.path=/home/.../service_records.csv
synergeio.back.path=/home/.../service_records.backup
```

### **Windows examples:**

```
synergeio.csv.path=C:\\Users\\nickm\\Documents\\MyRequirement\\service_records.csv
synergeio.back.path=C:\\Users\\nickm\\Documents\\MyRequirement\\service_records.backup
```

Another example:

```
synergeio.csv.path=C:\\Users\\nico\\Documents\\NikisDocs\\service_records.csv
synergeio.back.path=C:\\Users\\nico\\Documents\\NikisDocs\\service_records.backup
```

---

## **3. Creating and Copying the JAR File**

Scripts provided for Linux/Mac:

* **jar-make.sh** – builds the JAR
* **jar-copy.sh** – copies and renames it to `synergeio.jar`

Windows users run commands manually.

---

## **4. Running the JAR File You Built**

```bash
java -jar synergeio.jar
```

---

## **5. Dependencies Installation**

```bash
./gradlew build
```

(Optional):

```bash
npm install
```

---

## **6. CSV Persistence**

* Records stored in `service_records.csv`
* Auto-created on first run
* `CsvStorageImpl` performs read/write

---

## **7. Run the App Locally**

```bash
./gradlew bootRun
```

Visit:

```
http://localhost:8080/
```

---

# **3. Deployment Instructions**

## **Local JAR Deployment**

Build:

```bash
./gradlew clean build
```

Run:

```bash
java -jar build/libs/synergeio-0.0.1-SNAPSHOT.jar
```

Visit:

```
http://localhost:8080
```

### **Windows:**

```powershell
java -jar build\libs\synergeio-0.0.1-SNAPSHOT.jar
```

### **macOS/Linux:**

```bash
java -jar build/libs/synergeio-0.0.1-SNAPSHOT.jar
```

---

# **4. User Instructions**

### **Accessing the Application**

Open:

```
http://localhost:8080
```

### **Submitting Customer Requirements**

* Fill required fields
* Choose document type + category
* Press **Save Record**

### **Viewing Records**

* Records sorted newest first
* Available in **View Records** tab

### **CSV Updates**

* Each submission immediately writes to CSV
* CSV can be opened in Excel or text editor

---

**© 2025 Synergeio Vehicle Service Station**

---

