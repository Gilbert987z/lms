package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户
 */
@JsonIgnoreProperties(value = {"accountNonExpired", "accountNonLocked", "authorities",
        "credentialsNonExpired", "enabled", "deletedAt", "handler"})
@Data
public class User extends BaseEntity implements UserDetails   {
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//仅限写入额权限
    @NotBlank(message = "密码不能为空") //字符串不能为空
    private String password;

    @NotBlank(message = "mobile不能为空")
    private String mobile;

    private String avatar; //图片
    private Integer status;//状态 0禁止登录 1正常

    private Integer isAdmin;//1是超级管理员；2普通管理员；3读者

    @Size(max = 200, message = "备注最多可输入{max}字")
    private String remark;//备注
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    //多对多
    @TableField(exist = false)
    private List<SysRole> sysRoles = new ArrayList<>();


    //spring security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    } //获取所有权限

    @Override
    public boolean isAccountNonExpired() {
        return false;
    } //是否账号过期

    @Override
    public boolean isAccountNonLocked() {
        return false;
    } //是否账号被锁定

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    } //凭证（密码）是否过期

    @Override
    public boolean isEnabled() {
        return false;
    } //是否可用
}
