package cn.zjut.lms.common.dto.book;

import cn.zjut.lms.entity.BaseEntity;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@ExcelIgnoreUnannotated //没有注解的字段都不转换 easyexcel
//@EqualsAndHashCode(callSuper = true)
public class BookDto {

//    private static final long serialVersionUID = 1L;
    @ExcelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 书名
     */
    @ExcelProperty("书名")
    private String name;



    private Integer publisherId;

    private Integer bookTypeId;
    //一对一
    //由于 bookType 不是 Book 数据库表里的字段，因此需要添加 @TableField 注解，并将 exist 属性设置为 false。
    @TableField(exist = false) //表中不存在
    private String bookType;
    @TableField(exist = false)
    private String publisher;
}
