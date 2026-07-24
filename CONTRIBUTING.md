# Contributing Guide — Mindeck

Strategy: **GitHub Flow** + **Conventional Commits**

---

## Issues

An issue is created **before** the branch — the issue number is required in the branch name (see below).

### Title

Same format as commits/PRs:

```
<type>(<scope>): <description>
```

- `scope` may be omitted if the issue is not tied to a single module
- `type` — from the type table in the "Branches" section

Examples from the repository:
```
fix(data): validate photo-by-link URL to prevent SSRF in FileDownloaderImpl
fix(core-mvi): add CoroutineExceptionHandler to BaseViewModel.viewModelScope
feat: add media input options for cards
```

### Labels

Priority is set with a label:

| Label | When |
|---|---|
| `high` | critical / blocks the release |
| `medium` | important, but not blocking |
| `low` | can be deferred |

### Issue body

**Bug / fix** — structure `Problem` → `Impact` → `Reproduction` → `Where`:

```markdown
## Problem

What is broken and why: the specific file/line, the call chain if it matters for
understanding the bug.

## Impact

What happens because of the bug (if not obvious from Problem).

## Reproduction

1. Step
2. Step

**Expected:** what should happen.
**Actual:** what actually happens.
**Environment:** device / OS / version (if relevant).

## Where

- `path/to/File.kt`
- `path/to/OtherFile.kt`
```

`Impact` and `Reproduction` are optional: omit `Impact` when it is obvious from `Problem`;
add `Reproduction` for bugs reproducible by hand (UI/runtime), and omit it for purely static
findings (naming, dead code).

Do not add `## Fix` — the proposed solution belongs in the PR/discussion, not in the issue. Do
not add `Source:`/references to where the problem came from (audit, chat, etc.) — an issue
describes the problem on its own.

If an issue describes several independent problems, number the blocks with a heading
`## N. Severity — title` (example — `#98`: `## 1. Critical — ...`, `## 2. High — ...`),
each block with its own `Problem`/`Impact`.

**Feature** — a short description + a list of scope items, no required subheadings:

```markdown
Add multiple ways to attach media to a card:

- Add image/audio by URL
- Pick image/audio from device storage
- Take a photo with camera
```

---

## Branches

### Template

```
<type>/<issue-id>/<short-description>
```

- `type` — the type of change (see the table below)
- `issue-id` — the GitHub Issue number, **required** (create the issue before the branch)
- `short-description` — 2-4 words separated by hyphens, English, lowercase

### Types

| Type | When to use |
|---|---|
| `feat` | New functionality |
| `fix` | Bug fix |
| `refactor` | Refactoring with no behavior change |
| `chore` | Dependencies, configs, build |
| `docs` | Documentation |
| `test` | Tests |
| `ci` | CI/CD pipeline |
| `perf` | Performance optimization |
| `style` | Formatting, lint (no logic) |

### Examples

```
feat/12/card-study-screen
fix/34/session-queue-crash
refactor/27/extract-viewmodel-types
chore/update-agp-version
docs/contributing-guide
ci/add-lint-workflow
```

---

## Commits

Format per [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```
<type>(<scope>): <description>
```

- `scope` — module or feature: `domain`, `data`, `home`, `card`, `core-ui`, `core-mvi`, `navigation`, `app`
- `description` — what was done, lowercase, no trailing period (≤50 characters)
- commit body — **optional**: only for non-trivial changes (a blank line after the subject,
  wrapped at 72 characters, "what and why", not "how"). For small/self-explanatory changes —
  just the subject, no body

### Examples

```
feat(card): add card study screen
fix(data): protect session queue with mutex
refactor(home): move interval formatting to ui layer
chore(deps): update kotlin to 2.1.0
test(domain): add use case unit tests
ci: add github actions lint workflow
```

### Breaking change

```
feat(domain)!: replace CardRepository with split query/command interfaces

BREAKING CHANGE: CardRepository removed, use CardQueryRepository and CardCommandRepository
```

---

## Pull Requests

### PR title = the first commit or the gist of the branch

```
<type>(<scope>): <description>
```

Examples:
```
feat(card): add deck selection to creation card screen
fix(data): fix room migration from version 3 to 4
refactor(domain): extract use case types into subpackages
```

Do **not** add the issue number to the title. The title must stay a valid Conventional
Commit: on squash-merge GitHub uses it as the commit message, and PR-title linters and
`semantic-release` parse it — a prefix like `#42 ` breaks them. Link the issue via
`Closes #<id>` in the PR body (below). The number is also already in the branch name.

### PR description (template)

```markdown
## What
- ...

## Why
- ...

## How to test
- ...

Closes #<issue-id>
```

> `Closes #42` — GitHub closes the issue automatically on merge into `develop`. This is how
> (rather than with a number in the title) we link a PR to an issue.

---

## GitHub Flow rules

1. `develop` — the main branch, always working
2. Any change — through a branch + PR, never commit directly
3. A branch lives 1-3 days max — longer = risk of conflicts
4. A PR requires review before merge (even solo — self-review)
5. After merge the branch is deleted
6. CI must be green before merge

---

## Link with GitHub Issues

> How to format the issue itself (title, labels, body) — see the "Issues" section above.

**Workflow:**
1. Create an Issue on GitHub → get the number (`#42`)
2. Create a branch with that number
3. Write `Closes #42` in the PR description
4. After merge into `develop` — the issue closes automatically

```
feat/42/dark-theme-support
      ↑
   issue #42
```

The task type is visible from the title prefix (`feat`/`fix`/`refactor`/...); labels are used
only for priority — see the "Issues" section above.
