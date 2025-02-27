package configs.common

import fakes.codegen.config.PluginConfig

// Local
val PluginConfig.LoadableStatePackage
    get() = "$appPackage.core.utils.LoadableState"
val PluginConfig.PagedStatePackage
    get() = "$appPackage.core.utils.PagedState"
val PluginConfig.StandardDialogControlPackage
    get() = "$appPackage.core.dialog.standard.StandardDialogControl"
val PluginConfig.FakeStandardDialogControlPackage
    get() = "$appPackage.core.dialog.standard.fakeStandardDialogControl"
val PluginConfig.SimpleDialogControl
    get() = "$appPackage.core.dialog.simple.SimpleDialogControl"
val PluginConfig.FakeSimpleDialogControl
    get() = "$appPackage.core.dialog.simple.fakeSimpleDialogControl"
val PluginConfig.CreateFakeChildStack
    get() = "$appPackage.core.utils.createFakeChildStack"

// Libs
val InputControlPackage = "ru.mobileup.kmm_form_validation.control.InputControl"
val CheckControlPackage = "ru.mobileup.kmm_form_validation.control.CheckControl"
val GlobalScopePackage = "kotlinx.coroutines.GlobalScope"
val ChildStack = "com.arkivanov.decompose.router.stack.ChildStack"
