package com.onlineauctions.onlineauctions.pojo.type;

import lombok.Getter;

public enum Role {

    /**
     * 普通用户
     */
    USER(0),
    /**
     * 课堂助手(班长)
     */
    COURSE_ASSISTANT(1),
    /**
     * 教师
     */
    TEACHER(2),
    /**
     * 公司管理员
     */
    COMPANY_ADMIN(3),
    /**
     * 学校管理员
     */
    SCHOOL_ADMIN(4),
    /**
     * 超级管理员
     */
    ADMIN(5),
    ;

    Role(int role) {
        this.role = role;
    }


    @Getter
    private int role;

    public static boolean isCourseRole(Role[] roles){
        for(Role role : roles){
            if(role.equals(COURSE_ASSISTANT) || role.equals(TEACHER)){
                return true;
            }
        }
        return false;
    }

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
