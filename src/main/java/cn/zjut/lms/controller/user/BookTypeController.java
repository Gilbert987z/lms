package cn.zjut.lms.controller.user;


import cn.zjut.lms.model.BookType;
import cn.zjut.lms.service.BookTypeService;
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
@RequestMapping("/user/booktype")
public class BookTypeController {
    @Autowired
    BookTypeService bookTypeService;


    //查询所有数据
    @GetMapping("")
    public ResultJson list() {
        Map<String, Object> data = bookTypeService.list();
        return ResultJson.ok().data(data);
    }
    //分页查询
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = bookTypeService.listByPage(page, size);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        BookType bookType = bookTypeService.getById(id);
        return ResultJson.ok().data("detail", bookType);
    }



}
