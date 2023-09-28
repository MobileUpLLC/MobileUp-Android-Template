1. Create new package in feature or sub-feature directory.
2. Right click on new package, new -> Templates, choose type,
Component creates interface, real and fake implementation, composable ui and preview.
RouterComponent similarly, but with childstack.
3. Component prefix - for example Login = LoginComponent
4. Package path - for example you create package google in feature.auth.login.google,
then you should write auth.login.google in package path field. If you create new package in root of
feature just write it's name.
5. Toggle generate output, it will create sealed interface in component and output lambda in
RealComponent