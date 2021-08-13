package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SysRole {
    private int id;

    private String roleName;
    private String desc;//备注

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

}
