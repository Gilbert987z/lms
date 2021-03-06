package cn.zjut.lms.controller.admin;


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
@RequestMapping("/admin/booktype")
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

    //增加
    @PostMapping(value = "create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody BookType bookTypeModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            String bookType = bookTypeModel.getBookType();

            int countBookType = bookTypeService.countBookType(bookType);

            if (countBookType > 0) { //查重校验
                return ResultJson.error().message("藏书类型重复");
            } else {
                boolean result = bookTypeService.add(bookTypeModel);
                if (result) {
                    return ResultJson.ok().message("增加成功");
                } else {
                    return ResultJson.error().message("数据不存在");
                }
            }
        }
    }

    //修改
    @PostMapping(value = "update", consumes = "application/json")
    public ResultJson update(@Valid @RequestBody BookType bookTypeModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            String bookType = bookTypeModel.getBookType();

            int countBookType = bookTypeService.countBookType(bookType);

            if (countBookType > 0) { //查重校验
                return ResultJson.error().message("藏书类型重复");
            } else {
                boolean result = bookTypeService.update(bookTypeModel);
                if (result) {
                    return ResultJson.ok().message("修改成功");
                } else {
                    return ResultJson.error().message("数据不存在");
                }
            }
        }
    }

    //根据id删除
    @PostMapping(value = "delete", consumes = "application/json")
    public ResultJson deleteById(@RequestBody BookType bookType) {
//        int id = bookType.getId();
        boolean result = bookTypeService.delete(bookType);
        if (result) {
            return ResultJson.ok().message("删除成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

}
