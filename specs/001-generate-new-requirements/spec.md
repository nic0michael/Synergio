# Feature Specification: Show Customer Vehicles

**Feature Branch**: `001-generate-new-requirements`
**Created**: 2026-02-12
**Status**: Draft
**Input**: User description: "we want to creat the code in this project as specified by Requirements.md"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - View Customer Vehicles (Priority: P1)

As a service station manager, I want to select a customer and view all their registered vehicles so that I can quickly reference their fleet.

**Why this priority**: Core functionality to link customers to vehicles implicitly required for service management.

**Independent Test**: Can be tested by navigating to the get-customer-vehicles page, selecting a customer, and verifying the redirected page shows the correct vehicle list.

**Acceptance Scenarios**:

1. **Given** the manager is on the `getcustvehicles.html` page, **When** they select a customer from the dropdown and submit, **Then** they are redirected to `displaycustvehicles.html` showing a table of that customer's vehicles.
2. **Given** the manager is on the vehicle display page, **When** keeping the page open, **Then** the page header displays the selected Customer's Name.
3. **Given** the system has vehicles for a customer (e.g., CustomerID=1), **When** `getCustomerVehicles(1)` is called on `CsvStorageImpl`, **Then** it returns a `List<CustomerVehicle>` containing only vehicles for that customer.

### Edge Cases

- **Empty State**: If a customer exists but has no vehicles, display the `displaycustvehicles.html` page with the correct Customer Name header, an empty table, and a visible "No vehicles found" message.
- **Invalid ID**: If the requested `CustomerId` does not correspond to an existing customer, redirect the user back to the search page (`getcustvehicles.html`) and display a "Customer not found" error message.

## Clarifications

### Session 2026-02-12
- Q: What happens if a customer has no vehicles? → A: Display the customer name with empty table rows and a distinct "No vehicles found" message.
- Q: What happens if the customer ID is invalid? → A: Redirect the user back to the search page (`getcustvehicles.html`) with a flash error message "Customer not found".
- Q: How should legacy CSV rows without `customerId` be handled? → A: Treat them as "Unassigned" (Default `customerId` to 0).
- Q: How should the customer dropdown be sorted? → A: Alphabetically by Customer Name (A-Z).
- Q: Should the "Create Vehicle" form be updated to support the new `customerId`? → A: Yes, update the form to save the `customerId`.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a web page `getcustvehicles.html` with a dropdown of existing customers, **sorted alphabetically by Customer Name**.
- **FR-002**: System MUST provide a `CsvStorageImpl` method to retrieve vehicles filtered by customer ID.
- **FR-003**: System MUST provide an `MvcController` endpoint to handle the vehicle retrieval request and return the `displaycustvehicles` view.
- **FR-004**: System MUST display the list of vehicles (Registration, Make/Model, Colour) and the Customer Name on `displaycustvehicles.html`.
- **FR-005**: The existing "Create Vehicle" functionality MUST be updated to include a Customer selection dropdown and save the selected `customerId` to the new `CustomerVehicle` record.

### Technical Requirements (from Requirements.md Section 3.1)

- **TR-001**: `CsvStorageImpl` method signature: `List<CustomerVehicle> getCustomerVehicles(int customerId)`.
- **TR-002**: `MvcController` method signature: `getRequestVehicles(@RequestParam("CustomerId") int CustomerId)` (inferred).
- **TR-003**: HTML Template `getcustvehicles.html` must perform a GET request to the controller.
- **TR-004**: HTML Template `displaycustvehicles.html` must use Thymeleaf to iterate over the `vehicles` model attribute and display the `customerName`.
- **TR-005**: **Legacy Data Support**: The CSV parsing logic MUST assign a default `customerId` of `0` (Unassigned) to any existing vehicle records that lack the new `customerId` column.

### Key Entities

- **CustomerVehicle**: Represents a vehicle owned by a customer.
- **Customer**: Represents a customer entity.
