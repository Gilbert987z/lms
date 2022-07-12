package cn.zjut.lms.common.dto.book;


import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
public class BookBorrowLogMemberDto {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "book_id")
    private Long bookId;
    private Integer borrowNum;
    private Integer borrowDays;


    /**
     * 状态：-1禁用 1正常
     */
    private Integer status;

    private String remark;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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
