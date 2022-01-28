package cn.zjut.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 角色和权限关联
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysRolePermission extends BaseEntity{
    private Long roleId;
    private Long permissionId;



}
