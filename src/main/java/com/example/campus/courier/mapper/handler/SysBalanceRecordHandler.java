package com.example.campus.courier.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.courier.mapper.SysBalanceRecordMapper;
import com.example.campus.courier.model.entity.SysBalanceRecord;
import com.example.campus.courier.model.entity.SysStudent;
import com.example.campus.courier.model.enums.BalanceChangeTypeEnum;
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
        // 判断是否为收入类型（充值=1, 收入=3）
        boolean isIncome = type.equals(BalanceChangeTypeEnum.RECHARGE.getCode()) 
                       || type.equals(BalanceChangeTypeEnum.INCOME.getCode());
        
        // 计算变动前的余额
        BigDecimal beforeBalance = isIncome ? 
            student.getBalance().subtract(amount) :  // 收入类型：当前余额减去变动金额
            student.getBalance().add(amount);        // 支出类型：当前余额加上变动金额
        
        // 创建记录
        SysBalanceRecord record = new SysBalanceRecord()
                .setStudentId(student.getId())
                .setAmount(amount)
                .setType(type)
                .setBeforeBalance(beforeBalance)
                .setAfterBalance(student.getBalance())
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