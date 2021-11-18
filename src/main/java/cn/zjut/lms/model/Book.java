package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@JsonIgnoreProperties(value = {"updatedAt","deletedAt","handler"})
@Data
public class Book {
    /*
    字段：
    id（借书证号）、书名、作者、出版社关联id、
    藏书种类id、库存（本）、总数（本）、图书价格、
    创建时间字段、修改时间字段、删除时间字段
     */
    private int id;

    @NotBlank(message = "图书名称不能为空") //必须要传值
    @Size(min=1,max=20,message="图书名称需{min}-{max}字")  //被注释的字符串的大小必须在指定的范围内
    private String bookName;

    @NotBlank(message = "图片需要上传") //必须要传值
    private String image; // 封面图片

    @NotBlank(message = "作者名称不能为空") //必须要传值
    @Size(min=1,max=10,message="作者名称需{min}-{max}字")
    private String author;

    @NotNull(message = "出版社不能为空") //必须要传值
    private Integer publisherId;
    @NotNull(message = "图书类型不能为空") //必须要传值
    private Integer bookTypeId;

    @Min(value = 0, message = "库存不能小于0")
    private Integer inventory; //库存
    @Min(value = 0, message = "总数不能小于0")
    private Integer total;
    @Min(value = 0, message = "价格不能小于0")
    private double price;

    private Integer userId;  //管理员的ID

    @Size(max=500,message="备注最多{max}字")
    private String desc;//备注

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;


    private String bookType;
    private String publisher;
    //关联
//    private BookType bookType;
//    private Publisher publisher;
}
