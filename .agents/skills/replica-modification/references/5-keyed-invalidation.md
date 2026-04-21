# Example: Advanced Keyed Invalidation

For `KeyedPagedReplica`, you can target specific children or invalidate the whole set.

## Code Examples

### Iterate and Invalidate

```kotlin
_personalPostsFeedReplica.onEachPagedReplica {
    if (it.userId == currentUserId) invalidate()
}
```

### Invalidate All

```kotlin
_globalPostsFeedReplica.invalidateAll()
```

## Key Points

- Selectively invalidate only relevant data (e.g., posts from a specific user)
- Invalidate all cached variants when necessary (e.g., after posting new content)
- Use `onEachPagedReplica` to iterate over all cached keys
- Use `invalidateAll()` for bulk operations
