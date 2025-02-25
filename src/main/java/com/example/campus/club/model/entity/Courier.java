package com.example.campus.club.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("courier")
@Schema(description = "跑腿者实体")
public class Courier {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "头像")
    private String avatar;
    
    @Schema(description = "昵称")
    private String nickname;
    
    @Schema(description = "联系方式")
    private String contact;
    
    @Schema(description = "个人简介")
    private String introduction;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "完成订单数")
    private Integer completedOrders;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 