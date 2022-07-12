package cn.zjut.lms.service;

import cn.zjut.lms.common.dto.book.BookBorrowLogMemberDto;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookBorrowLog;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 图书借阅日志表 服务类
 * </p>
 *
 * @author zhangzhe
 * @since 2022-06-07
 */
public interface BookBorrowLogService extends IService<BookBorrowLog> {
    Page<BookBorrowLog> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
//    Page<BookBorrowLog> admin_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
//    Page<BookBorrowLogMemberDto> member_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    Boolean task();
}
