package cn.zjut.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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

    private String bookName;
    private String images; // 封面图片

    private String author;

    private int publisherId;

    private int bookTypeId;

    private int inventory; //库存

    private int total;

    private double price;

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
