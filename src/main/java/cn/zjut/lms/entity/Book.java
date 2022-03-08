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
@ExcelIgnoreUnannotated //没有注解的字段都不转换
//@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

//    private static final long serialVersionUID = 1L;
    @ExcelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
    private Integer inventory;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 价格
     */
    private Double price;

    /**
     * 创建书籍的管理员id
     */
    private Integer userId;

    /**
     * 状态：上架：1；下架：0
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    //一对一
    @TableField(exist = false)
    private String bookType;
    @TableField(exist = false)
    private String publisher;
}
