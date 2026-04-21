---
name: compose-ui
description: Patterns for building Compose screens and widgets — CustomTheme, state binding, Scaffold + AppToolbar, LCE widgets, BottomSheet, and @Preview rules
---

# Compose UI — Screen & Widget Patterns

This skill defines how to write `XxxUi.kt` files: theme usage, state binding, Scaffold structure, LCE widgets, dialogs, BottomSheet, and @Preview requirements.

---

## 1. CustomTheme — NEVER MaterialTheme

All visual tokens come from `CustomTheme`. `MaterialTheme` is never used directly in UI code.

```kotlin
// ✅ CORRECT
color = CustomTheme.colors.text.primary
style = CustomTheme.typography.body.regular
color = CustomTheme.colors.fill.secondary

// ❌ WRONG
color = MaterialTheme.colorScheme.onSurface
style = MaterialTheme.typography.bodyMedium
```

Token names are **project-specific** — check the project's `CustomTheme` definition for available groups and names.

---

## 2. State Binding — collectAsState

Read component state using `collectAsState()` with delegation (`by`). Always import `getValue`.

```kotlin
val dataState by component.dataState.collectAsState()
val isLoading by component.isLoading.collectAsState()
```

**Rules:**
- Collect state in the **content composable**, not at the root Scaffold level
- Each `StateFlow` is collected separately — never inline inside `LceWidget`/`if`
- Multiple flows are collected at the top of the content function before layout code
- Pass events via method references: `onClick = component::onRetryClick`

```kotlin
@Composable
private fun ScreenContent(
    component: XxxComponent,
    modifier: Modifier = Modifier,
) {
    val dataState by component.dataState.collectAsState()
    val isActionLoading by component.isActionLoading.collectAsState()

    LceWidget(
        state = dataState,
        onRetryClick = component::onRetryClick,
    ) { data, _ ->
        // content using data
    }
}
```

---

## 3. Scaffold with AppToolbar

Standard screen layout: `Scaffold` at the root, `AppToolbar` in `topBar`, private content function.

```kotlin
@Composable
fun XxxUi(
    component: XxxComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        containerColor = CustomTheme.colors.fill.secondary,
        topBar = {
            AppToolbar(
                title = stringResource(id = R.string.xxx_title),
                backgroundColor = CustomTheme.colors.fill.secondary,
            )
        },
        content = {
            XxxContent(
                component = component,
                modifier = Modifier.padding(it),
            )
        }
    )
}
```

**Alternative — statusBarsPadding on toolbar** (when top padding calculation is needed separately):
```kotlin
Scaffold(
    containerColor = CustomTheme.colors.fill.secondary,
    topBar = {
        AppToolbar(
            title = stringResource(id = R.string.xxx_title),
            modifier = Modifier.statusBarsPadding(),
        )
    },
    content = {
        XxxContent(
            component = component,
            modifier = Modifier.padding(top = it.calculateTopPadding()),
        )
    }
)
```

**Toolbar with trailing action:**
```kotlin
AppToolbar(
    title = stringResource(id = R.string.xxx_title),
    trailingAction = {
        AppToolbarTrailingActionText(
            text = stringResource(id = R.string.common_save),
            onClick = component::onSaveClick,
        )
    }
)
```

### Scaffold background

Set `containerColor` on Scaffold, not `.background()` on content modifier:
```kotlin
Scaffold(
    containerColor = CustomTheme.colors.fill.secondary,
    ...
)
```

---

## 4. LCE Pattern

Choose the widget based on whether the user expects pull-to-refresh.

**`PullRefreshLceWidget`** — preferred for lists, feeds, and dashboards:
```kotlin
PullRefreshLceWidget(
    state = itemsState,
    onRefresh = component::onRefresh,
    onRetryClick = component::onRetryClick,
) { data, _ ->
    // show list
}
```

**`LceWidget`** — for detail or form screens where pull-to-refresh doesn't make sense:
```kotlin
LceWidget(
    state = detailState,
    onRetryClick = component::onRetryClick,
) { data, _ ->
    // show detail
}
```

