package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户和角色关联
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysUserRole extends BaseEntity{
    private Long userId;
    private Long roleId;
}
