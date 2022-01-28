package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
