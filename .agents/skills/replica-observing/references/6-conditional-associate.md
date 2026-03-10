# Example: Conditional Logic with `associate`

Use for conditional logic flows, such as switching between Authorized and Guest views.

## Code Example

```kotlin
private val mainContentReplica = associate<Boolean, MainViewData> { isAuthorized ->
    if (isAuthorized) {
        combine(
            upcomingEventsReplica,
            userStatsReplica,
            ::MainViewData
        )
    } else {
        constReplica(MainViewData.GuestMode) // Returns static placeholder data
    }
}.withKey(isAuthorizedFlow)
```

## Key Points

- Switches between different data flows based on a condition
- Automatically updates when the condition changes (via `withKey`)
- Useful for auth-based UI branching or feature flags
- Each branch can return any Replica (including combined, transformed, or static)
