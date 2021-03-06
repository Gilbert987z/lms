package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * 权限
 */
@JsonIgnoreProperties(value = {"updatedAt","deletedAt","handler"})
@Data
public class SysPermission {
    private int id;
    private String path;//路径
    private String name;//例子sys.user.info
    private String desc;//备注

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;
}
