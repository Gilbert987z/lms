package cn.zjut.lms.config.security.filter;

import cn.zjut.lms.common.Const;
import cn.zjut.lms.common.exception.CaptchaException;
import cn.zjut.lms.config.security.handler.LmsAuthenticationFailureHandler;
import cn.zjut.lms.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {

	@Autowired
    RedisUtil redisUtil;

	@Autowired
    LmsAuthenticationFailureHandler lmsAuthenticationFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		log.info("CaptchaFilter");

		String url = httpServletRequest.getRequestURI();
		log.info(url);

		//当login接口的时候，校验验证码
		if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {

			try{
				// 校验验证码
				validate(httpServletRequest);
			} catch (CaptchaException e) {
				log.info(String.valueOf(e));
				// 交给认证失败处理器
                lmsAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	// 校验验证码逻辑
	private void validate(HttpServletRequest httpServletRequest) {
		log.info("校验验证码");
		String code = httpServletRequest.getParameter("code");//获取请求的code值
		String key = httpServletRequest.getParameter("token");

		if(code.equals("11111")){ //万能验证码的设置
			return;
		}else{
			if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
				throw new CaptchaException("验证码错误");
			}

			if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
				throw new CaptchaException("验证码错误");
			}
		}

		log.info("key:"+key+";code:"+code);
		System.out.println(redisUtil.hget(Const.CAPTCHA_KEY, key));


		// 一次性使用 删除该数据
		redisUtil.hdel(Const.CAPTCHA_KEY, key);
	}
}
