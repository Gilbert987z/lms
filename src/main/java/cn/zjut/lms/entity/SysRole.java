package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 */
@JsonIgnoreProperties(value = {"deletedAt", "handler"})
@Data
public class SysRole extends BaseEntity {
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    private String name; //角色名称

    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Size(max = 200, message = "备注最多可输入{max}字")
    private String remark;//备注
    private Integer status;//状态 0禁止 1正常

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    //多对多
    @TableField(exist = false)
    private List<Long> permissionIds = new ArrayList<>();
}
