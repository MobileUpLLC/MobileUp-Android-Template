# Example: List Screen with PullRefreshLceWidget

A list/feed screen: `PullRefreshLceWidget` for pull-to-refresh, `LazyColumn` for items.

---

## Full implementation

```kotlin
// ItemListUi.kt

@Composable
fun ItemListUi(
    component: ItemListComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = CustomTheme.colors.fill.secondary,
        topBar = {
            AppToolbar(
                title = stringResource(id = R.string.items_title),
                modifier = Modifier.statusBarsPadding(),
            )
        },
        content = {
            ItemListContent(
                component = component,
                modifier = Modifier.padding(top = it.calculateTopPadding()),
            )
        }
    )
}

@Composable
private fun ItemListContent(
    component: ItemListComponent,
    modifier: Modifier = Modifier,
) {
    val itemsState by component.itemsState.collectAsState()

    PullRefreshLceWidget(
        state = itemsState,
        onRefresh = component::onRefresh,
        onRetryClick = component::onRetryClick,
        modifier = modifier,
    ) { items, _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 8.dp,
                bottom = 16.dp + navigationBarsPaddingDp(),
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items, key = { it.id }) { item ->
                ItemCard(
                    item = item,
                    onClick = { component.onItemClick(item.id) },
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemListUiPreview() {
    AppTheme {
        ItemListUi(FakeItemListComponent())
    }
}
```

---

## Key rules

- `PullRefreshLceWidget` for anything that has a list or pull-to-refresh expectation
- `onRefresh` and `onRetryClick` can point to the same handler if behaviour is identical
- `navigationBarsPaddingDp()` as bottom `contentPadding` in `LazyColumn` — not on Scaffold
