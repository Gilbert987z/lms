package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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
