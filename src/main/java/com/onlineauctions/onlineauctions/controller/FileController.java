package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thciwei.x.file.storage.core.FileInfo;
import org.thciwei.x.file.storage.core.FileStorageService;

@Slf4j
@RestController
@Permission(isAllowAll = true)
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;//注入实列

    @Autowired
    private UserService userService;

    /**
     * 上传文件
     * @param file  文件
     * @param fileType  文件类型
     * @return 上传结果
     */
    @PostMapping("/upload/image/{fileType}")
    public Result<String> upload(MultipartFile file,
                                 @PathVariable String fileType,
                                 @RequestToken User user) {
        long time = System.currentTimeMillis() / 1000;
        Long username = user.getUsername();
        if (file == null) return Result.failure("上传失败！ 图片不能没有");
        String fileName = time + "-" + username + file.getOriginalFilename();
        // 调用文件存储服务，上传文件
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(fileType + "/") // 保存到相对路径下，为了方便管理，不需要可以不写
                .setName(fileName )
                .setObjectId(username) // 设置文件对象ID为用户名
                .upload(); // 将文件上传到对应地方

        // 返回上传结果
        return fileInfo == null ? Result.failure("上传失败！") : Result.success("上传成功", fileInfo.getUrl());
    }

    /**
     * 处理上传头像的请求
     * @param file 上传的文件
     * @param user 用户信息
     * @return 上传结果
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(MultipartFile file,
                                       @RequestToken User user) {
        if (file == null) return Result.failure("上传失败！ 图片不能没有");
        Long username = user.getUsername();
        // 对文件进行处理和存储
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("avatar/") // 设置文件存储路径为"avatar/"
                .setObjectId(username) // 设置文件对象ID为用户名
                .setSaveFilename(username + ".jpg")
                .putAttr("role", user.getRole()) // 设置文件属性为用户角色
                .image(img -> img.size(1000,1000)) // 对图片进行大小调整到 1000*1000
                .thumbnail(th -> th.size(200,200)) // 生成一张 200*200 的缩略图
                .upload(); // 上传文件并返回文件信息

        if (fileInfo == null){
            return Result.failure("上传失败！");
        }
        String thUrl = fileInfo.getThUrl();
        // 更新用户头像路径为缩略图路径
        userService.setAvatar(username,thUrl);

        // 返回上传结果，如果文件信息为空表示上传失败，否则返回上传成功和缩略图路径
        return Result.success("上传成功", thUrl);
    }

}
