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
    @Select("SELECT * FROM book WHERE deleted_at is null")
    @Results({
            @Result(id=true,column = "id", property = "id"),
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
    List<Book> list();

    Book getById(int id);

    int delete(Book book);

    int update(Book book);


    int add(Book book);

    int selectCount(); //个数计数，分页使用
}