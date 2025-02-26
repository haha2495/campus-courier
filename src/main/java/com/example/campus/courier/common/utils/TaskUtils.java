package com.example.campus.courier.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 任务相关工具类
 */
public class TaskUtils {

    private static final Random RANDOM = new Random();

    /**
     * 生成任务编号
     * 格式：TASK + 年月日时分秒 + 4位随机数
     * 例如：TASK2023112015301234
     * 
     * @return 生成的任务编号
     */
    public static String generateTaskNo() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化时间为yyyyMMddHHmmss
        String timeStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 生成4位随机数
        String randomStr = String.format("%04d", RANDOM.nextInt(10000));
        // 拼接任务编号
        return "TASK" + timeStr + randomStr;
    }
} 