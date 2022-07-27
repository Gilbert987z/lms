package cn.zjut.lms.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
@Data
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@ExcelIgnoreUnannotated //没有注解的字段都不转换 easyexcel
//@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

//    private static final long serialVersionUID = 1L;
    @ExcelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 书名
     */
    @ExcelProperty("书名")
    private String name;

    /**
     * 封面图片
     */
    @ExcelProperty("封面图片")
    private String image;

    /**
     * 作者
     */
    @ExcelProperty("作者")
    private String author;

    private Integer publisherId;

    private Integer bookTypeId;

    /**
     * 库存
     */

    @Min(value = 0, message = "图书库存必须大于等于0")
    private Integer inventory;

    /**
     * 总数
     */
    @Min(value = 0, message = "图书总数必须大于等于0")
    private Integer total;

    /**
     * 价格
     */
    @Min(value = 0, message = "图书价格必须大于等于0")
    private Double price;

    /**
     * 创建书籍的管理员id
     */
    private Long userId;

    /**
     * 状态：上架：1；下架：0
     */
    //todo  怎么只限制在1和0 ； range()?
    private Integer status;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注最多可输入{max}字")
    private String remark;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    //一对一
    //由于 bookType 不是 Book 数据库表里的字段，因此需要添加 @TableField 注解，并将 exist 属性设置为 false。
    @TableField(exist = false) //表中不存在
    private String bookType;
    @TableField(exist = false)
    private String publisher;
}
