package libraries

import org.neotech.plugin.rootcoverage.RootCoveragePlugin
import org.neotech.plugin.rootcoverage.RootCoveragePluginExtension


apply<RootCoveragePlugin>()

configure<JacocoPluginExtension> {
    toolVersion = "0.8.8"
}

configure<RootCoveragePluginExtension> {
    // The default build variant for every module
    buildVariant = "qaDebug"
    // Overrides the default build variant for specific modules.
    // buildVariantOverrides ":moduleA" : "debugFlavourA", ":moduleB": "debugFlavourA"

    // Since 1.1 generateHtml is by default true
    generateCsv = false
    generateHtml = false
    generateXml = true

    // Since 1.2: Same as executeTests except that this only affects the instrumented Android tests
    executeAndroidTests = false

    // Since 1.2: Same as executeTests except that this only affects the unit tests
    executeUnitTests = true

    // Since 1.2: When true include results from instrumented Android tests into the coverage report
    includeAndroidTestResults = true

    // Since 1.2: When true include results from unit tests into the coverage report
    includeUnitTestResults = true

    // Since 1.4: Sets jacoco.includeNoLocationClasses, so you don't have to. Helpful when using Robolectric
    // which usually requires this attribute to be true
    includeNoLocationClasses = false

    // Class & package exclude patterns
    excludes += listOf(
        // Core Android generated class filters
        "androidx/**/*.*",
        "**/AndroidManifest*.*",
        "**/*Test*.*",
        // kotlin generated class filters
        "**/*\$Lambda$*.*",
        "**/*Companion*.*",
        // sealed and data classes generated class filters
        "**/*\$Result.*",
        "**/*\$Result$*.*",
        // android room generated class filters
        "**/*_Impl.*",
        // generated classes and functions filters
        "**/*$*.*",
        "**/generated/*",
        "**/generated/**/*",

        // current project generated class filters
        "**/model/**/*",
        "**/models/**/*",
        "**/*ViewHolder.*",
        "**/*Fragment.*",
        "**/*Activity.*",
        "**/*Dialog.*",
        "**/*DialogFragment.*",
        "**/*App.*",
        "**/ui/*Adapter.*",
        "**/ui/**/*Adapter.*",
        "**/*JsonAdapter.*",
        "**/dao/*",
        "com/inetpsa/**/*",
        "com/gma/**/*",
        "com/space/gma/**/*",
        "**/di/**/*", // filter dependency injection modules
        "**/bindings/**/*", // filter data bindings function
        "**/binding/**/*",
        "**/ui/views/**/*", // filter custom views components
        "**/ui/drawables/**/*", // filter custom drawables
        "**/ui/delegates/*", // filter UI delegates
        "**/presentation/compose/*", // filter Compose
        "**/test/**/*", // filter test package
        "com/stellantis/space/feature/theme/**", // filter not needed theme modules
        "com/stellantis/logger/**" // filter not needed logger modules
    )
}
