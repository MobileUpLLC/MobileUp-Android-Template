package ${packageName}.${packagePath}

import ru.mobileup.template.core.utils.createFakeChildStackStateFlow

class Fake${componentName} : ${componentName} {

    override val childStack = createFakeChildStackStateFlow(${componentName}.Child.Default)

}