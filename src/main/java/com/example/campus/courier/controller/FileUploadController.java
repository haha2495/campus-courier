package com.example.campus.courier.controller;

import com.example.campus.courier.model.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Tag(name = "文件上传接口")
@RestController
@RequestMapping("/api/file")
public class FileUploadController {

    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failed("请选择要上传的图片");
        }

        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            // 生成新的文件名
            String newFileName = UUID.randomUUID().toString() + suffix;

            // 指定文件存储路径（使用绝对路径）
            String projectPath = System.getProperty("user.dir");
            String staticPath = projectPath + "/static/images/";
            File uploadDir = new File(staticPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 实际文件存储路径
            File dest = new File(uploadDir.getAbsolutePath() + File.separator + newFileName);

            // 保存文件
            file.transferTo(dest);

            // 返回文件访问路径
            String fileUrl = "http://127.0.0.1:9001/static/images/" + newFileName;
            return Result.success(fileUrl, "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("文件上传失败：" + e.getMessage());
        }
    }
} 