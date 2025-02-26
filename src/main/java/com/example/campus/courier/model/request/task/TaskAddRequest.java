package com.example.campus.courier.model.request.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务添加请求对象
 */
@Data
@Schema(description = "任务添加请求")
public class TaskAddRequest {

    @NotNull(message = "任务类型ID不能为空")
    @Schema(description = "任务类型ID", required = true)
    private Integer typeId;

    @NotBlank(message = "任务标题不能为空")
    @Size(min = 2, max = 50, message = "任务标题长度应在2-50个字符之间")
    @Schema(description = "任务标题", required = true)
    private String title;

    @NotBlank(message = "任务描述不能为空")
    @Size(min = 5, max = 500, message = "任务描述长度应在5-500个字符之间")
    @Schema(description = "任务描述", required = true)
    private String description;

    @NotBlank(message = "取件地址不能为空")
    @Schema(description = "取件地址", required = true)
    private String pickupAddress;

    @NotBlank(message = "送达地址不能为空")
    @Schema(description = "送达地址", required = true)
    private String deliveryAddress;

    @NotBlank(message = "预期完成时间不能为空")
    @Schema(description = "预期完成时间(格式:yyyy-MM-dd HH:mm:ss)", required = true)
    private String expectedFinishTime;

    @NotNull(message = "任务报酬不能为空")
    @DecimalMin(value = "0.01", message = "任务报酬必须大于0")
    @Schema(description = "任务报酬", required = true)
    private BigDecimal reward;

    @Schema(description = "是否加急(true-是,false-否)", defaultValue = "false")
    private Boolean urgent = false;

    @Schema(description = "加急费用")
    private BigDecimal urgentReward;

    @Schema(description = "预估重量(kg)")
    private BigDecimal estimatedWeight;

    @Schema(description = "预估大小(小件/中件/大件/特大件)")
    private String estimatedSize;

    @NotBlank(message = "联系人姓名不能为空")
    @Schema(description = "联系人姓名", required = true)
    private String contactName;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号码")
    @Schema(description = "联系电话", required = true)
    private String contactPhone;

    @Schema(description = "图片列表")
    private List<String> images;

    @Schema(description = "备注说明")
    private String notes;
} 