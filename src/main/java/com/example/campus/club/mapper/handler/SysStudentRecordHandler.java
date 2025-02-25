package com.example.campus.club.mapper.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus.club.mapper.SysStudentRecordMapper;
import com.example.campus.club.model.dto.StudentRecordDTO;
import com.example.campus.club.model.entity.SysStudentRecord;
import com.example.campus.club.model.enums.StudentRecordType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "学生记录处理器")
@Component
@RequiredArgsConstructor
public class SysStudentRecordHandler extends ServiceImpl<SysStudentRecordMapper, SysStudentRecord> {

    /**
     * 创建学生记录
     */
    public void createRecord(Integer studentId, StudentRecordType type, String content) {
        save(SysStudentRecord.build(studentId, type, content));
    }

    /**
     * 获取学生的所有记录
     */
    public List<SysStudentRecord> getStudentRecords(Integer studentId) {
        LambdaQueryWrapper<SysStudentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudentRecord::getStudentId, studentId)
                .orderByDesc(SysStudentRecord::getCreateTime);
        return list(wrapper);
    }

    /**
     * 分页获取学生的记录
     */
    public Page<SysStudentRecord> pageStudentRecords(Integer studentId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysStudentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysStudentRecord::getStudentId, studentId)
                .orderByDesc(SysStudentRecord::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 获取学生记录列表
     *
     * @param studentId 学生ID
     * @return 记录列表
     */
    public List<StudentRecordDTO> getStudentRecords(Long studentId) {
        return lambdaQuery()
                .eq(SysStudentRecord::getStudentId, studentId)
                .orderByDesc(SysStudentRecord::getCreateTime)
                .list()
                .stream()
                .map(record -> {
                    StudentRecordDTO dto = new StudentRecordDTO();
                    StudentRecordType recordType = StudentRecordType.getByCode(record.getType());
                    dto.setType(recordType != null ? recordType.getDesc() : "未知操作");
                    dto.setContent(record.getContent());
                    dto.setCreateTime(record.getCreateTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }
} 