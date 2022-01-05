package cn.zjut.lms.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AccessToken {
//    private Integer id;
    private Long userId; //关联的userId
    public String accessToken; //为后续访问系统的token, 在redis里也有一个备份。
    private String loginIp; //IP地址

    private Integer expireTime; //过期时间
    private boolean validation; //校验是否通过

    //指定LocalDateTime这样的序列化以及反序列化器
    //https://blog.csdn.net/wwrzyy/article/details/90232835
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime; //登录时间
}
