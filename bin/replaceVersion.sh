#!/bin/bash

#-----------------------------------------------------------
# 此脚本用于替换相应位置的版本号
#-----------------------------------------------------------

set -o errexit

pwd=$(pwd)

echo "当前路径：${pwd}"

if [ -n "$1" ];then
    newVersion="$1"
    oldVersion=`cat ${pwd}/bin/version.txt`
    echo "$oldVersion 替换为新版本 $newVersion"
else
    # 参数错误，退出
    echo "ERROR: 请指定新版本！"
    exit
fi

if [ ! -n "$oldVersion" ]; then
    echo "ERROR: 旧版本不存在，请确认bin/version.txt中信息正确"
    exit
fi

# 替换README.md中的版本
sed -i "s/${oldVersion}/${newVersion}/g" $pwd/README.md

# 保留新版本号
echo "$newVersion" > $pwd/bin/version.txt