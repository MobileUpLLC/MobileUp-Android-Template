1. Create a new directory in the features directory. For example `features/auth/presentation`
   or `features/auth/presentation/login/google`.
2. Right click on the directory, New -> Templates, choose type:

- Component - creates interface, real and fake implementation, composable UI and preview.
- RouterComponent - similarly, but with `childStack` for navigation.

3. Enter "Component prefix". For example `Auth` or `GoogleLogin`
4. Enter "Package path" - path inside `features` package. For example `auth.presentation`
   or `auth.presentation.login.google`.
5. Toggle "Generate output" if required. When toggled outputs will be created: a sealed interface in
   component interface and an output lambda in real component constructor.