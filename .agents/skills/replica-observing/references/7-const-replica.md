# Example: Static Data with `constReplica`

Returns a Replica with static placeholder data. Useful for providing default/fallback data, testing, or guest mode.

## Code Examples

### Fallback Data

```kotlin
private val defaultSettingsReplica = constReplica(
    Settings(
        theme = Theme.Light,
        language = Language.English,
        notificationsEnabled = true
    )
)

// Use in combination with actual data
private val settingsReplica = combine(
    userSettingsReplica,
    defaultSettingsReplica
) { userSettings, defaults ->
    userSettings ?: defaults
}
```

### Guest Mode

```kotlin
private val profileReplica = associate<Boolean, ProfileData> { isAuthenticated ->
    if (isAuthenticated) {
        repository.userProfileReplica
    } else {
        constReplica(ProfileData.Guest) // Static guest profile
    }
}.withKey(isAuthenticatedFlow)
```

### Testing/Preview

```kotlin
// For Composable previews or testing
private val mockDataReplica = constReplica(
    listOf(
        Item(id = "1", name = "Sample Item 1"),
        Item(id = "2", name = "Sample Item 2"),
        Item(id = "3", name = "Sample Item 3")
    )
)
```

## Key Points

- Returns immediately with provided data (no loading state)
- Never fails (always in Content state)
- Useful for default values, guest modes, and testing
- Can be combined with real Replicas for fallback logic
