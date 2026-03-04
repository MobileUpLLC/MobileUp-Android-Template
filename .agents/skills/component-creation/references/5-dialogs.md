# Example: Dialogs (AlertDialog & BottomSheet)

Shows how to implement StandardDialogControl, SelectFromEnumDialogControl, and custom DialogControl with proper Fake implementations.

---

## 1. StandardDialogControl (AlertDialog)

Standard confirmation/alert dialogs.

### Component Interface

```kotlin
interface SettingsComponent {
    val settingsState: StateFlow<LoadableState<SettingsState>>
    val confirmDeleteDialog: StandardDialogControl

    fun onDeleteAccountClick()
    fun onResetSettingsClick()
}
```

### Real Implementation

```kotlin
class RealSettingsComponent(
    componentContext: ComponentContext,
    private val repository: SettingsRepository,
    private val errorHandler: ErrorHandler,
    private val onOutput: (SettingsComponent.Output) -> Unit
) : ComponentContext by componentContext, SettingsComponent {

    private val settingsReplica = repository.settingsReplica

    override val settingsState = settingsReplica.observe(this, errorHandler)

    override val confirmDeleteDialog = StandardDialogControl(componentContext)

    override fun onDeleteAccountClick() {
        confirmDeleteDialog.show(
            title = StringDesc.Resource(R.string.settings_delete_account_title),
            message = StringDesc.Resource(R.string.settings_delete_account_message),
            positiveButtonText = StringDesc.Resource(R.string.settings_delete_confirm),
            negativeButtonText = StringDesc.Resource(R.string.cancel),
            onResult = ::onDeleteAccountConfirmed
        )
    }

    private fun onDeleteAccountConfirmed(confirmed: Boolean) {
        if (confirmed) {
            // Handle account deletion
            onOutput(SettingsComponent.Output.AccountDeleted)
        }
    }

    override fun onResetSettingsClick() {
        confirmDeleteDialog.show(
            title = StringDesc.Resource(R.string.settings_reset_title),
            message = StringDesc.Resource(R.string.settings_reset_message),
            positiveButtonText = StringDesc.Resource(R.string.settings_reset_confirm),
            negativeButtonText = StringDesc.Resource(R.string.cancel),
            onResult = { confirmed ->
                if (confirmed) {
                    repository.resetSettings()
                }
            }
        )
    }
}
```

### Fake Implementation

```kotlin
class FakeSettingsComponent : SettingsComponent {
    override val settingsState = MutableStateFlow(
        LoadableState.Content(SettingsState.MOCK)
    )

    override val confirmDeleteDialog = fakeStandardDialogControl()

    override fun onDeleteAccountClick() = Unit
    override fun onResetSettingsClick() = Unit
}
```

### UI Composable

```kotlin
@Composable
fun SettingsUi(
    component: SettingsComponent,
    modifier: Modifier = Modifier
) {
    val settingsState by component.settingsState.collectAsState()

    // Dialog rendering
    StandardDialog(component.confirmDeleteDialog)

    LceWidget(
        state = settingsState,
        onRetryClick = { /* ... */ }
    ) { settings, _ ->
        Column(modifier = modifier.fillMaxSize()) {
            CustomButton(
                text = "Delete Account",
                onClick = component::onDeleteAccountClick
            )

            CustomButton(
                text = "Reset Settings",
                onClick = component::onResetSettingsClick
            )
        }
    }
}

@Preview
@Composable
private fun SettingsUiPreview() {
    CustomTheme {
        SettingsUi(component = FakeSettingsComponent())
    }
}
```

---

## 2. SelectFromEnumDialogControl (BottomSheet)

Bottom sheet with standard selection list from enum.

### Component Interface

```kotlin
interface PreferencesComponent {
    val preferencesState: StateFlow<PreferencesState>
    val themeSelector: SelectFromEnumDialogControl<Theme>
    val languageSelector: SelectFromEnumDialogControl<Language>

    fun onSelectThemeClick()
    fun onSelectLanguageClick()
}
```

**Note:** Enum classes (Theme, Language) should be defined in the **domain layer**, not in presentation.

```kotlin
// In domain layer
enum class Theme {
    Light, Dark, System
}

enum class Language {
    English, Russian, French
}
```

### Real Implementation

