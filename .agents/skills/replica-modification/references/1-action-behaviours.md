# Example: Handling Actions via Behaviours

When defining a Replica in the Repository, use `behaviours` (or `childBehaviours` for keyed replicas) to listen for specific actions.

## Code Example

```kotlin
override val postDetailsReplica = replicaClient.createKeyedReplica(
    // ...
    childBehaviours = { postId ->
        listOf(
            ReplicaBehaviour.mutateOnAction { action: ReplicaActions.UpdatePostLikeState, post: Post ->
                // Check if the action belongs to this specific post and update locally
                post.withPostLikeState(postId, action.isLikedByCurrentUser)
            }
        )
    }
)
```

## Key Points

- Define behaviours when creating Replica
- Use `childBehaviours` for KeyedReplica
- Actions trigger mutations across all Replicas that listen for them
- Great for global state synchronization (e.g., likes, favorites)
