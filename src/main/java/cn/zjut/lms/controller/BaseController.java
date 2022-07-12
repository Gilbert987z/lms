package cn.zjut.lms.controller;


import cn.zjut.lms.service.*;
import cn.zjut.lms.util.RedisUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

	@Autowired
	UserService userService;
	@Autowired
	SysRoleService sysRoleService;
	@Autowired
	SysPermissionService sysPermissionService;
	@Autowired
	SysUserRoleService sysUserRoleService;
	@Autowired
	SysRolePermissionService sysRolePermissionService;
	@Autowired
	BookService bookService;
	@Autowired
	BookTypeService bookTypeService;
	@Autowired
	BookPublisherService bookPublisherService;
	@Autowired
	BookBorrowLogService bookBorrowLogService;

	//redis
	@Autowired
	RedisUtil redisUtil;
	//密码加密
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	HttpServletRequest req;
	/**
	 * 获取页面   从请求的参数的中获取数据
	 * @return
	 */
	public Page getPage() {
		//接受page和size的请求参数
		int current = ServletRequestUtils.getIntParameter(req, "page", 1);
		int size = ServletRequestUtils.getIntParameter(req, "size", 10);

		return new Page(current, size);
	}
}
