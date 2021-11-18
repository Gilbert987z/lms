package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@JsonIgnoreProperties(value = {"updatedAt","deletedAt","handler"})
@Data
public class User  implements UserDetails {
    /*
    {id} 自增主键
    {name} 人员姓名
    {mobile} 人员电话
     */
    private Integer id;


    @NotBlank(message = "姓名不能为空") //字符串不能为空
    @Size(min = 3, max = 10,message="用户名需{min}-{max}字")
    private String username;

    @NotBlank(message = "密码不能为空") //字符串不能为空
    private String password;

    @NotBlank(message = "mobile不能为空")
    private String mobile;

    private String images; //图片
    private String desc;//备注

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

//    public User() {
//    }



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
