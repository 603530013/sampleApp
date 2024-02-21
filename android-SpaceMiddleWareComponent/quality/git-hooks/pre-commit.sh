#!/bin/sh

echo "Check branch name convention..."

BRANCH=$(git rev-parse --abbrev-ref HEAD)
REGEX="((feature|bugfix|hotfix)\/(SPMDWSDK)-[0-9]+_|(task|improvement|ci)/)([a-zA-Z0-9\.]+-?)+$"

if ! [[ $BRANCH =~ $REGEX ]]; then
  echo "Your commit was rejected due to branching name"
  echo "Please rename your branch with $REGEX syntax"
  echo "Branch name should be like feature/SPMDWSDK-XX_short-title"
  exit 1
else
  echo "Your commit respect the branching name"
fi

echo "Running static analysis..."

JAVA_HOME=$(/usr/libexec/java_home)
export JAVA_HOME

OUTPUT="build/tmp/analysis-result"
mkdir -p ${OUTPUT}
./gradlew detekt spotlessApply lintFix >${OUTPUT}/output.txt

EXIT_CODE=$?
if [ ${EXIT_CODE} -ne 0 ]; then
  cat ${OUTPUT}
  rm ${OUTPUT}
  echo "*********************************************"
  echo "            Static Analysis Failed           "
  echo "Please fix the above issues before committing"
  echo "*********************************************"
  exit ${EXIT_CODE}
else
  rm ${OUTPUT}
  echo "*********************************************"
  echo "      Static analysis no problesms found      "
  echo "*********************************************"
fi
