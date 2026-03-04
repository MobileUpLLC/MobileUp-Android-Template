# Example: PagedReplica (no parameters, with pagination)

Use `createPagedReplica` for paginated lists that don't need filtering parameters — for example, a notifications feed. When the API also returns a total item count, use `PageWithTotalAmount` to carry that metadata.

Three conventions to follow:
- Define a `typealias` for the page type to keep generic signatures readable
- Define a domain `XxxData` class that wraps items + metadata for the UI
- Use `.map { data -> ... }` on the replica itself to produce `XxxData` — the component receives the data ready to use

## Code Example

```kotlin
// domain/NotificationData.kt
data class NotificationData(
    val notificationsAmount: Int,
    val notifications: List<Notification>,
    val hasNextPage: Boolean
) {
    companion object {
        val MOCK = NotificationData(
            notificationsAmount = Notification.MOCKS.size,
            notifications = Notification.MOCKS,
            hasNextPage = false
        )
    }
}

// data/dto/NotificationResponse.kt
typealias NotificationsPage = PageWithTotalAmount<Notification>

internal fun ApiPageResponse<NotificationResponse>.toDomain(pageSize: Int): NotificationsPage {
    return NotificationsPage(
        totalAmount = total,
        items = data.map { it.toDomain() },
        hasNextPage = data.size >= pageSize
    )
}

// data/repository/NotificationsRepository.kt
interface NotificationsRepository {
    val notificationsReplica: PagedReplica<NotificationData>
}

// data/repository/NotificationsRepositoryImpl.kt
class NotificationsRepositoryImpl(
    private val api: NotificationsApi,
    private val replicaClient: ReplicaClient
) : NotificationsRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override val notificationsReplica: PagedReplica<NotificationData> =
        replicaClient.createPagedReplica(
            name = "notifications",
            settings = PagedReplicaSettings(staleTime = 5.minutes),
            idExtractor = { it.id.value },
            tags = setOf(ReplicaTags.UserSpecificData),
            fetcher = object : PagedFetcher<Notification, NotificationsPage> {
                override suspend fun fetchFirstPage(): NotificationsPage {
                    return api.getNotifications(page = 1, pageSize = PAGE_SIZE).toDomain(PAGE_SIZE)
                }

                override suspend fun fetchNextPage(
                    currentData: PagedData<Notification, NotificationsPage>
                ): NotificationsPage {
                    return api.getNotifications(
                        page = currentData.pages.size + 1,
                        pageSize = PAGE_SIZE
                    ).toDomain(PAGE_SIZE)
                }
            }
        ).map { data ->
            NotificationData(
                notificationsAmount = data.pages.lastOrNull()?.totalAmount ?: 0,
                notifications = data.items,
                hasNextPage = data.hasNextPage
            )
        }
}
```

## Key Points

- The interface exposes `PagedReplica<NotificationData>` — the `XxxData` mapping happens in the repository via `.map { data -> ... }`, not in the component
- `data` in `.map` is `PagedData<Notification, NotificationsPage>` — use `data.items`, `data.hasNextPage`, and `data.pages.lastOrNull()` to access page metadata
- Define `typealias XxxPage = PageWithTotalAmount<Xxx>` next to the DTO mapping to avoid repeating the generic in `PagedFetcher`
- `toDomain(pageSize)` receives the page size to compute `hasNextPage = data.size >= pageSize`
- `idExtractor = { it.id.value }` — use `.value` when the ID field is a value class
- `idExtractor` is required: it uniquely identifies each item so duplicates are detected across pages
- Use `SimplePage` instead of `PageWithTotalAmount` if no extra metadata is needed
- `fetchNextPage` receives `currentData` — use `currentData.pages.size` to compute the next page number
