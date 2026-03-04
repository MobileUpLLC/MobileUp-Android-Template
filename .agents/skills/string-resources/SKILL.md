---
name: string-resources
description: Guidelines for working with strings and localization using Moko Resources
---

# String Resources

This skill describes how strings are represented, created, and resolved in the app. Moko Resources is used **not** for cross-platform support but to avoid passing `Context` around the codebase when resolving string resources.

The central abstraction is `StringDesc` — an interface from Moko Resources that represents a string in a deferred way. It is resolved into an actual `String` only at the last moment: in the UI layer via `.localized()` (Compose) or via a service outside Compose.

---

## 1. StringDesc — the Universal String Type

`StringDesc` is used wherever a localized string needs to flow through domain or presentation code without being immediately resolved. All string-carrying domain fields use `StringDesc`.

**Creating a StringDesc from a string resource:**

```kotlin
// Extension on Int (resource ID) — always use this
R.string.my_string.strResDesc()                     // import dev.icerock.moko.resources.desc.strResDesc

// obsolete, DO NOT USE — prefer .strResDesc() instead
StringDesc.Resource(R.string.my_string)
```

Always prefer `.strResDesc()` — it is shorter, reads well inline, and is the project standard.

**Creating a StringDesc from a raw string:**

```kotlin
"Some raw text".desc()                              // import dev.icerock.moko.resources.desc.desc
```

Use `.desc()` only when the string content comes from a source that is already resolved (e.g., a server error message that is already in the current language). Do **not** hardcode user-facing strings as raw strings — always use resource IDs.

**Null safety helper:**

```kotlin
stringDescOrNull.orEmptyRaw()   // returns StringDesc.Raw("") when null
```

---

## 2. Formatted Strings (Dynamic Values)

When a string resource contains format placeholders (`%s`, `%d`, etc.), use:

```kotlin
StringDesc.ResourceFormatted(R.string.hello_name, "John")
StringDesc.ResourceFormatted(R.string.items_count, count, total)  // multiple args
```

Defined in `StringDescUtils.kt` (`<package>.core.utils`).

---

## 3. LocalizedStringDesc — Backend Multi-Language Strings

Use `LocalizedStringDesc` **only** when the backend sends a string in multiple languages simultaneously — a `LocalizedResponse` (`Map<String, String?>` keyed by language code, e.g., city names, category titles).

If the backend returns a plain string (already resolved in one language), work with it as a regular `String` or wrap it with `.desc()` if you need `StringDesc` compatibility. Do not use `LocalizedStringDesc` in that case.

**Conversion in the data layer (inside `toDomain()`):**

```kotlin
localizedName.toLocalizedStringDesc()   // extension on LocalizedResponse
```

`LocalizedStringDesc` implements `StringDesc`. At resolution time it reads the current language code from resources (`R.string.common_language_key`) and picks the matching translation, falling back to English.

**Creating test/mock instances:**

```kotlin
LocalizedStringDesc.mock(mapOf("en" to "Dubai", "ru" to "Дубай"))

// Single-language shortcut:
"Dubai".localizedDesc()                 // defaults to "en"
"Дубай".localizedDesc("ru")
```

---

## 4. Resolving StringDesc to String

### In Compose UI — `.localized()`

```kotlin
import dev.icerock.moko.resources.compose.localized

Text(text = stringDesc.localized())
```

This is the standard way. Call `.localized()` only in composable functions — never in components or repositories.

### Outside Compose — StringService

Inject `StringService` into a component when you need to resolve a `StringDesc` to a plain `String` at runtime (e.g., to pass into a third-party API, build a shareable link, format a notification).

```kotlin
stringService.resolveString(stringDesc): String
```

---

## 5. Domain Entities with Display Names

Implement `SelectableEnum` with `nameForDisplay: StringDesc` in two cases:
- The enum needs a **localized** label resolved from string resources
- The enum is used as a **list of selectable items** in a selection UI (e.g., `SelectFromEnumUi` bottom sheet)

```kotlin
enum class TrainingType : SelectableEnum {
    Training, OpenMat;

    override val nameForDisplay: StringDesc get() = when (this) {
        Training -> R.string.training_type_training.strResDesc()
        OpenMat  -> R.string.training_type_open_mat.strResDesc()
    }
}

// UI usage:
Text(text = trainingType.nameForDisplay.localized())
```

