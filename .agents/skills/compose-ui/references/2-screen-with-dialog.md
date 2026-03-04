# Example: Screen with Dialog

A settings screen that opens a confirmation dialog. `StandardDialog` is placed **after** the `Scaffold` block, at the composable root — never inside content.

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

    // Dialog is placed AFTER Scaffold, at the composable root
    StandardDialog(dialogControl = component.changeLanguageDialogControl)
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

## Multiple dialogs

When a screen has several dialogs or bottom sheets, all of them go after Scaffold:

```kotlin
@Composable
fun ProfileUi(component: ProfileComponent, modifier: Modifier = Modifier) {
    Scaffold(...) { ProfileContent(component, Modifier.padding(it)) }

    StandardDialog(component.deleteAccountDialogControl)
    StandardDialog(component.logoutDialogControl)
    BottomSheet(dialogControl = component.selectAvatarDialogControl) {
        AvatarPickerUi(it)
    }
}
```

---

## Key rules

- `StandardDialog` and `BottomSheet` always live **outside** Scaffold — they overlay the entire screen
- Order matters: dialogs declared last are rendered on top
- The `@Preview` shows the screen without the dialog open — that's expected
