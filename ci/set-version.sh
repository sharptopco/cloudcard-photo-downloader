#!/usr/bin/env bash

set -e

version="$1"

if [ -z "$1" ]; then
    version="$(date +"%y.%m.%d.%H%M")"
fi

echo "Setting version to $version"

#if [[ -n $(git status -s) ]]; then
#    echo "Please make sure git is in a clean state before running this script."
#    exit
#fi

GRADLE_CONF="build.gradle"
PROP_CONF="src/main/java/com/cloudcard/photoDownloader/ApplicationPropertiesValidator.java"

BACKUP=".tmp"
sed -i"$BACKUP" -e "s/String version = .*$/String version = \"$version\";/" $PROP_CONF
sed -i"$BACKUP" -e "s/version = .*$/version = \'$version\'/" $GRADLE_CONF

rm "${PROP_CONF}$BACKUP"
rm "${GRADLE_CONF}$BACKUP"

git add $PROP_CONF
git add $GRADLE_CONF

git commit -m "$version"
git tag "$version"