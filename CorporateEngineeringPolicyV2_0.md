# Corporate Policy
This should be enforced in the constitution.md

## 0. Tech Stack
1. Spring Boot Microservice
2. Thymeleaf with Javascript
3. persist to CSV files do not use database
4. Gradle project
5. Java: openjdk 21.0.5 2024-10-15 LTS 

## 1. Writing to the Logs
Before an exception is thrown an error must be logged with appropriate information
If a method ran successfully an info log must be written with appropriate information
especially a method that returns something.
this excludes setter and getter methods

## 2. Simplicity KISS philosophy
**This is an important engineering principle that should be practiced**
Keep it simple stupid.
Avoid over engineering and unnecessary complexity.
Focus on solving the problem at hand in the simplest way possible.
It is importtant to deliver the simplest solution that works this produces reliable code.

## 3. Test Driven development
Dont do test driven development

## 4. Clean Code
**This is one of our most important requirements**
All code must follow clean code principles.
Methods should be small and focused on a single task.
Variables and methods should have meaningful names that convey their purpose.
Avoid code duplication by reusing existing methods and classes where possible.
Long methods hide classes and should be refactored into smaller methods ot classes.
Coments are regarded as dirty code and should never be used to explain bad code.
OnLy Javadoc comments are allowed.
Dont modify existing code and write clean code unless instructed to do so

### 4.1 Self Documenting Code
**This is part of clean code and should have the same importance.**
All code must be self documenting.
Class names, method names and variable names should be clear and descriptive so no comments are needed.
Dont modify existing code and make it Self Documenting Code unless instructed to do so

## 5. Database
in this project we dont use databases we use CSV files

## 6. Development Language
Use Java unless otherwise specified


## 7. Commit Messages
Use clear and descriptive commit messages that explain the purpose of the changes made. 
Follow the conventional commits style for consistency.
This needs to be reviewed and decided by the AI  Council

**Compliance to this is mandatory unless otherwise instructed**

## 8. Requirement Tracking
All requirements in `Requirements.md` must be explicitly tracked.
- Completed or existing requirements must be marked with:
  ```
  Status:
  DONE
  ```
- New or pending requirements must be marked with:
  ```
  Status:
  TO DO
  ```