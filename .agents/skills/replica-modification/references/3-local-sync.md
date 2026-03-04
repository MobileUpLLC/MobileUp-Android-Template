# Example: Local Sync After API Call

Inside Repository methods, update the cache manually after a successful API call to provide an instant UI update.

## Code Example

```kotlin
override suspend fun sendResponse(id: InvitationId) {
    api.postResponse(id) // API Call first

    // Manually update specific item state in Keyed Replica
    invitationDetailsReplica.mutateData(id) {
        it.copy(status = InvitationStatus.Accepted)
    }
}
```

## Key Points

- The API call is made first
- After successful response, `mutateData` immediately updates the local cache
- The UI reflects the change instantly without waiting for a full refresh
- Use for optimistic UI updates
