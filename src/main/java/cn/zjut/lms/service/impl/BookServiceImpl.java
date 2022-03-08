package cn.zjut.lms.service.impl;

import cn.zjut.lms.entity.Book;
import cn.zjut.lms.mapper.BookMapper;
import cn.zjut.lms.service.BookService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    BookMapper bookMapper;

    @Override
    public Page<Book> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper) {
        Page<Book> books = bookMapper.list(page, wrapper);
        return books;
    }
}
