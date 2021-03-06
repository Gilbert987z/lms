package cn.zjut.lms.service;

import cn.zjut.lms.dao.PublisherDao;
import cn.zjut.lms.model.Publisher;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublisherService {
    @Autowired
    PublisherDao publisherDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<Publisher> publisherList = publisherDao.list();

        // 当前页面显示的内容

        resultMap.put("list", publisherList);

        return resultMap;
    }

    public Map<String, Object> listByPage(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);

        List<Publisher> publisherList = publisherDao.listByPage();

        // 查询总计数据行数，计算总计页码
        int totalSize = publisherDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容

        resultMap.put("list", publisherList);


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

    public Publisher getById(int id) {
        return publisherDao.getById(id);
    }

    public boolean delete(Publisher publisher) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        publisher.setUpdatedAt(currentTime);
        publisher.setDeletedAt(currentTime);

        int flag = publisherDao.delete(publisher);
        if (flag != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("删除失败");

        }
        return true;
    }

    public boolean update(Publisher publisher) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        publisher.setUpdatedAt(currentTime);

        int rows = publisherDao.update(publisher);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("修改失败");

        }
        return true;
    }

    public boolean add(Publisher publisher) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        publisher.setCreatedAt(currentTime);
        publisher.setUpdatedAt(currentTime);

        int rows = publisherDao.add(publisher);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增失败");

        }
        return true;
    }

    public int countPublisher(String publisher){
        return publisherDao.countPublisher(publisher);
    }
}
