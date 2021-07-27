package cn.zjut.lms.controller;

import cn.zjut.lms.model.BookBorrow;
import cn.zjut.lms.service.BookBorrowService;
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
@RequestMapping("/bookBorrow")
public class BookBorrowController {
    @Autowired
    BookBorrowService bookBorrowService;

    //查询所有数据
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size,
                                @RequestParam(value = "bookName", defaultValue = "") String bookName,
                                @RequestParam(value = "publisherId",required = false ) Integer publisherId,
                                @RequestParam(value = "bookTypeId",required = false) Integer bookTypeId) {
        Map<String, Object> data = bookBorrowService.list(page, size, bookName,publisherId,bookTypeId);
        return ResultJson.ok().data("items", data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        BookBorrow bookBorrow = bookBorrowService.getById(id);
        return ResultJson.ok().data("detail", bookBorrow);
    }

    //增加
    @PostMapping(value = "create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody BookBorrow bookBorrow, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookBorrowService.add(bookBorrow);
            if (result) {
                return ResultJson.ok().message("增加成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    //修改
    @PostMapping(value = "update", consumes = "application/json")
    public ResultJson update(@Valid @RequestBody BookBorrow bookBorrow, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookBorrowService.update(bookBorrow);
            if (result) {
                return ResultJson.ok().message("修改成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

    //修改状态
    @PostMapping(value = "updateStatus", consumes = "application/json")
    public ResultJson updateStatus(@Valid @RequestBody BookBorrow bookBorrow, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            boolean result = bookBorrowService.update(bookBorrow);
            if (result) {
                return ResultJson.ok().message("修改状态成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }

//    //根据id删除
//    @PostMapping(value = "delete", consumes = "application/json")
//    public ResultJson deleteById(@RequestBody Book book) {
////        int id = book.getId();
//        boolean result = bookBorrowService.delete(book);
//        if (result) {
//            return ResultJson.ok().message("删除成功");
//        } else {
//            return ResultJson.error().message("数据不存在");
//        }
//    }

}