//package com.demo.controller;
//
//import com.demo.domain.FileUploads;
//import com.demo.mapper.FileUploadsMapper;
//import com.demo.service.FileUploadsService;
//import com.demo.utils.UploadUtils;
//import com.demo.utils.Value;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//
///**
// * 资源获取接口
// * @author peter
// * @date 2025/5/6
// * 注意：若要通过redis(单机)存储分片信息，请注释掉此类,并反注释掉IndexController2
// */
//@Api(tags = "文件上传接口")
//@CrossOrigin
//@RestController
//@RequestMapping("/api")
//public class IndexController {
//
//    @Qualifier("fileUploadsServiceImpl")
//    @javax.annotation.Resource
//    private FileUploadsService fileUploadsService;
//
//    @javax.annotation.Resource
//    private FileUploadsMapper fileUploadsMapper;
//
//    // 文件上传接口
//    @ApiOperation(value = "文件上传接口")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "name", value = "文件名"),
//            @ApiImplicitParam(name = "sha256", value = "文件sha256"),
//            @ApiImplicitParam(name = "size", value = "文件大小"),
//            @ApiImplicitParam(name = "chunks", value = "分片数量"),
//            @ApiImplicitParam(name = "chunk", value = "分片序号"),
//            @ApiImplicitParam(name = "file", value = "文件")
//    })
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploads(
//            @RequestParam("name") String name,
//            @RequestParam("sha256") String sha256,
//            @RequestParam("size") Long size,
//            @RequestParam("chunks") Integer chunks,
//            @RequestParam("chunk") Integer chunk,
//            MultipartFile file,
//            HttpServletRequest request) throws IOException {
//        String s = null;
//        if (chunks!=0){
//            s = fileUploadsService.uploadChunks(name, sha256, size, chunks, chunk, file);
//        }else {
//            s = fileUploadsService.upload(name, sha256, size, file);
//        }
//
//        String host = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
//        String url = host + "/resource/" + s;
//        return ResponseEntity.status(200).body(url);
//    }
//
//    // 查询文件上传进度和状态
//    @ApiOperation(value = "查询文件上传进度和状态")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "sha256", value = "文件sha256")
//    })
//    @PostMapping("/progress")
//    public ResponseEntity<String> progress(
//            String sha256) {
//        Map<String, Object> chunkInfo;
//        try {
//            chunkInfo = UploadUtils.getChunkInfo(sha256);
//            System.out.println("分块存储查询："+chunkInfo);
//        } catch (NullPointerException e) {
//            chunkInfo = null;
//        }
//        Long b = fileUploadsService.checkSha256(sha256);
//
//        if (b!=0L){
//            // 已上传
//            String path = "/api/resource/"+b;
//            return ResponseEntity.status(400).body(path);
//        }
//        // 没有上传中的文件
//        if (chunkInfo==null){
//            return ResponseEntity.status(404).body("404");
//        }
//        // 有上传中的文件
//        boolean[] status = (boolean[]) chunkInfo.get("status");
//        int count = 0;
//        for (boolean value : status) {
//            if (value) {
//                count++;
//            }
//        }
//        if (count==0){
//            return ResponseEntity.status(404).body("404");
//        }
//        return ResponseEntity.status(200).body(String.valueOf(count));
//    }
//
//
//
//    // 文件下载接口
//    @ApiOperation(value = "文件下载接口")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "fileId", value = "文件id"),
//            @ApiImplicitParam(name = "displayName", value = "文件名")
//    })
//    @GetMapping("/resource/{fileId}")
//    public ResponseEntity<Resource> onlineFile(
//            @PathVariable String fileId,
//            @RequestParam(required = false) String displayName,
//            HttpServletRequest request) throws IOException {
//
//        // 1. 从数据库查询文件元信息
//        FileUploads meta = fileUploadsMapper.selectById(Long.valueOf(fileId));
//        if (meta == null) {
//            return ResponseEntity.notFound().build();
//        }else {
//            meta.setDownloadCount(meta.getDownloadCount()+1);
//            fileUploadsMapper.updateById(meta);
//        }
//        // 服务器存储名（如 abc123）
//        String realFileName = meta.getStoragePath();
//        // 原始文件名（如 报告.pdf）
//        String originalFileName = meta.getOriginalFilename();
//
//        // 2. 读取文件
//        Path filePath = Paths.get(realFileName);
//        Resource resource = new FileSystemResource(String.valueOf(filePath));
//
//        // 3. 动态设置下载文件名（优先使用传入的 displayName，否则用原始名）
//        String downloadName = (displayName != null) ? displayName : originalFileName;
//        String encodedFileName = URLEncoder.encode(downloadName, "UTF-8")
//                .replace("+", "%20");
//        if (request.getRequestURI().contains("/resource/")) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.valueOf(meta.getFileType()))
//                    .header("Content-Disposition",
//                            "inline; filename=\"" + downloadName + "\"; " +
//                                    "filename*=UTF-8''" + encodedFileName) // 兼容各浏览器
//                    .body(resource);
//        }else {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.valueOf(meta.getFileType()))
//                    .header("Content-Disposition",
//                            "attachment; filename=\"" + downloadName + "\"; " +
//                                    "filename*=UTF-8''" + encodedFileName) // 兼容各浏览器
//                    .body(resource);
//        }
//    }
//
//    @PostMapping("/info")
//    public Map<String, Value> getInfo(){
//        return UploadUtils.getChunkMapInfo();
//    }
//}
