# Research & Decisions: Show Customer Vehicles

## Unknowns & Clarifications

### 1. Linking Vehicles to Customers
**Issue**: `CustomerVehicle.java` currently lacks a foreign key to `Customer`. It relies on denormalized string fields (`customerName`). The requirement asks for `getCustomerVehicles(int customerId)`.
**Resolution**: 
- We MUST modify `CustomerVehicle` to include `private int customerId`.
- This field will store the `index` of the associated `Customer`.
- Existing CSV data might need migration or default values (e.g., 0) if mixed. For new development, strict linking will be enforced via the UI.
- **Decision**: Add `customerId` field to `CustomerVehicle`.

### 2. URL Mapping and Flow
**Requirement**: "This must be a REST method (`/vehicleRecords` with a `@RequestParam("CustomerId") int CustomerId)`... MVC controller should redirect to a template `displaycustvehicles`"
**Analysis**: 
- A REST method commonly implies returning JSON (`@RestController` or `@ResponseBody`), but the requirement says "redirect to a template".
- In Spring MVC, returning a String (template name) is standard for `@Controller`.
- **Decision**: The method will be standard MVC, not "REST" in the JSON API sense, despite the terminology in Requirements.md. It will return `String` (view name).
- **Flow**:
    1. `GET /getcustvehicles` -> returns `getcustvehicles.html`
    2. User submits form -> `GET /vehicleRecords?CustomerId=123`
    3. Controller calls `storage.getCustomerVehicles(123)`
    4. Controller adds attributes `vehicles` and `customerName` to Model.
    5. Controller returns `displaycustvehicles` view.

### 3. Displaying Customer Name
**Requirement**: `displaycustvehicles.html` must display "customerName" on top.
**Resolution**:
- The `getCustomerVehicles(int customerId)` returns specific vehicles.
- Does `CustomerVehicle` contain the correct `customerName`? Yes, it has the denormalized field.
- **Decision**: We can use the first vehicle's `customerName` OR perform a separate `storage.searchForCustomer(customerId)` to get the canonical name.
- **Refinement**: To be safe (what if list is empty?), the controller should probably look up the Customer object separately to put the name in the header, even if no vehicles exist.
- **Decision**: Controller will perform two lookups: `getCustomerVehicles(id)` and `searchForCustomer(id)` (existing method) to get the name reliably.

## Technology Choices

- **Persistence**: Continue using `CsvStorage` as per Constitution.
- **Frontend**: Thyemleaf for iteration (`th:each`) and form binding.
- **Testing**: Create a stub for CsvStorage to test the Controller, and a unit test for the CsvStorageImpl logic.

## Alternatives Considered

- **Filtering by Name**: We could filter by `customerName` string.
    - *Rejected*: Requirement explicitly asks for `int customerId`. Names are not unique or stable identifiers.
- **No Schema Change**: Try to infer relationship.
    - *Rejected*: Impossible without unique ID linkage.
