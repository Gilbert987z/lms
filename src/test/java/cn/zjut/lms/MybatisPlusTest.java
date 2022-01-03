package cn.zjut.lms;

import cn.zjut.lms.mapper.UserMapper;
import cn.zjut.lms.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
public class MybatisPlusTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        User user = userMapper.selectById(1);
//        Assert.assertEquals("aa", user.getName());
        System.out.println(user);
    }

}
