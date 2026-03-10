# Complex Feature - Dependency Injection

DI configuration template for complex features with subdomains.

## File Location

`DI.kt` (root of feature package)

## Template

```kotlin
val featureModule = module {
    // Subdomain A
    single { get<NetworkApiFactory>().authorizedKtorfit.createSubdomainAApi() }
    singleOf(::SubdomainARepositoryImpl) bind SubdomainARepository::class

    // Subdomain B
    single { get<NetworkApiFactory>().authorizedKtorfit.createSubdomainBApi() }
    singleOf(::SubdomainBRepositoryImpl) bind SubdomainBRepository::class
}

// Subdomain A component factory
fun ComponentFactory.createSubdomainAComponent(
    componentContext: ComponentContext
): SubdomainAComponent {
    return RealSubdomainAComponent(
        componentContext = componentContext,
        repository = get(),
        errorHandler = get()
    )
}

// Subdomain B component factory
fun ComponentFactory.createSubdomainBComponent(
    componentContext: ComponentContext
): SubdomainBComponent {
    return RealSubdomainBComponent(
        componentContext = componentContext,
        repository = get(),
        errorHandler = get()
    )
}

// Common component factory
fun ComponentFactory.createSelectorComponent(
    componentContext: ComponentContext,
    onSelected: (SharedData) -> Unit
): SelectorComponent {
    return RealSelectorComponent(
        componentContext = componentContext,
        onSelected = onSelected
    )
}
```

## Structure

1. **Single module** for entire feature (all subdomains)
2. **Separate factories** for each subdomain component
3. **Factories for common components** if they need DI

## Key Points

- Module name: `{featureName}Module`
- Factory functions: `ComponentFactory.create{SubdomainX}Component(...)`
- All subdomains in single module
- Dependencies resolved via `get()` from Koin
