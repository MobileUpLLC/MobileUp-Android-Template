# Project Context for Claude

## Project Overview

**Project Name:** MobileUp Android Template

Template repository for bootstrapping new Android projects at MobileUp.
After cloning, teams customize app identity, remove demo functionality, configure project keys, and evolve the template into a production product.

---

## Template Initialization

After cloning the template:

1. Run project setup:
   `./scripts/setup-project <new.application.id> <JIRA_PROJECT_KEY>`
2. Update `AGENTS.md` for the concrete product you are building.
3. Replace application name and icon with project-specific assets.
4. Remove the demo `pokemons` feature and related wiring.
5. Replace default error text resources with product text.
6. Build a project-specific `CustomTheme` and reusable widget set in `core`.
7. Continue with product feature implementation.

Notes:
- `setup-project` also runs Git history reset and hook setup scripts.
- If needed separately, history reset script is: `./scripts/reset-git-history`.

---

## Tech Stack

**Language:** Kotlin | **UI:** Jetpack Compose | **Min SDK:** 26 | **Target SDK:** 35

**Core libraries:**
- **Decompose** — component architecture and navigation
- **Replica** — data loading, caching, state management
- **Koin** — dependency injection
- **Ktor** + **Ktorfit** — HTTP client
- **Moko Resources** — string resources without Context
- **Coil** — image loading
- **Kotlinx DateTime**, **Coroutines**
- **Kotlin Serialization** — JSON serialization/parsing
- **DataStore**, **Security Crypto**
- **Detekt** — static code analysis
- **Module Graph Gradle Plugin** — feature dependency graph validation
- Debug only: **Chucker**, **Hyperion**, **Replica DevTools**

---

## Module Structure

```
app/        — Application, Activity, root DI, build configs
core/       — Shared infrastructure: network, theme, error handling,
              message service, widgets (LceWidget, buttons, inputs),
              storage, security, string/resource services
features/   — Feature modules, package-by-feature
              features/src/main/kotlin/.../features/{feature_name}/
```

Each feature: `data/` → `domain/` → `presentation` layers.

---

## Architecture Overview

### Components (Decompose)

Every screen is a **four-part component**:
- `XxxComponent` — interface (StateFlows + event methods + Output)
- `RealXxxComponent` — implementation (business logic, ComponentContext)
- `FakeXxxComponent` — mock data for previews
- `XxxUi` — pure composable, no business logic

Two types: **Simple** (single screen) and **Router** (manages ChildStack).

Parent↔child communication via `Output` sealed interface — never direct calls.

> For details: **`component-creation`** skill

### Data Loading (Replica)

- **Repository** creates Replicas (`createReplica`, `createKeyedReplica`, `createPagedReplica`, `createKeyedPagedReplica`)
- **Component** observes Replicas → `StateFlow<LoadableState<T>>`
- **UI** uses `LceWidget` or `PullRefreshLceWidget` to handle Loading / Content / Error

> For details: **`replica-creation`**, **`replica-observing`**, **`replica-modification`** skills

### Feature File Structure

**Simple** (8–12 files) — flat `data/domain/presentation` for single-context features.
**Complex** (20–50+ files) — subdomain packages for multi-role or multi-context features.

> For details: **`feature-creation`** skill

### Dependency Injection (Koin)

Each feature has `DI.kt` with a Koin module and `ComponentFactory` extension functions.
Components are always created via `componentFactory.createXxxComponent(...)`, never directly.

### String Resources (Moko Resources)

`StringDesc` is the universal string type — resolved lazily via `.localized()` in Compose or via `StringService` outside Compose.

> For details: **`string-resources`** skill

---

## Critical Rules

### 1. CustomTheme, NOT MaterialTheme
```kotlin
// ✅
style = CustomTheme.typography.body.regular
color = CustomTheme.colors.text.primary

// ❌
style = MaterialTheme.typography.bodyMedium
color = MaterialTheme.colorScheme.onSurface
```

### 2. LCE Pattern for all data-loading screens
- `LceWidget` — detail screens, forms (no pull-to-refresh needed)
- `PullRefreshLceWidget` — lists, feeds, dashboards (pull-to-refresh expected)

### 3. Strict Layer Separation
- **Data** — API, DTOs, RepositoryImpl (Replica creation, DTO→Entity mapping)
- **Domain** — entities, queries, pure functions (no API, no UI)
- **Presentation** — components, composables (no direct API calls)
- **No Interactors/UseCases** — logic lives in Repository + Component

### 4. Replica Ownership
- Create replicas in **Repository**, expose narrow interface (`Replica<T>`, not `PhysicalReplica<T>`)
- Observe in **Component** (`private val xxxReplica` + `override val xxxState`)
- Mutate (mutateData, invalidate, sendAction) in **Repository**

### 5. State in Components
- Always `xxxState` suffix, never generic `val state`
- Derived state: `computed()`, never `combine().stateIn()`
- Mutable local state: `MutableStateFlow` directly, no wrapping in `computed()`
- Keep replica and state as separate variables

---

## Git Workflow

**Branch naming:**
- `feature/<JIRA_KEY>-XXXX/description`
- `bugfix/<JIRA_KEY>-XXXX/description`

**Flow:** branch from `develop` → MR to `develop` → code review → merge
**Production:** `master` (merged from `develop`)

---

## Build Variants

**Flavors:** `dev` (development backend) / `prod` (production backend)
**Types:** `debug` (with dev tools) / `release` (ProGuard)

Backend URLs:
- Dev: `https://pokeapi.co/`
- Prod: `https://pokeapi.co/`