package com.pps.back.frame.pupansheng.core.authority.security.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_role
 * @author 
 */
@Data
public class SysRole implements Serializable {
    private Long id;

    private String name;

    private Long userId;

    private static final long serialVersionUID = 1L;
}