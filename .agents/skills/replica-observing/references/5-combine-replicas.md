# Example: Combining Multiple Replicas

Merges multiple Replicas into one. The result emits data only when **all** source Replicas have successfully loaded.

## Code Example

```kotlin
private val isActionInProgress = MutableStateFlow(false)

private val dashboardReplica = combine(
    repository.profileReplica,
    flowReplica(isActionInProgress),
    repository.notificationsReplica.toReplica() // Use toReplica() for paged data
) { profile, inProgress, notifications ->
    DashboardData(profile, inProgress, notifications)
}
```

## Key Points

- Waits for all data sources to load
- Combines them into a single domain object
- Shows loading until all sources are ready
- Shows error if any source fails
- Use `toReplica()` to convert PagedReplica to regular Replica
