# Research & Technical Decisions: Implement Customer Selection Flow

**Feature**: Implement Customer Selection Flow
**Date**: 2026-02-14

## Unknowns & Clarifications

| ID | Question | Status | Resolution |
|----|----------|--------|------------|
| U-001 | Exact fields for Customer entity? | Resolved | Use existing `Customer` or define basic fields (Name, ID) required for selection dropdown. Assuming standard ID/Name pair for now. |
| U-002 | Endpoint response format? | Resolved | Return HTML view (`selectacustomer`), not JSON. |

## Technology Decisions

| Area | Decision | Rationale | Alternatives |
|------|----------|-----------|--------------|
| **Framework** | Spring Web MVC | Existing project standard. | Spring WebFlux (Overkill), Plain Servlet (Too low level) |
| **View Engine** | Thymeleaf | Existing project standard. Support for server-side rendering is required. | JSP, FreeMarker |
| **Storage** | CSV via `CsvStorage` | Strict project requirement (Constitution). | Database (Prohibited) |
| **Frontend Interaction** | Standard Form Submission | Simple, KISS principle. | AJAX/Fetch (Avoid complexity unless needed) |

## Implementation Patterns

### Controller Logic
- **Endpoint**: `/customers`
- **Action**: Fetch all customers -> Add to Model -> Return View "selectacustomer".
- **View**: `selectacustomer.html`.
- **Form**: `<form action="/customervehiclesbycust" method="get">` (or post, depending on design, GET is usually safer for pure queries, but `customervehiclesbycust` likely renders a view).

### Data Access
- Use `csvStorage.readAllCustomers()` (method assumed to exist or needs creation based on requirement "getting searched records" appearing in Rec 2.10.1 of Requirements.md).

## Constitution Compliance Check
- **Simplicity**: Using standard MVC flow.
- **Clean Code**: Independent controller methods.
- **Persistence**: CSV only.
- **TDD**: Explicitly avoided.
