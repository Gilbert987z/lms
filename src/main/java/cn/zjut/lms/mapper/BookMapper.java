package cn.zjut.lms.mapper;

import cn.zjut.lms.entity.Book;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
@Repository
public interface BookMapper extends BaseMapper<Book> {
    @Select({"<script>",
            "SELECT book.*,type.book_type,publish.publisher FROM book as book",
            "LEFT JOIN book_type as type ON type.id = book.book_type_id",
            "LEFT JOIN book_publisher as publish ON publish.id = book.publisher_id ${ew.customSqlSegment} ",
//            "WHERE book.deleted_at is null AND book.book_name like concat('%',#{bookName},'%')",
//            "<if test='publisherId!=null'>",
//            "AND book.publisher_id = #{publisherId}",
//            "</if>",
//            "<if test='bookTypeId!=null'>",
//            "AND book.book_type_id = #{bookTypeId}",
//            "</if>",

            "</script>"
    })
    Page<Book> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
