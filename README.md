# MobileUp-Android-Template 🌀

Template for initializing an android project in [MobileUp](https://mobileup.ru/).

### Creating Project

After cloning the template:

1. Search for ru.mobileup.template and replace it with the name of your project in the right places;
2. Remove the pokens feature. It created for example-code;
3. Replace error text resources with text for your project;
4. If there is no authorization in the application, remove the extra code from NetworkApiFactory.

### Architecture
The architecture is based on three gradle modules: app, core, features.

#### app

The module contains classes such as: App, MainActivity and DI. Also in this module are helpful utilities for creating unit tests.

#### core

This module contains classes that are used everywhere.
- error_handling (Here are classes with error types, classes for error handling)
- messages (Here are classes for showing popup-messages)
- network (Here are classes for working with the network)
- themes (Here are classes for application style settings)
- widget (Frequently used widgets are stored here)
  
#### features

This module contains features-classes. A feature, as a rule, has its own folder. Inside the folder can be located:
- DI-file
- ui folder (Component, RealComponent, Layout)
- domain folder (Interactors, data classes)
- data folder (Storeges, Gateways)

### Technology stack
- [Sesame](https://github.com/aartikov/Sesame)
- [Replica](https://github.com/aartikov/Replica)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Decompose](https://github.com/arkivanov/Decompose)
- [Retrofit](https://github.com/square/retrofit)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Coil](https://github.com/coil-kt/coil)
- [Detekt](https://github.com/detekt/detekt)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
