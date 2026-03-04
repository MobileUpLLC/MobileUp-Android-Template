---
name: feature-creation
description: Guidelines for organizing feature module file structure using package-by-feature principle
---

# Feature Creation - File Structure Organization

This skill defines the **file and folder structure** for feature modules following the **package-by-feature** principle.

---

## Two Structure Types

### Simple Feature
**For:** Single-responsibility features without role/context variations
**Structure:** Flat organization (`data/`, `domain/`, `presentation/`)
**File Count:** Typically 8-12 files
**Example:** Item details, user profile, settings

### Complex Feature with Subdomains
**For:** Multi-context features with role-based or subdomain variations
**Structure:** Subdomain packages (`data/subdomain_a/`, `domain/subdomain_a/`, etc.)
**File Count:** Typically 20-50+ files
**Example:** Dashboard with admin/user views, analytics with different roles

---

## When to Use Which

| Criteria | Simple Feature | Complex Feature |
|----------|----------------|-----------------|
| **Responsibility** | Single, focused | Multiple contexts/roles |
| **Variations** | None or minimal | Role-based or context-specific |
| **Files per layer** | 2-5 | 6+ (with subdomains) |
| **Shared entities** | Not needed | Common package required |
| **Growth potential** | Stable scope | Expected to grow with new subdomains |

**Decision flow:**
1. Multiple user roles/contexts? → **Complex**
2. Each subdomain has 3+ unique files? → **Complex**
3. Need shared entities across subdomains? → **Complex**
4. Otherwise → **Simple**

---

## File Structure

### Simple Feature Structure

```
feature_name/
├── DI.kt
├── data/
│   ├── FeatureApi.kt
│   ├── repository/
│   └── dto/
├── domain/
│   └── Entity.kt
└── presentation/
    ├── FeatureComponent.kt
    ├── RealFeatureComponent.kt
    ├── FakeFeatureComponent.kt
    ├── FeatureUi.kt
    ├── ... (nested components) ...  # Child components if needed
    └── widget/          # OPTIONAL: feature-specific only
```

**See detailed examples:**
> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Simple: Structure](references/simple/1-structure.md) - File organization
- [Simple: DI Template](references/simple/3-di.md) - Dependency injection
- [Common: Naming Patterns](references/common/2-naming.md) - File naming table (applies to both Simple and Complex)
- [Common: String Resources](references/common/4-strings.md) - XML resources (applies to both Simple and Complex)

### Complex Feature Structure

```
feature_name/
├── DI.kt
├── data/
│   ├── subdomain_a/
│   │   ├── SubdomainAApi.kt
│   │   ├── SubdomainARepository.kt
│   │   └── dto/
│   └── subdomain_b/
├── domain/
│   ├── subdomain_a/
│   ├── subdomain_b/
│   └── common/         # Shared entities
└── presentation/
    ├── subdomain_a/
    │   ├── SubdomainAComponent.kt
    │   ├── RealSubdomainAComponent.kt
    │   ├── SubdomainAUi.kt
    │   └── ... (nested components) ...  # Child components if needed
    ├── subdomain_b/
    │   └── ... (similar structure) ...
    └── common/         # Shared UI
```

**See detailed examples:**
> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Complex: Structure](references/complex/1-structure.md) - Subdomain organization
- [Complex: DI Template](references/complex/3-di.md) - Multiple factories
- [Common: Naming Patterns](references/common/2-naming.md) - File naming table (applies to both Simple and Complex)
- [Common: String Resources](references/common/4-strings.md) - XML resources (applies to both Simple and Complex)

---

## Key Organization Rules

### 1. Layer Separation
- **Data** - API, Repository, DTOs (no UI, no domain logic)
- **Domain** - Entities, Queries (no API, no UI)
- **Presentation** - Components, UI, widgets (no direct API calls)

### 2. Package by Feature
- Each feature = self-contained package
- Path: `features/src/main/kotlin/com/example/app/features/feature_name/`
- All layers for one feature in one place

### 3. Common Package
- **Simple features:** No common package needed
- **Complex features:**
  - `domain/common/` - Shared entities across subdomains
  - `presentation/common/` - Shared UI within feature
  - **NO** `data/common/` - Each subdomain has own API/Repository

### 4. Widget Placement
- **Feature-specific widgets** → `feature/presentation/widget/`
- **Subdomain-specific widgets** → `feature/presentation/subdomain_a/widget/`
- **Feature-shared widgets** → `feature/presentation/common/widget/`
- **App-wide widgets** → `/core/widget/` (NOT in feature)

### 5. DI Configuration
- **Location:** `DI.kt` at feature root
- **Content:** Koin module + ComponentFactory extensions
- **Simple:** One module, one factory
- **Complex:** One module, multiple factories (one per subdomain)

### 6. String Resources
- **Location:** `src/values/{feature_name}_strings.xml`
- **Naming:** `{feature_name}_{context}_{element}`
- **Complex:** Add subdomain prefix for subdomain-specific strings

---

## Quick Reference

| Aspect | Simple | Complex |
|--------|--------|---------|
| **Structure** | Flat | Subdomain packages |
| **Common package** | ❌ | ✅ domain/common, presentation/common |
| **DI** | 1 module, 1 factory | 1 module, N factories |
| **Entity naming** | Domain name (`Item`, `User`) | Subdomain name (`SubdomainA`) |

