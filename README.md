# MobileUp Android Template 🌀

Hi! This is a template for initializing an Android project in [MobileUp](https://mobileup.ru/).

### Creating Project

After cloning the template:

1. Search for `ru.mobileup.template` and replace it with the name of your project in the right
   places;
2. Replace application name and icon to the correct ones;
3. Remove the pokemons feature. It created for example-code;
4. Replace error text resources with text for your project;
5. If there is no authorization in the application, remove the extra code from `NetworkApiFactory`.

### Architecture

The architecture is based on three gradle modules: **app**, **core**, **features**.

#### app

The module pieces all the features together and contains `App` and `MainActivity`.

#### core

This module contains general purpose packages:

- `error_handling` (Here are classes with error types, classes for error handling)
- `messages` (Here are classes for showing popup-messages)
- `network` (Here are classes for working with the network)
- `themes` (Here are classes for application style settings)
- `widget` (Frequently used widgets are stored here)

#### features

This module contains concrete features. A feature, as a rule, has its own package. Inside the
package can be located:

- DI-file
- ui package (Components, Jetpack Compose UI)
- domain package (Entities, Interactors)
- data package (Repositories, Storages)

### Technology stack

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Decompose](https://github.com/arkivanov/Decompose)
- [Replica](https://github.com/aartikov/Replica)
- [Sesame](https://github.com/aartikov/Sesame)
- [Retrofit](https://github.com/square/retrofit)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Coil](https://github.com/coil-kt/coil)
- [Detekt](https://github.com/detekt/detekt)
- [Hyperion](https://github.com/willowtreeapps/Hyperion-Android)


### Good coding and happy day!🤘
