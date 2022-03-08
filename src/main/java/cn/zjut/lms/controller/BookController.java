package cn.zjut.lms.controller;


import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.Book;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangzhe
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/book")
public class BookController extends BaseController {
    /**
     * 分页列表
     *
     * @return
     */
    @GetMapping("list")
    public ResultJson list(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "publisherId", required = false) Integer publisherId,
                           @RequestParam(value = "bookTypeId", required = false) Integer bookTypeId) {

        //分页查询
//        Page<Book> books = bookService.page(getPage(), new QueryWrapper<Book>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(name), "name", name).eq("publisher_id", publisherId).eq("book_type_id", bookTypeId).orderBy(true, true, "created_at");

//        Page<Book> books = bookService.page(getPage(), queryWrapper); //按时间正排
        Page<Book> books = bookService.list(getPage(), queryWrapper);


        return ResultJson.ok().data(books);
    }

    @GetMapping("export")
    public void export(
            @RequestParam(value = "action") String action,
            HttpServletResponse response) {

        final String FILENAME_BOOKDATA = "图书信息";

        if (action.equals("export")) {
            try {
                //获取需要导出的数据
                List<Book> dataList = bookService.list();
                WriteCellStyle headWriteCellStyle = new WriteCellStyle();
                //设置头居中
                headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                //内容策略
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                //设置 水平居中
                contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
                HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
                String fileName = URLEncoder.encode(FILENAME_BOOKDATA, "UTF-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition"); //响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
                // 这里需要设置不关闭流
                EasyExcel.write(response.getOutputStream(), Book.class).autoCloseStream(Boolean.FALSE).registerWriteHandler(horizontalCellStyleStrategy).sheet("用户信息表").doWrite(dataList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
