# Skill: Bug Fix

Fixing a bug in NFC Helper. The goal is a minimal targeted fix + proof that the bug is fixed
and nothing is broken.

## Process

1. **Formalize the bug.** State: expected behavior / actual behavior / reproduction steps.
   If there is not enough information — ask the user before making changes.
2. **Localize.** Find the responsible code via project search. Typical places:
   - UI bugs → `home_screen/`, `settings/`, `ui/theme/`
   - Navigation → `navigation/NfcHelperApp.kt`, `navigation/Screens.kt`
   - State → the corresponding `XxxViewModel`
3. **Find the root cause.** Do not fix the symptom: if rendering crashes, figure out what data
   arrives in `UiState` instead of just wrapping it in an `if`.
4. **The fix.** Minimal diff, no drive-by refactoring. If you find unrelated problems along
   the way — report them separately, do not drag them into the fix.
5. **Regression check:**
   - `./gradlew :app:assembleDebug` — build is green;
   - if the bug can be covered by a unit test — add a test following [testing-unit.md](testing-unit.md)
     and run `./gradlew :app:testDebugUnitTest`;
   - if the bug is UI/navigation and a unit test is impossible — describe how to verify manually.
6. **Report to the user:** cause → what you changed (diff) → how you verified it.

## Constraints

- Do not change public contracts (`Screen`, screen signatures) without explicit agreement —
  this affects navigation.
- Do not "improve" the code around the fix.
- Strings added to the UI during a fix go only through `strings.xml`.

## Red flags (stop and ask the user)

- The fix requires a new permission/manifest change.
- The fix requires changing dependencies in `app/build.gradle.kts`.
- The bug's cause is in a third-party library (Navigation 3, etc.) and a workaround is needed.