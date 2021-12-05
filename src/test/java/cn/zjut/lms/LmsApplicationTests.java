package cn.zjut.lms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class LmsApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作key-value都是字符串，最常用

    @Test
    void contextLoads() {
        //字符串操作
        stringRedisTemplate.opsForValue().append("msg:test","coder");

        //列表操作
        stringRedisTemplate.opsForList().leftPush("mylist","1");
        stringRedisTemplate.opsForList().leftPush("mylist","2");
    }

    @Test
    void test(){
        String a = "sdf";
        Integer b = 1;
        System.out.println(a.getClass().toString());
        System.out.println(b.getClass().toString());
    }

    @Test
    void tesEqual(){
        String username = null;
        System.out.println(username.equals("erqw"));
    }
}
