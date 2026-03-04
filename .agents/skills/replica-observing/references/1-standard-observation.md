# Example: Standard Observation

The basic pattern for observing a Replica in a Component.

## Code Example

```kotlin
class RealCategoriesComponent(
    componentContext: ComponentContext,
    private val categoriesRepository: CategoriesRepository,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, CategoriesComponent {

    // 1. Prepare the source replica (private)
    private val categoriesReplica = categoriesRepository.categoriesReplica

    // 2. Observe it to produce the UI state (override)
    // Pass 'this' as ComponentContext and the errorHandler
    override val categoriesState = categoriesReplica.observe(this, errorHandler)
}
```

## Key Points

- Get Replica from Repository
- Use `observe(this, errorHandler)` to convert to LCE StateFlow
- Expose as `override val` to UI
- Replica private, State public
