package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysPermission extends BaseEntity{
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    private String path;//路径
    @NotBlank(message = "菜单名称不能为空")
    private String name;//例子sys.user.info
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    private String component;
    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer orderNum;
//    @Size(max = 200, message = "备注最多可输入{max}字")
//    private String remark;//备注

    private Integer status;//状态 0禁止登录 1正常

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;



    //用于输出子类的权限
    @TableField(exist = false)
    private List<SysPermission> children = new ArrayList<>();


}
