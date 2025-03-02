package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.campus.courier.common.annotation.IpReplace;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Data
@TableName("sys_admin")
@Schema(description = "系统管理员")
public class SysAdmin {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    @IpReplace
    private String avatar;

    @Schema(description = "状态：1-正常，0-禁用")
    private Boolean status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 初始化管理员信息
     *
     * @param username 用户名
     * @param password 密码（未加密）
     * @param nickname 昵称
     * @param avatar   头像URL
     * @param status   状态
     * @return 当前对象
     */
    public SysAdmin init(String username, String password, String nickname, String avatar, Boolean status) {
        this.username = username;
        this.password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        this.nickname = nickname;
        this.avatar = avatar;
        this.status = status;
        this.createTime = LocalDateTime.now();
        return this;
    }

}