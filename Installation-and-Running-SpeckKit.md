# Workflow:

Step 1: specify init  → Creates New(Or uses Existing Project) 
Step 2: /constitution →  Generates Constitution
Step 3: /plan         → Generates implementation plan
Step 4: /clarify      → Reviews plan, asks questions, refines understanding
Step 5: /tasks        → Breaks plan into specific tasks
Step 6: /implement    → Executes the tasks

You need uv installed if you dont have it in your windows:
```sh
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
```


# 1. install speck kit
```sh
uv tool install specify-cli --force --from git+https://github.com/github/spec-kit.git
```
---

# 2. Creating Project OR Using existing project
## 2.1 For existing project using PowerShell as terminal
```sh
specify init --here --ai copilot --script ps
```

## 2.2 For generating a New Project using PowerShell as terminal
```sh
specify init prompt-maker --ai copilot --script ps
```
---

# 3. Run /specify
```sh
/specify we want to create prompt that can be used to generate a file: Requirements.md in a chapter ## 1. Existing Requirements
for this a Java, Grails, Spring Boot Micoservice project
with the technical details in the plan do not create the file yet
do not create a prompt yet 
confirn this spec is valid
```
---

# 4. Run /constitution
```sh
/constitution add rules in CorporatePolicyV2_0.md to constitution
```
---

# 5. Run /plan
```sh
/plan add two chapters to the Requirements.md
1. ## 1. Existing Requirements
2. ## 2. New Requirements

analyze the code and associated files to define a prompt that could be used to build this project

place the prompt in Requirements.md
chapter ## Existing Requirements
as a requirement that could define and be used to build this project 
chapter ## New Requirements 
will only be provided much later not even at implementation
do not create a prompt yet 
confirn this plan is valid 
```
---

# 6. Run Clarify
```sh
/clarify based on current plan
```
 ---

# 7. Run /tasks
```sh
/tasks generate the tasks to implement this project requirement
do not generate Requirements.md yet
```
---

# 8. Run /implement
This Implements and Runs the code
```sh
/implement Now Implement the changes to Requirements.md
chapter ## New Requirements 
will only be provided much later and in this implementation
```
---
# ======================================================================= #
# Now we make changes to Requirements.md

**(Or we ask the Agent to make our changes there)**

---

# 9. Revise plan
```sh
/plan create a new plan referring only to changes in our Requirements.md and constitution
do not create anything yet 
confirn this plan is valid
```
---

# 10. Run Clarify
```sh
/clarify this revised plan
```
---

# 11. Revise Tasks
```sh
/tasks revise the tasks refering to the changed plan
do not create anything yet 
confirn the tasks are valid
```
---

# 12. Revised Implementation
```sh
/implement Based on revised task Implement the revised code requirements
```
---