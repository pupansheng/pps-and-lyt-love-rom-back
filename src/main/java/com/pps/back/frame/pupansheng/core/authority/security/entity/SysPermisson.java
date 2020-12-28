package com.pps.back.frame.pupansheng.core.authority.security.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

/**
 * sys_permisson
 * @author 
 */
@Data
public class SysPermisson implements Serializable {
    private Long id;

    private String action;

    private String page;

    private Long sysRoleId;

    private static final long serialVersionUID = 1L;

    public List<String> getActionArray(){

        return Arrays.asList(action.split(","));

    }
}