package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.common.Const;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookType;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

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
    @GetMapping("/admin/bookType/list")
    public ResultJson list(@RequestParam(value = "bookType", defaultValue = "") String bookType) {

        //分页查询
        Page<BookType> bookTypes = bookTypeService.page(getPage(), new QueryWrapper<BookType>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(bookType), "book_type", bookType).orderBy(true, true, "created_at")); //按时间正排

        return ResultJson.ok().data(bookTypes);
    }
    /**
     * 下拉列表
     *
     * @return
     */
    @GetMapping("/index/bookType/list")
    public ResultJson index_list() {
        //分页查询
        Page<BookType> bookTypes = bookTypeService.page(getPage(), new QueryWrapper<BookType>().isNull("deleted_at")
                .orderBy(true, true, "created_at")); //按时间正排

        return ResultJson.ok().data(bookTypes);
    }

    //查询单个数据
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
    @PostMapping("/admin/bookType/update")
    public ResultJson update(@Validated @RequestBody BookType bookType, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        boolean result = bookTypeService.updateById(bookType);
        if (result) {
            return ResultJson.ok().message("修改成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

    //根据id删除  批量删除
    @PostMapping(value = "/admin/bookType/delete", consumes = "application/json")
    public ResultJson deleteById(@RequestBody Long[] ids) {

        boolean result = bookTypeService.removeByIds(Arrays.asList(ids));//Arrays.asList()将数组转化成List集合的方法;因为removeByIds方法接收的是集合
        if (result) {
            return ResultJson.ok().message("删除成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

}
