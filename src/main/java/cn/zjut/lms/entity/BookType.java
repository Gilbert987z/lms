package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 图书类型表
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotBlank
    @Size(max = 10, message = "图书类型最多可输入{max}字")
    private String bookType;

    /**
     * 状态：-1禁用 1正常
     */
    private Integer status;

    @Size(max = 200, message = "备注最多可输入{max}字")
    private String remark;

    private LocalDateTime deletedAt;

}
