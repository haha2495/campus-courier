package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysBalanceRecordMapper;
import com.example.campus.courier.model.entity.SysBalanceRecord;
import com.example.campus.courier.model.entity.SysStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SysBalanceRecordHandler extends ServiceImpl<SysBalanceRecordMapper, SysBalanceRecord> {

    /**
     * 创建余额变动记录
     *
     * @param student     学生信息
     * @param amount      变动金额
     * @param type        变动类型
     * @param description 变动说明
     */
    public void createRecord(SysStudent student, BigDecimal amount, Integer type, String description) {
        // 创建记录
        SysBalanceRecord record = new SysBalanceRecord()
                .setStudentId(student.getId())
                .setAmount(amount)
                .setType(type)
                .setBeforeBalance(student.getBalance())
                .setAfterBalance(student.getBalance().add(amount))
                .setDescription(description);

        // 保存记录
        save(record);
    }

    /**
     * 获取学生的余额变动记录
     *
     * @param studentId 学生ID
     * @return 余额变动记录列表
     */
    public List<SysBalanceRecord> getStudentBalanceRecords(Integer studentId) {
        return list(new LambdaQueryWrapper<SysBalanceRecord>()
                .eq(SysBalanceRecord::getStudentId, studentId)
                .orderByDesc(SysBalanceRecord::getCreateTime));
    }
} 