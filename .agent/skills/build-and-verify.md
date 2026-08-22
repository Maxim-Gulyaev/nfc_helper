# Skill: Build & Verify

Building the project and verifying changes. Performed before finishing any task
and after any code edits.

## Step 1: Build

```bash
./gradlew :app:assembleDebug
```

- Success → proceed to step 2.
- Compilation error → read the first error (subsequent ones are often consequences), fix it,
  repeat. Typical causes in this project:
  - a forgotten `@OptIn(ExperimentalMaterial3Api::class)` for an M3 API;
  - a non-existent `R.string...` / `R.drawable...` (typo or the string was not added
    to `strings.xml`);
  - explicit backing field: `val uiState: StateFlow<...>` must have `field = ...`.

## Step 2: Unit tests

```bash
./gradlew :app:testDebugUnitTest
```

- Green → done.
- Failure → open the report at `app/build/reports/tests/testDebugUnitTest/index.html`,
  analyze the failed assertion; if the failure is caused by your change — fix it; if the test
  is outdated relative to the new logic — agree on changing the test with the user.

## Step 3: Quick static self-check

Go through the diff with the checklist from [review-code.md](review-code.md) (briefly):

- [ ] no hardcoded strings in the UI;
- [ ] state via ViewModel/StateFlow, composables stateless;
- [ ] navigation only via Navigation 3;
- [ ] unused imports removed.

## Useful launch variants

```bash
./gradlew :app:assembleDebug --console=plain -q      # quiet output
./gradlew :app:testDebugUnitTest --tests "com.maxim.nfchelper.home_screen.HomeViewModelTest"
./gradlew :app:clean :app:assembleDebug              # if you suspect stale artifacts
```

Completion criterion: both commands pass without errors and there are no checklist remarks.