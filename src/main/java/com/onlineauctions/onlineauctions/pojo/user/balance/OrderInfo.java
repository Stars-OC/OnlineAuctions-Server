package com.onlineauctions.onlineauctions.pojo.user.balance;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@TableName("order_info")
public class OrderInfo {
    @TableId(type = IdType.AUTO)
    private Long orderId;
    private Long cargoId;
    private String title;
    private String description;
    private BigDecimal balance;
    private Integer type;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER)
    private Long createAt;
}
