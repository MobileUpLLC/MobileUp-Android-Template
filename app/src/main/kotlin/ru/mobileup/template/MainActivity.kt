package ru.mobileup.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import ru.mobileup.template.core.ComponentFactory
import ru.mobileup.template.core.koin
import ru.mobileup.template.core.theme.AppTheme
import ru.mobileup.template.features.root.createRootComponent
import ru.mobileup.template.features.root.ui.RootUi

// Note: rootComponent survives configuration changes due to "android:configChanges" setting in the manifest.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val componentFactory = application.koin.get<ComponentFactory>()
        val rootComponent = componentFactory.createRootComponent(defaultComponentContext())

        setContent {
            AppTheme {
                RootUi(rootComponent)
            }
        }
    }
}