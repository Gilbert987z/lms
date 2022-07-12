package cn.zjut.lms.entity.handler;


import cn.zjut.lms.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * 用户
 */

@Data
public class UserEditDto extends BaseEntity {
    /*
    {id} 自增主键
    {name} 人员姓名
    {mobile} 人员电话
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "姓名不能为空") //字符串不能为空
    @Size(min = 3, max = 10, message = "用户名需{min}-{max}字")
    private String username;

    @NotBlank(message = "mobile不能为空")
    private String mobile;

    private Integer status;//状态 0禁止登录 1正常

    private Integer isAdmin;//1是超级管理员；2普通管理员；3读者

    @Size(max = 200, message = "备注最多可输入{max}字")
    private String remark;//备注

}
