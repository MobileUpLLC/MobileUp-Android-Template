# Example: KeyedPagedReplica (with parameters, with pagination)

Use `createKeyedPagedReplica` for paginated lists that depend on filter or search parameters — for example, a city search filtered by name and country. The key is a domain `data class` encapsulating all filter options.

Same conventions as `PagedReplica`:
- `typealias` for the page type
- Domain `XxxData` class that the component maps `PagedData` into
- `emptyPage()` when the query state doesn't warrant a real request

## Code Example

```kotlin
// domain/CitySearchQuery.kt
data class CitySearchQuery(
    val searchQuery: String?,  // null = no search yet, return empty
    val country: String?
)

// domain/CitiesData.kt
data class CitiesData(
    val cities: List<City>,
    val hasNextPage: Boolean
) {
    companion object {
        val MOCK = CitiesData(
            cities = City.MOCKS,
            hasNextPage = false
        )
    }
}

// data/dto/CityResponse.kt
typealias CitySearchPage = PageWithTotalAmount<City>

internal fun ApiPageResponse<CityResponse>.toDomain(pageSize: Int): CitySearchPage {
    return CitySearchPage(
        totalAmount = total,
        items = data.map { it.toDomain() },
        hasNextPage = data.size >= pageSize
    )
}

// data/repository/CityRepository.kt
interface CityRepository {
    val citySearchReplica: KeyedPagedReplica<CitySearchQuery, CitiesData>
}

// data/repository/CityRepositoryImpl.kt
class CityRepositoryImpl(
    private val api: CityApi,
    private val replicaClient: ReplicaClient
) : CityRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override val citySearchReplica: KeyedPagedReplica<CitySearchQuery, CitiesData> =
        replicaClient.createKeyedPagedReplica(
            name = "citySearch",
            settings = KeyedPagedReplicaSettings(maxCount = 5),
            childName = { "query: $it" },
            childSettings = { PagedReplicaSettings(staleTime = 10.minutes) },
            idExtractor = { it.id },
            fetcher = object : KeyedPagedFetcher<CitySearchQuery, City, CitySearchPage> {
                override suspend fun fetchFirstPage(key: CitySearchQuery): CitySearchPage {
                    if (key.searchQuery == null) return CitySearchPage.emptyPage()

                    return api.getCities(
                        searchQuery = key.searchQuery,
                        country = key.country,
                        page = 1,
                        pageSize = PAGE_SIZE,
                    ).toDomain(PAGE_SIZE)
                }

                override suspend fun fetchNextPage(
                    key: CitySearchQuery,
                    currentData: PagedData<City, CitySearchPage>,
                ): CitySearchPage {
                    if (key.searchQuery == null) return CitySearchPage.emptyPage()

                    return api.getCities(
                        searchQuery = key.searchQuery,
                        country = key.country,
                        page = currentData.pages.size + 1,
                        pageSize = PAGE_SIZE,
                    ).toDomain(PAGE_SIZE)
                }
            }
        ).map { _, data ->
            CitiesData(
                cities = data.items,
                hasNextPage = data.hasNextPage,
            )
        }
}
```

## Key Points

- `KeyedPagedFetcher<K, I, P>` — both `fetchFirstPage(key)` and `fetchNextPage(key, currentData)` receive the key directly, no closure needed
- `CitySearchPage.emptyPage()` — call when the query state doesn't warrant a network request; resolves to `PageWithTotalAmount.emptyPage()` via the typealias
- `.map { _, data -> XxxData(...) }` transforms the replica after creation — `data` is `PagedData<I, P>`, use `data.items` and `data.hasNextPage`
- `KeyedPagedReplicaSettings(maxCount = N)` limits how many key variants are cached simultaneously
- `childName = { "query: $it" }` uses the query's `toString()` — ensure `data class` fields cover all filter dimensions
- For simple API query parameters, pass values directly from the key. Use `.value` when the field is a value class (e.g., `UserId`)
- For complex request body objects, use `XxxRequest.fromDomain(key)` from the DTO's companion object
