# Example: Broadcasting Actions

Call `replicaClient.sendAction(action)` to trigger the mutation logic defined in all active behaviours across the app.

## Code Example

```kotlin
replicaClient.sendAction(ReplicaActions.UpdatePostLikeState(id, isLiked))
```

## Key Points

- Single action updates all listening Replicas
- No need to manually track which Replicas need updating
- Decouples mutation logic from UI actions
- Actions are processed synchronously
