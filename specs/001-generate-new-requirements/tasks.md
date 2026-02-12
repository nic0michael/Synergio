# Implementation Tasks: Show Customer Vehicles

**Branch**: `001-generate-new-requirements`
**Spec**: [specs/001-generate-new-requirements/spec.md](specs/001-generate-new-requirements/spec.md)

## Phase 1: Setup
*(No specific setup tasks required for this feature beyond existing project structure)*

## Phase 2: Foundational (Model & Storage)
Goal: Update data model to support customer linkage and ensure persistence layer handles it.

- [x] T001 [US1] Update `CustomerVehicle` model to include `customerId` field in `src/main/java/za/co/synergio/georgiou/model/CustomerVehicle.java`
- [x] T002 [US1] Create unit test for `CsvStorageImpl` to verify `getCustomerVehicles` filtering and legacy data handling in `src/test/java/za/co/synergio/georgiou/storage/CsvStorageImplTest.java`
- [x] T003 [US1] Update `CsvStorage` interface to include `getCustomerVehicles(int customerId)` in `src/main/java/za/co/synergio/georgiou/storage/CsvStorage.java`
- [x] T004 [US1] Implement CSV reading/writing logic for `customerId` (handling missing value as 0) in `src/main/java/za/co/synergio/georgiou/storage/CsvStorageImpl.java`
- [x] T005 [P] [US1] Implement `getCustomerVehicles` filtering method in `src/main/java/za/co/synergio/georgiou/storage/CsvStorageImpl.java`

## Phase 3: User Story 1 (View Customer Vehicles)
Goal: Implement the UI and Controller logic to select a customer and view their vehicles.

- [x] T006 [US1] Create stub/mock for `CsvStorage` to test controller logic in `src/test/java/za/co/synergio/georgiou/controller/MvcControllerTest.java`
- [x] T007 [US1] Create `getcustvehicles.html` template with customer dropdown (sorted A-Z) in `src/main/resources/templates/getcustvehicles.html`
- [x] T008 [US1] Create `displaycustvehicles.html` template with vehicle table and "No vehicles found" state in `src/main/resources/templates/displaycustvehicles.html`
- [x] T009 [US1] Implement `getCustVehicles` (GET) endpoint in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java` to populate customer dropdown
- [x] T010 [US1] Implement `getRequestVehicles` (GET) endpoint in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java` to handle search and error redirection

## Phase 4: Data Integrity (Update Create Vehicle Flow)
Goal: Ensure new vehicles are linked to customers (FR-005).

- [x] T011 [US1] Update `createvehicle.html` to include customer selection dropdown in `src/main/resources/templates/createvehicle.html`
- [x] T012 [US1] Update `submitVehicle` (or equivalent) method in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java` to parse and save `customerId`

## Phase 5: Polish & Documentation
Goal: Final verification and cleanup.

- [x] T013 Verify proper SLF4J logging in all new Controller and Storage methods in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java`
- [x] T014 Verify "No vehicles found" message appears correctly on empty results in `src/main/resources/templates/displaycustvehicles.html`
- [x] T015 Verify error redirection for invalid Customer ID works as expected in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java`

## Dependencies
- Phase 2 must be completed before Phase 3 and 4 starts.
- Phase 3 (T009) and Phase 4 (T011) share logic for "populating customer dropdown" - consider a shared private method in Controller.

## Parallel Execution Opportunities
- T007 and T008 (Templates) can be built in parallel with T009/T010 (Controller) once T001 (Model) is agreed upon.
- T004 and T005 (Storage Impl) can be done in parallel.
