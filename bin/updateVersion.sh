#!/bin/bash

#------------------------------------------------
# 更新B-IM版本，包括：
# 1. 升级pom.xml中的版本号
# 2. 替换README.md的版本号
#------------------------------------------------

if [ ! -n "$1" ]; then
    echo "ERROR: 新版本不存在，请指定参数1"
    exit
fi

# 版本
version=$1

# 项目路径
projectPath=$(dirname $(readlink -f "$0"))/..

# 替换所有模块pom.xml中的版本
mvn versions:set -DnewVersion=$version -f $projectPath/pom.xml

# 替换其它地方的版本
$projectPath/bin/replaceVersion.sh "$version"