package cn.zjut.lms.util;

public interface  ResultCode {
    public static Integer SUCCESS = 20000; //成功

    public static Integer ERROR = 20001; //失败

    public static Integer VALIDATION_ERROR = 422; //校验失败

    public static Integer LOGIN_ERROR =400;//, "用户名或密码错误"
}
