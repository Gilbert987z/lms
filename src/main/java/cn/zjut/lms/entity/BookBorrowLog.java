package cn.zjut.lms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * <p>
 * 图书借阅日志
 * </p>
 *
 * @author zhangzhe
 * @since 2022-06-07
 */
@Data
@JsonIgnoreProperties(value = {"deletedAt","handler"})
@TableName("book_borrow_log")
public class BookBorrowLog extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "book_id")
    private Long bookId;
    private Integer borrowNum;
    @Min(value = 1, message = "借阅天数需要大于等于1天")
    private Integer borrowDays;


    /**
     * 状态：-1禁用 1正常
     */
    private Integer status;

    private String remark;


    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;  //操作时间  /归还时间/遗失时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passTime;  //超时时间

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;


    //一对一
//    @TableField(exist = false)
//    private String username;
//    @TableField(exist = false)
//    private String bookname;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Book book;

}
