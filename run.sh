#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac -d out
java -cp out com.wordgame.Main

