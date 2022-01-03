package cn.zjut.lms.mapper;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LoginDao {

    User findByUsername(String username);

    AccessToken tokenfindByUserId(int userId);
    User findByUserId(Integer userId);

    /*
    更新
    {p} 要更新的User实例
     */
    int update(AccessToken accessToken);

    int logout(AccessToken accessToken);

    /*
    增加
    {p} 要新增的User实例
     */
    int add(AccessToken accessToken);

}
