---
name: replica-observing
description: Guidelines for integrating Replicas into the Presentation layer (Components)
---

# Replica Observer (Presentation Layer Integration)

This document defines the patterns for consuming, transforming, and merging Replicas with UI state streams within `RealXxxComponent` classes.

## 1. Core Observation Pattern

In this project, we follow a strict naming and visibility convention to separate the data source from the UI state:

1. **`xxxReplica`**: A `private val` that holds the `Replica<T>` (after all algebra transformations are applied).
2. **`xxxState`**: An `override val` that holds the `StateFlow<LoadableState<T>>` produced by the `.observe()` extension.

## 2. Deriving Parameters with `computed`

Use `computed` to create reactive Query objects from one or more UI `StateFlows` (e.g., search text, selected tabs, filters). The `computed` function supports up to 14 parameters and automatically recomputes when any parameter changes.

## 3. Integrating UI Flows (`flowReplica`)

Use `flowReplica` to treat a standard `Flow` or `StateFlow` (like user preferences from `Settings` / `SettingsFactory` abstraction) as a `Replica`. This allows it to participate in Replica Algebra (like `combine` with API data).

## 4. Keyed Replicas and UX

### `withKey`
Binds a `KeyedReplica` to a specific key or a `StateFlow` of keys.

### `keepPreviousData`
**Crucial for UX.** Use this on `KeyedPagedReplica` or `KeyedReplica` to keep showing current data while a new key is being loaded. This prevents the screen from flickering back to a loading state during search or tab switching.

## 5. Replica Algebra (Composition)

### `combine`
Merges multiple Replicas into one. The result emits data only when **all** source Replicas have successfully loaded.

### `associate`
Use for conditional logic flows, such as switching between different Replica sources based on a condition (e.g., switching between Authorized and Guest data sources).

### `constReplica`
Returns a Replica with static placeholder data. Useful for providing default/fallback data or for testing.

## 6. Utility Extensions

- **`toReplica()`**: Transforms a `PagedReplica` into a normal `Replica` for use in `combine`.
- **`map`**: Transforms data type: `replica.map { it.toViewData() }`.
- **`requireNotNull(message)`**: Throws an error if data is null, moving the Replica into an Error state.
- **`refresh()`**: Manually triggers a network request.
- **`revalidate()`**: Triggers a request only if data is stale.

## 7. Summary of Conventions

1. **Naming:** `xxxReplica` (private source) -> `xxxState` (public LCE flow).
2. **Context:** Use `observe(this, errorHandler)` inside `RealComponent`.
3. **Reactive Binding:** Use `withKey(StateFlow)` instead of manual key management.
4. **UX Polish:** Always use `keepPreviousData()` for searchable or filtered lists.
5. **Logic Location:** Keep heavy filtering/sorting in domain extension functions, applied inside the `combine` or `map` blocks.

## Examples

> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Standard Observation](references/1-standard-observation.md) - Basic pattern
- [Computed Query](references/2-computed-query.md) - Reactive parameters with `computed`
- [Flow Integration](references/3-flow-integration.md) - Using `flowReplica` with preferences
- [Keep Previous Data](references/4-keep-previous-data.md) - Preventing loading flicker
- [Combine Replicas](references/5-combine-replicas.md) - Merging multiple sources
- [Conditional Associate](references/6-conditional-associate.md) - Auth-based branching
- [Const Replica](references/7-const-replica.md) - Static placeholder data
