package cn.zjut.lms.mapper;

import cn.zjut.lms.common.dto.book.BookBorrowLogMemberDto;
import cn.zjut.lms.common.dto.book.BookDto;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookBorrowLog;
import cn.zjut.lms.entity.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 图书借阅日志表 Mapper 接口
 * </p>
 *
 * @author zhangzhe
 * @since 2022-06-07
 */
@Repository
public interface BookBorrowLogMapper extends BaseMapper<BookBorrowLog> {
    @Select({"<script>",
            "SELECT id FROM book_borrow_log where status = 1 and now()>date_add(created_at, interval borrow_days day)",
            "</script>"
    })
    List<Long> getTaskIds();

//    @Select({"<script>",
//            "SELECT bbl.*,b.name as bookname,u.username FROM book_borrow_log as bbl",
//            "LEFT JOIN book as b ON b.id = bbl.book_id",
//            "LEFT JOIN user as u ON u.id = bbl.user_id ${ew.customSqlSegment} ",
//            "</script>"
//    })
//    @Select({"<script>",
//            "SELECT * FROM book_borrow_log ${ew.customSqlSegment} ",
//            "</script>"
//    })
    @Select({"SELECT bbl.*,b.*,u.* ",
            "FROM book_borrow_log as bbl",
            "LEFT JOIN book as b ON b.id = bbl.book_id",
            "LEFT JOIN user as u ON u.id = bbl.user_id ${ew.customSqlSegment} "
    })
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "bookId",column = "book_id"),
            @Result(
                    property = "user", //当前类order所封装的属性名user
                    column = "user_id", //在数据表中根据字段名来查询user表数据
                    javaType = User.class,//要封装的实体类型
                    one = @One(select = "cn.zjut.lms.mapper.UserMapper.selectById") //查询UserMapper接口中的方法来获取数据
            ),
            @Result(
                    property = "book", //当前类order所封装的属性名user
                    column = "book_id", //在数据表中根据字段名来查询user表数据
                    javaType = Book.class,//要封装的实体类型
                    one = @One(select = "cn.zjut.lms.mapper.BookMapper.selectById") //查询UserMapper接口中的方法来获取数据
            )
    })
   // https://www.jb51.net/article/223062.htm

//    @Select({"<script>",
//            "SELECT bbl.*,b.*,u.* FROM book_borrow_log as bbl",
//            "LEFT JOIN book as b ON b.id = bbl.book_id",
//            "LEFT JOIN user as u ON u.id = bbl.user_id ${ew.customSqlSegment} ",
//            "</script>"
//    })
//    @Results({
//            @Result(column="b.*", property="book", jdbcType= JdbcType.INTEGER, id=true),
//            @Result(column="u.*", property="user", jdbcType=JdbcType.VARCHAR),
//    })
    Page<BookBorrowLog> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);


//    @Select({"<script>",
//            "SELECT * FROM book_borrow_log ${ew.customSqlSegment} ",
//            "</script>"
//    })
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "userId", column = "user_id"),
//            @Result(property = "bookId",column = "book_id"),
//            @Result(
//                    property = "book", //当前类order所封装的属性名user
//                    column = "book_id", //在数据表中根据字段名来查询user表数据
//                    javaType = Book.class,//要封装的实体类型
//                    one = @One(select = "cn.zjut.lms.mapper.BookMapper.selectById") //查询UserMapper接口中的方法来获取数据
//            ),
//            @Result(
//                    property = "user", //当前类order所封装的属性名user
//                    column = "user_id", //在数据表中根据字段名来查询user表数据
//                    javaType = User.class,//要封装的实体类型
//                    one = @One(select = "cn.zjut.lms.mapper.UserMapper.selectById") //查询UserMapper接口中的方法来获取数据
//            )
//    })
//    Page<BookBorrowLogMemberDto> member_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
//        @Select({"SELECT bbl.*,b.*,u.* ",
//                "FROM book_borrow_log as bbl",
//            "LEFT JOIN book as b ON b.id = bbl.book_id",
//            "LEFT JOIN user as u ON u.id = bbl.user_id ${ew.customSqlSegment} "
//    })
//        @Results({
////                @Result(property = "id", column = "id"),
//                @Result(property = "userId", column = "user_id"),
//                @Result(property = "bookId",column = "book_id"),
//                @Result(
//                        property = "user", //当前类order所封装的属性名user
//                        column = "user_id", //在数据表中根据字段名来查询user表数据
//                        javaType = User.class,//要封装的实体类型
//                        one = @One(select = "cn.zjut.lms.mapper.UserMapper.selectById") //查询UserMapper接口中的方法来获取数据
//                ),
//                @Result(
//                        property = "book", //当前类order所封装的属性名user
//                        column = "book_id", //在数据表中根据字段名来查询user表数据
//                        javaType = Book.class,//要封装的实体类型
//                        one = @One(select = "cn.zjut.lms.mapper.BookMapper.selectById") //查询UserMapper接口中的方法来获取数据
//                )
//        })
//    Page<BookBorrowLogMemberDto> member_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
