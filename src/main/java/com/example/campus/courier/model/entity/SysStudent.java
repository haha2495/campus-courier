package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.campus.courier.model.enums.UserRoleEnum;
import com.example.campus.courier.model.request.student.StudentProfileUpdateRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Data
@TableName("sys_student")
@Schema(description = "学生信息")
public class SysStudent {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "学号（登录账号）")
    private String studentNo;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "院系")
    private String department;

    @Schema(description = "专业")
    private String major;

    @Schema(description = "年级")
    private String grade;

    @Schema(description = "班级")
    private String className;

    @Schema(description = "入学年份")
    private Integer enrollmentYear;

    @Schema(description = "身份类型")
    private Integer identityType;

    @Schema(description = "状态：1-正常，0-禁用")
    private Boolean status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "账户余额")
    private BigDecimal balance;

    /**
     * 初始化学生信息
     */
    public SysStudent init(String studentNo, String password, String name, Integer gender,
                           String department, String major, String grade, String className,
                           Integer enrollmentYear, Boolean status,
                           String avatar, String email) {
        this.studentNo = studentNo;
        this.password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.major = major;
        this.grade = grade;
        this.className = className;
        this.enrollmentYear = enrollmentYear;
        this.identityType = UserRoleEnum.STUDENT.getCode();
        this.status = status;
        this.createTime = LocalDateTime.now();
        this.avatar = avatar;
        this.email = email;
        this.balance = BigDecimal.ZERO;
        return this;
    }

    /**
     * 更新个人信息
     *
     * @param request 更新请求
     */
    public void updateProfile(StudentProfileUpdateRequest request) {
        if (StringUtils.hasText(request.getName())) {
            this.name = request.getName();
        }
        if (StringUtils.hasText(request.getAvatar())) {
            this.avatar = request.getAvatar();
        }
        if (StringUtils.hasText(request.getEmail())) {
            this.email = request.getEmail();
        }
        if (StringUtils.hasText(request.getPassword())) {
            this.password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        }
    }

    public void updatePassword(String newPassword) {
        this.password = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 增加余额
     * @param amount 增加的金额
     */
    public void increaseBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        }
    }

    /**
     * 减少余额
     * @param amount 减少的金额
     * @return 是否扣减成功
     */
    public boolean decreaseBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }

    /**
     * 检查余额是否足够
     * @param amount 要检查的金额
     * @return 余额是否足够
     */
    public boolean hasEnoughBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }
}