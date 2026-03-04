# Example: Adding New Screen to Router

Step-by-step guide for adding a new screen to an existing router component.

---

## Scenario

We have a `FlowComponent` router with Form and Results screens. We want to add a **Options** screen.

**Initial structure:**
```
FlowComponent (router)
  ├── InputFormComponent
  └── ResultsListComponent
```

**Target structure:**
```
FlowComponent (router)
  ├── InputFormComponent
  ├── ResultsListComponent
  └── OptionsComponent (NEW)
```

---

## Step 1: Create Four Files for New Component

Create the new screen component following the four-part structure:

```
features/.../search/presentation/options/
  ├── OptionsComponent.kt        # Interface
  ├── RealOptionsComponent.kt    # Implementation
  ├── FakeOptionsComponent.kt    # Mock
  └── OptionsUi.kt               # Composable
```

**OptionsComponent.kt:**
```kotlin
interface OptionsComponent {
    val state: StateFlow<OptionsState>

    fun onOptionClick(category: Option)
    fun onApplyClick()

    sealed interface Output {
        data class Applied(val options: Options) : Output
        data object CloseRequested : Output
    }
}
```

---

## Step 2: Add ComponentFactory Extension

**In `DI.kt`:**

```kotlin
fun ComponentFactory.createOptionsComponent(
    componentContext: ComponentContext,
    currentOptions: Options,
    onOutput: (OptionsComponent.Output) -> Unit
): OptionsComponent {
    return RealOptionsComponent(
        componentContext,
        currentOptions,
        onOutput,
        get() // Dependencies from Koin
    )
}
```

---

## Step 3: Update Router's ChildConfig

**In `RealFlowComponent.kt`:**

```kotlin
@Serializable
private sealed interface ChildConfig {
    @Serializable
    data object Form : ChildConfig

    @Serializable
    data class Results(val query: String) : ChildConfig

    // ✅ ADD THIS
    @Serializable
    data class Options(val currentOptions: com.example.domain.Options) : ChildConfig
}
```

**Note:** If your ChildConfig parameter is a complex object (like `Options`), ensure it's `@Serializable`:

```kotlin
@Serializable
data class Options(
    val categories: List<String>,
    val priceRange: PriceRange
)
```

---

## Step 4: Update Router's Child Interface

**In `FlowComponent.kt`:**

```kotlin
sealed interface Child {
    data class Form(val component: InputFormComponent) : Child
    data class Results(val component: ResultsListComponent) : Child

    // ✅ ADD THIS
    data class Options(val component: OptionsComponent) : Child
}
```

---

## Step 5: Handle in createChild Factory

**In `RealFlowComponent.kt`:**

```kotlin
private fun createChild(
    childConfig: ChildConfig,
    componentContext: ComponentContext
): FlowComponent.Child = when (childConfig) {
    ChildConfig.Form -> FlowComponent.Child.Form(
        componentFactory.createInputFormComponent(
            componentContext,
            ::onFormOutput
        )
    )
    is ChildConfig.Results -> FlowComponent.Child.Results(
        componentFactory.createResultsListComponent(
            componentContext,
            childConfig.query,
            ::onResultsOutput
        )
    )
    // ✅ ADD THIS
    is ChildConfig.Options -> FlowComponent.Child.Options(
        componentFactory.createOptionsComponent(
            componentContext,
            childConfig.currentOptions,
            ::onOptionsOutput
        )
    )
}
```

---

## Step 6: Add Output Handler

**In `RealFlowComponent.kt`:**

```kotlin
private fun onOptionsOutput(output: OptionsComponent.Output) {
    when (output) {
        is OptionsComponent.Output.Applied -> {
            // Update options in results screen
            childStack.value.getChild<FlowComponent.Child.Results>()
                ?.component
                ?.applyOptions(output.options)

            navigation.pop()
        }
        OptionsComponent.Output.CloseRequested -> {
            navigation.pop()
        }
    }
}
```

