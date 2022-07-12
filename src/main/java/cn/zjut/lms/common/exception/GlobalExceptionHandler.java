package cn.zjut.lms.common.exception;

import cn.zjut.lms.util.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;



//写的很好，值得一看
//https://www.cnblogs.com/xiaokangk/p/14208090.html
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 实体校验异常捕获
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK) //返回200的状态码
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultJson handler(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();

        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return ResultJson.error().data(objectError.getDefaultMessage());
    }

    //	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultJson handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e.getMessage());
        return ResultJson.error().data(e.getMessage());
    }

    //	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RuntimeException.class)
    public ResultJson handler(RuntimeException e) {
        log.error("运行时异常：----------------{}", e.getMessage());
        return ResultJson.error().data(e.getMessage());
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseBody
//    public ResultJson handlerNoFoundException(Exception e) {
//        log.error("运行时异常：----------------{}", e.getMessage());
//        return ResultJson.error().message("路径不存在，请检查路径是否正确");
//    }
}
