---
name: replica-modification
description: Guidelines for imperative data modification, global actions, and cache invalidation
---

# Replica Modifier (Repository Layer Mutations)

This skill focuses on managing the data state after mutations (POST/PUT/DELETE). It covers local cache updates, global action broadcasting, and tag-based invalidation.

## 1. Replica Actions & Behaviours

`ReplicaActions` (located in `<package>.core.network.ReplicaActions`) are global events used to synchronize data across multiple replicas without a full network refresh.

### Handling Actions via Behaviours
When defining a Replica in the Repository, use `behaviours` (or `childBehaviours` for keyed replicas) to listen for specific actions.

### Broadcasting Actions
Call `replicaClient.sendAction(action)` to trigger the mutation logic defined in all active behaviours across the app.

## 2. Imperative Modifications (`mutateData` & `setData`)

Inside Repository methods, update the cache manually after a successful API call to provide an instant UI update.

### `mutateData`
Modifies the existing cached data using a transformation function.
- **Simple Replica:** `replica.mutateData { it.copy(status = newStatus) }`
- **Keyed Replica:** `replica.mutateData(key) { it.copy(status = newStatus) }`

## 3. Cache & Tags Management

Tags are used for bulk operations like invalidation or clearing. All tags are centralized in: `<package>.core.network.ReplicaTags`.

### Primary Tag: `UserSpecificData`
The most common tag. Use it for any replica containing data belonging to the logged-in user. This ensures the cache is wiped correctly when calling `clearByTag` during Logout.

### Invalidation Logic
Invalidation marks data as "stale," forcing a background refresh if there are active observers.

- **Direct Invalidation:** `replica.invalidate()`
- **Bulk Invalidation by Tag:** Use `replicaClient.invalidateByTag(tag)` to mark all replicas with a specific tag as stale.

### Advanced Keyed Invalidation
For `KeyedPagedReplica`, you can target specific children or invalidate the whole set.

- **Iterate and Invalidate:** Use `onEachPagedReplica` to selectively invalidate specific keys.
- **Invalidate All:** Use `invalidateAll()` to mark all cached keys as stale.

## 4. Summary of Conventions

1.  **Sync Priority:** Use `mutateData` for immediate local updates after a successful API call.
2.  **Global Events:** Use `ReplicaActions` to sync states (like "Like" status or "Member Status") that appear on multiple screens.
3.  **Correct Tagging:** Always attach `ReplicaTags.UserSpecificData` to account-related replicas.
4.  **No Logic in UI:** All `mutateData`, `invalidate`, and `sendAction` calls must stay in the **Repository** layer. The Component layer only triggers these Repository methods.
5.  **Selective Cleanup:** Use `onEachPagedReplica` to avoid heavy global refreshes when only a subset of the cache is affected.

## Examples

> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Action Behaviours](references/1-action-behaviours.md) - Listening for global actions
- [Broadcast Actions](references/2-broadcast-actions.md) - Triggering mutations across app
- [Local Sync](references/3-local-sync.md) - Instant UI updates after API calls
- [Tag Invalidation](references/4-tag-invalidation.md) - Bulk stale marking
- [Keyed Invalidation](references/5-keyed-invalidation.md) - Selective cache invalidation