```kotlin
class RealPreferencesComponent(
    componentContext: ComponentContext,
    private val repository: PreferencesRepository
) : ComponentContext by componentContext, PreferencesComponent {

    private val currentTheme = MutableStateFlow(Theme.System)
    private val currentLanguage = MutableStateFlow(Language.English)

    override val preferencesState = computed(
        currentTheme,
        currentLanguage
    ) { theme, language ->
        PreferencesState(theme = theme, language = language)
    }

    override val themeSelector = SelectFromEnumDialogControl(
        componentContext = componentContext,
        dismissOnBackPress = true
    )

    override val languageSelector = SelectFromEnumDialogControl(
        componentContext = componentContext,
        dismissOnBackPress = true
    )

    override fun onSelectThemeClick() {
        themeSelector.show(
            values = Theme.entries,
            selected = currentTheme.value,
            onDismiss = ::onThemeSelected
        )
    }

    private fun onThemeSelected(theme: Theme?) {
        theme?.let {
            currentTheme.value = it
            repository.saveTheme(it)
        }
    }

    override fun onSelectLanguageClick() {
        languageSelector.show(
            values = Language.entries,
            selected = currentLanguage.value,
            onDismiss = ::onLanguageSelected
        )
    }

    private fun onLanguageSelected(language: Language?) {
        language?.let {
            currentLanguage.value = it
            repository.saveLanguage(it)
        }
    }
}
```

### Fake Implementation

```kotlin
class FakePreferencesComponent : PreferencesComponent {
    override val preferencesState = MutableStateFlow(PreferencesState.MOCK)

    override val themeSelector = fakeSelectFromEnumDialogControl<Theme>()
    override val languageSelector = fakeSelectFromEnumDialogControl<Language>()

    override fun onSelectThemeClick() = Unit
    override fun onSelectLanguageClick() = Unit
}
```

### UI Composable

```kotlin
@Composable
fun PreferencesUi(
    component: PreferencesComponent,
    modifier: Modifier = Modifier
) {
    val preferencesState by component.preferencesState.collectAsState()

    // Bottom sheets rendering
    BottomSheet(
        dialogControl = component.themeSelector,
        skipPartiallyExpanded = true
    ) {
        SelectFromEnumUi(it)
    }

    BottomSheet(
        dialogControl = component.languageSelector,
        skipPartiallyExpanded = true
    ) {
        SelectFromEnumUi(it)
    }

    Column(modifier = modifier.fillMaxSize()) {
        SettingRow(
            title = "Theme",
            value = preferencesState.theme.name,
            onClick = component::onSelectThemeClick
        )

        SettingRow(
            title = "Language",
            value = preferencesState.language.name,
            onClick = component::onSelectLanguageClick
        )
    }
}

@Preview
@Composable
private fun PreferencesUiPreview() {
    CustomTheme {
        PreferencesUi(component = FakePreferencesComponent())
    }
}
```

---

## 3. DialogControl (Custom BottomSheet with Component)

Custom bottom sheet that renders another component inside.

### Component Interface

```kotlin
interface ProfileComponent {
    val howToDialogControl: DialogControl<HowToComponent.Config, HowToComponent>

    fun onHowToClick()
}
```

### Real Implementation

```kotlin
class RealProfileComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, ProfileComponent {

    override val howToDialogControl = dialogControl<HowToComponent.Config, HowToComponent>(
        key = "howToDialog",
        dialogComponentFactory = { config, context, _ ->
            componentFactory.createHowToComponent(
                componentContext = context,
                // ... pass config params
                onOutput = ::onHowToOutput
            )
        }
    )

    override fun onHowToClick() {
        howToDialogControl.show(
            config = HowToComponent.Config(/* ... */)
        )
    }

    private fun onHowToOutput(output: HowToComponent.Output) {
        // Handle output, dismiss dialog
    }
}
```

### Fake Implementation

```kotlin
class FakeProfileComponent : ProfileComponent {
    override val howToDialogControl = fakeDialogControl<HowToComponent.Config, HowToComponent>(
        config = HowToComponent.Config(/* ... */),
        component = FakeHowToComponent()
    )

    override fun onHowToClick() = Unit
}
```

### UI Composable

```kotlin
@Composable
fun ProfileUi(
    component: ProfileComponent,
    modifier: Modifier = Modifier
) {
    // ... main content
    
    BottomSheet(
        dialogControl = component.howToDialogControl,
        skipPartiallyExpanded = true,
    ) {
        HowToUi(component = it)
    }
}
```

---

## Key Points

### Fake Implementations:
- **StandardDialogControl** → `fakeStandardDialogControl()`
- **SelectFromEnumDialogControl<T>** → `fakeSelectFromEnumDialogControl<T>()`
- **DialogControl<T>** → `fakeDialogControl<T>()`

### Dialog Rendering in UI:
Always render dialogs in the Composable:
- `StandardDialog(control)` - for AlertDialog
- `BottomSheet(dialogControl, skipPartiallyExpanded = true) { SelectFromEnumUi(it) }` - for enum selection
- `Dialog(control) { data, onDismiss -> }` - for custom bottom sheets

### Data for Custom Dialogs:
- Create data class for dialog parameters
- Add `.MOCK` companion object for Fake usage
- Pass data via `show(data = ..., onDismiss = ...)`

### Dialog Dismissal:
- `onDismiss` callback receives result (nullable)
- `null` = dialog dismissed without action
- Non-null = user selected/confirmed something
