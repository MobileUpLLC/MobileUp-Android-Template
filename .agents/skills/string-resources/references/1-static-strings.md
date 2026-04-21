# Example: Static String Resources

Use `.strResDesc()` to convert an `R.string` resource ID into a `StringDesc`. This is the most common pattern for all user-facing labels defined in XML resources. Resolve to a plain string in Compose UI using `.localized()`.

## Domain Layer — Enum with Display Name

```kotlin
// domain/UserTag.kt
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import <package>.core.common_domain.SelectableEnum

enum class UserTag : SelectableEnum {
    Coach, Admin, Owner, Moderator, You;

    override val nameForDisplay: StringDesc get() = when (this) {
        Coach     -> R.string.user_tag_coach.strResDesc()
        Admin     -> R.string.user_tag_admin.strResDesc()
        You       -> R.string.user_tag_you.strResDesc()
        Owner     -> R.string.user_tag_owner.strResDesc()
        Moderator -> R.string.user_tag_moderator.strResDesc()
    }
}
```

## UI Layer — Composable

```kotlin
// presentation/UserProfileUi.kt
import dev.icerock.moko.resources.compose.localized

@Composable
fun UserTagChip(tag: UserTag) {
    Text(
        text = tag.nameForDisplay.localized(),
        style = CustomTheme.typography.caption.regular,
        color = CustomTheme.colors.text.secondary
    )
}
```

## Key Points

- Implement `SelectableEnum` on enums with user-facing labels — expose `nameForDisplay: StringDesc` as a property.
- Always use `StringDesc` for display names — never `String`. Resolution happens only in the UI layer.
- Call `.localized()` only inside `@Composable` functions.
