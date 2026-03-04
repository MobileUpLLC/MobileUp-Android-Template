# Example: Formatted and Raw Strings

Use `StringDesc.ResourceFormatted()` when a string resource contains placeholders (`%s`, `%d`). Use `"text".desc()` for raw strings that are already resolved (e.g., a message text coming from the server). Use `.orEmptyRaw()` as a null-safe fallback.

## Formatted String — Dialog with Dynamic Value

The string resource contains a placeholder for the event name:

```xml
<!-- res/values/strings_events.xml -->
<string name="event_remove_from_schedule_title">Remove "%s" from schedule?</string>
```

```kotlin
// presentation/RealEventListComponent.kt
import <package>.core.utils.ResourceFormatted  // StringDesc.ResourceFormatted extension

StandardDialogData(
    title = StringDesc.ResourceFormatted(
        R.string.event_remove_from_schedule_title,
        event.name       // String argument injected into %s
    ),
    message = R.string.event_remove_from_schedule_message.strResDesc(),
    confirmText = R.string.common_remove.strResDesc(),
    dismissText = R.string.common_cancel.strResDesc()
)
```

## Formatted String — Multiple Arguments

```kotlin
// Resource: "Showing %d of %d results"
StringDesc.ResourceFormatted(
    R.string.search_results_count,
    currentCount,
    totalCount
)
```

## Raw String — Server-Provided Message

```kotlin
// error_handling/ErrorMessage.kt
is ServerException -> {
    val detail = this.serverError.details.detail
    if (detail != null) {
        detail.desc()                   // server already sent a localized message
    } else {
        R.string.error_unexpected.strResDesc()
    }
}
```

## Debug-Only Formatted Error with Concatenation

```kotlin
// Concatenate StringDesc values with + operator
R.string.error_unexpected.strResDesc() + "\n\n$message".desc()
```

## Null-Safe Fallback with orEmptyRaw()

```kotlin
// When StringDesc? may be null and an empty string is acceptable
val label: StringDesc = optionalDesc.orEmptyRaw()   // never null, empty string if input was null
```

## Key Points

- `StringDesc.ResourceFormatted()` is defined in `StringDescUtils.kt` under `core/utils` — import it with `import <package>.core.utils.ResourceFormatted`.
- Use `str.desc()` only when the string is already in the correct language (e.g., server error messages, user-generated content). Never use it for hardcoded UI labels.
- `StringDesc` values can be concatenated with the `+` operator from Moko Resources.
- `.orEmptyRaw()` is defined in `StringDescUtils.kt` — use it as a null-safe empty fallback.
