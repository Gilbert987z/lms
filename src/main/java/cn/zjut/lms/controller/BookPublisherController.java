package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookPublisher;
import cn.zjut.lms.entity.BookType;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图书出版商表 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@RestController
//@RequestMapping("/bookPublisher")
public class BookPublisherController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('bookPublisher.list')")
    @GetMapping("/admin/bookPublisher/list")
    public ResultJson list(@RequestParam(value = "publisher", defaultValue = "") String publisher) {

        QueryWrapper<BookPublisher> queryWrapper = new QueryWrapper<>();

        queryWrapper.isNull("deleted_at");
        queryWrapper.like(StrUtil.isNotBlank(publisher), "publisher", publisher.trim()); //模糊查询，去空格
        queryWrapper.orderBy(true, true, "created_at");


        //分页查询
        Page<BookPublisher> bookPublishers = bookPublisherService.page(getPage(), queryWrapper); //按时间正排

        return ResultJson.ok().data(bookPublishers);
    }

    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/index/bookPublisher/list")
    public ResultJson index_list() {
        QueryWrapper<BookPublisher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);//仅限上架
        queryWrapper.isNull("deleted_at");
        queryWrapper.orderBy(true, true, "created_at");//按时间正排

        List<BookPublisher> bookPublishers = bookPublisherService.list(queryWrapper);

        return ResultJson.ok().data(bookPublishers);
    }

    //查询单个数据
    @PreAuthorize("hasAuthority('bookPublisher.detail')")
    @GetMapping("/admin/bookPublisher/detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        BookPublisher bookPublisher = bookPublisherService.getById(id);
        return ResultJson.ok().data(bookPublisher);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @PreAuthorize("hasAuthority('bookPublisher.save')")
    @PostMapping("/admin/bookPublisher/save")
    public ResultJson save(@Validated @RequestBody BookPublisher bookPublisher, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        boolean result = bookPublisherService.save(bookPublisher);
        if (result) {
            return ResultJson.ok().message("添加成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @Transactional //事务
    @PreAuthorize("hasAuthority('bookPublisher.update')")
    @PostMapping("/admin/bookPublisher/update")
    public ResultJson update(@Validated @RequestBody BookPublisher bookPublisher, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        boolean result = bookPublisherService.updateById(bookPublisher);

        if (bookPublisher.getStatus() == 0) {//下架操作需要下架所有关联的图书
            UpdateWrapper<Book> bookUpdateWrapper = new UpdateWrapper<>();
            bookUpdateWrapper.set("status", 0);
            bookUpdateWrapper.eq("status", 1); //下架所有上架的图书
            bookUpdateWrapper.isNull("deleted_at");//未删除
            bookUpdateWrapper.eq("publisher_id", bookPublisher.getId());
            //UPDATE book SET status=? WHERE (status = ? AND deleted_at IS NULL AND publisher_id = ?)

            bookService.update(null, bookUpdateWrapper); //修改状态
        }

        if (result) {
            return ResultJson.ok().message("修改成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

//    //根据id删除  批量删除
//    @PostMapping(value = "/admin/bookPublisher/delete", consumes = "application/json")
//    public ResultJson deleteById(@RequestBody Long[] ids) {
//
//        boolean result = bookPublisherService.removeByIds(Arrays.asList(ids));//Arrays.asList()将数组转化成List集合的方法;因为removeByIds方法接收的是集合
//        if (result) {
//            return ResultJson.ok().message("删除成功");
//        } else {
//            return ResultJson.error().message("数据不存在");
//        }
//    }

//    //根据id删除  批量删除
//    @PostMapping(value = "/admin/bookPublisher/delete", consumes = "application/json")
//    public ResultJson deleteById(@RequestBody Long[] ids) {
//
//        boolean result = bookPublisherService.removeByIds(Arrays.asList(ids));//Arrays.asList()将数组转化成List集合的方法;因为removeByIds方法接收的是集合
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
    @PreAuthorize("hasAuthority('bookPublisher.switch')")
    @PostMapping(value = "/admin/bookPublisher/switch", consumes = "application/json")
    public ResultJson switchBookPublisherStatus(@RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        Integer status = (Integer) map.get("status");

        BookPublisher bookPublisher = bookPublisherService.getById(id);

        if (bookPublisher.getDeletedAt() != null) { //不能操作软删除的数据
            return ResultJson.error().message("无法操作该数据");
        }
        if (status > 1 || status < 0) {
            return ResultJson.error().message("状态参数异常");
        }
        if (status.equals(bookPublisher.getStatus())) {
            return ResultJson.error().message("已经操作成功，请勿重复操作");
        }

        UpdateWrapper<BookPublisher> bookPublisherUpdateWrapper = new UpdateWrapper<>();
        bookPublisherUpdateWrapper.set("status", status);
        bookPublisherUpdateWrapper.eq("id", bookPublisher.getId()); //UPDATE book SET status=? WHERE (id = ?)

        boolean result = bookPublisherService.update(null, bookPublisherUpdateWrapper); //修改状态

        if (status == 0) {//下架操作需要下架所有关联的图书
            UpdateWrapper<Book> bookUpdateWrapper = new UpdateWrapper<>();
            bookUpdateWrapper.set("status", status);
            bookUpdateWrapper.eq("status", 1); //下架所有上架的图书
            bookUpdateWrapper.isNull("deleted_at");//未删除
            bookUpdateWrapper.eq("publisher_id", bookPublisher.getId());
            //UPDATE book SET status=? WHERE (status = ? AND deleted_at IS NULL AND publisher_id = ?)

            bookService.update(null, bookUpdateWrapper); //修改状态
        }

        if (result) {
            return ResultJson.ok().message("修改成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }
}
