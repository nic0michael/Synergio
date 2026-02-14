# Quickstart: Testing the New Requirements

## 1. Prerequisites
- Ensure the application is running (`./gradlew bootRun`).
- Ensure `data/customers.csv` and `data/customer_vehicles.csv` exist and contain sample data.

## 2. Testing /customervehiclesbycust

### URL
`http://localhost:8080/customervehiclesbycust?customerId=<ID>`

### Steps
1.  Open browser.
2.  Navigate to `http://localhost:8080/customervehiclesbycust?customerId=1` (replace `1` with a valid customer ID).
3.  **Expected**: Page loads displaying a table of vehicles associated with Customer 1.

## 3. Testing /customerbycust

### URL
`http://localhost:8080/customerbycust?customerId=<ID>`

### Steps
1.  Open browser.
2.  Navigate to `http://localhost:8080/customerbycust?customerId=1`.
3.  **Expected**: Page loads displaying details (Name, Address, Cell) for Customer 1.

## 4. Verification Data

**Sample Customer (ID 1):**
- Name: John Doe
- Address: 123 Main St
- Cell: 0821234567

**Sample Vehicle (linked to ID 1):**
- Reg: ABC 123 GP
- Make: Toyota Corolla
