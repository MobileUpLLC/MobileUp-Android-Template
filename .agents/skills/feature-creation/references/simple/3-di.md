# Simple Feature - Dependency Injection

DI configuration template for simple features.

## File Location

`DI.kt` (root of feature package)

## Template

```kotlin
val itemDetailsModule = module {
    // API
    single { get<NetworkApiFactory>().authorizedKtorfit.createItemApi() }

    // Repository
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class
}

// Component Factory
fun ComponentFactory.createItemDetailsComponent(
    componentContext: ComponentContext,
    itemId: ItemId
): ItemDetailsComponent {
    return RealItemDetailsComponent(
        componentContext = componentContext,
        itemId = itemId,
        repository = get(),
        errorHandler = get()
    )
}
```

## Structure

1. **Module definition** - Register dependencies (API, Repository)
2. **ComponentFactory extensions** - Create component instances

## Key Points

- Module name: `{featureName}Module`
- Factory function: `ComponentFactory.create{FeatureName}Component(...)`
- Dependencies resolved via `get()` from Koin
- Never create components directly - always use factory
