package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 图书出版商表
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookPublisher extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String publisher;

    /**
     * 状态：-1禁用 1正常
     */
    private Integer status;

    private LocalDateTime deletedAt;


}
