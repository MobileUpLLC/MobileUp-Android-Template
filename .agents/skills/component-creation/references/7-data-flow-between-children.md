# Example: Data Flow Between Child Components

Shows how to pass data from one child component to another through the parent router using `getChild()`.

---

## Scenario

We have a team management flow:
1. **Team List Screen** - shows team members, has "Add Member" button
2. **Person Picker Screen** - user selects a person from list
3. Data flows back: Picker → Parent Router → Team List

**Challenge:** How does selected person get back to Team List screen?

**Solution:** Parent router receives Output from Picker, then calls method on Team List component directly.

---

## 1. Child Components

### Staff List Component

```kotlin
interface TeamListComponent {
    val teamListState: StateFlow<LoadableState<List<TeamMember>>>

    fun onAddStaffClick()
    fun onPersonAdded(user: Person, role: Role)
}

class RealTeamListComponent(
    componentContext: ComponentContext,
    private val repository: StaffRepository,
    private val errorHandler: ErrorHandler,
    private val messageService: MessageService,
    private val onOutput: (TeamListComponent.Output) -> Unit
) : ComponentContext by componentContext, TeamListComponent {

    private val teamListReplica = repository.teamListReplica

    override val teamListState = teamListReplica.observe(this, errorHandler)

    override fun onAddStaffClick() {
        // Request parent to open user search
        onOutput(
            TeamListComponent.Output.PersonPickerRequested(
                targetRole = Role.Manager,
                roleId = RoleId("manager")
            )
        )
    }

    override fun onPersonAdded(user: Person, role: Role) {
        // This method is called by parent when user is selected
        componentScope.safeLaunch(errorHandler) {
            repository.addTeamMember(user, role)

            messageService.showMessage(
                Message(
                    text = Res.string.team_member_added.resourceDesc(),
                    type = MessageType.Positive
                )
            )
        }
    }

    sealed interface Output {
        data class PersonPickerRequested(val targetRole: Role) : Output
    }
}
```

### Person Search Component

```kotlin
interface PersonPickerComponent {
    val personsState: StateFlow<LoadableState<List<Person>>>

    fun onPersonClick(user: Person)

    sealed interface Output {
        data class PersonSelected(val user: Person) : Output
    }
}

class RealPersonPickerComponent(
    componentContext: ComponentContext,
    private val repository: PersonRepository,
    private val errorHandler: ErrorHandler,
    private val onOutput: (PersonPickerComponent.Output) -> Unit
) : ComponentContext by componentContext, PersonPickerComponent {

    private val personsReplica = repository.usersReplica

    override val personsState = personsReplica.observe(this, errorHandler)

    override fun onPersonClick(user: Person) {
        // Emit selected user to parent
        onOutput(PersonPickerComponent.Output.PersonSelected(user))
    }
}
```

---

## 2. Parent Router Component

```kotlin
class RealManagementComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, ManagementComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = ChildConfig.serializer(),
        initialConfiguration = ChildConfig.TeamList,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        childConfig: ChildConfig,
        componentContext: ComponentContext
    ): Child = when (childConfig) {
        ChildConfig.TeamList -> Child.TeamList(
            componentFactory.createTeamListComponent(
                componentContext,
                ::onTeamListOutput
            )
        )
        
        is ChildConfig.PersonPicker -> Child.PersonPicker(
            componentFactory.createPersonPickerComponent(
                componentContext,
                ::onPersonPickerOutput
            )
        )
    }

    private fun onTeamListOutput(output: TeamListComponent.Output) {
        when (output) {
            is TeamListComponent.Output.PersonPickerRequested -> {
                navigation.safePush(
                    ChildConfig.PersonPicker(
                        targetRole = output.targetRole
                    )
                )
            }
        }
    }

    private fun onPersonPickerOutput(output: PersonPickerComponent.Output) {
        when (output) {
            is PersonPickerComponent.Output.PersonSelected -> {
                // Get config to know targetRole
                val config = childStack.value.active.configuration as? ChildConfig.PersonPicker
                val targetRole = config?.targetRole

                // Pass selected user to the requester child
                if (targetRole != null) {
                    childStack.value
                        .getChild<Child.TeamList>()
                        ?.component
                        ?.onPersonAdded(output.user, targetRole)
                }

                // Close search screen
                navigation.pop()
            }
        }
    }

    @Serializable
    private sealed interface ChildConfig {

        @Serializable
        data object TeamList : ChildConfig

        @Serializable
        data class PersonPicker(
            val targetRole: Role
        ) : ChildConfig
    }
}
```

---

## 3. Key Points

### Pattern Flow:
1. **Team List** requests user search via Output
2. **Parent** opens Search screen, stores context in ChildConfig (targetRole)
3. **Picker** selects user, emits Output
4. **Parent** receives Output
5. **Parent** finds requester via `getChild<Child.TeamList>()`
6. **Parent** calls method on Team List component directly
7. **Parent** closes Search screen

### When to Use getChild():
- ✅ Passing data from picker/selector to requester screen
- ✅ Multiple screens can request same picker
- ✅ Requester screen is in the back stack (not active)
- ❌ Don't use for simple parent-child communication (use Output)

---

## Summary
**getChild() pattern:**
- Allows passing data from picker back to specific requester
- Parent coordinates data flow between children
- Store context in ChildConfig
- Clean separation: each child only knows its own Output contract
