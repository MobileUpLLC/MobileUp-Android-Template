---
name: component-creation
description: Guidelines for creating Decompose components (simple and complex routers), navigation via ChildStack, communication via Outputs, dialogs, messages, and data flow between screens
---

# Component Creation - Decompose Patterns

This skill defines how to create and organize Decompose components following the **four-part mandatory structure**.

---

## The Mandatory Four-Part Structure

Every component MUST consist of **four files** to ensure testability and UI preview capabilities:

1. **`XxxComponent.kt` (interface)** - Contract
   - Defines `StateFlow` for state
   - Defines methods for user actions
   - Defines `sealed interface Output` for parent communication

2. **`RealXxxComponent.kt` (implementation)** - Logic
   - Implements `ComponentContext by componentContext`
   - Handles business logic and lifecycle
   - Injects dependencies (repositories, factories)
   - Creates child components (for routers)

3. **`FakeXxxComponent.kt` (mock)** - Testing
   - Provides static/mock data
   - Methods return `Unit`
   - Used in Composable previews

4. **`XxxUi.kt` (composable)** - Pure UI
   - Takes component interface as parameter
   - NO business logic
   - Reads state via `component.state.collectAsState()`
   - Calls component methods on user actions

---

## Component Types

### 1. Simple Component (Screen)
A single screen without nested navigation.

**When to use:**
- Single screen features
- Leaf screens in navigation hierarchy
- Reusable UI components with state

**Key characteristics:**
- NO `ChildStack`
- NO `StackNavigation`
- Communicates via `Output` only

**Example:** Filter screen, period selector, settings screen

### 2. Router Component (Navigator)
A component that manages `ChildStack` and coordinates multiple screens.

**When to use:**
- Feature entry points
- Multi-screen flows
- Tab-based interfaces

**Key characteristics:**
- Has `childStack: StateFlow<ChildStack<*, Child>>`
- Manages `StackNavigation<ChildConfig>`
- Handles children's `Output` events
- Can bubble up events to parent via own `Output`

**Example:** Main flow, onboarding, search flow

---

## Communication: Output Pattern

**CRITICAL RULE:** Components NEVER call parent methods directly. All communication happens via `Output` callbacks.

**Two event types:**
- **`Requested`** suffix - child requests action from parent (navigation, dialogs)
  - `LoginRequested`, `ItemDetailsRequested`, `SelectCityRequested`
- **Past tense** - child informs parent about completed action
  - `ItemSaved`, `DataUpdated`, `ProfileDeleted`

**Flow:**
1. Child defines `sealed interface Output` with event types
2. Child emits events via `onOutput(Output.EventName)`
3. Parent handles events in `onChildOutput()` method

> **For detailed examples:** See [Output Communication](references/3-output-communication.md)

---

## Router Component Anatomy

**5 Essential Elements:**

1. **StackNavigation** - navigation controller for managing stack
2. **ChildStack** - observable stack state with active/backstack children
3. **ChildConfig** - serializable configurations for state restoration
4. **Child** - sealed interface with component instances
5. **createChild** - factory method for creating child components

> **For full implementation:** See [Router Component](references/2-router-component.md)

---

## Navigation Operations

**Basic operations:**
- `navigation.safePush(config)` - Add screen (prevents duplicates)
- `navigation.pop()` - Remove current screen
- `navigation.replaceCurrent(config)` - Replace current (after form submission)
- `navigation.replaceAll(config)` - Reset entire stack (logout, onboarding)

**Accessing children:**
- `childStack.value.active.instance` - Get active child
- `childStack.value.getChild<Child.Xxx>()` - Get specific child in stack

**Use case for getChild():** Pass data from picker/selector back to requester screen in stack.

> **For navigation examples:** See [Router Component](references/2-router-component.md)
> **For data flow between children:** See [Data Flow Between Children](references/7-data-flow-between-children.md)

---

## Showing Messages and Dialogs

### MessageService
Global service for Toast/Snackbar messages. Inject and call `showMessage()`.

**Message types:** `Positive` (green), `Negative` (red), `Neutral` (gray)

### Dialog Controls
Create dialog control as component property, call `.show()` to display:

- **StandardDialogControl** - Confirmation/alert dialogs (AlertDialog)
- **SelectFromEnumDialogControl<T>** - Selection from enum list (BottomSheet)
- **DialogControl<T>** - Custom bottom sheet content

**Fake implementations:** `fakeStandardDialogControl()`, `fakeSelectFromEnumDialogControl<T>()`, `fakeDialogControl<T>()`

### PrivilegesChecker
Automatic authorization/permission checks with built-in UI:

- `requireAuthorization(block)` - Shows auth dialog if not authorized
- `requireAuthorizationAndSubscription(block)` - Shows auth or subscription screen
- `requirePermissionForAcademyAdmin(permission, block)` - Shows restriction dialog if no permission

> **For detailed examples:** See [Dialogs](references/5-dialogs.md) and [Messages](references/6-messages.md)

---

## ComponentFactory Pattern

