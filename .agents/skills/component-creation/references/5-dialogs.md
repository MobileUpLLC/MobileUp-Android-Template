# Example: Dialogs (AlertDialog & BottomSheet)

Shows how to implement StandardDialogControl, SimpleDialogControl and custom DialogControl with proper Fake implementations.

---

## 1. StandardDialogControl

Standard confirmation/alert dialogs.

### Component Interface

```kotlin
interface SettingsComponent {
    val settingsState: StateFlow<LoadableState<SettingsState>>
    val confirmDeleteDialog: StandardDialogControl

    fun onDeleteAccountClick()
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
        // TODO: исправить вызов
        confirmDeleteDialog.show(
            title = Res.string.settings_delete_account_title.resourceDesc(),
            message = Res.string.settings_delete_account_message.resourceDesc(),
            positiveButtonText = CoreRes.string.confirm.resourceDesc(),
            negativeButtonText = CoreRes.string.cancel.resourceDesc(),
            onResult = ::onDeleteAccountConfirmed
        )
    }

    private fun onDeleteAccountConfirmed(confirmed: Boolean) {
        // Handle account deletion
        onOutput(SettingsComponent.Output.AccountDeleted)
    }
}
```

### Fake Implementation

```kotlin
class FakeSettingsComponent : SettingsComponent {
    override val settingsState = MutableStateFlow(LoadableState(data = SettingsState.MOCK))

    override val confirmDeleteDialog = fakeStandardDialogControl()

    override fun onDeleteAccountClick() = Unit
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
## 2. SimpleDialogControl
Custom bottom sheet or dialog that renders plain data.

---

## 3. DialogControl
Custom bottom sheet or dialog that renders another component inside.

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
        },
        serializer = HowToComponent.Config.serializer()
    )

    override fun onHowToClick() {
        howToDialogControl.show(
            config = HowToComponent.Config(/* ... */)
        )
    }

    private fun onHowToOutput(output: HowToComponent.Output) {
        // Handle output
        howToDialogControl.dismiss()
    }
}
```

### Fake Implementation

```kotlin
class FakeProfileComponent : ProfileComponent {
    override val howToDialogControl = fakeDialogControl(FakeHowToComponent())

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
    
    BottomSheet(component.howToDialogControl) {
        HowToUi(component = it)
    }
}
```

---

## Key Points

### Dialog Rendering in UI:
- `StandardDialog(control)` - for alert dialogs
- `BottomSheet(control) { component -> }` - for custom bottom sheets 

### Fake Implementations:
- **StandardDialogControl** → `fakeStandardDialogControl()`
- **DialogControl<T>** → `fakeDialogControl<T>()`

