package com.onlineauctions.onlineauctions.pojo.user.balance;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("wallet")
public class Wallet {
    private Long username;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private BigDecimal money;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private BigDecimal fund;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateAt;

}
