package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.common.dto.book.BookBorrowLogMemberDto;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookBorrowLog;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.service.BookBorrowLogService;
import cn.zjut.lms.util.Param;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 图书借阅表 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-06-07
 */
@RestController
@RequestMapping("")
public class BookBorrowLogController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('book.borrowLog.list')")
    @GetMapping("/admin/book/borrowLog/list")
    public ResultJson list(@RequestParam(value = "bookName", defaultValue = "") String bookName,//图书名称
                           @RequestParam(value = "bookId", defaultValue = "") Long bookId, //借书编号
                           @RequestParam(value = "username",defaultValue = "") String username, //借书人
                           @RequestParam(value = "status",defaultValue = "") Integer status //借书状态
                           ) {

        //分页查询
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        //图书名称搜索
        queryWrapper.like(StrUtil.isNotBlank(bookName), "b.name", bookName);
        //图书编号搜索
        queryWrapper.eq(bookId != null, "b.id", bookId);
        //借书人搜索
        queryWrapper.like(StrUtil.isNotBlank(username), "u.username", username);
        //借书状态搜索
        queryWrapper.eq(status != null, "bbl.status", status);

        queryWrapper.isNull("bbl.deleted_at");
        queryWrapper.orderBy(true, true, "bbl.created_at");
//        Page<BookBorrowLog> list = bookBorrowLogService.page(getPage(), new QueryWrapper<BookBorrowLog>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排

        Page<BookBorrowLog> list = bookBorrowLogService.list(getPage(),queryWrapper);
        return ResultJson.ok().data(list);
    }
//    //根据id删除  批量删除
//    @PostMapping(value = "/admin/book/borrowLog/delete", consumes = "application/json")
//    public ResultJson deleteById(@RequestBody Long[] ids) {
//
//        try{
//            for (Long id : ids){
//                BookBorrowLog bookBorrowLog = bookBorrowLogService.getById(id);
//                bookBorrowLog.setDeletedAt(LocalDateTime.now()); //设置删除时间
//                bookBorrowLogService.updateById(bookBorrowLog);//软删除
//            }
//        }catch (Exception e){
//            return ResultJson.error().message("删除失败");
//        }
//
//        return ResultJson.ok().message("删除成功");
//    }
    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/member/book/borrowLog/list")
    public ResultJson member_list(@RequestParam(value = "bookName", defaultValue = "") String bookName,//图书名称
                                  @RequestParam(value = "bookId", defaultValue = "") Long bookId, //借书编号
                                  @RequestParam(value = "status",defaultValue = "") Integer status, //借书状态
                                  Principal principal) {
        User user = userService.getByUsername(principal.getName());

        //分页查询
        QueryWrapper<BookBorrowLog> queryWrapper = new QueryWrapper<>();
        //数据权限
        queryWrapper.eq("bbl.user_id", user.getId());
        //图书名称搜索
        queryWrapper.like(StrUtil.isNotBlank(bookName), "b.name", bookName);
        //图书编号搜索
        queryWrapper.eq(bookId != null, "b.id", bookId);

        //借书状态搜索
        queryWrapper.eq(status != null, "bbl.status", status);

        queryWrapper.isNull("bbl.deleted_at");
        queryWrapper.orderBy(true, true, "bbl.created_at");

        Page<BookBorrowLog> list = bookBorrowLogService.list(getPage(),queryWrapper);
        return ResultJson.ok().data(list);
    }



    /**
     * 图书借阅
     *
     * @return
     */
    @Transactional
    @PostMapping("/member/book/borrow")
    public ResultJson member_book_borrow(@Valid @RequestBody BookBorrowLog bookBorrowLog, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            Book book = bookService.getById(bookBorrowLog.getBookId());
            int inventory = book.getInventory();
            int borrowNum = 1;//默认借阅一本书
            if(book.getStatus()==Param.BOOK_STATUS_OFF){
                return ResultJson.error().message("图书下架无法借阅");
            }
            if(inventory - borrowNum < 0){ //如果库存不足
                return ResultJson.error().message("库存不足");
            }

            User user = userService.getByUsername(principal.getName());
            bookBorrowLog.setUserId(user.getId());

            bookBorrowLog.setBorrowNum(borrowNum);
            bookBorrowLog.setStatus(Param.BOOK_BORROW_STATUS_BORROW); // 借阅状态

            System.out.println(bookBorrowLog);

            boolean result = bookBorrowLogService.save(bookBorrowLog);

            if(inventory - borrowNum == 0){ //如果库存为0，下架图书
                book.setStatus(Param.BOOK_STATUS_OFF); //图书下架
            }
            book.setInventory(inventory-borrowNum); //减去库存
            bookService.updateById(book);


            if (result) {
                return ResultJson.ok().message("创建成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    @PostMapping("/member/book/return")
    public ResultJson member_book_return(@RequestBody Map<String, Object> map, Principal principal) {
        User user = userService.getByUsername(principal.getName());

        Long bookBorrowLogId  = Long.valueOf(map.get("id").toString()); //借阅编号   1、转String型：A.toString 2、再由String型转Long 型即可
        BookBorrowLog bookBorrowLog = bookBorrowLogService.getById(bookBorrowLogId);

        //数据校验
        if(!user.getId().equals(bookBorrowLog.getUserId())){
            return ResultJson.error().message("无法操作该数据！1234");
        }
        //状态不对，无法操作
        if(bookBorrowLog.getStatus()==2 || bookBorrowLog.getStatus()==3){
            return ResultJson.error().message("无法操作该数据！2345");
        }

        UpdateWrapper<BookBorrowLog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",2); //2 归还
        updateWrapper.set("updated_at", LocalDateTime.now());
        updateWrapper.set("operate_time", LocalDateTime.now());//操作时间-归还时间
        updateWrapper.eq("id",bookBorrowLog.getId()); //UPDATE BookBorrowLog SET status=? WHERE (id = ?)
        //软删除不展示
        updateWrapper.isNull("deleted_at");
        boolean result = bookBorrowLogService.update(null, updateWrapper);

        if (result) {
            return ResultJson.ok().message("归还成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }

    }

    @PostMapping("/member/book/lost")
    public ResultJson member_book_lost(@RequestBody Map<String, Object> map, Principal principal) {
        User user = userService.getByUsername(principal.getName());

        Long bookBorrowLogId  = Long.valueOf(map.get("id").toString()); //借阅编号
        String remark  = (String) map.get("remark"); //遗失理由
        BookBorrowLog bookBorrowLog = bookBorrowLogService.getById(bookBorrowLogId);

        //数据校验
        if(!user.getId().equals(bookBorrowLog.getUserId())){
            return ResultJson.error().message("无法操作该数据！");
        }
        //状态不对，无法操作
        if(bookBorrowLog.getStatus()==2 || bookBorrowLog.getStatus()==3){
            return ResultJson.error().message("无法操作该数据！");
        }


        UpdateWrapper<BookBorrowLog> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",3); //2 遗失
        updateWrapper.set("remark",remark); //2 遗失备注
        updateWrapper.set("updated_at", LocalDateTime.now());
        updateWrapper.set("operate_time", LocalDateTime.now());//操作时间-遗失时间
        updateWrapper.eq("id",bookBorrowLog.getId()); //UPDATE BookBorrowLog SET status=? WHERE (id = ?)
        //软删除不展示
        updateWrapper.isNull("deleted_at");
        boolean result = bookBorrowLogService.update(null, updateWrapper);

        if (result) {
            return ResultJson.ok().message("成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }

    }

}
