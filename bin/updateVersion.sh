#!/bin/bash

#------------------------------------------------
# 更新B-IM版本，包括：
# 1. 升级pom.xml中的版本号
# 2. 替换README.md的版本号
#------------------------------------------------

if [[ ! -n "$1" ]]; then
    echo "ERROR: 新版本不存在, 请指定参数1"
    exit
fi

# 项目路径
projectPath=$(dirname $(readlink -f "$0"))/..
# 新版本
newVersion="$1"
# 旧版本
oldVersion=`cat ${projectPath}/bin/version.txt`

if [[ ! -n "${oldVersion}" ]]; then
    echo "ERROR: 旧版本不存在, 请确认bin/version.txt中信息正确"
    exit
fi

# 替换所有模块pom.xml中的版本
mvn versions:set -DnewVersion=${newVersion} -f ${projectPath}/pom.xml

# 替换README.md中的版本
sed -i "s/${oldVersion}/${newVersion}/g" ${projectPath}/README.md

# 保留新版本
echo "${newVersion}" > ${projectPath}/bin/version.txt

echo "版本更新成功: ${oldVersion} -> ${newVersion}"