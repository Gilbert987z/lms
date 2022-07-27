package cn.zjut.lms.service.impl;

import cn.zjut.lms.common.dto.book.BookBorrowLogMemberDto;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookBorrowLog;
import cn.zjut.lms.mapper.BookBorrowLogMapper;
import cn.zjut.lms.mapper.BookMapper;
import cn.zjut.lms.service.BookBorrowLogService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 图书借阅日志表 服务实现类
 * </p>
 *
 * @author zhangzhe
 * @since 2022-06-07
 */
@Service
public class BookBorrowLogServiceImpl extends ServiceImpl<BookBorrowLogMapper, BookBorrowLog> implements BookBorrowLogService {
    @Autowired
    BookBorrowLogMapper bookBorrowLogMapper;

    @Override
    public Page<BookBorrowLog> list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper) {
        Page<BookBorrowLog> list = bookBorrowLogMapper.list(page, wrapper);
        return list;
    }

    /**
     *
     * @return
     */
    @Override
    public Boolean task() {
        List<Long> ids = bookBorrowLogMapper.getTaskIds();
        System.out.println(ids);

        boolean isSuccess = true;
        for(Long id:ids){
//            BookBorrowLog bookBorrowLog = bookBorrowLogMapper.selectById(id);
//
//            bookBorrowLog.setStatus(4);//4:未及时归还'
            UpdateWrapper<BookBorrowLog> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status",4);
            updateWrapper.set("updated_at", LocalDateTime.now());
            updateWrapper.set("pass_time", LocalDateTime.now());
            updateWrapper.eq("id",id); //UPDATE BookBorrowLog SET status=? WHERE (id = ?)
            int result = bookBorrowLogMapper.update(null, updateWrapper);

//            int result = bookBorrowLogMapper.updateById(bookBorrowLog);
            if(result == 0){ // 0 表示执行失败，正整数是执行成功
                isSuccess = false;
                break;
            }
        }


        return isSuccess;
    }
//
//    @Override
//    public Page<BookBorrowLog> admin_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper) {
//        Page<BookBorrowLog> list = bookBorrowLogMapper.admin_list(page, wrapper);
//        return list;
//    }
//
//    @Override
//    public Page<BookBorrowLogMemberDto> member_list(Page page, @Param(Constants.WRAPPER) Wrapper wrapper) {
//        Page<BookBorrowLogMemberDto> list = bookBorrowLogMapper.member_list(page, wrapper);
//        return list;
//    }
}
