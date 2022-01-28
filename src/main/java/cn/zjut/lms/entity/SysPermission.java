package cn.zjut.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 权限
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysPermission extends BaseEntity{
    private Long id;
    private String path;//路径
    private String name;//例子sys.user.info
    private String remark;//备注
    private Integer status;//状态 0禁止登录 1正常

}
