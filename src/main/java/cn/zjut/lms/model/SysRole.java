package cn.zjut.lms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@JsonIgnoreProperties(value = {"deletedAt", "handler"})
@Data
public class SysRole extends BaseEntity {
    private Long id;

    private String name; //角色名称
    private String remark;//备注
    private Integer status;//状态 0禁止 1正常

    //多对多
    @TableField(exist = false)
    private List<Long> permissionIds = new ArrayList<>();
}
