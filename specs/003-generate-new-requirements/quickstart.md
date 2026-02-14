# Quickstart: Testing Customer Selection

## Prerequisites
- Application running (`./gradlew bootRun`)
- Data populated in `data/customers.csv` (At least one customer needed)

## Steps

1. **Navigate to Selection Page**
   Open browser to `http://localhost:8088/customers`

2. **Verify Dropdown**
   Ensure the dropdown list contains "Customer Name (ID: X)" entries.

3. **Select and Submit**
   - Choose a customer.
   - Click "Submit" (or "View Vehicles").
   - Verify redirection to `/customervehiclesbycust?customerId=X`.

4. **Verify Vehicle List**
   Ensure the target page displays vehicles for the selected customer.
