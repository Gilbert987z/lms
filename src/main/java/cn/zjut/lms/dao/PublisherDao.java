package cn.zjut.lms.dao;

import cn.zjut.lms.model.Book;
import cn.zjut.lms.model.Publisher;
import cn.zjut.lms.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PublisherDao {
    /*
    查所有
    return List<User>
     */
    List<Publisher> list();
    List<Publisher> listByPage();

    /*
    根据ID查询
    {id} 要查询人员的 id
     */
    Publisher getById(int id);

    /*
    删除
    {id} 要删除人员的 id
     */
//    int delete(int id);
    int delete(Publisher publisher);

    /*
    更新
    {p} 要更新的User实例
     */
    int update(Publisher publisher);

    /*
    增加
    {p} 要新增的User实例
     */
    int add(Publisher publisher);


    int selectCount(); //个数计数，分页使用

    int countPublisher(String publisher);  //查重
}
