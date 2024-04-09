package cn.granitech.business.service;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2021/2/19 17:21
 * @Version: 1.0
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 系统功能权限定义，从6000以后开始
 */

public class SystemRights {

    private static final List<Integer> rightList = new ArrayList<Integer>() {{
        /* 系统功能权限定义，从6000以后开始 */
        add(6001);  //实体管理权限，包括实体增改、字段增删改权限
        add(6002);  //实体删除权限（删除实体是高危操作，故独立设置权限）
        add(6003);  //表单设计权限
        add(6004);  //数据列表设计权限
        add(6005);  //数据字典单选项关联权限
        add(6006);  //数据字典多选项关联权限

        /* 注意：自行添加的权限请从7000开始，以免跟后续添加的系统功能权限冲突！！ */
        //待添加...
    }};


    public static List<Integer> getRightList() {
        return rightList;
    }

}
