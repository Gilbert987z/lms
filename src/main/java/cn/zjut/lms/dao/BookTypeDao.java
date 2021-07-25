package cn.zjut.lms.dao;

import cn.zjut.lms.model.BookType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BookTypeDao {
    /*
    查所有
    return List<User>
     */
    List<BookType> list();

    /*
    根据ID查询
    {id} 要查询人员的 id
     */
    BookType getById(int id);

    /*
    删除
    {id} 要删除人员的 id
     */
    int delete(BookType bookType);

    /*
    更新
    {p} 要更新的User实例
     */
    int update(BookType bookType);

    /*
    增加
    {p} 要新增的User实例
     */
    int add(BookType bookType);


    int selectCount(); //个数计数，分页使用
}