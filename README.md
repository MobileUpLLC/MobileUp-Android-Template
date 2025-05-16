# MobileUp Android Template 🌀


Hi! This is a template for initializing an Android project in [MobileUp](https://mobileup.ru/).

## Creating Project

After cloning the template:

1. Replace `ru.mobileup.template` with the package name of your project.
2. Replace application name and icon to the correct ones.
3. Remove the pokemons feature. It is created as an example.
4. Replace error text resources with text for your project.

Or simply run the setup script:

`bash scripts/setup-project <new.application.id> <JIRA_PROJECT_KEY>`

This will:

* Rename the package
* Reset Git history
* Set up Git hooks for Detekt and commit message formatting

## Modules
The project is based on three gradle modules:

#### app
It pieces all the features together, contains `Application` and `Activity` classes.

#### core
It contains general purpose things: error handing, message showing, network, theme, utils, reusable widgets.

#### features
It consists of concrete features. Each feature has its own package which contains:
- DI configuration
- **ui layer** - components, Jetpack Compose UI
- **domain layer** - entities, interactors
- **data layer** - repositories, storages

## Features structure
<img src="features/module_graph/modules.svg">

## Technology stack
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI
- [Decompose](https://github.com/arkivanov/Decompose) - componentization and navigation
- [Replica](https://github.com/aartikov/Replica) - organizing of network communication
- [Koin](https://github.com/InsertKoinIO/koin) - Dependency Injection
- [Ktor](https://ktor.io/) - Network
- [Ktorfit](https://github.com/Foso/Ktorfit) - network requests in Retrofit way
- [Coroutines](https://developer.android.com/kotlin/coroutines) - asynchronous operations
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization) - JSON serialization and parsing
- [Coil](https://github.com/coil-kt/coil) - image loading
- [Detekt](https://github.com/detekt/detekt) - static code analysis
- [Hyperion](https://github.com/willowtreeapps/Hyperion-Android) - debug panel
- [Module Graph Gradle Plugin](https://github.com/MobileUpLLC/Module-Graph-Gradle-Plugin) - feature dependency graph visualization and validation

### Git hooks
1. Runs detekt static code analysis and module graph validation before commit. Installation:
   `cp git_hooks/pre-commit .git/hooks/ && chmod 0775 .git/hooks/pre-commit`

2. Adds issue prefix in every commit message according to branch name. Installation:
   `cp git_hooks/prepare-commit-msg .git/hooks/ && chmod 0775 .git/hooks/prepare-commit-msg`

Or simply run the setup script:

`bash scripts/setup-git-hooks <JIRA_PROJECT_KEY>`

### Geminio templates - creates boilerplate code
1. Read installation instruction root/geminio/SETUP.MD
2. Read user instruction  root/geminio/HOW_TO_USE.MD

### Compose detekt rules docs
https://mrmans0n.github.io/compose-rules/

### Good coding and happy day!🤘

## License
```
MIT License

Copyright (c) 2023 MobileUp

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```