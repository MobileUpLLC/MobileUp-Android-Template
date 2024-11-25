import liveplugin.registerIntention
import fakes.view.CreateFakeComponentIntention
import fakes.codegen.api.rules.common.withTopType

// depends-on-plugin org.jetbrains.kotlin
// add-to-classpath $PLUGIN_PATH/kotlinpoet-1.14.0.jar

registerIntention(CreateFakeComponentIntention())
