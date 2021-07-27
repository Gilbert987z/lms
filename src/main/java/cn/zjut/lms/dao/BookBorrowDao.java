package cn.zjut.lms.dao;

import cn.zjut.lms.model.Book;
import cn.zjut.lms.model.BookBorrow;
import cn.zjut.lms.model.BookType;
import cn.zjut.lms.model.Publisher;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BookBorrowDao {

    /*
    查询出借书列表
     */
    //    @Select("SELECT book.*,type.book_type,publish.publisher FROM book as book\n" +
//            "        left join book_type as type on type.id = book.book_type_id\n" +
//            "        left join publisher as publish on publish.id = book.publisher_id\n" +
//            "        where book.deleted_at is null")
    @Select({"<script>",
            "SELECT * FROM book ",
            "WHERE deleted_at is null AND book_name like concat('%',#{bookName},'%')",
            "<if test='publisherId!=null'>",
            "AND publisher_id = #{publisherId}",
            "</if>",
            "<if test='bookTypeId!=null'>",
            "AND book_type_id = #{bookTypeId}",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "book_name", property = "bookName"),
            @Result(column = "author", property = "author"),
//            @Result(column = "publisher_id", property = "publisherId"),
//            @Result(column = "book_type_id", property = "bookTypeId"),
            @Result(column = "inventory", property = "inventory"),
            @Result(column = "total", property = "total"),
            @Result(column = "price", property = "price"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "book_type_id", property = "bookType", javaType = BookType.class, one = @One(select = "cn.zjut.lms.dao.BookTypeDao.getById", fetchType = FetchType.LAZY)),
            @Result(column = "publisher_id", property = "publisher", javaType = Publisher.class, one = @One(select = "cn.zjut.lms.dao.PublisherDao.getById", fetchType = FetchType.LAZY))}
    )
    List<BookBorrow> list(String bookName, Integer publisherId, Integer bookTypeId);

    /*
    获取借书详情
     */
    BookBorrow getById(int id);

//    int delete(Book book);

    /*
    修改借书信息
     */
    int update(BookBorrow bookBorrow);

    /*
    修改借书状信息
     */
    int statusUpdate(int status);

    /*
    增加借书状信息
     */
    int add(BookBorrow bookBorrow);

    /*
    统计借书数量  //个数计数，分页使用
     */
    int selectCount(String bookName, Integer publisherId, Integer bookTypeId);
}