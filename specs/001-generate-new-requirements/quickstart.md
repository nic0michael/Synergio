# Quickstart: Testing Vehicle Views

## Prerequisites
- Application running on port 8088 (default).
- At least one Customer created.
- At least one Vehicle created and linked to that Customer (requires updated creation flow or manual CSV edit if existing data is present).

## Usage Steps

1. **Navigate to Search Page**
   - Go to `http://localhost:8088/getcustvehicles`
   - You should see a dropdown labeled "Select Customer".

2. **Select Customer**
   - Choose a customer from the list.
   - Click "Submit" (or similar button).

3. **View Results**
   - You will be redirected to the display page.
   - Verify the header shows "Customer: [Name]".
   - Verify the table lists their vehicles.

## Verification

- **Empty State**: Create a customer with no vehicles. Search for them. The table should be empty, but the name header should be correct.
- **Multiple Vehicles**: Ensure all vehicles for that ID appear.
