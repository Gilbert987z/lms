package cn.zjut.lms.controller;

import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("file")
public class UploadFileController {

    /**
     * 文件保存路径，开发时一般使用 @value 注解注入或者写入常量类
     */
    private final static String FILE_SAVE_PATH = "D:/file/picture/";

    @PostMapping(value = "upload")
    public ResultJson upload(MultipartFile file) {
        //获取上传的文件名 比如：abc.jpg
        String uploadFileName = file.getOriginalFilename();
        //获取后缀 比如：.jpg
        String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        //此处生成 uuid 作为新的文件名称
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //此处拼接得到最终文件保存路径（ D:/file/picture/生成的uuid.jpg）
        String savePath = FILE_SAVE_PATH + uuid + suffixName;

        File f = new File(savePath);
        // f.getParentFile()获取文件的父级路径，即：FILE_SAVE_PATH 的值
        if (!f.getParentFile().exists()) {
            //mkdirs()是创建多级目录
            f.getParentFile().mkdirs();
        }
        //保存文件
        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
//            return new Result(1, "Upload failed!", e.getMessage());
            return ResultJson.error().message(e.getMessage());
        }
//        return new Result(0, "Upload succeeded!");

        String url = "/image/"+uuid+suffixName;
        return ResultJson.ok().data("url",url).message("Upload succeeded!");
    }


}
