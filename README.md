- # android-external-sdk
- #### Make code static analyses

Install GitHooks Pre-Commit on OSX/Linux

```sh
./gradlew installGitHooks
```

GitHooks will start some tasks on pre-commit with "detekt" and "spotlessApply" to ensure the code
respect standard of quality and format.

If commit failed, read the error threw, then fix your code and commit again.

```sh
./gradlew detekt spotlessApply --daemon
```
