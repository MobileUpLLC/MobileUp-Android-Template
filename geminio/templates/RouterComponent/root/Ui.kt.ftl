package ${packageName}.${packagePath}

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mobileup.template.core.theme.AppTheme

@Composable
fun ${uiName}(
    component: ${componentName},
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
         when (val instance = child.instance) {
             is ${componentName}.Child.Default -> {}
         }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ${uiName}(component = Fake${componentName}())
    }
}
