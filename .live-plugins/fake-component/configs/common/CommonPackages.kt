package configs.common

import fakes.codegen.config.PluginConfig

// Local
val PluginConfig.LoadableState
    get() = "$appPackage.core.utils.LoadableState"
val PluginConfig.PagedState
    get() = "$appPackage.core.utils.PagedState"
val PluginConfig.StandardDialogControl
    get() = "$appPackage.core.dialog.standard.StandardDialogControl"
val PluginConfig.FakeStandardDialogControl
    get() = "$appPackage.core.dialog.standard.fakeStandardDialogControl"
val PluginConfig.SimpleDialogControl
    get() = "$appPackage.core.dialog.simple.SimpleDialogControl"
val PluginConfig.FakeSimpleDialogControl
    get() = "$appPackage.core.dialog.simple.fakeSimpleDialogControl"
val PluginConfig.CreateFakeChildStack
    get() = "$appPackage.core.utils.createFakeChildStack"

// Libs
val InputControl = "ru.mobileup.kmm_form_validation.control.InputControl"
val CheckControl = "ru.mobileup.kmm_form_validation.control.CheckControl"
val GlobalScope = "kotlinx.coroutines.GlobalScope"
val ChildStack = "com.arkivanov.decompose.router.stack.ChildStack"
