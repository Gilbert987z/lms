package cn.zjut.lms.util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResultJson {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    public ResultJson() {
    }

    public static ResultJson ok() {
        ResultJson r = new ResultJson();
        r.setSuccess(true);
//        r.setCode(ResultCode.SUCCESS);
        r.setCode(HttpServletResponse.SC_OK);
        r.setMessage("成功");
        return r;
    }

    public static ResultJson error() {
        ResultJson r = new ResultJson();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
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
        r.setSuccess(false);
        r.setCode(ResultCode.VALIDATION_ERROR);
        r.setMessage("validation_error");
        return r;
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

    public ResultJson data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ResultJson data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

}
