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
    fun onPersonAdded(user: Person, roleId: RoleId, role: Role)
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

    override fun onPersonAdded(user: Person, roleId: RoleId, role: Role) {
        // This method is called by parent when user is selected
        componentScope.safeLaunch(errorHandler) {
            repository.addTeamMember(user, roleId, role)

            messageService.showMessage(
                Message(
                    text = StringDesc.Resource(R.string.team_member_added),
                    type = MessageType.Positive
                )
            )
        }
    }

    sealed interface Output {
        data class PersonPickerRequested(
            val targetRole: Role,
            val roleId: RoleId
        ) : Output
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

    @Serializable
    private sealed interface ChildConfig {
        @Serializable
        data object TeamList : ChildConfig

        @Serializable
        data object GroupList : ChildConfig

        @Serializable
        data class PersonPicker(
            val targetRole: Role,
            val roleId: RoleId,
            val requesterId: String // "team" or "groups"
        ) : ChildConfig
    }

    sealed interface Child {
        data class TeamList(val component: TeamListComponent) : Child
        data class GroupList(val component: GroupListComponent) : Child
        data class PersonPicker(val component: PersonPickerComponent) : Child
    }

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
        ChildConfig.GroupList -> Child.GroupList(
            componentFactory.createGroupListComponent(
                componentContext,
                ::onGroupListOutput
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
                // Open search, remember who requested it
                navigation.safePush(
                    ChildConfig.PersonPicker(
                        targetRole = output.targetRole,
                        roleId = output.roleId,
                        requesterId = "team"
                    )
                )
            }
        }
    }

    private fun onGroupListOutput(output: GroupListComponent.Output) {
        when (output) {
            is GroupListComponent.Output.PersonPickerRequested -> {
                navigation.safePush(
                    ChildConfig.PersonPicker(
                        targetRole = Role.Coach,
                        roleId = RoleId("coach"),
                        requesterId = "groups"
                    )
                )
            }
        }
    }

    private fun onPersonPickerOutput(output: PersonPickerComponent.Output) {
        when (output) {
            is PersonPickerComponent.Output.PersonSelected -> {
                // Get config to know who requested search
                val config = childStack.value.active.configuration as? ChildConfig.PersonPicker
                val requesterId = config?.requesterId
                val targetRole = config?.targetRole
                val roleId = config?.roleId

                // Pass selected user to the requester child
                when (requesterId) {
                    "team" -> {
                        if (targetRole != null && roleId != null) {
                            childStack.value
                                .getChild<Child.TeamList>()
                                ?.component
                                ?.onPersonAdded(output.user, roleId, targetRole)
                        }
                    }
                    "groups" -> {
                        childStack.value
                            .getChild<Child.GroupList>()
                            ?.component
                            ?.onCoachAdded(output.user)
                    }
                }

                // Close search screen
                navigation.pop()
            }
        }
    }
}
```

---

## 3. Key Points

### Pattern Flow:
1. **Staff List** requests user search via Output
2. **Parent** opens Search screen, stores context in ChildConfig (requesterId)
3. **Search** selects user, emits Output
4. **Parent** receives Output, checks config to know who requested
5. **Parent** finds requester via `getChild<Child.TeamList>()`
6. **Parent** calls method on Staff List component directly
7. **Parent** closes Search screen

### Why Store Context in ChildConfig?
```kotlin
@Serializable
data class PersonPicker(
    val requesterId: String, // Who requested this search
    val targetRole: Role, // What role to assign
    val roleId: RoleId    // Role ID for assignment
) : ChildConfig
```

This allows parent to know:
- Who opened the search screen
- What to do with selected data
- Where to pass the data back

### Alternative: Output Bubbling

Instead of direct method call, you could bubble Output up:

```kotlin
private fun onPersonPickerOutput(output: PersonPickerComponent.Output) {
    when (output) {
        is PersonPickerComponent.Output.PersonSelected -> {
            // Bubble up to grandparent
            onOutput(
                ManagementComponent.Output.PersonSelectedForStaff(
                    user = output.user,
                    roleId = ...,
                    role = ...
                )
            )
        }
    }
}
```

But this makes grandparent responsible for coordination, which is less clean.

### When to Use getChild():
- ✅ Passing data from picker/selector to requester screen
- ✅ Multiple screens can request same picker (Staff, Coaches, Members)
- ✅ Requester screen is in the back stack (not active)
- ❌ Don't use for simple parent-child communication (use Output)
- ❌ Don't use if requester is not in stack (data will be lost)

---

## 4. Complete Example with UI

### Staff List UI

```kotlin
@Composable
fun TeamListUi(
    component: TeamListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    LceWidget(
        state = state,
        onRetryClick = { /* ... */ }
    ) { teamList, _ ->
        Column(modifier = modifier.fillMaxSize()) {
            CustomButton(
                text = "Add Staff Member",
                onClick = component::onAddStaffClick
            )

            LazyColumn {
                items(teamList) { team ->
                    TeamMemberRow(team = team)
                }
            }
        }
    }
}
```

### Person Search UI

```kotlin
@Composable
fun PersonPickerUi(
    component: PersonPickerComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    LceWidget(
        state = state,
        onRetryClick = { /* ... */ }
    ) { users, _ ->
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(users) { user ->
                PersonRow(
                    user = user,
                    onClick = { component.onPersonClick(user) }
                )
            }
        }
    }
}
```

### Router UI

```kotlin
@Composable
fun ManagementUi(
    component: ManagementComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is ManagementComponent.Child.TeamList -> {
                TeamListUi(instance.component)
            }
            is ManagementComponent.Child.GroupList -> {
                GroupListUi(instance.component)
            }
            is ManagementComponent.Child.PersonPicker -> {
                PersonPickerUi(instance.component)
            }
        }
    }
}
```

---

## 5. Testing

```kotlin
@Test
fun `user selection flows back to team list`() {
    val navigation = TestStackNavigation<ChildConfig>()
    val component = RealManagementComponent(
        componentContext = DefaultComponentContext(...),
        componentFactory = FakeComponentFactory(),
        navigation = navigation
    )

    // 1. Open team list
    val teamComponent = component.childStack.value
        .getChild<Child.TeamList>()!!
        .component

    // 2. Request user search
    teamComponent.onAddStaffClick()
    assertEquals(ChildConfig.PersonPicker::class, navigation.stack.last()::class)

    // 3. Select user in search
    val searchComponent = component.childStack.value
        .getChild<Child.PersonPicker>()!!
        .component

    val selectedPerson = Person.MOCK
    searchComponent.onPersonClick(selectedPerson)

    // 4. Verify user added to team (getChild works even after pop)
    // Parent should call onPersonAdded on team component
}
```

---

## Summary

**getChild() Pattern:**
- Allows passing data from picker/selector back to specific requester
- Parent coordinates data flow between children
- Store context (requesterId) in ChildConfig
- Call methods on non-active children in the stack
- Clean separation: each child only knows its own Output contract
