package libraries

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

apply<SpotlessPlugin>()

configure<SpotlessExtension> {
    format("misc") {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.md", "**/.gitignore", "**/*.yaml", "**/*.yml"),
                    "exclude" to listOf(
                        ".gradle/**",
                        ".gradle-cache/**",
                        "**/tools/**",
                        "**/build/**"
                    )
                )
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    format("xml") {
        target("**/res/**/*.xml")
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlin {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.kt"),
                    "exclude" to listOf("**/build/**", "**/buildSrc/**", "**/.*")
                )
            )
        )

        ktlint()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()

        custom("Remove commented-out import statements") {
            it.replace(Regex("\\R?(//|/\\*)+\\s*import.*"), "")
        }

        custom("Refuse wildcard imports") {
            // Wildcard imports can't be resolved by spotless itself.
            // This will require the developer themselves to adhere to best practices.

            when (Regex("\\R?import.*\\.\\*").find(it)) {
                null -> it

                else -> {
                    val message = "Don't use wildcard imports.  'spotlessApply' cannot resolve this issue."
                    throw AssertionError(message)
                }
            }
        }
    }

    kotlinGradle {
        target(
            fileTree(
                mapOf(
                    "dir" to ".",
                    "include" to listOf("**/*.gradle.kts", "*.gradle.kts"),
                    "exclude" to listOf("**/build/**")
                )
            )
        )

        ktlint()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}