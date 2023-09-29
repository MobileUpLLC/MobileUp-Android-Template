package ${packageName}.${path}

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@Composable
fun ${componentPrefix}Ui(
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

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ${componentPrefix}Ui(component = Fake${componentName}())
}
