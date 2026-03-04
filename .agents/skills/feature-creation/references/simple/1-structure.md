# Simple Feature - File Structure

Complete file structure for a simple feature (flat organization, no subdomains).

```
item_details/
├── DI.kt
├── data/
│   ├── ItemApi.kt
│   ├── repository/
│   │   ├── ItemRepository.kt
│   │   └── ItemRepositoryImpl.kt
│   └── dto/
│       └── ItemResponse.kt
├── domain/
│   └── Item.kt
└── presentation/
    ├── ItemDetailsComponent.kt
    ├── RealItemDetailsComponent.kt
    ├── FakeItemDetailsComponent.kt
    ├── ItemDetailsUi.kt
    └── widget/                      # OPTIONAL: only feature-specific widgets
        └── ItemCard.kt              # General widgets → /core/widget/
```

## Layer Breakdown

### Data Layer
```
data/
├── ItemApi.kt                       # Ktorfit API interface
├── repository/
│   ├── ItemRepository.kt            # Repository interface
│   └── ItemRepositoryImpl.kt        # Repository implementation
└── dto/
    └── ItemResponse.kt              # API response DTO + toDomain()
```

### Domain Layer
```
domain/
└── Item.kt        # Contains Item entity and ItemId
```

### Presentation Layer
```
presentation/
├── ItemDetailsComponent.kt          # Component interface
├── RealItemDetailsComponent.kt      # Real implementation
├── FakeItemDetailsComponent.kt      # Fake for previews
└── ItemDetailsUi.kt                 # Composable UI
```

### Presentation Layer (with optional widgets)
```
presentation/
├── ItemDetailsComponent.kt
├── RealItemDetailsComponent.kt
├── FakeItemDetailsComponent.kt
├── ItemDetailsUi.kt
└── widget/                          # Optional: only feature-specific widgets
    ├── ItemCard.kt                  # Example: specific to this feature
    └── ItemMetadata.kt              # NOT general-purpose (those go to /core/widget/)
```

## What Goes Where

- **Data Layer** - API interfaces, Repositories, DTOs
- **Domain Layer** - Entities (data classes), Type-safe IDs, Query objects
- **Presentation Layer** - Components (Real/Fake), UI, feature-specific widgets
- **widget/ (optional)** - Feature-specific reusable UI only. General widgets → `/core/widget/`
