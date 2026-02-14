# Tasks: Generate Code from Requirements

**Feature**: Generate Code from Requirements
**Status**: In Progress

## Phase 1: Setup
*Goal: Ensure environment is ready for new endpoints.*

- [x] T001 Verify existence of CSV data files in `data/` for testing

## Phase 2: Foundational
*Goal: No shared foundational tasks required.*

*(Empty - leveraging existing infrastructure)*

## Phase 3: User Story 1 - View Customer Vehicles
*Goal: Allow users to view all vehicles for a specific customer.*
*Priority: P1*

**Independent Test**: Navigate to `/customervehiclesbycust?customerId=1` and verify vehicle list.

- [x] T002 [US1] Implement `getCustomerVehiclesByCustomer` endpoint logic in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java`
- [x] T003 [US1] Update `src/main/resources/templates/displaycustvehicles.html` to display vehicle list and customer name

## Phase 4: User Story 2 - View Customer Details
*Goal: Allow users to view detailed information for a specific customer.*
*Priority: P1*

**Independent Test**: Navigate to `/customerbycust?customerId=1` and verify customer details.

- [x] T004 [US2] Implement `getCustomerByCustomer` endpoint logic in `src/main/java/za/co/synergio/georgiou/controller/MvcController.java`
- [x] T005 [US2] Create new template `src/main/resources/templates/displaycustomerbycustomerId.html` to display customer details

## Phase 5: Polish & Cross-Cutting
*Goal: Ensure consistency and final verification.*

- [x] T006 Verify all explicit requirements from Requirements.md are implemented
- [x] T007 Run application and verify all new endpoints with sample data

## Dependencies
- US1 and US2 are functionally independent but share the `MvcController.java` file.
- T003 depends on T002 (Controller must provide model attributes).
- T005 depends on T004 (Controller must provide model attributes).

## Parallel Execution
- T002 and T004 can be implemented in parallel (different methods in same class).
- T003 and T005 can be implemented in parallel (independent HTML files).

## Implementation Strategy
1.  Implement US1 Controller logic + View.
2.  Implement US2 Controller logic + View.
3.  Verify both.
