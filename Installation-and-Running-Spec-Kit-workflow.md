# Workflow:

Step 1: specify init  → Creates New(Or uses Existing Project) 
Step 2: /constitution →  Generates Constitution
Step 3: /plan         → Generates implementation plan
Step 4: /clarify      → Reviews plan, asks questions, refines understanding
Step 5: /tasks        → Breaks plan into specific tasks
Step 6: /implement    → Executes the tasks

**You need uv installed if you dont have it in your windows:**
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

==================================================================

# 3. CODE GENERATION : PHASE 1 :REST Backend

DID YOU START A NEW COPILOT CHAT?
# Run these commands in Agent chat (Claude Sonnette 4.5) in VS Code with this project folder opened there

## 4. Run /constitution (or /speckit.specify )
```sh
/speckit.constitution if missing create a new and empty constitution 
Incorporate into the constitution this file:
corporate-technical-policy.md
Then incorporate the these files into the constitution:
git-policy.md
CorporateEngineeringPolicyV2_0.md

Branch-type: 
"feature"

Scope-number: 
"SCO-0001"

You are to comply with the git-policy.md
You are not permitted to delete the constitution
```

**ADD THE NEW REQUIREMENTS TO THE REQUIREMENTS FILE**

DID YOU DO THAT?

##  5. Run /specify (or /speckit.specify) Build the high level specification
```sh
/speckit.specify we want to creat the code in this project as specified by Requirements.md 

Branch-Description: 
"Generate-new-requirements"

You are to comply with the git-policy.md
You are not permitted to delete the constitution
You can create spec.md file 
However you are not to create any other documents, files or folders yet
```

## 6. Run /plan Build the Technical Plan
```sh
/speckit.plan create a plan to be use impliment the Requirements

The purpose of this plan is to perform any Requirements with status: TO DO in the ProjectRequirements.md as tasks 

do not create a prompt, tasks, a checklist, or files or update any files yet
do not show issues about missing changes to files  
 specified by ProjectRequirements to produce the code yet
You are to comply with the git-policy.md
You are not permitted to delete the constitution 
Confirm this plan is valid  
```

---


## 7. Run Clarify Confirm Plan is not vague, has no issues
```sh
/speckit.clarify based on current plan and show a brief summary of outstanding issues ignore NFRs
```
---

## 8. Run /tasks Create the tasks for implementation
```sh
/speckit.tasks generate the tasks to implement the new plan 
 specified by ProjectRequirements to produce a list of requirements to produce
You are to comply with the git-policy.md
You are not permitted to delete the constitution 
do not make changes to: 
Requirements.md 
or the code base yet 
```
---

## 9. Run /analyze Validate the above steps before implementation
```sh
/speckit.analyze the previous steps before we Implement the changes needed
You are to comply with the git-policy.md
You are not permitted to delete the constitution 

```
---


## 10. Run /implement
```sh
/speckit.implement Based on new tasks Implement the changes needed
You are to comply with the git-policy.md
You are not permitted to delete the constitution 
If a Unit Test fails more than 3 times remove the test
```
---

==================================================================