The second lambda param (`_`) is `refreshing: Boolean` — use it when an inline refresh indicator is needed.

**Not all screens need LCE.** If there's no async data loading (e.g. a static settings screen), skip LCE entirely and render content directly.

---

## 5. Dialogs and BottomSheet

Dialogs and BottomSheets are placed **after** the `Scaffold` block, at the composable root:

```kotlin
@Composable
fun XxxUi(component: XxxComponent, modifier: Modifier = Modifier) {
    Scaffold(...) { ... }

    StandardDialog(component.confirmDialogControl)

    BottomSheet(dialogControl = component.selectDialogControl) {
        // sheet content
    }
}
```

### BottomSheet

Use the project's `BottomSheet` composable from core — **never assemble a bottom sheet manually from Material3 primitives**.

```kotlin
BottomSheet(
    dialogControl = component.selectCategoryDialogControl,
    skipPartiallyExpanded = true,
) { item ->
    SelectFromEnumUi(item)
}
```

`BottomSheet` handles internally: show/hide animation, dismiss control, `imePadding`, `statusBarsPadding`, scrim, and theming. No manual `ModalBottomSheet` setup needed.

Use `BottomSheetHandle()` to add a visible drag handle at the top of sheet content when needed.

---

## 6. Edge-to-edge & safe areas

| Situation | Solution |
|---|---|
| System bars (no keyboard) | `systemBarsPadding()` on Scaffold root modifier |
| Toolbar at top only | `statusBarsPadding()` on `AppToolbar` |
| Content above nav bar | `.padding(bottom = 16.dp + navigationBarsPaddingDp())` |
| Keyboard push-up | `.imePadding()` on scrollable content |
| Both system bars + keyboard | `systemBarsWithImePadding()` on Scaffold root modifier |

---

## 7. Private composables

Split into root (Scaffold + dialogs) and private content composable(s). Sub-widgets receive only data and callbacks — no component reference.

```kotlin
// Root — no state collection here
@Composable
fun XxxUi(component: XxxComponent, modifier: Modifier = Modifier) {
    Scaffold(...) { XxxContent(component, Modifier.padding(it)) }
    StandardDialog(component.dialogControl)
}

// Content — state collected here
@Composable
private fun XxxContent(component: XxxComponent, modifier: Modifier = Modifier) {
    val dataState by component.dataState.collectAsState()
    ...
}

// Sub-widget — pure, no component reference
@Composable
private fun ItemCard(item: Item, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ...
}
```

---

## 8. Mandatory @Preview Rules

**Every `XxxUi.kt` MUST have a @Preview** — for screens and standalone widgets alike.

### Screen Preview

```kotlin
@Preview
@Composable
private fun XxxUiPreview() {
    AppTheme {
        XxxUi(component = FakeXxxComponent())
    }
}
```

- Always wrap in `AppTheme { }` (not `CustomTheme { }`)
- Use `FakeXxxComponent()` as the data source
- Function is `private`, named `XxxUiPreview` for screens and `XxxPreview` for widgets
- Add `showSystemUi = true` for full-screen layouts

### Widget Preview

```kotlin
@Preview
@Composable
private fun ItemCardPreview() {
    AppTheme {
        ItemCard(
            item = Item.MOCK,
            onClick = {}
        )
    }
}
```

- Use `MOCKS` / companion object mock data
- Preview is in the **same file** as the widget
- Add multiple `@Preview` annotations for different states (loading, empty, error)

---

## Examples

> Load examples only when needed — don't load all at once.

- [Simple screen](references/1-simple-screen.md) — Scaffold + AppToolbar, no data loading (settings pattern)
- [Screen with Dialog](references/2-screen-with-dialog.md) — StandardDialog placed outside Scaffold
- [Form screen](references/3-form-screen.md) — multiple collectAsState, imePadding, bottom button
- [List screen with PullRefreshLceWidget](references/4-pull-refresh-screen.md) — list/feed pattern
- [Screen with BottomSheet](references/5-bottomsheet-screen.md) — using core BottomSheet
- [Widget with Preview](references/6-widget-preview.md) — standalone widget + @Preview
