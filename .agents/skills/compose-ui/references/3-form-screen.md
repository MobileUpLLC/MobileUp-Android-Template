# Example: Form Screen

A profile creation/edit screen: multiple `collectAsState` calls, keyboard handling, bottom action button.

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
        modifier = modifier,
        containerColor = CustomTheme.colors.fill.secondary,
        topBar = {
            AppToolbar(
                title = stringResource(id = R.string.profile_edit_title),
                backgroundColor = CustomTheme.colors.fill.secondary,
                modifier = Modifier.statusBarsPadding(),
            )
        },
        bottomBar = {
            val isSaveEnabled by component.isSaveEnabled.collectAsState()
            val isSaveInProgress by component.isSaveInProgress.collectAsState()

            AppButton(
                text = stringResource(id = R.string.common_save),
                isEnabled = isSaveEnabled,
                isLoading = isSaveInProgress,
                buttonSize = ButtonSize.Medium,
                buttonType = ButtonType.Primary,
                onClick = component::onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 16.dp + navigationBarsPaddingDp(),
                    )
                    .noOverlapByMessage(),  // avoid button hidden behind message snackbar
            )
        },
        content = {
            ProfileEditContent(
                component = component,
                modifier = Modifier.padding(it),
            )
        }
    )
}

@Composable
private fun ProfileEditContent(
    component: ProfileEditComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()                 // push up on keyboard
            .verticalScroll(rememberScrollState()),
    ) {
        AppTextField(
            inputControl = component.nameInputControl,
            label = stringResource(id = R.string.profile_edit_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
        )

        AppTextField(
            inputControl = component.bioInputControl,
            minLines = 3,
            label = stringResource(id = R.string.profile_edit_bio),
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

## Variant: form that loads initial data (LceWidget)

When the form needs to pre-fill values from an API, wrap content in `LceWidget`:

```kotlin
@Composable
private fun ProfileEditContent(
    component: ProfileEditComponent,
    modifier: Modifier = Modifier,
) {
    val initialDataState by component.initialDataState.collectAsState()

    LceWidget(
        state = initialDataState,
        onRetryClick = component::onRetryClick,
    ) { _, _ ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            // form fields ...
        }
    }
}
```

---

## Key rules

- Action button is pinned to the bottom via `bottomBar` in Scaffold — never placed inside the scrollable Column
- Button in `bottomBar` uses `.noOverlapByMessage()` to avoid being hidden behind the message snackbar
- `imePadding()` goes on the **scrollable Column**, not on Scaffold
- Content uses `Modifier.padding(it)` so Scaffold provides correct padding for both toolbar and bottom bar
- Multiple `StateFlow`s for `bottomBar` are collected as separate `val` lines at the top of the `bottomBar` lambda
- `InputControl` fields from the Forms library are passed directly to `AppTextField` — no need to observe them manually
