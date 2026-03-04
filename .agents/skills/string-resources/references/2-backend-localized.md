# Example: Backend Localized Strings

When the API returns a field in multiple languages, it comes as a `LocalizedResponse` — a `Map<String, String?>` keyed by language code (e.g., `{"en": "Dubai", "ru": "Дубай"}`). Convert it to `LocalizedStringDesc` in the data layer so the rest of the app works with it as a regular `StringDesc`.

## Data Layer — DTO to Domain Mapping

```kotlin
// data/dto/NewsCategoryResponse.kt
import <package>.core.common_domain.language.LocalizedResponse

@Serializable
internal data class NewsCategoryResponse(
    @SerialName("id") val id: String,
    @SerialName("localized_name") val localizedName: LocalizedResponse  // Map<String, String?>
)

internal fun NewsCategoryResponse.toDomain(): NewsCategory {
    return NewsCategory(
        id = NewsCategoryId(id),
        name = localizedName.toLocalizedStringDesc()   // convert inside toDomain()
    )
}
```

## Domain Layer — Entity Holds LocalizedStringDesc

```kotlin
// domain/NewsCategory.kt
import <package>.core.common_domain.language.LocalizedStringDesc

data class NewsCategory(
    val id: NewsCategoryId,
    val name: LocalizedStringDesc   // implements StringDesc — no special treatment needed
) {
    companion object {
        val MOCK = NewsCategory(
            id = NewsCategoryId("1"),
            name = LocalizedStringDesc.mock(mapOf("en" to "Training", "ru" to "Тренировки"))
        )
    }
}
```

## Presentation Layer — Fake Component

```kotlin
// presentation/FakeDiscoverComponent.kt
class FakeDiscoverComponent : DiscoverComponent {
    override val categories = MutableStateFlow(
        LoadableState.Content(listOf(NewsCategory.MOCK))
    )
}
```

## UI Layer — Composable

```kotlin
// presentation/DiscoverUi.kt
import dev.icerock.moko.resources.compose.localized

@Composable
fun CategoryChip(category: NewsCategory) {
    Text(
        text = category.name.localized(),   // resolves using current app language
        style = CustomTheme.typography.body.regular
    )
}
```

## Key Points

- Call `toLocalizedStringDesc()` inside `toDomain()` in the data layer — never outside.
- `LocalizedStringDesc` implements `StringDesc`, so it works with `.localized()` in Compose without any extra handling.
- At resolution time, `LocalizedStringDesc` reads the current language from `R.string.common_language_key` and falls back to `"en"` if a translation is missing.
- For mock data, define a `MOCK` constant in the domain class's companion object and reference it from `FakeXxxComponent` — don't construct `LocalizedStringDesc.mock(...)` directly in fake components.
