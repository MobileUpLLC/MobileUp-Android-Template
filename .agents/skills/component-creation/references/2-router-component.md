# Example: Router Component (Navigator)

A component that manages ChildStack and coordinates multiple screens through navigation.

---

## 1. Component Interface

```kotlin
interface SearchComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Form(val component: SearchFormComponent) : Child
        data class Results(val component: SearchResultsComponent) : Child
        data class Details(val component: ItemDetailsComponent) : Child
    }

    sealed interface Output {
        data object Finished : Output
    }
}
```

---

## 2. Real Implementation

```kotlin
class RealSearchComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val onOutput: (SearchComponent.Output) -> Unit
) : ComponentContext by componentContext, SearchComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, SearchComponent.Child>> = childStack(
        source = navigation,
        serializer = ChildConfig.serializer(),
        initialConfiguration = ChildConfig.Form,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    @Serializable
    private sealed interface ChildConfig {
        @Serializable
        data object Form : ChildConfig

        @Serializable
        data class Results(val query: String) : ChildConfig

        @Serializable
        data class Details(val itemId: String) : ChildConfig
    }

    private fun createChild(
        childConfig: ChildConfig,
        componentContext: ComponentContext
    ): SearchComponent.Child = when (childConfig) {
        ChildConfig.Form -> SearchComponent.Child.Form(
            componentFactory.createSearchFormComponent(
                componentContext,
                ::onFormOutput
            )
        )
        is ChildConfig.Results -> SearchComponent.Child.Results(
            componentFactory.createSearchResultsComponent(
                componentContext,
                childConfig.query,
                ::onResultsOutput
            )
        )
        is ChildConfig.Details -> SearchComponent.Child.Details(
            componentFactory.createItemDetailsComponent(
                componentContext,
                childConfig.itemId,
                ::onDetailsOutput
            )
        )
    }

    private fun onFormOutput(output: SearchFormComponent.Output) {
        when (output) {
            is SearchFormComponent.Output.SearchSubmitted -> {
                navigation.safePush(ChildConfig.Results(output.query))
            }
            SearchFormComponent.Output.CloseRequested -> {
                onOutput(SearchComponent.Output.Finished)
            }
        }
    }

    private fun onResultsOutput(output: SearchResultsComponent.Output) {
        when (output) {
            is SearchResultsComponent.Output.ItemClicked -> {
                navigation.safePush(ChildConfig.Details(output.itemId))
            }
            SearchResultsComponent.Output.BackRequested -> {
                navigation.pop()
            }
        }
    }

    private fun onDetailsOutput(output: ItemDetailsComponent.Output) {
        when (output) {
            ItemDetailsComponent.Output.CloseRequested -> {
                navigation.pop()
            }
        }
    }
}
```

---

## 3. Fake Implementation

```kotlin
class FakeSearchComponent(
    child: SearchComponent.Child = SearchComponent.Child.Form(FakeSearchFormComponent())
) : SearchComponent {
    override val childStack = createFakeChildStackStateFlow(child)
}
```

---

## 4. UI Composable

```kotlin
@Composable
fun SearchUi(
    component: SearchComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is SearchComponent.Child.Form -> SearchFormUi(
                component = instance.component
            )
            is SearchComponent.Child.Results -> SearchResultsUi(
                component = instance.component
            )
            is SearchComponent.Child.Details -> ItemDetailsUi(
                component = instance.component
            )
        }
    }
}

@Preview
@Composable
private fun SearchUiPreview() {
    CustomTheme {
        SearchUi(component = FakeSearchComponent())
    }
}
```

---

## 5. ComponentFactory Extension

```kotlin
// In DI.kt
fun ComponentFactory.createSearchComponent(
    componentContext: ComponentContext,
    onOutput: (SearchComponent.Output) -> Unit
): SearchComponent {
    return RealSearchComponent(
        componentContext,
        get(),
        onOutput
    )
}
```

---

## Key Points

- **StackNavigation** - Manages navigation state
- **ChildStack** - Observable stack of child components
- **ChildConfig** - Serializable configurations for state restoration
- **Child sealed interface** - Type-safe child component instances
- **createChild factory** - Creates child components via ComponentFactory
- **Output handlers** - Each child has dedicated output handler method
- **safePush()** - Prevents duplicate screens in stack
- **handleBackButton** - Automatically handles Android back button
- **Children composable** - Renders active child with animations
