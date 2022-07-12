package cn.zjut.lms.util;

public interface  ResultCode {
    /**
     * 系统问题
     */
    public static Integer SUCCESS = 20000; //成功

    public static Integer ERROR = 20001; //失败

    public static Integer VALIDATION_ERROR = 422; //校验失败

    public static Integer LOGIN_ERROR =400;//, "用户名或密码错误"

    public static Integer Unauthorized  =401;   //refresh_token失效或没用,refresh_token失效才算真正的失效
    public static Integer Forbidden  =403;   //用户权限
    public static Integer REFRESH_TOKEN_EXPIRED  =418;   //token和refresh_token都失效,用户需要重新登录

}
