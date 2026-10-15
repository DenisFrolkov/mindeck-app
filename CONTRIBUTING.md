# Contributing Guide — Mindeck

Strategy: **GitHub Flow** + **Conventional Commits** + **squash merge**

---

## Issues

For `feat`, `fix`, `refactor`, `perf` and `test`, an issue is created **before** the branch — its number is required
in the branch name (see below). Changes of type `docs`, `chore`, `ci` and `style` may skip the issue.

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
<type>/<short-description>              # only for docs, chore, ci, style
```

- `type` — the type of change (see the table below)
- `issue-id` — the GitHub Issue number. **Required** for `feat`, `fix`, `refactor`, `perf`, `test` (create the issue
  before the branch). Optional for `docs`, `chore`, `ci`, `style`
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
- `description` — what was done, lowercase, no trailing period
- the whole subject line (`<type>(<scope>): <description>`) — ≤72 characters
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
feat(domain)!: split CardRepository into query and command interfaces

BREAKING CHANGE: CardRepository removed, use CardQueryRepository and
CardCommandRepository
```

---

## Pull Requests

### PR title = the squash commit

```
<type>(<scope>): <description>
```

Examples:
```
feat(card): add deck selection to creation card screen
fix(data): fix room migration from version 3 to 4
refactor(domain): extract use case types into subpackages
```

The PR title is the only text that reaches `develop`: the squash commit has no body.

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

## Known limitations
- ...

Closes #<issue-id>
```

- `Known limitations` — what is not verified or not covered (for example, no automated test, iOS not run). Write
  `None` if there is nothing to add.
- Drop sections that do not apply to a small change (for example, a documentation fix). Drop the `Closes` line
  if the PR has no issue.
- The same template is in `.github/pull_request_template.md`; GitHub fills it in when a PR is opened.

> `Closes #42` — GitHub closes the issue automatically on merge into `develop`. This is how
> (rather than with a number in the title) we link a PR to an issue.

---

## Merging

A PR is merged into `develop` with **squash and merge**. This is the only merge method enabled in the repository
settings.

- The squash commit title is the PR title. The commit body is empty. Details stay on the PR page, linked from the
  commit as `(#N)`.
- Commits inside the branch can be written freely: they do not reach `develop`.
- Keep a PR small: one PR — one change. A squash commit cannot be split afterwards.
- Merge only when CI is green (both required jobs), the branch is up to date with `develop` and you have read your
  own diff.
- The branch is deleted after the merge. Do not reuse it: start a new branch from `develop`.
- History note: PRs up to #123 were merged with merge commits. Since #124 every PR is squash-merged. The old history
  is not rewritten.

Exceptions. Enable the method temporarily in Settings → General → Pull Requests and switch it off after the merge:

- **Rebase and merge** — a PR of several steps where every commit is clean, follows Conventional Commits and
  builds. Prefer to split such a PR into two PRs.
- **Merge commit** — merging long-lived branches, for example `develop` into `main` if `main` appears.

---

## GitHub Flow rules

1. `develop` — the main branch, always working
2. Any change — through a branch + PR, never commit directly
3. A branch lives 1-3 days max — longer = risk of conflicts
4. Read your own diff before merge. The repository does not require an approving review
5. After merge the branch is deleted (GitHub does it automatically)
6. CI must be green before merge (both jobs are required checks on `develop`)

---

## Link with GitHub Issues

> How to format the issue itself (title, labels, body) — see the "Issues" section above.

**Workflow:**
1. Create an Issue on GitHub → get the number (`#42`); not needed for `docs`, `chore`, `ci`, `style`
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

---

## Releases

1. Open a PR `chore(release): bump version to X.Y.Z` and merge it as usual.
2. Tag `vX.Y.Z` on `develop` and create a GitHub Release with the signed APK.
