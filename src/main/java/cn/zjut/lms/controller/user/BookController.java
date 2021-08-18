package cn.zjut.lms.controller.user;

import cn.zjut.lms.model.Book;
import cn.zjut.lms.service.BookService;
import cn.zjut.lms.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

//@Api(description = "商户平台应用接口")
@RestController
@RequestMapping("/user/book")
public class BookController {
    @Autowired
    BookService bookService;

    //查询所有数据
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                @RequestParam(value = "bookName", defaultValue = "") String bookName,
                                @RequestParam(value = "publisherId",required = false ) Integer publisherId,
                                @RequestParam(value = "bookTypeId",required = false) Integer bookTypeId) {
        Map<String, Object> data = bookService.list(page, size, bookName,publisherId,bookTypeId);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        Book book = bookService.getById(id);
        return ResultJson.ok().data("detail", book);
    }


}
