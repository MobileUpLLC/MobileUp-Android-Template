# Complex Feature - File Structure

Complete file structure for a complex feature with subdomains.

```
feature_name/
├── DI.kt
├── data/
│   ├── subdomain_a/
│   │   ├── SubdomainAApi.kt
│   │   ├── SubdomainARepository.kt
│   │   ├── SubdomainARepositoryImpl.kt
│   │   └── dto/
│   │       ├── SubdomainARequest.kt
│   │       └── SubdomainAResponse.kt
│   └── subdomain_b/
│       ├── SubdomainBApi.kt
│       ├── SubdomainBRepository.kt
│       ├── SubdomainBRepositoryImpl.kt
│       └── dto/
│           └── SubdomainBResponse.kt
├── domain/
│   ├── subdomain_a/
│   │   ├── SubdomainAData.kt
│   │   └── SubdomainAQuery.kt
│   ├── subdomain_b/
│   │   └── SubdomainBData.kt
│   └── common/
│       └── SharedData.kt
└── presentation/
    ├── subdomain_a/
    │   ├── SubdomainAComponent.kt
    │   ├── RealSubdomainAComponent.kt
    │   ├── FakeSubdomainAComponent.kt
    │   ├── SubdomainAUi.kt
    │   └── widget/                  # OPTIONAL: subdomain-specific widgets only
    │       └── SubdomainACard.kt
    ├── subdomain_b/
    │   ├── SubdomainBComponent.kt
    │   ├── RealSubdomainBComponent.kt
    │   ├── FakeSubdomainBComponent.kt
    │   └── SubdomainBUi.kt
    └── common/
        ├── selector/                # Shared component within feature
        │   ├── SelectorComponent.kt
        │   ├── RealSelectorComponent.kt
        │   ├── FakeSelectorComponent.kt
        │   └── SelectorUi.kt
        └── widget/                  # OPTIONAL: feature-shared widgets
            └── CommonChip.kt        # General widgets → /core/widget/
```

## Layer Breakdown

### Data Layer (per subdomain)
```
data/
├── subdomain_a/                     # First subdomain
│   ├── SubdomainAApi.kt             # API interface
│   ├── SubdomainARepository.kt      # Repository interface
│   ├── SubdomainARepositoryImpl.kt
│   └── dto/
│       ├── SubdomainARequest.kt
│       └── SubdomainAResponse.kt
└── subdomain_b/                     # Second subdomain
    ├── SubdomainBApi.kt
    ├── SubdomainBRepository.kt
    ├── SubdomainBRepositoryImpl.kt
    └── dto/
        └── SubdomainBResponse.kt
```

### Domain Layer (with common)
```
domain/
├── subdomain_a/              # First subdomain entities
│   ├── SubdomainAData.kt     # Main entity
│   └── SubdomainAQuery.kt    # Query object
├── subdomain_b/              # Second subdomain entities
│   └── SubdomainBData.kt
└── common/                   # Shared entities
    └── SharedData.kt         # Used by both subdomains
```

### Presentation Layer (with common)
```
presentation/
├── subdomain_a/                     # First subdomain
│   ├── SubdomainAComponent.kt
│   ├── RealSubdomainAComponent.kt
│   ├── FakeSubdomainAComponent.kt
│   ├── SubdomainAUi.kt
│   ├── main/                        # Optional: nested screens
│   │   ├── MainComponent.kt
│   │   └── MainUi.kt
│   └── widget/                      # Subdomain-specific widgets
│       └── SubdomainACard.kt
├── subdomain_b/                     # Second subdomain
│   ├── SubdomainBComponent.kt
│   ├── RealSubdomainBComponent.kt
│   ├── FakeSubdomainBComponent.kt
│   ├── SubdomainBUi.kt
│   └── widget/
│       └── SubdomainBCard.kt
└── common/                          # Shared UI
    ├── selector/                    # Shared component
    │   ├── SelectorComponent.kt
    │   ├── RealSelectorComponent.kt
    │   ├── FakeSelectorComponent.kt
    │   └── SelectorUi.kt
    └── widget/                      # Shared widgets
        └── CommonChip.kt
```

## Organization Rules

- **Data Layer** - Each subdomain is self-contained, no `common/`
- **Domain Layer** - Subdomain packages + `common/` for shared entities
- **Presentation Layer** - Subdomain packages + `common/` for shared UI
- **widget/** (optional) - Feature-specific widgets only. General widgets → `/core/widget/`
