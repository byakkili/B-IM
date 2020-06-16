package com.github.byakkili.bim.demo.util;

import cn.hutool.core.util.RandomUtil;

/**
 * @author Guannian Li
 */
public class NameUtils {
    /**
     * 姓氏
     */
    private static final String[] FAMILY_NAMES = new String[]{"谭", "刘", "张", "李", "胡", "沈", "朱", "钱", "王", "伍", "赵", "孙", "吕", "马", "秦", "毛", "成", "梅", "黄", "郭", "杨", "季", "童", "习", "郑",
            "吴", "周", "蒋", "卫", "尤", "何", "魏", "章", "郎", " 唐", "汤", "苗", "孔", "鲁", "韦", "任", "袁", "贺", "狄朱"};
    /**
     * 名字
     */
    private static final String[] SECOND_NAMES = new String[]{"艺昕", "红薯", "明远", "天蓬", "三丰", "德华", "歌", "佳", "乐", "天", "燕子", "子牛", "海", "燕", "花", "娟", "冰冰", "丽娅", "大为", "无为", "渔民", "大赋",
            "明", "远平", "克弱", "亦菲", "靓颖", "富城", "岳", "先觉", "牛", "阿狗", "阿猫", "辰", "蝴蝶", "文化", "冲之", "悟空", "行者", "悟净", "悟能", "观", "音", "乐天", "耀扬", "伊健", "炅", "娜", "春花", "秋香", "春香",
            "大为", "如来", "佛祖", "科比", "罗斯", "詹姆屎", "科神", "科蜜", "库里", "卡特", "麦迪", "乔丹", "魔术师", "加索尔", "法码尔", "南斯", "伊哥", "杜兰特", "保罗", "杭州", "爱湘", "湘湘", "昕", "函", "鬼谷子", "膑", "荡",
            "子家", "德利优视", "五方会谈", "来电话了", "T-IO", "Talent", "轨迹", "超"};

    /**
     * 随机生成姓名
     *
     * @return 姓名
     */
    public static String randomNickname() {
        return FAMILY_NAMES[RandomUtil.randomInt(0, FAMILY_NAMES.length - 1)] + SECOND_NAMES[RandomUtil.randomInt(0, SECOND_NAMES.length - 1)];
    }
}
