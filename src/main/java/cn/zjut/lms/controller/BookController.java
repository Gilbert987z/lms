package cn.zjut.lms.controller;

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
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    //查询所有数据
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = bookService.list(page, size);
        return ResultJson.ok().data("items", data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        Book book = bookService.getById(id);
        return ResultJson.ok().data("detail", book);
    }

    //增加
    @PostMapping(value = "create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookService.add(book);
            if (result) {
                return ResultJson.ok().message("增加成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    //修改
    @PostMapping(value = "update", consumes = "application/json")
    public ResultJson update(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookService.update(book);
            if (result) {
                return ResultJson.ok().message("修改成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    //根据id删除
    @PostMapping(value = "delete", consumes = "application/json")
    public ResultJson deleteById(@RequestBody Book book) {
//        int id = book.getId();
        boolean result = bookService.delete(book);
        if (result) {
            return ResultJson.ok().message("删除成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

}