If neither case applies — the label doesn't need localization and the enum is not used in a selection UI — returning a plain `String` or `.desc()` is acceptable.

```kotlin
enum class TrainingType : SelectableEnum {
    Training, OpenMat;

    override val nameForDisplay: StringDesc get() = when (this) {
        Training -> R.string.training_type_training.strResDesc()
        OpenMat  -> R.string.training_type_open_mat.strResDesc()
    }
}

// UI usage:
Text(text = trainingType.nameForDisplay.localized())
```

---

## 6. Error Messages

All exceptions have an `errorMessage: StringDesc` extension property defined in `ErrorMessage.kt`. The `ErrorHandler` automatically uses this property to show localized toasts — you do not need to call it manually in most cases.

When building UI-level error dialogs or inline messages from domain results, use `.strResDesc()`:

```kotlin
R.string.qr_scanner_event_not_found_title.strResDesc()
```

---

## 7. String Resource Files and Translation

All user-facing strings **must** be defined in XML string resource files (English). Hardcoded string literals anywhere outside domain mocks and fake components are forbidden.

Strings are split across multiple files per feature — do not put everything in one file. Each feature module has its own `res/values/strings_<feature>.xml` (e.g., `strings_profile.xml`, `strings_events.xml`). The corresponding translated files live in `res/values-XX/strings_<feature>.xml`.

The app supports 8 languages: **en, ru, fr, es, pt, it, de, pl**. Every new string must be translated into all 7 non-English languages and placed into the corresponding `res/values-XX/strings_<feature>.xml` files.

**Hardcoded strings in non-Fake code — fix procedure:**

If you find (or write) a hardcoded string literal in a component, repository, UI composable, or domain class (anywhere except domain mocks and fake components):

1. Extract it to the appropriate `res/values/strings_<feature>.xml` with a descriptive key.
2. Reference it via `.strResDesc()`.
3. Add translations for all 7 non-English languages into the corresponding `res/values-XX/strings_<feature>.xml` files.

```kotlin
// ❌ Forbidden — hardcoded string outside domain mocks and fake components
title = "Remove from schedule?".desc()

// ✅ Correct — resource ID + translated
title = R.string.training_remove_from_schedule_title.strResDesc()
```

**The only exception:** domain `MOCK` objects and `FakeXxxComponent` classes may use raw strings because they are never shown to real users. In practice, fake components usually reference domain `MOCK` objects rather than constructing strings directly:

```kotlin
// ✅ Allowed — domain Mock
data class Training(...) {
    companion object {
        val MOCK = Training(name = "Morning BJJ", ...)
    }
}

// ✅ Allowed — Fake component references domain Mock
class FakeTrainingListComponent : TrainingListComponent {
    override val trainingsState = MutableStateFlow(LoadableState.Content(listOf(Training.MOCK)))
}
```

---

## 8. Quick Reference

| Situation | API |
|---|---|
| Static string from R.string | `R.string.foo.strResDesc()` |
| String with format args | `StringDesc.ResourceFormatted(R.string.foo, arg)` |
| Raw string (already resolved) | `"text".desc()` |
| Nullable StringDesc → empty fallback | `desc.orEmptyRaw()` |
| Backend multi-language map | `localizedResponse.toLocalizedStringDesc()` |
| Mock/test localized string | `"text".localizedDesc()` |
| Resolve in Compose | `stringDesc.localized()` |
| Resolve outside Compose | `stringService.resolveString(desc)` |

---

## Examples

> **Important:** Read examples **only when you need specific details**. Don't load all examples at once - use them pointedly to avoid context bloat.

- [Static String Resources](references/1-static-strings.md) - `.strResDesc()`, `SelectableEnum`, `.localized()` in UI
- [Backend Localized Strings](references/2-backend-localized.md) - `LocalizedStringDesc`, `toLocalizedStringDesc()`
- [Formatted and Raw Strings](references/3-formatted-and-raw.md) - `ResourceFormatted`, `.desc()`, `orEmptyRaw()`
- [Resolving Outside Compose](references/4-resolving-outside-compose.md) - `StringService`