Components created via **extension functions** on `ComponentFactory` in `DI.kt`:
- Function creates `RealXxxComponent` with dependencies from Koin (`get()`)
- Parent calls `componentFactory.createXxxComponent(...)`
- Never create components directly with constructors

> **For implementation example:** See [Simple Component](references/1-simple-component.md) section 5

---

## Adding New Screen to Router

**6 Steps:**
1. Create four files (Interface, Real, Fake, Ui)
2. Add ComponentFactory extension in `DI.kt`
3. Add new variant to Router's `ChildConfig` (`@Serializable`)
4. Add new variant to Router's `Child`
5. Handle in `createChild` factory method
6. Add output handler method

> **For step-by-step guide:** See [Adding Screen to Router](references/4-add-screen-to-router.md)

---

## Best Practices

1. **Naming Convention:**
   - Interface: `XxxComponent`
   - Implementation: `RealXxxComponent`
   - Mock: `FakeXxxComponent`
   - UI: `XxxUi`
   - State properties: `val xxxState: StateFlow<LoadableState<Xxx>>` (NEVER just `val state`)

2. **State Management:**
   - **CRITICAL:** Separate variables for replica and state
     ```kotlin
     // ❌ WRONG - Combined in one line
     override val settingsState = repository.settingsReplica.observe(this, errorHandler)

     // ✅ CORRECT - Separate variables
     private val settingsReplica = repository.settingsReplica
     override val settingsState = settingsReplica.observe(this, errorHandler)
     ```
   - **ALWAYS** use descriptive names with `State` suffix: `profileState`, `newsState`, `statisticsState`
   - **NEVER** use generic `val state` - always specify what state it is
   - Use `StateFlow` for state
   - **CRITICAL:** Use `computed()` for derived state, NEVER use `combine()` with `stateIn()`
     ```kotlin
     // ❌ WRONG - Using combine with stateIn
     override val preferencesState = combine(currentTheme, currentLanguage) { theme, language ->
         PreferencesState(theme, language)
     }.stateIn(scope, SharingStarted.Lazily, initialValue)

     // ✅ CORRECT - Using computed
     override val preferencesState = computed(currentTheme, currentLanguage) { theme, language ->
         PreferencesState(theme, language)
     }
     ```
   - **CRITICAL:** For mutable local state (not derived from Replicas), use `MutableStateFlow` directly — do NOT wrap it in `computed()`:
     ```kotlin
     // ❌ WRONG - Wrapping mutable state in computed
     private val _selectedItem = MutableStateFlow<Item?>(null)
     override val selectedItem: StateFlow<Item?> = computed(_selectedItem) { it }

     // ✅ CORRECT - MutableStateFlow is the state itself
     override val selectedItem = MutableStateFlow<Item?>(null)
     ```
     Update its value directly in response to user actions:
     ```kotlin
     override fun onItemSelected(item: Item) {
         selectedItem.value = item
     }
     ```
   - Keep state immutable where possible; use `MutableStateFlow` only for state that changes via user actions

3. **Dependencies:**
   - Inject via constructor
   - Use `ComponentContext by componentContext`
   - Get dependencies from Koin in ComponentFactory

4. **Output Events:**
   - Always use `sealed interface Output`
   - Use **`Requested`** suffix for requesting actions from parent (navigation, dialogs, etc.)
     - `LoginRequested`, `ArticleDetailsRequested`, `SelectCityRequested`
   - Use **past tense** for completed actions (informing parent of facts)
     - `TrainingCreated`, `DataSaved`, `ProfileUpdated`, `ItemDeleted`
   - Never use `OnClick` or event handler names - describe what happened/needed
   - Include necessary data in Output events (IDs, queries, parameters)

5. **Navigation:**
   - Use `safePush()` to prevent duplicates
   - Use `replaceCurrent()` to replace screen after form submission
   - Use `replaceAll()` to reset navigation stack (logout, onboarding)
   - Handle back button in router
   - Clean up resources in lifecycle

6. **Messages and Dialogs:**
   - Use `MessageService` for Toast/Snackbar messages
   - Use `StandardDialogControl` for confirmation/alert dialogs
   - Use `SelectFromEnumDialogControl` for standard selection lists
   - Use `DialogControl` for custom bottom sheets
   - Use `PrivilegesChecker` for authorization/permission checks with automatic dialogs

---

## Examples

> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Simple Component](references/1-simple-component.md) - Basic screen without navigation
- [Router Component](references/2-router-component.md) - Multi-screen flow with Child Stack
- [Output Communication](references/3-output-communication.md) - Parent-child communication
- [Adding Screen to Router](references/4-add-screen-to-router.md) - Step-by-step guide
- [Dialogs](references/5-dialogs.md) - StandardDialogControl, SelectFromEnumDialogControl, DialogControl with Fake implementations
- [Messages](references/6-messages.md) - MessageService for Toast/Snackbar with usage patterns
- [Data Flow Between Children](references/7-data-flow-between-children.md) - Passing data between child components via getChild()
