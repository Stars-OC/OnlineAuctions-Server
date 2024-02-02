package com.onlineauctions.onlineauctions.pojo.user.balance;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@TableName("order")
public class Order {
    @TableId
    private Long orderId;
    private Long username;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER)
    private Long createAt;
}
