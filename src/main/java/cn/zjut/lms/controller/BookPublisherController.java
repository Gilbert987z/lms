package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.entity.BookPublisher;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 图书出版商表 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@RestController
//@RequestMapping("/book/publisher")
public class BookPublisherController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/book/publisher/list")
    public ResultJson list(@RequestParam(value = "name", defaultValue = "") String name) {

        //分页查询
        Page<BookPublisher> bookPublishers = bookPublisherService.page(getPage(), new QueryWrapper<BookPublisher>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排

        return ResultJson.ok().data(bookPublishers);
    }

    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("/index/book/publisher/list")
    public ResultJson index_list() {

        //分页查询
        Page<BookPublisher> bookPublishers = bookPublisherService.page(getPage(), new QueryWrapper<BookPublisher>().isNull("deleted_at")
                .orderBy(true, true, "created_at")); //按时间正排

        return ResultJson.ok().data(bookPublishers);
    }
}
