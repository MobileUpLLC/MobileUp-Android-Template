package ${packageName}.${packagePath}

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.mobileup.template.core.theme.AppTheme

@Composable
fun ${uiName}(
    component: ${componentName},
    modifier: Modifier = Modifier
) {

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ${uiName}(component = Fake${componentName}())
    }
}
