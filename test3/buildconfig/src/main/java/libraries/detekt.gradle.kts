package libraries

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import plugins.Version

apply<DetektPlugin>()

configure<DetektExtension> {
    source = project.files("src/main/kotlin")
    config = files("$rootDir/.detekt/config.yml")
    ignoreFailures = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Version.jdk.toString()
    parallel = true
    autoCorrect = true
    ignoreFailures = true

    reports {
        xml {
            required.set(true)
            outputLocation.set(file("$rootDir/build/reports/detekt/report.xml"))
        }

        html {
            required.set(true)
            outputLocation.set(file("$rootDir/build/reports/detekt/report.html"))
        }
    }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = Version.jdk.toString()
}