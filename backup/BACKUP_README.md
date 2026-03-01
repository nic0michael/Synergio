# Database Backup Instructions

This folder contains scripts to export the H2 database tables into CSV format.
**The application (and database) must be running.**

## **Windows**

Run these batch files from the Command Prompt or PowerShell:

```cmd
backup\run_dump_customer.bat
backup\run_dump_customer_vehicle.bat
backup\run_dump_service_record.bat
```

## **Linux / macOS**

Run these shell scripts from the terminal:

```bash
chmod +x backup/*.sh
./backup/run_dump_customer.sh
./backup/run_dump_customer_vehicle.sh
./backup/run_dump_service_record.sh
```

## **Output**

The following CSV files will be generated in the `data/` folder:

*   `DB_DUMP_CUSTOMER.csv`
*   `DB_DUMP_CUSTOMER_VEHICLE.csv`
*   `DB_DUMP_SERVICE_RECORD.csv`
