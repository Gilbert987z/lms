package cn.zjut.lms.controller.user;

import cn.zjut.lms.model.Publisher;
import cn.zjut.lms.service.PublisherService;
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
@RequestMapping("/user/publisher")
public class PublisherController {
    @Autowired
    PublisherService publisherService;
    //查询所有数据
    @GetMapping("")
    public ResultJson list() {
        Map<String, Object> data = publisherService.list();
        return ResultJson.ok().data(data);
    }
    //查询所有数据
    @GetMapping("list")
    public ResultJson listByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = publisherService.listByPage(page, size);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        Publisher publisher = publisherService.getById(id);
        return ResultJson.ok().data("detail", publisher);
    }

}
