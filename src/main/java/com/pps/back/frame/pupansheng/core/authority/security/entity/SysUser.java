package com.pps.back.frame.pupansheng.core.authority.security.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_user
 * @author 
 */
@Data
public class SysUser implements Serializable {

    private Long id;

    private String name;

    private String password;

    private String phone;

    private static final long serialVersionUID = 1L;

}