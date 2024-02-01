package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

public enum Role {

    /**
     * 普通用户
     */
    USER(0),

    /**
     * 拍卖物品管理员
     */
    CARGO_ADMIN(3),

    /**
     * 管理员
     */
    ADMIN(5),
    ;

    Role(int role) {
        this.role = role;
    }


    @Getter
    private final int role;


    /**
     * 检查用户是否具有指定的角色
     * @param roles 角色数组
     * @param role 待检查的角色
     * @return 如果用户具有指定角色，返回true；否则返回false
     */
    public static boolean checkRole(Role[] roles,int role){
        for (Role type : roles) {
            // 如果遍历到的角色类型和当前用户的角色类型一致，则继续执行方法
            if (type.getRole() == role) {
                return true;
            }
        }
        return false;
    }


}