---

## Step 7: Add Navigation to New Screen

**Add method to parent component that triggers navigation:**

In `RealResultsListComponent.kt`:

```kotlin
override fun onFilterClick() {
    onOutput(ResultsListComponent.Output.OptionsRequested(currentOptions))
}
```

**Update parent's Output:**

In `ResultsListComponent.kt`:

```kotlin
sealed interface Output {
    data class ItemClicked(val itemId: ItemId) : Output
    data object BackRequested : Output

    // ✅ ADD THIS
    data class OptionsRequested(val currentOptions: Options) : Output
}
```

**Handle in router:**

In `RealFlowComponent.kt`:

```kotlin
private fun onResultsOutput(output: ResultsListComponent.Output) {
    when (output) {
        is ResultsListComponent.Output.ItemClicked -> {
            navigation.safePush(ChildConfig.Details(output.itemId))
        }
        ResultsListComponent.Output.BackRequested -> {
            navigation.pop()
        }
        // ✅ ADD THIS
        is ResultsListComponent.Output.OptionsRequested -> {
            navigation.safePush(ChildConfig.Options(output.currentOptions))
        }
    }
}
```

---

## Step 8: Add UI Rendering

**In `SearchUi.kt`:**

```kotlin
Children(childStack, modifier) { child ->
    when (val instance = child.instance) {
        is FlowComponent.Child.Form -> SearchFormUi(instance.component)
        is FlowComponent.Child.Results -> SearchResultsUi(instance.component)

        // ✅ ADD THIS
        is FlowComponent.Child.Options -> OptionsUi(instance.component)
    }
}
```

---

## Step 9: Update Fake Component (Optional)

**In `FakeFlowComponent.kt`:**

If you want to preview the new screen in isolation:

```kotlin
class FakeFlowComponent(
    child: FlowComponent.Child = FlowComponent.Child.Form(FakeInputFormComponent())
) : FlowComponent {
    override val childStack = createFakeChildStackStateFlow(child)
}

// For preview
@Preview
@Composable
private fun OptionsScreenPreview() {
    CustomTheme {
        SearchUi(
            component = FakeFlowComponent(
                child = FlowComponent.Child.Options(FakeOptionsComponent())
            )
        )
    }
}
```

---

## Checklist

- [ ] Created 4 files for new component (Interface, Real, Fake, Ui)
- [ ] Added ComponentFactory extension in `DI.kt`
- [ ] Added new `ChildConfig` variant (with `@Serializable`)
- [ ] Added new `Child` variant
- [ ] Handled new config in `createChild` factory
- [ ] Created output handler method (`onXxxOutput`)
- [ ] Added navigation trigger from parent screen
- [ ] Updated parent's `Output` interface
- [ ] Added UI rendering in `Children` composable
- [ ] Updated fake component for previews (optional)
- [ ] Tested navigation flow
- [ ] Tested back button behavior

---

## Common Mistakes

**1. Forgetting @Serializable:**
```kotlin
// ❌ Will crash on configuration restore
data class Options(val categories: List<String>)

// ✅ Correct
@Serializable
data class Options(val categories: List<String>)
```

**2. Not handling output in router:**
```kotlin
// ❌ Compiler won't force you to handle it
private fun onOptionsOutput(output: OptionsComponent.Output) {
    // Empty - forgot to handle events!
}
```

**3. Using push instead of safePush:**
```kotlin
// ❌ Can create duplicate screens
navigation.push(ChildConfig.Options(...))

// ✅ Prevents duplicates
navigation.safePush(ChildConfig.Options(...))
```

**4. Passing non-serializable data:**
```kotlin
// ❌ ViewModel is not serializable
data class Details(val viewModel: DetailsViewModel) : ChildConfig

// ✅ Pass only primitive data or serializable objects
data class Details(val itemId: String) : ChildConfig
```
