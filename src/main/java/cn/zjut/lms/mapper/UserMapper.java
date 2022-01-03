package cn.zjut.lms.mapper;

import cn.zjut.lms.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {

}
