# Example: Screen with BottomSheet

A screen that opens a BottomSheet to pick a value. The core `BottomSheet` composable handles everything — never use Material3 `ModalBottomSheet` directly.

---

## Full implementation

```kotlin
// ProfileEditUi.kt

@Composable
fun ProfileEditUi(
    component: ProfileEditComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        containerColor = CustomTheme.colors.fill.secondary,
        topBar = {
            AppToolbar(
                title = stringResource(id = R.string.profile_edit_title),
                backgroundColor = CustomTheme.colors.fill.secondary,
            )
        },
        content = {
            ProfileEditContent(
                component = component,
                modifier = Modifier.padding(it),
            )
        }
    )

    // BottomSheet placed AFTER Scaffold, at the composable root
    BottomSheet(
        dialogControl = component.selectCategoryDialogControl,
        skipPartiallyExpanded = true,
    ) { category ->
        SelectFromEnumUi(category)
    }
}

@Composable
private fun ProfileEditContent(
    component: ProfileEditComponent,
    modifier: Modifier = Modifier,
) {
    val selectedCategory by component.selectedCategory.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        SelectorCard(
            text = stringResource(id = R.string.profile_edit_category),
            isSelected = selectedCategory != null,
            selectorText = selectedCategory?.displayName.orEmpty(),
            onClick = component::onSelectCategoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
        )
    }
}

@Preview
@Composable
private fun ProfileEditUiPreview() {
    AppTheme {
        ProfileEditUi(FakeProfileEditComponent())
    }
}
```

---

## What BottomSheet handles automatically

| Handled by `BottomSheet` | You don't need to add |
|---|---|
| Show/hide animation | `sheetState`, `LaunchedEffect` |
| Dismiss on outside tap | `onDismissRequest` |
| `imePadding` inside sheet | `.imePadding()` on sheet content |
| `statusBarsPadding` | `Modifier.statusBarsPadding()` on sheet |
| Scrim (dark overlay) | `scrimColor` |
| Theming | `containerColor`, `shape`, `cornerRadius` |

---

## BottomSheet with custom content and handle

```kotlin
BottomSheet(
    dialogControl = component.actionsDialogControl,
    skipPartiallyExpanded = false,
) { data ->
    Column {
        BottomSheetHandle()  // drag handle at the top
        ActionListContent(
            items = data.actions,
            onActionClick = { component.onActionClick(it) },
        )
    }
}
```

---

## Key rules

- Import `BottomSheet` from the project's core, not from Material3
- `BottomSheet` and `StandardDialog` are always placed **after** Scaffold at the composable root
- `skipPartiallyExpanded = true` when the sheet should open fully (most cases)
- `skipPartiallyExpanded = false` when the sheet has variable height and partial expand makes sense
