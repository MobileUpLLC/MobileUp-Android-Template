# Example: Integrating UI Flows with `flowReplica`

Use `flowReplica` to treat a standard `Flow` or `StateFlow` (like user preferences from DataStore or app settings) as a `Replica`. This allows it to participate in Replica Algebra (like `combine` with API data).

## Code Example

```kotlin
// User preference from DataStore/settings
private val sortOrder: StateFlow<SortOrder> = settingsRepository.sortOrderFlow

// Combine API data with user preferences
private val sortedItemsReplica = combine(
    itemsReplica,
    flowReplica(sortOrder),
) { items, order ->
    // Sorting logic resides in a domain function
    items.sortedBy(order)
}

override val sortedItemsState = sortedItemsReplica.observe(this, errorHandler)
```

## Key Points

- Combines network-fetched data with local settings/preferences
- Applies transformations (sorting, filtering) reactively
- Keeps the UI updated when user changes preferences
- `flowReplica` wraps any Flow/StateFlow to participate in Replica operations
