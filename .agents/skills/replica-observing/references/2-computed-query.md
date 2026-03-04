# Example: Deriving Parameters with `computed`

Use `computed` to create reactive Query objects from one or more UI `StateFlows` (e.g., search text, selected tabs, filters).

## Code Example

```kotlin
private val selectedPeriod = MutableStateFlow(Period.Month)
private val selectedCategory = MutableStateFlow(Category.All)

// Create a reactive Query object (up to 14 parameters supported)
private val reportQuery = computed(
    selectedPeriod,
    selectedCategory
) { period, category ->
    ReportQuery(period, category)
}

private val reportReplica = repository.reportReplica
    .keepPreviousData()
    .withKey(reportQuery)

override val reportState = reportReplica.observe(this, errorHandler)
```

## Key Points

- Automatically recomputes the query when any parameter changes
- Triggers a new data fetch with the updated query
- Keeps previous data visible during the transition (via `keepPreviousData()`)
- Supports up to 14 parameters
