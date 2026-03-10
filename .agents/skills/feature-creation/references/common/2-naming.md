# File Naming Patterns

Naming conventions for feature files (applies to both Simple and Complex features).

## Important Rules

**Domain Entities:**
- Classes ending with `Data` → Complex entities containing multiple nested models
  - Example: `ItemData`, `UserData`, `ProfileData`, `SubdomainAData`
- Simple entities → No suffix
  - Example: `Item`, `User`, `Profile`, `SubdomainA`

**Presentation:**
- Usually works directly with domain models
- `ViewData` suffix → Only when serious UI transformation needed
  - Example: `ItemViewData` (only if `ItemData` needs complex mapping for UI)

## Naming Table

> **Note:** Use `{Entity}` name for Simple features (e.g., `Item`, `User`) or `Subdomain{X}` name for Complex features (e.g., `SubdomainA`, `SubdomainB`). Patterns are the same.

| Layer | Pattern | Notes |
|-------|---------|-------|
| **Domain** | `{Entity}.kt` | Simple entity, straightforward data structure |
| **Domain (complex entity)** | `{Entity}Data.kt` | Complex entity with multiple nested models |
| **Domain ID** | `{Entity}Id.kt` | Can be defined inside entity file |
| **Query** | `{Entity}Query.kt` | For KeyedReplica parameters |
| **Presentation (transformed)** | `{Entity}ViewData.kt` | Only if serious UI transformation needed |
| **API** | `{Entity}Api.kt` | Ktorfit interface |
| **Repository Interface** | `{Entity}Repository.kt` | Repository contract |
| **Repository Impl** | `{Entity}RepositoryImpl.kt` | Repository implementation |
| **DTO** | `{Entity}Response.kt` / `{Entity}Request.kt` | API DTOs |
| **Component Interface** | `{Feature}Component.kt` | Component contract |
| **Real Component** | `Real{Feature}Component.kt` | Component implementation |
| **Fake Component** | `Fake{Feature}Component.kt` | For Composable previews |
| **UI** | `{Feature}Ui.kt` | Composable UI |
| **Widget** | Developer's choice | In `widget/` directory if needed |

## Examples

> **Note:** Same patterns apply to both Simple and Complex features. Examples show typical entity names:
> - **Simple feature:** `Item` (domain entity name)
> - **Complex feature:** `SubdomainA` (subdomain name), `SharedData` (common entities)

### Domain Layer
```kotlin
// Simple entity
Item.kt              // or SubdomainA.kt for complex features
ItemId.kt            // or inside Item.kt

// Complex entity with nested models (most common)
ItemData.kt          // or SubdomainAData.kt for complex features
ItemId.kt

// Query for KeyedReplica
ItemQuery.kt         // or SubdomainAQuery.kt
```

### Data Layer
```kotlin
ItemApi.kt           // or SubdomainAApi.kt
ItemRepository.kt
ItemRepositoryImpl.kt
ItemResponse.kt
ItemRequest.kt
```

### Presentation Layer
```kotlin
ItemDetailsComponent.kt      // or SubdomainAComponent.kt
RealItemDetailsComponent.kt
FakeItemDetailsComponent.kt
ItemDetailsUi.kt             // or SubdomainAUi.kt
ItemViewData.kt              // OPTIONAL: only if serious transformation needed

widget/                      // OPTIONAL: feature-specific widgets
```

### Complex Features - Common Package
```kotlin
// domain/common/
SharedData.kt                // Entity shared across subdomains

// presentation/common/
SelectorComponent.kt         // Component shared within feature
RealSelectorComponent.kt
FakeSelectorComponent.kt
SelectorUi.kt
widget/                      // OPTIONAL: feature-shared widgets
```

## When to Use What

**Simple Entity (`{Entity}.kt`):**
- Straightforward data structure
- Few fields, no nesting
- Used as-is in presentation
- Example: `Item.kt`, `User.kt`, `SubdomainA.kt`

**Complex Entity (`{Entity}Data.kt`):**
- **Most common in this project**
- Contains multiple nested models
- Aggregates data from different sources
- Example: `ItemData` (contains `Item`, `Metadata`, `Relations`), `SubdomainAData`

**ViewData (`{Entity}ViewData.kt`):**
- **Only create when serious transformation needed**
- Presentation usually works directly with domain models
- Use when: formatting, complex calculations, combining multiple domain models for UI
- Example: `ItemViewData.kt`, `SubdomainAViewData.kt`
