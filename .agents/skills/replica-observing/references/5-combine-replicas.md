# Example: Combining Multiple Replicas

Merges multiple Replicas into one. The result emits data only when **all** source Replicas have successfully loaded.

## Code Example

```kotlin
private val dashboardReplica = combine(
    repository.profileReplica,
    flowReplica(localSettingsFlow),
    repository.notificationsReplica.toReplica() // Use toReplica() for paged data
) { profile, settings, notifications ->
    DashboardData(profile, settings, notifications)
}
```

## Key Points

- Waits for all data sources to load
- Combines them into a single domain object
- Shows loading until all sources are ready
- Shows error if any source fails
- Use `toReplica()` to convert PagedReplica to regular Replica
