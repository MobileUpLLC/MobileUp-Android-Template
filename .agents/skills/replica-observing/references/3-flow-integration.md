# Example: Integrating UI Flows with `flowReplica`

Use `flowReplica` to treat a standard `Flow` or `StateFlow` as a `Replica`. This allows it to participate in Replica Algebra (like `combine` with API data).

## Code Example

```kotlin
private val isActionInProgress = MutableStateFlow(false)

// Combine API data with local UI flow
private val uiStateReplica = combine(
    itemsReplica,
    flowReplica(isActionInProgress),
) { items, inProgress ->
    UiState(
        items = items,
        isActionInProgress = inProgress,
    )
}

override val uiState = uiStateReplica.observe(this, errorHandler)
```

## Key Points

- Combines network-fetched data with local UI state
- Applies transformations reactively when local flow changes
- Keeps UI loading/action flags in the same Replica pipeline
- `flowReplica` wraps any Flow/StateFlow to participate in Replica operations
