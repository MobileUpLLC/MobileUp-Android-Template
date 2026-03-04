# Example: Bulk Invalidation by Tag

Invalidation marks data as "stale," forcing a background refresh if there are active observers.

## Code Example

```kotlin
// Makes all user-related data stale
replicaClient.invalidateByTag(tag = ReplicaTags.UserSpecificData)
```

## Key Points

- Invalidates all Replicas with the specified tag
- Useful for logout, profile updates, or global state changes
- Does not clear data immediately - marks it as stale
- Triggers refresh only if there are active observers
