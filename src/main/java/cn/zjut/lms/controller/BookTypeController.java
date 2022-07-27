package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.common.Const;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookPublisher;
import cn.zjut.lms.entity.BookType;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图书类型表 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@RestController
//@RequestMapping("/book/type")
public class BookTypeController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('bookType.list')")
    @GetMapping("/admin/bookType/list")
    public ResultJson list(@RequestParam(value = "bookType", defaultValue = "") String bookType) {

        //分页查询
        Page<BookType> bookTypes = bookTypeService.page(getPage(), new QueryWrapper<BookType>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(bookType), "book_type", bookType.trim()).orderBy(true, true, "created_at")); //按时间正排

        return ResultJson.ok().data(bookTypes);
    }
    /**
     * 下拉列表
     *
     * @return
     */
    @GetMapping("/index/bookType/list")
    public ResultJson index_list() {

        QueryWrapper<BookType> queryWrapper = new QueryWrapper<BookType>();
        queryWrapper.eq("status",1);//上架
        queryWrapper.isNull("deleted_at");
        queryWrapper.orderBy(true, true, "created_at");//按时间正排

        List<BookType> bookTypes = bookTypeService.list(queryWrapper);

        return ResultJson.ok().data(bookTypes);
    }

    //查询单个数据
    @PreAuthorize("hasAuthority('bookType.detail')")
    @GetMapping("/admin/bookType/detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        BookType bookType = bookTypeService.getById(id);
        return ResultJson.ok().data(bookType);
    }

    /**
     * 添加
     * @param
     * @return
     */
    @PreAuthorize("hasAuthority('bookType.save')")
    @PostMapping("/admin/bookType/save")
    public ResultJson save(@Validated @RequestBody BookType bookType, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        boolean result = bookTypeService.save(bookType);
        if (result) {
            return ResultJson.ok().message("添加成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

    /**
     * 添加
     * @param
     * @return
     */
    @Transactional //事务
    @PreAuthorize("hasAuthority('bookType.update')")
    @PostMapping("/admin/bookType/update")
    public ResultJson update(@Validated @RequestBody BookType bookType, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        boolean result = bookTypeService.updateById(bookType);
        if(bookType.getStatus() == 0){//下架操作需要下架所有关联的图书
            UpdateWrapper<Book> bookUpdateWrapper = new UpdateWrapper<>();
            bookUpdateWrapper.set("status", 0);
            bookUpdateWrapper.eq("status", 1); //下架所有上架的图书
            bookUpdateWrapper.isNull("deleted_at");//未删除
            bookUpdateWrapper.eq("book_type_id", bookType.getId()); //UPDATE book SET status=? WHERE (id = ?)

            bookService.update(null, bookUpdateWrapper); //修改状态
        }

        if (result) {
            return ResultJson.ok().message("修改成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

//    //根据id删除  批量删除
//    @PostMapping(value = "/admin/bookType/delete", consumes = "application/json")
//    public ResultJson deleteById(@RequestBody Long[] ids) {
//
//        boolean result = bookTypeService.removeByIds(Arrays.asList(ids));//Arrays.asList()将数组转化成List集合的方法;因为removeByIds方法接收的是集合
//        if (result) {
//            return ResultJson.ok().message("删除成功");
//        } else {
//            return ResultJson.error().message("数据不存在");
//        }
//    }
    /**
     * 上下架
     * 连带着关联的图书也要上下架
     */
    @Transactional //事务
    @PreAuthorize("hasAuthority('bookType.switch')")
    @PostMapping(value = "/admin/bookType/switch", consumes = "application/json")
    public ResultJson switchBookTypeStatus(@RequestBody Map<String,Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        Integer status = (Integer) map.get("status");

        BookType bookType = bookTypeService.getById(id);

        if (bookType.getDeletedAt() != null) { //不能操作软删除的数据
            return ResultJson.error().message("无法操作该数据");
        }
        if (status > 1 || status < 0) {
            return ResultJson.error().message("状态参数异常");
        }
        if (status.equals(bookType.getStatus())) {
            return ResultJson.error().message("已经操作成功，请勿重复操作");
        }

        UpdateWrapper<BookType> BookTypeUpdateWrapper = new UpdateWrapper<>();
        BookTypeUpdateWrapper.set("status", status);
        BookTypeUpdateWrapper.eq("id", bookType.getId()); //UPDATE book SET status=? WHERE (id = ?)

        boolean result = bookTypeService.update(null, BookTypeUpdateWrapper); //修改状态

        if(status == 0){//下架操作需要下架所有关联的图书
            UpdateWrapper<Book> bookUpdateWrapper = new UpdateWrapper<>();
            bookUpdateWrapper.set("status", status);
            bookUpdateWrapper.eq("status", 1); //下架所有上架的图书
            bookUpdateWrapper.isNull("deleted_at");//未删除
            bookUpdateWrapper.eq("book_type_id", bookType.getId()); //UPDATE book SET status=? WHERE (id = ?)

            bookService.update(null, bookUpdateWrapper); //修改状态
        }

        if (result) {
            return ResultJson.ok().message("修改成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }
}
