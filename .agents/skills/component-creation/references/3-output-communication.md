# Example: Output Communication Pattern

Shows how child components communicate with parents via Output callbacks without direct method calls.

---

## The Problem

**WRONG APPROACH:** Child calls parent methods directly

```kotlin
// ❌ BAD: Tight coupling, hard to test
class RealChildComponent(
    private val parentComponent: ParentComponent // Direct dependency!
) {
    fun onItemClick(id: ItemId) {
        parentComponent.navigateToDetails(id) // Direct call!
    }
}
```

**Problems:**
- Child depends on parent interface
- Hard to reuse child in different contexts
- Difficult to test in isolation
- Violates separation of concerns

---

## The Solution

**CORRECT APPROACH:** Child emits Output events, parent handles them

---

## 1. Child Component Defines Output

```kotlin
interface ItemListComponent {
    val state: StateFlow<LoadableState<List<Item>>>

    fun onItemClick(item: Item)
    fun onAddClick()

    sealed interface Output {
        data class ItemSelected(val itemId: ItemId) : Output
        data object AddRequested : Output
    }
}
```

---

## 2. Child Implementation Calls onOutput

```kotlin
class RealItemListComponent(
    componentContext: ComponentContext,
    private val onOutput: (ItemListComponent.Output) -> Unit, // Output callback
    private val repository: ItemRepository
) : ComponentContext by componentContext, ItemListComponent {

    private val itemsReplica = repository.itemsReplica

    override val state = itemsReplica.observe(this, errorHandler)

    override fun onItemClick(item: Item) {
        // Emit event to parent
        onOutput(ItemListComponent.Output.ItemSelected(item.id))
    }

    override fun onAddClick() {
        // Emit event to parent
        onOutput(ItemListComponent.Output.AddRequested)
    }
}
```

---

## 3. Parent Creates Child with Output Handler

```kotlin
class RealMainComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, MainComponent {

    private val navigation = StackNavigation<ChildConfig>()

    private fun createChild(
        childConfig: ChildConfig,
        componentContext: ComponentContext
    ): Child = when (childConfig) {
        ChildConfig.List -> Child.List(
            componentFactory.createItemListComponent(
                componentContext,
                ::onItemListOutput // Pass output handler
            )
        )
        // ...
    }

    // Handle child's Output events
    private fun onItemListOutput(output: ItemListComponent.Output) {
        when (output) {
            is ItemListComponent.Output.ItemSelected -> {
                // Navigate to details
                navigation.safePush(ChildConfig.Details(output.itemId))
            }
            ItemListComponent.Output.AddRequested -> {
                // Navigate to creation form
                navigation.safePush(ChildConfig.Create)
            }
        }
    }
}
```

---

## 4. Child Can Bubble Events Up

Sometimes a child wants to bubble events to grandparent:

```kotlin
class RealFeatureRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val onOutput: (FeatureRootComponent.Output) -> Unit // Own output
) : ComponentContext by componentContext, FeatureRootComponent {

    private fun onChildOutput(output: ChildComponent.Output) {
        when (output) {
            is ChildComponent.Output.LocalEvent -> {
                // Handle locally
                navigation.safePush(...)
            }
            ChildComponent.Output.CloseRequested -> {
                // Bubble up to parent
                onOutput(FeatureRootComponent.Output.FeatureClosed)
            }
        }
    }
}
```

---

## Key Points

- **Decoupling** - Child doesn't know about parent
- **Reusability** - Child can be used in different contexts
- **Testability** - Easy to test with fake output handlers
- **Type Safety** - Sealed interface ensures all events are handled
- **Bubbling** - Events can be forwarded up the hierarchy
- **Clarity** - Explicit contract between child and parent

---

## When to Use Output vs StateFlow

**Use Output for:**
- One-time events (navigation, dialogs)
- User actions (clicks, form submissions)
- Events that trigger side effects

**Use StateFlow for:**
- Continuous state (loading, data, UI state)
- Values that can change over time
- State that UI needs to observe
