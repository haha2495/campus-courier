package com.example.campus.courier.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.campus.courier.common.annotation.IpReplace;
import com.example.campus.courier.common.utils.TaskUtils;
import com.example.campus.courier.model.enums.TaskStatusEnum;
import com.example.campus.courier.model.request.task.TaskAddRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 任务实体类
 */
@Data
@TableName(value = "sys_task",autoResultMap = true)
@Schema(description = "任务实体")
public class SysTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "任务ID")
    private Integer id;

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "任务标题")
    private String title;

    @Schema(description = "任务详细描述")
    private String description;

    @Schema(description = "发布者用户ID")
    private Integer publisherId;

    @Schema(description = "接单者用户ID")
    private Integer runnerId;

    @Schema(description = "任务类型ID")
    private Integer taskTypeId;

    @Schema(description = "任务状态：0-待接单，1-已接单，2-进行中，3-待确认，4-已完成，5-已取消，6-已过期，7-存在纠纷（参见TaskStatusEnum枚举类）")
    private Integer status;

    @Schema(description = "任务报酬")
    private BigDecimal reward;

    @Schema(description = "是否加急：0-否，1-是")
    private Boolean urgent;

    @Schema(description = "加急费")
    private BigDecimal urgentReward;

    @Schema(description = "取件地址")
    private String pickupAddress;

    @Schema(description = "送达地址")
    private String deliveryAddress;

    @Schema(description = "预估重量(kg)")
    private BigDecimal estimatedWeight;

    @Schema(description = "预估大小（如小、中、大）")
    private String estimatedSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "预期完成时间")
    private LocalDateTime expectedFinishTime;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @IpReplace
    @Schema(description = "图片(JSON格式)")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    @Schema(description = "备注说明")
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 初始化任务信息
     * 
     * @param request 任务添加请求
     * @param publisherId 发布者ID
     * @return 当前实体对象
     */
    public SysTask init(TaskAddRequest request, Integer publisherId) {
        // 1. 设置任务基本信息
        this.taskNo = TaskUtils.generateTaskNo();
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.publisherId = publisherId;
        this.taskTypeId = request.getTypeId();
        this.status = TaskStatusEnum.PENDING.getCode(); // 设置初始状态为待接单
        this.reward = request.getReward();
        this.urgent = request.getUrgent();
        this.urgentReward = request.getUrgentReward();
        this.pickupAddress = request.getPickupAddress();
        this.deliveryAddress = request.getDeliveryAddress();
        this.estimatedWeight = request.getEstimatedWeight();
        this.estimatedSize = request.getEstimatedSize();
        
        // 2. 转换并设置预期完成时间
        if (request.getExpectedFinishTime() != null && !request.getExpectedFinishTime().isEmpty()) {
            this.expectedFinishTime = LocalDateTime.parse(
                request.getExpectedFinishTime(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
        }
        
        // 3. 设置联系人信息
        this.contactName = request.getContactName();
        this.contactPhone = request.getContactPhone();
        
        // 4. 设置图片和备注
        this.images = request.getImages();
        this.notes = request.getNotes();
        
        // 5. 设置创建时间
        this.createTime = LocalDateTime.now();
        
        return this;
    }

    /**
     * 更新任务状态
     *
     * @param status 任务状态枚举
     */
    public void updateStatus(TaskStatusEnum status) {
        this.status = status.getCode();
    }

    public void acceptTask(Integer userId) {
        this.status = TaskStatusEnum.ACCEPTED.getCode();
        this.runnerId = userId;
    }

    /**
     * 计算任务总价(包含加急费)
     *
     * @return 任务总价
     */
    public BigDecimal calculateTotalPrice() {
        BigDecimal totalPrice = this.reward;
        if (Boolean.TRUE.equals(this.urgent) && this.urgentReward != null) {
            totalPrice = totalPrice.add(this.urgentReward);
        }
        return totalPrice;
    }
}