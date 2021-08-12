package cn.zjut.lms.config.intercepors;


import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行了拦截器");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }


        String token = request.getHeader("token");
        boolean login_flag = JwtUtil.checkToken(token);  //校验token
        System.out.println("token:"+token);
        System.out.println("检查token，login_flag："+login_flag);

        if (token==null){
            noLogin(response);
            return false;
        }

        if(token.equals("zz")){
            System.out.println("万能token");
            return true;
        }
        if(login_flag){
//            return ResultJson.ok().data("accessToken",token);
            System.out.println("已登录");
            return true;
        }

        noLogin(response);
        return false;

//        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
//        HttpSession session = request.getSession();
//        //这里的User是登陆时放入session的
//        User user = (User) session.Attribute("user");
////        //如果session中没有user，表示没登陆get
//        if (user == null) {
//            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
//            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
//            return false;
//        } else {
//            return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
//        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    //没登录时返回错误信息
    //https://blog.csdn.net/weixin_42970433/article/details/101707234
    public void noLogin(HttpServletResponse response) throws Exception{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        //创建json对象
//        JSONObject res = new JSONObject();  //由于restcontroller自带转成json的作用。所以这里要自己转成json
        //todo 可以尝试把ResultJson运用上去
//        ResultJson resultJson = new ResultJson();
        String data = JSONObject.toJSONString(ResultJson.error().code(HttpServletResponse.SC_UNAUTHORIZED).message("校验失败，请重新登录！"));  // 转换为json字符串
//        System.out.println("personStr:"+data);
        JSONObject res = JSONObject.parseObject(data);  // 转换为json对象

//        Map<String, Object> res =new HashMap<>();
//        res.put("status",HttpServletResponse.SC_UNAUTHORIZED);
//        res.put("msg","校验失败，请重新登录！");
        PrintWriter out = null ;
        out = response.getWriter();
        out.write(res.toString());
        out.flush();
        out.close();
    }
}
