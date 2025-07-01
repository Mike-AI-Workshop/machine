package com.example.machinebackend.controller;

import com.example.machinebackend.entity.ImageResource;
import com.example.machinebackend.service.ImageResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.stream.Collectors;

/**
 * ImageResourceController，提供图片资源相关的RESTful接口。
 */
@RestController
@RequestMapping("/api/images")
public class ImageResourceController {
    /** ImageResourceService依赖，用于业务处理 */
    private final ImageResourceService imageResourceService;

    @Autowired
    public ImageResourceController(ImageResourceService imageResourceService) {
        this.imageResourceService = imageResourceService;
    }

    private String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 上传图片（这里只做元数据入库，实际文件存储需结合OSS或本地存储实现）
     * @param file 图片文件
     * @param type 图片类型
     * @return 统一返回结构，包含code和data
     */
    @PostMapping
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file,
                                           @RequestParam("type") String type,
                                           HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        // 获取项目根目录的绝对路径
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "upload";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("创建upload目录：" + created + "，路径：" + dir.getAbsolutePath());
        } else {
            System.out.println("upload目录已存在，路径：" + dir.getAbsolutePath());
        }
        // 保存文件到本地upload目录
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
            System.out.println("文件已保存到：" + dest.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 1);
            result.put("message", "文件保存失败: " + e.getMessage());
            return result;
        }
        // 保存图片元数据
        String relativeUrl = "/upload/" + filename;
        ImageResource image = new ImageResource();
        image.setUrl(relativeUrl);
        image.setType(type);
        image.setCreatedAt(new Date());
        imageResourceService.addImageResource(image);

        // 返回包含完整URL的image对象
        String baseUrl = getBaseUrl(request);
        image.setUrl(baseUrl + relativeUrl);

        result.put("code", 0);
        result.put("data", image);
        return result;
    }

    /**
     * 获取图片详情
     * @param id 图片ID
     * @return 统一返回结构，包含code和data
     */
    @GetMapping("/{id}")
    public Map<String, Object> getImageById(@PathVariable Integer id, HttpServletRequest request) {
        ImageResource image = imageResourceService.getImageResourceById(id);
        if (image != null) {
            String baseUrl = getBaseUrl(request);
            image.setUrl(baseUrl + image.getUrl());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", image);
        return result;
    }

    /**
     * 删除图片
     * @param id 图片ID
     * @return 统一返回结构，包含code和message
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteImage(@PathVariable Integer id) {
        boolean success = imageResourceService.deleteImageResourceById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    /**
     * 获取所有图片资源（可选，便于管理）
     * @return 统一返回结构，包含code和data
     */
    @GetMapping
    public Map<String, Object> getAllImages(HttpServletRequest request) {
        List<ImageResource> images = imageResourceService.getAllImageResources();
        String baseUrl = getBaseUrl(request);
        images.forEach(image -> image.setUrl(baseUrl + image.getUrl()));

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", images);
        return result;
    }
} 