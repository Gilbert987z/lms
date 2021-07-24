package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AccessToken {
    private int id;
    private int userId; //关联的userId
    public String accessToken; //为后续访问系统的token, 在redis里也有一个备份。
    private String loginIp; //IP地址

    private Date loginTime; //登录时间
    private int expireTime; //过期时间
    private boolean validation; //校验是否通过

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt; //创建时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt; //修改时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt; //删除时间
}
