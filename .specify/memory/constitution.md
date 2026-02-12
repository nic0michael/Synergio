<!--
Sync Impact Report:
- Version change: 1.1.0 -> 1.1.1
- List of modified principles: Technology Stack Standards (Updated with specifics from CorporateEngineeringPolicyV2_0.md)
- Added sections: N/A
- Templates requiring updates:  checked
-->
# Synergeio Vehicle Service Station Constitution

## Core Principles

### I. Logging Standards (MANDATORY)
**Before an exception is thrown, an error must be logged with appropriate information.**
- All methods that return values must write info logs with appropriate information upon successful execution
- Excludes setter and getter methods
- Logging provides traceability and debugging capability
- Use SLF4J logging framework consistently

### II. Simplicity - KISS Philosophy (CRITICAL)
**Keep It Simple Stupid - This is an important engineering principle that should be practiced.**
- Avoid over-engineering and unnecessary complexity
- Focus on solving the problem at hand in the simplest way possible
- Deliver the simplest solution that works - this produces reliable code
- Complexity must be justified and documented
- Prefer straightforward implementations over clever code

### III. Test-Driven Development (NON-NEGOTIABLE)
**Code must be developed using Test-Driven Development methodology.**
- First write a failing Unit Test, then write code to make the test pass
- Do NOT use Mockito - if mocking is needed, use real mock stub classes
- Prefer simple unit tests over integration tests where possible
- Refer to ChamelionTests.md for detailed mock testing guidance using stub classes
- Keep Mockito dependencies in build files but prefer stub classes
- Red-Green-Refactor cycle strictly enforced
- **Do not modify existing code to add tests unless explicitly instructed**

### IV. Clean Code Principles (HIGHEST PRIORITY)
**All code must follow clean code principles - This is one of our most important requirements.**
- Methods should be small and focused on a single task
- Variables and methods must have meaningful names that convey their purpose
- Avoid code duplication by reusing existing methods and classes
- Long methods hide classes and should be refactored into smaller methods or classes
- **Comments are regarded as dirty code and should NEVER be used to explain bad code**
- **ONLY Javadoc comments are allowed**
- **Do not modify existing code to apply clean code unless explicitly instructed**

### V. Self-Documenting Code (MANDATORY)
**All code must be self-documenting - This is part of clean code with equal importance.**
- Class names, method names, and variable names must be clear and descriptive
- Code should be readable without comments
- Intent should be obvious from the code structure itself
- **Do not modify existing code to make it self-documenting unless explicitly instructed**

## Technology Stack Standards

### Database
- **Persist to CSV files - Do NOT use a database.** (Override: Project Specific per CorporateEngineeringPolicyV2_0 Section 0)
- Although general policy prefers MS SQL, this project is explicitly CSV-based.

### Development Environment
- **Project Structure**: Spring Boot Microservice
- **Language**: Java (OpenJDK 21.0.5 LTS)
- **Build Tool**: Gradle Project
- **Frontend**: Thymeleaf with JavaScript

### Framework Standards
- Spring Boot is the preferred Java framework for microservices
- Follow Spring Boot best practices and conventions
- Leverage Spring's dependency injection and component model

## Development Workflow

### Version Control & Git Policy
- **Branches**: The Agent is fully permitted to create new Git branches following the naming convention.
- **Pushing**: The Agent may commit and push code **only** after the user explicitly permits the action following an /implement command.
- **Merging**: The Agent is **strictly prohibited** from merging branches into protected branches (e.g., main, develop) unless explicitly instructed.
- **Commit Messages**: Use clear and descriptive commit messages following conventional commits style.

### Branch Naming Convention (MANDATORY)
All branches created must strictly follow this structure:
<branch-type>/<scope-number>/<git-spec-branch-name>/<branch-description>
- **Branch-type**: eature, ugfix, hotfix, chore.
- **Scope-number**: Ticket ID (e.g., SCO-0001 or ALPS-7345).
- **Git-Spec-Branch-Name**: Unique identifier generated during planning (e.g., bc12).
- **Branch-Description**: Kebab-case description (e.g., generate-new-requirements).
- **Case**: The final branch name must be entirely in **lowercase**.

### Requirement Tracking
- All requirements in Requirements.md must be explicitly tracked.
- Status MUST be marked as DONE for completed requirements.
- Status MUST be marked as TO DO for new requirements.

### Code Modification Policy
- **Do not modify existing code unless explicitly instructed**
- Respect existing implementations and patterns
- New features should be additive when possible
- Refactoring requires explicit approval

## Governance

### Compliance Requirements
- **Compliance to this constitution is MANDATORY unless otherwise instructed**
- The constitution is not negotiable.
- You are not permitted to delete the constitution.
- Git operations are limited to the User (unless permissions granted).
- Constitution supersedes individual preferences or conflicting practices.
- Exceptions must be documented and justified.

### Enforcement
- All development work must comply with these standards
- Code that violates these principles may be rejected
- Quality gates enforce constitution compliance
- Regular reviews ensure ongoing adherence

### Priority Order (when principles conflict)
1. Clean Code & Self-Documenting Code (highest)
2. Simplicity (KISS)
3. Test-Driven Development
4. Logging Standards
5. Technology Stack Standards

**Version**: 1.1.1 | **Ratified**: 2025-12-04 | **Last Amended**: 2026-02-12
