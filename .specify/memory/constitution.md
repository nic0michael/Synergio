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
- **Use Microsoft SQL Server unless otherwise specified**
- CSV-based persistence is acceptable for lightweight applications
- Database choice must be justified for the use case

### Development Language
- **Use Java unless otherwise specified**
- Follow Java best practices and conventions
- Use appropriate Java version for Spring Boot compatibility

### Framework Standards
- Spring Boot is the preferred Java framework for microservices
- Follow Spring Boot best practices and conventions
- Leverage Spring's dependency injection and component model

## Development Workflow

### Version Control
- **Use clear and descriptive commit messages that explain the purpose of changes**
- Follow conventional commits style for consistency
- Commit messages must be reviewed and approved by development team
- Each commit should represent a logical unit of work

### Code Modification Policy
- **Do not modify existing code unless explicitly instructed**
- Respect existing implementations and patterns
- New features should be additive when possible
- Refactoring requires explicit approval

## Governance

### Compliance Requirements
- **Compliance to this constitution is MANDATORY unless otherwise instructed**
- All code reviews must verify adherence to these principles
- Constitution supersedes individual preferences or conflicting practices
- Exceptions must be documented and justified
- Amendments require team approval and documentation update

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

**Version**: 1.0.0 | **Ratified**: 2025-12-04 | **Source**: CorporatePolicyV2_0.md
