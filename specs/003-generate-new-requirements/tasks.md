---
description: "Task list for Implement Customer Selection Flow"
---

# Tasks: Implement Customer Selection Flow

**Input**: Design documents from `/specs/003-generate-new-requirements/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests must be written to verify implementation, not to drive it (No TDD). Verify manually via Quickstart.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel
- **[Story]**: Which user story this task belongs to (e.g., US1)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Verify project structure and build status (build.gradle)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

*No new blocking infrastructure required for this feature.*

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Select Customer for Vehicle Lookup (Priority: P1) 🎯 MVP

**Goal**: Allow users to select a customer from a list to view their vehicles.

**Independent Test**: Navigate to `/customers`, see sorted dropdown, select customer, verify redirect to `/customervehiclesbycust`.

### Implementation for User Story 1

- [ ] T002 [US1] Create View Template in src/main/resources/templates/selectacustomer.html (Dropdown with "Name Only", Action: /customervehiclesbycust)
- [ ] T003 [US1] Implement Controller Endpoint in src/main/java/za/co/synergio/georgiou/controller/MvcController.java (GET /customers, fetch all, sort A-Z, handle empty list)

### Verification for User Story 1

- [ ] T004 [US1] Verify Customer Selection Flow (Manual Test per Quickstart.md)

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T005 Code cleanup and formatting check
- [ ] T006 Ensure SLF4J logging is present for new endpoint

---

## Dependencies & Execution Order

### Phase Dependencies

- **User Story 1 (P1)**: Independent.
- **Polish (Final Phase)**: Depends on US1 completion.

### Parallel Opportunities

- T002 (View) and T003 (Controller) can essentially be drafted in parallel, but integration requires both.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete User Story 1 (View & Controller).
2. **STOP and VALIDATE**: Verify using the browser.
3. Polish and Commit.
