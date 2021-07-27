package cn.zjut.lms.service;

import cn.zjut.lms.dao.BookBorrowDao;
import cn.zjut.lms.model.BookBorrow;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookBorrowService {
    @Autowired
    BookBorrowDao bookBorrowDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list(int page, int size,String bookName,Integer publisherId,Integer bookTypeId) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<BookBorrow> bookList = bookBorrowDao.list(bookName,publisherId,bookTypeId);
        System.out.println(bookList);
        System.out.println("bookName:"+bookName);

        // 查询总计数据行数，计算总计页码
        int totalSize = bookBorrowDao.selectCount(bookName,publisherId,bookTypeId);
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

    public BookBorrow getById(int id) {
        return bookBorrowDao.getById(id);
    }

//    public boolean delete(Book book) {
//        java.util.Date date=new java.util.Date();
//        java.sql.Date currentTime=new java.sql.Date(date.getTime());
//        book.setUpdatedAt(currentTime);
//        book.setDeletedAt(currentTime);
//
//        int flag = bookBorrowDao.delete(book);
//        if (flag != 1) {
//            // 删除失败，回滚事务
//            throw new RuntimeException("删除失败");
//        }
//        return true;
//    }

    public boolean update(BookBorrow bookBorrow) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        bookBorrow.setUpdatedAt(currentTime);

        int rows = bookBorrowDao.update(bookBorrow);
        if (rows != 1) {
            // 修改失败，回滚事务
            throw new RuntimeException("修改失败");

        }
        return true;
    }

    public boolean add(BookBorrow bookBorrow) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        bookBorrow.setCreatedAt(currentTime);
        bookBorrow.setUpdatedAt(currentTime);

        int rows = bookBorrowDao.add(bookBorrow);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增失败");

        }
        return true;
    }
}