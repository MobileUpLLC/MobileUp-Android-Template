# Example: Simple Screen (no data loading)

A settings screen with no async data: Scaffold + AppToolbar, static content, private composables.

This pattern applies to any screen where content doesn't come from a Replica — settings, about, info pages, etc.

---

## Full implementation

```kotlin
// SettingsUi.kt

@Composable
fun SettingsUi(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.systemBarsPadding(),
        containerColor = CustomTheme.colors.fill.secondary,
        topBar = {
            AppToolbar(
                title = stringResource(id = R.string.settings_title),
                backgroundColor = CustomTheme.colors.fill.secondary,
            )
        },
        content = {
            SettingsContent(
                component = component,
                modifier = Modifier.padding(it),
            )
        }
    )
}

@Composable
private fun SettingsContent(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SettingsRow(
            title = stringResource(R.string.settings_language),
            subtitle = component.currentLanguage,
            onClick = component::onLanguageClick,
        )
        SettingsRow(
            title = stringResource(R.string.settings_notifications),
            subtitle = stringResource(R.string.settings_notifications_subtitle),
            onClick = component::onNotificationsClick,
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Column {
            Text(
                text = title,
                style = CustomTheme.typography.body.bold,
            )
            Text(
                text = subtitle,
                style = CustomTheme.typography.caption.mRegular,
                color = CustomTheme.colors.text.secondary,
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun SettingsUiPreview() {
    AppTheme {
        SettingsUi(FakeSettingsComponent())
    }
}
```

---

## Key rules

- No `LceWidget` — this screen has no async data loading
- `systemBarsPadding()` on Scaffold root modifier handles both status and nav bars
- State collection (if any) lives in `SettingsContent`, not in the root `SettingsUi`
- `SettingsRow` is a pure private composable — no component reference, only data + callback
- Non-observable component properties (`component.currentLanguage`) are read directly without `collectAsState`
- `AppTheme { }` wraps all previews
