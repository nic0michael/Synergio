# Research: Generate Code from Requirements

**Feature**: Generate Code from Requirements
**Status**: Complete

## Unknowns & Clarifications

### 1. Existing CsvStorageImpl Capabilities
**Question**: Does `CsvStorageImpl` support direct filtering by customer ID?
**Finding**: Based on `Requirements.md` instructions (Section 3.1.5), we are instructed to:
- Call `csvStorage.readAllVehicles()` and filter in `MvcController`.
- Call `csvStorage.readAllCustomers()` and find/filter in `MvcController`.
**Decision**: Follow strict instructions to implement filtering logic within `MvcController` rather than modifying `CsvStorageImpl` to add specific query methods. This aligns with the constraint "you are only permitted to write code to class MvcController now" (Requirements 3.1.5).

### 2. View Template Contracts
**Question**: What are the strict contracts for the new/modified HTML templates?
**Finding**:
- `displaycustvehicles.html`: Needs to display `vehicles` list.
- `displaycustomerbycustomerId.html`: Needs to display `customer` object details.
**Decision**: Create standard Thymeleaf templates binding to these model attributes.

## Technology Decisions

| Area | Decision | Rationale |
|------|----------|-----------|
| **Filtering Logic** | In-Memory (Controller) | Explicitly requested by requirements to avoid modifying Storage layer. |
| **View Engine** | Thymeleaf | Project standard [Constitution I.2]. |
| **API Style** | Server-Side MVC | Consistent with existing architecture; returning view names, not JSON. |

## Implementation Strategy

1.  **Controller**: Add two new `@GetMapping` methods to `MvcController` as specified.
2.  **Views**: Create/Update the specific `.html` templates in `src/main/resources/templates/`.
3.  **Data Persistence**: No changes to CSV structure required; only read operations.

## Alternatives Considered

- **Adding Filter Method to Storage**: Rejected due to constraint 3.1.5 in Requirements.md ("you are only permitted to write code to class MvcController now").
