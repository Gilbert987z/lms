package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SysRolePermssion {
    private int id;
    private int roleId;
    private int permissionId;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

}
