package ${packageName}.${path}

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ${componentPrefix}Ui(
    component: ${componentName},
    modifier: Modifier = Modifier
) {

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ${componentPrefix}Ui(component = Fake${componentName}())
}
