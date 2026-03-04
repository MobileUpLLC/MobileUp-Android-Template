# Example: Simple Component

A single screen that loads data via Replica and displays it using LceWidget.

---

## 1. Component Interface

```kotlin
interface ItemDetailsComponent {
    val itemDetailsState: StateFlow<LoadableState<ItemDetails>>

    fun onRetryClick()
    fun onLinkClick(url: String)
}
```

---

## 2. Real Implementation

```kotlin
class RealItemDetailsComponent(
    componentContext: ComponentContext,
    itemId: ItemId,
    errorHandler: ErrorHandler,
    repository: ItemRepository,
    private val externalAppService: ExternalAppService,
) : ComponentContext by componentContext, ItemDetailsComponent {

    private val itemDetailsReplica = repository.itemDetailsReplica.withKey(itemId)

    override val itemDetailsState = itemDetailsReplica.observe(this, errorHandler)

    override fun onRetryClick() {
        itemDetailsReplica.refresh()
    }

    override fun onLinkClick(url: String) {
        externalAppService.openUrl(url)
    }
}
```

---

## 3. Fake Implementation

```kotlin
class FakeItemDetailsComponent : ItemDetailsComponent {
    override val itemDetailsState = MutableStateFlow(LoadableState(data = ItemDetails.MOCK))

    override fun onRetryClick() = Unit
    override fun onLinkClick(url: String) = Unit
}
```

---

## 4. UI Composable

```kotlin
@Composable
fun ItemDetailsUi(
    component: ItemDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.itemDetailsState.collectAsState()

    LceWidget(
        modifier = modifier,
        state = state,
        onRetryClick = component::onRetryClick,
    ) { itemDetails, _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Content
        }
    }
}

@Preview
@Composable
private fun ItemDetailsUiPreview() {
    CustomTheme {
        ItemDetailsUi(component = FakeItemDetailsComponent())
    }
}
```

---

## Key Points

- **Replica observation** - Get Replica from Repository, call `observe(this, errorHandler)`
- **LceWidget** - Handles Loading/Content/Error states automatically
- **Fake for previews** - Mock data enables Composable previews
