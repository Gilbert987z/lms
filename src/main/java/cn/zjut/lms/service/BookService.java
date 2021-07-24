package cn.zjut.lms.service;

import cn.zjut.lms.dao.BookDao;
import cn.zjut.lms.model.Book;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    BookDao bookDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<Book> bookList = bookDao.list();
        System.out.println(bookList);

        // 查询总计数据行数，计算总计页码
        int totalSize = bookDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容
        resultMap.put("bookList", bookList);
        // 当前页码
        resultMap.put("currentPage", page);
        //每页条数
        resultMap.put("pageSize", size);
        // 总计页码
        resultMap.put("totalPages", totalPages);
        //总条数
        resultMap.put("totalSize", totalSize);


        return resultMap;
    }

    public Book getById(int id) {
        return bookDao.getById(id);
    }

    public boolean delete(Book book) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        book.setUpdatedAt(currentTime);
        book.setDeletedAt(currentTime);

        int flag = bookDao.delete(book);
        if (flag != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public boolean update(Book book) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        book.setUpdatedAt(currentTime);
        book.setDeletedAt(currentTime);

        int rows = bookDao.update(book);
        if (rows != 1) {
            // 修改失败，回滚事务
            throw new RuntimeException("修改联系方式失败");

        }
        return true;
    }

    public boolean add(Book book) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        book.setCreatedAt(currentTime);
        book.setUpdatedAt(currentTime);

        int rows = bookDao.add(book);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }
}