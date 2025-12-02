package ${packageName}.${packagePath}

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mobileup.template.core.theme.AppTheme

@Composable
fun ${uiName}(
    component: ${componentName},
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ${uiName}(component = Fake${componentName}())
    }
}
