package cn.zjut.lms.dao;

import cn.zjut.lms.model.Book;

import cn.zjut.lms.model.BookType;
import cn.zjut.lms.model.Publisher;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BookDao {
    //    @Select("SELECT book.*,type.book_type,publish.publisher FROM book as book\n" +
//            "        left join book_type as type on type.id = book.book_type_id\n" +
//            "        left join publisher as publish on publish.id = book.publisher_id\n" +
//            "        where book.deleted_at is null")
//    @Select({"<script>",
//            "SELECT * FROM book ",
//            "WHERE deleted_at is null AND book_name like concat('%',#{bookName},'%')",
//            "<if test='publisherId!=null'>",
//            "AND publisher_id = #{publisherId}",
//            "</if>",
//            "<if test='bookTypeId!=null'>",
//            "AND book_type_id = #{bookTypeId}",
//            "</if>",
//            "</script>"
//    })
    @Select({"<script>",
            "SELECT book.*,type.book_type,publish.publisher FROM book as book",
            "LEFT JOIN book_type as type ON type.id = book.book_type_id",
            "LEFT JOIN publisher as publish ON publish.id = book.publisher_id",
            "WHERE book.deleted_at is null AND book.book_name like concat('%',#{bookName},'%')",
            "<if test='publisherId!=null'>",
            "AND book.publisher_id = #{publisherId}",
            "</if>",
            "<if test='bookTypeId!=null'>",
            "AND book.book_type_id = #{bookTypeId}",
            "</if>",
            "</script>"
    })

//    @Select("select * from (SELECT a.publisher_id,a.book_name,a.author, a.inventory , b.book_type FROM  book a Left JOIN book_type b ON a.book_type_id = b.id) c Left JOIN publisher p ON p.id = c.publisher_id")
//    @Results({
//            @Result(id = true, column = "id", property = "id"),
//            @Result(column = "book_name", property = "bookName"),
//            @Result(column = "author", property = "author"),
////            @Result(column = "publisher_id", property = "publisherId"),
////            @Result(column = "book_type_id", property = "bookTypeId"),
//            @Result(column = "inventory", property = "inventory"),
//            @Result(column = "total", property = "total"),
//            @Result(column = "price", property = "price"),
//            @Result(column = "created_at", property = "createdAt"),
//            @Result(column = "book_type_id", property = "bookType", javaType = BookType.class, one = @One(select = "cn.zjut.lms.dao.BookTypeDao.getById", fetchType = FetchType.LAZY)),
//            @Result(column = "publisher_id", property = "publisher", javaType = Publisher.class, one = @One(select = "cn.zjut.lms.dao.PublisherDao.getById", fetchType = FetchType.LAZY))}
//    )
    List<Book> list(String bookName, Integer publisherId, Integer bookTypeId);

    Book getById(int id);

    int delete(Book book);

    int update(Book book);


    int add(Book book);

    int selectCount(String bookName, Integer publisherId, Integer bookTypeId); //个数计数，分页使用
}
