package com.example.campus.courier.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {
    
    /**
     * 获取中文姓名的首字母
     * @param name 中文姓名
     * @return 首字母（大写）
     */
    public static String getFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        char firstChar = name.charAt(0);
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        
        if (Character.toString(firstChar).matches("[\\u4E00-\\u9FA5]+")) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    result.append(pinyinArray[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            result.append(Character.toUpperCase(firstChar));
        }
        
        return result.toString();
    }
} 