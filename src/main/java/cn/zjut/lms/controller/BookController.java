package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookBorrowLog;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
@RestController
//@RequestMapping("/book")
public class BookController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/admin/book/list")
    public ResultJson list(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "publisherId", required = false) Integer publisherId,
                           @RequestParam(value = "bookTypeId", required = false) Integer bookTypeId,
                           Principal principal) {

        User user = userService.getByUsername(principal.getName());
        System.out.println(publisherId);

        //分页查询
//        Page<Book> books = bookService.page(getPage(), new QueryWrapper<Book>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        //数据权限
        //queryWrapper.eq(user.getIsAdmin() != 1, "book.user_id", user.getId())
        //图书出版社搜索
        queryWrapper.eq(publisherId != null, "book.publisher_id", publisherId);
        //图书类型搜索
        queryWrapper.eq(bookTypeId != null, "book.book_type_id", bookTypeId);
        //图书名称搜索
        queryWrapper.like(StrUtil.isNotBlank(name), "book.name", name);
        //软删除不展示
        queryWrapper.isNull("book.deleted_at");
        //按创建时间排序
        queryWrapper.orderBy(true, true, "book.created_at");//;
        //查询指定字段
//        queryWrapper.select("book.id","book.name","book.image");

//        Page<Book> books = bookService.page(getPage(), queryWrapper); //按时间正排
        Page<Book> books = bookService.list(getPage(), queryWrapper);


        return ResultJson.ok().data(books);
    }


    //查询单个数据
    @GetMapping("/admin/book/detail")
    public ResultJson detail(@RequestParam(value = "id") Long id) {
        Book book = bookService.getById(id);
        return ResultJson.ok().data(book);
    }

    @PostMapping(value = "/admin/book/create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody Book book, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            book.setUserId(Long.parseLong(principal.getName()));
            boolean result = bookService.save(book);
            if (result) {
                System.out.println(ResultJson.ok().message("增加成功"));
                return ResultJson.ok().message("增加成功");
            } else {
//                System.out.println();
                return ResultJson.error().message("数据不存在");
            }
        }
    }
    //修改
    @PostMapping(value = "/admin/book/update", consumes = "application/json")
    public ResultJson update(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookService.updateById(book);

            if (result) {
                return ResultJson.ok().message("修改成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }
//    //上下架
//    @PostMapping(value = "/admin/book/switch", consumes = "application/json")
//    public ResultJson switchBookStatus(@Valid @RequestBody Book book, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            Map<String, Object> fieldErrorsMap = new HashMap<>();
//
//            for (FieldError fieldError : bindingResult.getFieldErrors()) {
//                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//
//            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
//        } else {
//
//            Book bookSearch = bookService.getById(book.getId());
//            int status = book.getStatus();
//
//            if (bookSearch.getDeletedAt()!=null){ //不能操作软删除的数据
//                return ResultJson.error().message("无法操作该数据");
//            }
//            if(bookSearch.getInventory()<=0){
//                return ResultJson.error().message("当前库存不足，无法上架");
//            }
//            if(status>1 || status<0){
//                return ResultJson.error().message("状态参数异常");
//            }
//            if(status==bookSearch.getStatus()){
//                return ResultJson.error().message("已经操作成功，请勿重复操作");
//            }
//
//            //UPDATE book SET name=?, status=?, updated_at=? WHERE id=?
//            //如果多传字段，就会多改字段，会很不安全
//            boolean result = bookService.updateById(book);//根据id修改
//            if (result) {
//                return ResultJson.ok().message("修改成功");
//            } else {
//                return ResultJson.error().message("数据不存在");
//            }
//        }
//    }
    //上下架
    @PostMapping(value = "/admin/book/switch", consumes = "application/json")
    public ResultJson switchBookStatus(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {

            Book bookSearch = bookService.getById(book.getId());
            int status = book.getStatus();

            if (bookSearch.getDeletedAt()!=null){ //不能操作软删除的数据
                return ResultJson.error().message("无法操作该数据");
            }
            if(bookSearch.getInventory()<=0){
                return ResultJson.error().message("当前库存不足，无法上架");
            }
            if(status>1 || status<0){
                return ResultJson.error().message("状态参数异常");
            }
            if(status==bookSearch.getStatus()){
                return ResultJson.error().message("已经操作成功，请勿重复操作");
            }

            UpdateWrapper<Book> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status",book.getStatus()).eq("id",book.getId()); //UPDATE book SET status=? WHERE (id = ?)

            boolean result = bookService.update(null, updateWrapper);

//            boolean result = bookService.updateById(book);//根据id修改
            if (result) {
                return ResultJson.ok().message("修改成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    //根据id删除  批量删除
    @PostMapping(value = "/admin/book/delete", consumes = "application/json")
    public ResultJson deleteById(@RequestBody Long[] ids) {
//        Long id = book.getId();
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeee"+ids);

        for (Long id : ids){
            Book book = bookService.getById(id);
            book.setDeletedAt(LocalDateTime.now()); //设置删除时间
            bookService.updateById(book);//软删除
        }

//        boolean result = bookService.removeByIds(Arrays.asList(ids));//Arrays.asList()将数组转化成List集合的方法;因为removeByIds方法接收的是集合
//        if (result) {
//            return ResultJson.ok().message("删除成功");
//        } else {
//            return ResultJson.error().message("数据不存在");
//        }
        return ResultJson.ok().message("删除成功");
    }

    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/member/book/list")
    public ResultJson member_list(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "publisherId", required = false) Integer publisherId,
                           @RequestParam(value = "bookTypeId", required = false) Integer bookTypeId) {


        System.out.println(publisherId);

        //分页查询
//        Page<Book> books = bookService.page(getPage(), new QueryWrapper<Book>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>()
//                .eq("book.status" ,0) 下架的图书不展示？？
                .eq(publisherId != null, "book.publisher_id", publisherId).eq(bookTypeId != null, "book.book_type_id", bookTypeId)
                .like(StrUtil.isNotBlank(name), "book.name", name).isNull("book.deleted_at").orderBy(true, true, "book.created_at");//;

//        Page<Book> books = bookService.page(getPage(), queryWrapper); //按时间正排
        Page<Book> books = bookService.list(getPage(), queryWrapper);


        return ResultJson.ok().data(books);
    }
    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/index/book/list")
    public ResultJson indexList(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "publisherId", required = false) Integer publisherId,
                           @RequestParam(value = "bookTypeId", required = false) Integer bookTypeId) {


        System.out.println(publisherId);

        //分页查询
        Page<Book> books = bookService.page(getPage(), new QueryWrapper<Book>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
//        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>()
//                //数据权限
//                .eq(user.getIsAdmin() != 1, "book.user_id", user.getId())
//                .eq(publisherId != null, "book.publisher_id", publisherId).eq(bookTypeId != null, "book.book_type_id", bookTypeId)
//                .like(StrUtil.isNotBlank(name), "book.name", name).isNull("book.deleted_at").orderBy(true, true, "book.created_at");//;

//        Page<Book> books = bookService.page(getPage(), queryWrapper); //按时间正排
//        Page<Book> books = bookService.list(getPage(), queryWrapper);


        return ResultJson.ok().data(books);
    }


    //todo 怎么把export和list和在一起呢？list接口有return而export没有return
    @GetMapping("/admin/book/export")
    public void export(
            @RequestParam(value = "action") String action,
            HttpServletResponse response) throws IOException {
        //获取需要导出的数据
        List<Book> dataList = bookService.list();
        //excel文件名
        final String FILENAME = "图书信息";
        //sheetName
        final String SHEETNAME = "用户信息表";
        //获取model对象类
        Class book = Book.class;
        System.out.println(book);

        if (action.equals("export")) {
            try {

                //表头样式策略
                WriteCellStyle headWriteCellStyle = new WriteCellStyle();
                //设置头居中
                headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                //内容策略
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                //设置 水平居中
                contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                //初始化表格样式
                HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

//                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
                String fileName = URLEncoder.encode(FILENAME, "UTF-8").replaceAll("\\+", "%20");
                //响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。
                //此处设置了开放Content-Disposition，前端可获取该响应参数获取文件名称
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
                // 这里需要设置不关闭流
                EasyExcel.write(response.getOutputStream(), book).autoCloseStream(Boolean.FALSE)
                        .registerWriteHandler(horizontalCellStyleStrategy).sheet(SHEETNAME).doWrite(dataList);

            } catch (IOException e) { //下载失败情况的处理
                // 重置response
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                Map<String, String> map = MapUtils.newHashMap();
                map.put("status", "failure");
                map.put("message", "下载文件失败" + e.getMessage());
                response.getWriter().println(JSON.toJSONString(map));
            }
        }
    }
}
