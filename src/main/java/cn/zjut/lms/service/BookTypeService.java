package cn.zjut.lms.service;

import cn.zjut.lms.dao.BookTypeDao;
import cn.zjut.lms.model.BookType;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookTypeService {
    @Autowired
    BookTypeDao bookTypeDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<BookType> bookTypeList = bookTypeDao.list();

        // 当前页面显示的内容
        resultMap.put("list", bookTypeList);

        return resultMap;
    }

    public Map<String, Object> listByPage(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<BookType> bookTypeList = bookTypeDao.listByPage();


        // 查询总计数据行数，计算总计页码
        int totalSize = bookTypeDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容
        resultMap.put("list", bookTypeList);
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

    public BookType getById(int id) {
        return bookTypeDao.getById(id);
    }

    public boolean delete(BookType bookType) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        bookType.setUpdatedAt(currentTime);
        bookType.setDeletedAt(currentTime);

        int flag = bookTypeDao.delete(bookType);
        if (flag != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public boolean update(BookType bookType) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        bookType.setUpdatedAt(currentTime);

        int rows = bookTypeDao.update(bookType);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public boolean add(BookType bookType) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        bookType.setCreatedAt(currentTime);
        bookType.setUpdatedAt(currentTime);

        int rows = bookTypeDao.add(bookType);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public int countBookType(String bookType){
        return bookTypeDao.countBookType(bookType);
    }
}
