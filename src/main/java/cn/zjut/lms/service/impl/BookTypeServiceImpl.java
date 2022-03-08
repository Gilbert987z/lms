package cn.zjut.lms.service.impl;

import cn.zjut.lms.entity.BookType;
import cn.zjut.lms.mapper.BookTypeMapper;
import cn.zjut.lms.service.BookTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书类型表 服务实现类
 * </p>
 *
 * @author zhangzhe
 * @since 2022-02-07
 */
@Service
public class BookTypeServiceImpl extends ServiceImpl<BookTypeMapper, BookType> implements BookTypeService {

}
