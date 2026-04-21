# Example: Using `keepPreviousData`

**Crucial for UX.** Use this on `KeyedPagedReplica` or `KeyedReplica` to keep showing current data while a new key is being loaded. This prevents the screen from flickering back to a loading state during search or tab switching.

## Code Example

```kotlin
private val searchReplica = repository.searchResultsReplica
    .keepPreviousData()
    .withKey(searchQueryFlow)
```

## Key Points

- Prevents loading state flicker when switching keys
- Shows current data while new data loads
- Essential for search and tab switching UX
- Apply **before** `withKey()`
