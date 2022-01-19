package cn.zjut.lms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户和角色关联
 */
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@Data
public class SysUserRole extends BaseEntity{
    @TableId(value = "id",type= IdType.AUTO)
    private Long id;

//    @TableId(value = "user_id",type = IdType.INPUT)
//    @MppMultiId // 复合主键
//    @TableField("user_id")
    private Long userId;

//    @TableId(value = "role_id",type = IdType.INPUT)
//    @MppMultiId // 复合主键
//    @TableField("role_id")
    private Long roleId;
}
