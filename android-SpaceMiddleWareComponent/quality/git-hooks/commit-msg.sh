#!/bin/sh

NC='\033[0m' # No Color
RED='\033[0;31m'
YELLOW='\033[0;33m'
BOLDYELLOW='\033[1;33m'
GREEN='\033[0;32m'

if ! head -1 "$1" | grep -qE "^((ci|chore|docs|test|refactor|revert)\(.+\)|(feat|fix)\((SPMDWSDK)-[0-9]+\))!?: .+"; then
  echo -e "${RED}[ERROR] Your commit message is not valid, please follow the pattern:${NC}" >&2
  echo -e "   ${YELLOW}!type(?scope): !subject !#ticket${NC}" >&2
  echo "" >&2
  echo "Where:" >&2
  echo -e "   - ${BOLDYELLOW}type${NC}: is mandatory (!) and indicates the nature of the change. It accepts: feat, fix, pref, ci, chore, docs, test, style, refactor, revert or build." >&2
  echo -e "   - ${BOLDYELLOW}scope${NC}: is optional (?) and represents the code area/feature being changed." >&2
  echo -e "   - ${BOLDYELLOW}subject${NC}: is mandatory (!) and indicates what will happen when the commit is applied. Use verbs in infitive instead of past (i.e. 'add unit test' vs 'added unit test')." >&2
  echo -e "   - ${BOLDYELLOW}ticket${NC}: is mandatory (!) and represents the Azure DevOps ticket number related to the work." >&2
  echo "" >&2
  echo -e "Examples:${GREEN}" >&2
  echo "   feature(connectors/Pulse): add patient gender field #3333" >&2
  echo "   fix(labResults): change result value converter #1234" >&2
  echo "   test(diary): add integration tests #6543" >&2
  echo "   build: add integration test step #222" >&2
  echo "   docs: add instructions to debug locally #9876" >&2
  echo -e "   chore: update version to 3.24.0 #666${NC}" >&2
  exit 1
fi
if ! head -1 "$1" | grep -qE "^.{1,150}$"; then
  echo "Aborting commit. Your commit message is too long." >&2
  exit 1
fi
