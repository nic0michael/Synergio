# Git Repository Policy

This document outlines the permissions and conventions required for AI Agents interacting with this repository.

## 1. Permissions

### 1.1 Branch Creation
*   **Permitted:** The Agent is fully permitted to create new Git branches based on the naming conventions defined below.

### 1.2 Pushing Code
*   **Conditional Permission:** The Agent may commit and push code to the remote repository **only** after the user explicitly permits the action following an `/implement` command.

### 1.3 Merging
*   **Prohibited:** The Agent is **strictly prohibited** from merging branches into protected branches (e.g., `main`, `develop`) unless explicitly instructed by the user.

## 2. Branch Naming Convention

All branches created by the Agent must strictly follow this structure. **The final branch name must be entirely in lowercase.**

`<branch-type>/<scope-number>/<git-spec-branch-name>/<branch-description>`

### 2.1 Component Definitions

1.  **Branch-type**
    *   Source: Provided via the `/speckit.constitution` command.
    *   Examples: `feature`, `bugfix`, `hotfix`, `chore`.

2.  **Scope-number**
    *   Source: Provided via the `/speckit.constitution` command (originating from the Business Solutions Team/Tjoppie).
    *   Format: Ticket ID (e.g., `ALPS-7345`). Note: Hyphens in the ID should be preserved unless otherwise specified.

3.  **Git-Spec-Branch-Name**
    *   Source: Generated dynamically by the Agent during the `/plan` command execution.
    *   Purpose: Unique identifier for the specific implementation plan.

4.  **Branch-Description**
    *   Source: Derived from the `/speckit.specify` command context.
    *   Format: Kebab-case description (e.g., `Generate-REST-backend`).

### 2.2 Example
If the inputs are:
*   Type: `feature`
*   Scope: `SCO-12345`
*   Git Spec Branch Name: `abc12`
*   Description: `Generate-REST-backend`

The resulting branch name must be:
`feature/SCO-12345/abc12/generate-rest-backend`
