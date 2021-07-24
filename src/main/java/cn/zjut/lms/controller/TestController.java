package cn.zjut.lms.controller;

import cn.zjut.lms.util.IpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class TestController {

    /*
    测试IP
     */
    @RequestMapping(value = "/iptest", method = RequestMethod.GET)
    public String test(HttpServletRequest request){

        //获取IP地址
        String ipAddress = IpUtil.getIpAddr(request);

        return ipAddress;
    }

}
