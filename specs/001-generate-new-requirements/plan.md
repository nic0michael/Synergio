# Implementation Plan: Show Customer Vehicles

**Branch**: `001-generate-new-requirements` | **Date**: 2026-02-12 | **Spec**: [specs/001-generate-new-requirements/spec.md](specs/001-generate-new-requirements/spec.md)
**Input**: Feature specification from `specs/001-generate-new-requirements/spec.md`

## Summary

This feature implements the ability to view all vehicles associated with a specific customer. It involves updating the data model to link vehicles to customers (via ID), creating a search page with a customer dropdown, and a display page listing the results.

## Technical Context

**Language/Version**: Java (OpenJDK 21)
**Primary Dependencies**: Spring Boot 3.x/4.x (Web, Thymeleaf), Lombok (implied/optional)
**Storage**: CSV Files (Custom `CsvStorageImpl`)
**Testing**: JUnit 5, Mock stub classes (No Mockito per Constitution)
**Target Platform**: Windows/Linux/Mac
**Project Type**: Web Application (Spring MVC)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Logging Standards**: All new controller/storage methods will include SLF4J info/error logs.
- [x] **Simplicity (KISS)**: Using simple CSV filtering and standard Thymeleaf templates. No database intro.
- [x] **TDD**: Tests will be written for `CsvStorageImpl` filtering before implementation. Mocks will be hand-written stubs.
- [x] **Clean Code**: Methods will be small (`getCustomerVehicles`), variables descriptive.
- [x] **Technology Stack**: Adheres to CSV-only persistence and Java/Spring Boot stack.
- [x] **Git Policy**: Branch `001-generate-new-requirements` is correctly named.

## Project Structure

### Documentation (this feature)

```text
specs/001-generate-new-requirements/
├── plan.md              # This file
├── research.md          # Design decisions & unknown resolution
├── data-model.md        # Schema updates
├── quickstart.md        # Usage guide
└── checklists/          # Quality checks
```

### Source Code

```text
src/main/java/za/co/synergio/georgiou/
├── controller/
│   └── MvcController.java        # Add /getVehiclesForCustomer endpoint
├── model/
│   └── CustomerVehicle.java      # Add customerId field
└── storage/
    ├── CsvStorage.java           # Interface update
    └── CsvStorageImpl.java       # Implement filtering logic

src/main/resources/templates/
├── getcustvehicles.html          # Search form
└── displaycustvehicles.html      # Results table
```

## Complexity Tracking

None identified.
