//package cn.zjut.lms.common;
//
//import cn.zjut.lms.util.ResultJson;
//import org.springframework.validation.BindingResult;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FieldError {
//
//    public static Object validation_errors(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) { //数据校验
//            Map<String, Object> fieldErrorsMap = new HashMap<>();
//
//            for (org.springframework.validation.FieldError fieldError : bindingResult.getFieldErrors()) {
//                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//
//            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
//        }
//    }
//}
