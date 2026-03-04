# Example: Resolving Strings Outside Compose

In Compose, `stringDesc.localized()` handles resolution. Outside Compose — in components, domain helpers, or services — inject `StringService` to resolve a `StringDesc` to a plain `String`.

## StringService — Resolving a StringDesc in a Component

Use `StringService` when your component needs to convert a `StringDesc` to `String` at runtime. A typical case is converting a city name (stored as `LocalizedStringDesc`) into a plain address string to pass to a third-party SDK or an API call.

```kotlin
// domain/CityInfo.kt
fun CityInfo.getAddress(stringService: StringService): Address = Address(
    address = stringService.resolveString(name),    // LocalizedStringDesc → String
    coordinates = coordinates,
)
```

```kotlin
// presentation/RealAddressEditComponent.kt
class RealAddressEditComponent(
    componentContext: ComponentContext,
    private val stringService: StringService,
    // ...
) : AddressEditComponent {

    fun onCitySelected(city: CityInfo) {
        val address = city.getAddress(stringService)
        // use plain String address for Google Maps API, sharing, etc.
    }
}
```

## Key Points

- Never call `.localized()` outside a `@Composable` function — it is Compose-only.
- `StringService` wraps `Context` internally, so you never need to pass Context through components.
- Inject via Koin — `StringService` is registered as a singleton in `core/DI.kt`.
- Do **not** create a service instance manually; always inject it.
