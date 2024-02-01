package com.onlineauctions.onlineauctions.pojo.auction;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlineauctions.onlineauctions.pojo.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cargo")
public class Cargo {

    @TableId(type = IdType.AUTO)
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Long cargoId;

    @NotEmpty(message = "拍卖物品的名称不能为空")
    private String name;

    private String description;

    private Resource resource;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long seller;

    @NotNull(message = "起拍价不能为空")
    private BigDecimal startingPrice;

    @NotNull(message = "加价幅度不能为空")
    private BigDecimal additionalPrice;

    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Long startTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Long endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER)
    private Long createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer status;

}
