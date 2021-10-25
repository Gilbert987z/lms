package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@JsonIgnoreProperties(value = {"updatedAt","deletedAt","handler"})
@Data
public class SysUser {
    /*
    {id} 自增主键
    {name} 人员姓名
    {mobile} 人员电话
     */
    private int id;


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

    public SysUser() {
    }
}
