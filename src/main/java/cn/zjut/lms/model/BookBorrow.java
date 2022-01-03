package cn.zjut.lms.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 借书记录
 */
@Data
public class BookBorrow {

    private int id;

    private int userId;
    private int bookId;
    private int borrowBookNum; //借书数量
    private int borrowDays;
    private short status;  //1:借阅;2:归还;3:遗失;4:未及时归还

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt; //借书开始时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;
}
