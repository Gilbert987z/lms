package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 角色和权限关联
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysRolePermission extends BaseEntity{
    private Long roleId;
    private Long permissionId;



}
