# Example: Replica (no parameters, no pagination)

Use `createReplica` for fetching a single entity that doesn't depend on any external parameters and doesn't require pagination. A typical case is the current user's profile.

## Code Example

```kotlin
// data/repository/ProfileRepository.kt
interface ProfileRepository {
    val currentUserProfileReplica: Replica<Profile>
}

// data/repository/ProfileRepositoryImpl.kt
class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val replicaClient: ReplicaClient
) : ProfileRepository {

    override val currentUserProfileReplica: Replica<Profile> =
        replicaClient.createReplica(
            name = "currentUserProfile",
            settings = ReplicaSettings(staleTime = 5.minutes),
            tags = setOf(ReplicaTags.UserSpecificData),
            fetcher = {
                api.getCurrentUserProfile().toDomain()
            }
        )
}
```

## Key Points

- Expose the narrow `Replica<T>` interface, never `PhysicalReplica<T>`
- Use `ReplicaTags.UserSpecificData` for any data tied to the logged-in user so it is cleared on logout
- DTO → domain mapping (`toDomain()`) happens inside `fetcher`, not outside
- Don't catch exceptions manually — let them propagate; the `observe` extension in `core.utils` handles error localization
