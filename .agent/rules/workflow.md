# Rules: Workflow

The process for working on the NFC Helper repository.

## Mandatory minimum before finishing any task

1. `./gradlew :app:assembleDebug` — build is green.
2. `./gradlew :app:testDebugUnitTest` — tests are green.
3. If the task touched UI strings/resources — verify nothing is hardcoded
   (see [code-style.md](code-style.md)).

## Git

- Branches off `main`; meaningful names (`feature/read-tab-ui`, `fix/nfc-adapter-null`).
- Commits: short imperative subject in English
  (`Add read tab placeholder`, `Fix settings back navigation`).
- Do not commit build artifacts: `.gradle/`, `build/`, `.kotlin/`, local configs —
  they are already in `.gitignore`.
- Do not rewrite `main` history (rebase/force-push) without an explicit user request.

## Change boundaries

- Minimal diff: only what is related to the task.
- Found an unrelated problem → report it to the user separately, do not fix silently.
- Public contracts (screen signatures, `Screen`, the `UiState` structure) may only change
  as part of a task or after agreement.
- New dependencies in `app/build.gradle.kts` — only after agreement with the user.

## Communication with the user

- Communication language is Russian; code, comments, and commits are in English.
- Before writing code for an ambiguous task — clarify the requirements.
- On completion: a brief report (what was done, what was verified) + suggested next steps, if any.

## When to use skills

| Situation | Skill |
|---|---|
| Changed code and need to verify it | [build-and-verify](../skills/build-and-verify.md) |
| New screen/feature | [new-screen](../skills/new-screen.md) |
| Reviewing a diff | [review-code](../skills/review-code.md) |
| Bug | [fix-bug](../skills/fix-bug.md) |
| Tests | [testing-unit](../skills/testing-unit.md) |