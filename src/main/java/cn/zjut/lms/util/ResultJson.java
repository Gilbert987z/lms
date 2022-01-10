package cn.zjut.lms.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(value = {"handler"})
public class ResultJson {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    private Object data;


    public ResultJson() {
    }

    public ResultJson success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public ResultJson message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultJson code(Integer code) {
        this.setCode(code);
        return this;
    }

    public ResultJson data(Object object) {  //支持object传入
        this.setData(object);
        return this;
    }

    public static ResultJson ok() {
        ResultJson r = new ResultJson();
//        r.setCode(HttpServletResponse.SC_OK);
        r.setCode(ResultCode.SUCCESS);
        r.setSuccess(true);
        r.setMessage("成功");
        return r;
    }

    public static ResultJson error() {
        ResultJson r = new ResultJson();
        r.setCode(ResultCode.ERROR);
        r.setSuccess(false);

        r.setMessage("失败");
        return r;
    }

    //登录失败
    public static ResultJson login_error() {
        ResultJson r = new ResultJson();
        r.setSuccess(false);
        r.setCode(ResultCode.LOGIN_ERROR);
        r.setMessage("用户名或密码错误");
        return r;
    }

    //验证错误
    public static ResultJson validation_error() {
        ResultJson r = new ResultJson();
        r.setCode(ResultCode.VALIDATION_ERROR);
        r.setSuccess(false);
        r.setMessage("validation_error");
        return r;
    }


    public ResultJson data(String key, Object value) { //支持key和value转换成map传入
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
//        this.data.put(key, value);
        this.setData(map);
        return this;
    }

    public ResultJson data(Map<String, Object> map) {  //支持map传入
        this.setData(map);
        return this;
    }


}
