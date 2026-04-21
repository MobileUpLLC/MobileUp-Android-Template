# Example: KeyedReplica (with parameters, no pagination)

Use `createKeyedReplica` when the fetched data depends on external parameters (a key). Encapsulate multiple filter/sort options into a domain-level `data class` Query instead of passing raw primitives.

## Code Example

```kotlin
// domain/ReportQuery.kt
data class ReportQuery(
    val period: ReportPeriod,
    val category: ReportCategory
)

// data/repository/StatisticsRepository.kt
interface StatisticsRepository {
    val reportsReplica: KeyedReplica<ReportQuery, Report>
}

// data/repository/StatisticsRepositoryImpl.kt
class StatisticsRepositoryImpl(
    private val api: StatisticsApi,
    private val replicaClient: ReplicaClient
) : StatisticsRepository {

    override val reportsReplica: KeyedReplica<ReportQuery, Report> =
        replicaClient.createKeyedReplica(
            name = "reports",
            settings = KeyedReplicaSettings(maxCount = 20),
            childName = { "query: $it" },
            childSettings = { ReplicaSettings(staleTime = 5.minutes) },
            tags = setOf(ReplicaTags.UserSpecificData),
            fetcher = { query ->
                api.getReport(
                    period = query.period,
                    category = query.category
                ).toDomain()
            }
        )
}
```

## Key Points

- Use a domain `data class` or `value class` as the key — never raw strings or primitive maps
- `childName` receives the query as `it` — use `"query: $it"` to get a unique, human-readable name for each key variant for debugging
- For simple API query parameters, pass values directly from the query. Use `.value` when the field is a value class (e.g., `UserId`)
- For complex request body objects, use `XxxRequest.fromDomain(...)` from the DTO's companion object
- `childSettings` lets each cached variant have its own `staleTime`
- `maxCount` in `KeyedReplicaSettings` caps how many child replicas are kept in memory simultaneously
- Tag with `ReplicaTags.UserSpecificData` whenever the result is account-specific
