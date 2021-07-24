package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class User {
    /*
    {id} 自增主键
    {name} 人员姓名
    {mobile} 人员电话
     */
    private int id;

    @NotNull(message = "姓名不能为空") //必须要传值
    @NotBlank(message = "姓名不能为空1") //字符串不能为空
    private String username;

    private String password;

    @NotNull(message = "mobile不能为空")
    private String mobile;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    public User() {
    }
}
