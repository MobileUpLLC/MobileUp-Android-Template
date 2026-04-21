---
name: replica-creation
description: Guidelines for implementing data loading in the Data layer (RepositoryImpl)
---

# Replica Creator (Data Layer Configuration)

This skill focuses on setting up data sources in `XxxRepositoryImpl`. It covers the selection of Replica types, handling complex page metadata, and the project's error handling strategy.

## 1. Architectural Contract

- **Layering:** The implementation resides in `RepositoryImpl`.
- **Interface Exposure:** Always expose the replica interface type (`Replica<T>`, `KeyedReplica<K, T>`, `PagedReplica<T>`, `KeyedPagedReplica<K, T>`), never the `Physical*` counterparts.
- **Mapping:** All transformations (DTO -> Domain Entity and DTO -> Domain Page) MUST occur inside the `fetcher` block.

**Type Exposure Example:**

```
// ✅ GOOD: Expose replica interface
override val currentUserProfileReplica: Replica<Profile> = replicaClient.createReplica(name = "profile", ...)

// ❌ BAD: Expose physical type — leaks implementation details
override val currentUserProfileReplica: PhysicalReplica<Profile> = replicaClient.createReplica(name = "profile", ...)
```

**Why?**
- **Encapsulation:** Hides implementation details from consumers
- **Flexibility:** Allows transformations (map, combine) in Component layer
- **Type Safety:** Prevents accidental state modifications

## 2. Choosing Replica Type

Choose the appropriate Replica type based on whether your data needs parameters and pagination:

**Without Parameters:**
- `createReplica` - Simple data fetching (e.g., current user profile)
- `createPagedReplica` - Paginated data without filters (e.g., notifications feed)

**With Parameters (Query):**
- `createKeyedReplica` - Data that depends on parameters (e.g., statistics with date range)
- `createKeyedPagedReplica` - Paginated data with filters (e.g., user-specific posts feed)

### Decision Tree

```
Does data need parameters (Query)?
├─ NO
│  ├─ Is it paginated?
│  │  ├─ YES → createPagedReplica
│  │  └─ NO  → createReplica
│  │
└─ YES
   ├─ Is it paginated?
   │  ├─ YES → createKeyedPagedReplica
   │  └─ NO  → createKeyedReplica
```

## 3. Advanced Paging & Metadata

The project uses specialized `Page` implementations to carry extra metadata from the API to the UI. When implementing a `PagedFetcher`, choose the appropriate `Page` type:

| Domain Page Class | Extra Metadata | Use Case |
|---|---|---|
| `SimplePage<I>` | None | Standard infinite scroll. |
| `PageWithTotalAmount<I>` | `totalAmount: Int` | When the UI needs to show the total count of items. |
| `PageWithUserCityFlag<I>` | `isUserCity: Boolean` | When the list context depends on the user's location. |

## 4. Error Handling Strategy

In this project, the `fetcher` block should **not** manually catch exceptions unless a default value is required.

- **Logic:** Let exceptions propagate from the `fetcher`.
- **Processing:** Errors are caught and localized by the `observe` extension (in `core.utils`) during the Presentation layer observation.
- **Mapping:** The `LoadingError` from Replica is converted to a localized `StringDesc` via `errorMessage` extension and wrapped into `LoadableState`.

## 5. Summary of Implementation Rules

1. **DTO Mapping:** Keep API-specific logic (DTOs) strictly inside the `fetcher`.
2. **Page Choice:** Use `PageWithTotalAmount` if the backend provides a total count; it is often used for header statistics.
3. **Keyed Paging:** Use `createKeyedPagedReplica` for searchable or filtered lists with paging.
4. **Tags:** Always attach `ReplicaTags.UserSpecificData` for any data that belongs to the current account to ensure it is cleared on logout.
5. **Flow Dependencies:** Read `StateFlow.value` (like `userInfoFlow.value`) directly inside the `fetcher` to get the latest context at the moment of the request.

## Examples

> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Replica](references/1-replica.md) - No parameters, no pagination (e.g., current user profile)
- [KeyedReplica](references/2-keyed-replica.md) - With parameters, no pagination (e.g., filtered reports)
- [PagedReplica](references/3-paged-replica.md) - No parameters, with pagination (e.g., notifications feed)
- [KeyedPagedReplica](references/4-keyed-paged-replica.md) - With parameters and pagination (e.g., filtered posts feed)