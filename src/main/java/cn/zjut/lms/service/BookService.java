package cn.zjut.lms.service;

import cn.zjut.lms.entity.Book;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
public interface BookService extends IService<Book> {
    Page<Book> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
