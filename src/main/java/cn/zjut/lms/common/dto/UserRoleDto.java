package cn.zjut.lms.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分配角色用
 */
@Data
public class UserRoleDto implements Serializable {
    Long userId;
    List<Long> roleIds;
}
