package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.config.UploadConfig;
import com.demo.domain.FileUploads;
import com.demo.mapper.FileUploadsMapper;
import com.demo.service.FileUploadsService;
import com.demo.utils.FileUtils;
import com.demo.utils.UploadUtilsByRedis;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类名称
 *
 * @author peter
 * @date 2025/5/6
 */
@Service("fileUploadsServiceImpl2")
@RequiredArgsConstructor
public class FileUploadsServiceImpl2 implements FileUploadsService {

    private final FileUploadsMapper fileUploadsMapper;
    private final UploadUtilsByRedis uploadUtilsByRedis;
    private final RedissonClient  redissonClient;

    /**
     * 并发上传分片
     * @param name 文件名
     * @param sha256 文件指纹
     * @param size 分片大小
     * @param chunks 总分片数量
     * @param chunk 当前分片位置
     * @param file 分片文件
     */
    @Override
    public String uploadChunks(String name,
                             String sha256,
                             Long size,
                             Integer chunks,
                             Integer chunk,
                             MultipartFile file) throws IOException, RuntimeException {
        // 生成存储文件名
        String fileName = uploadUtilsByRedis.getFileName(sha256, chunks);
        // 存储文件路径
        String filePath = UploadConfig.PATH+fileName;
        // 如果文件不存在，创建文件

        File dir = new File(System.getProperty("user.dir")+UploadConfig.PATH);
        // 如果目录不存在，创建目录
        if (!dir.exists()){
            boolean mkdir = dir.mkdir();
            if (!mkdir) {
                throw new IllegalArgumentException("创建目录失败");
            }
        }


        FileUtils.writeWithBlok(filePath, size, file, file.getSize(), chunks, chunk);
        // 保存分片上传状态
        uploadUtilsByRedis.addChunk(sha256,chunk);


        // 判断所有分片是否已上传
        if (uploadUtilsByRedis.isUploaded(sha256)) {
            // 删除分块记录信息
            uploadUtilsByRedis.removeKey(sha256);

            // 将文件信息保存到数据库
            FileUploads fileUploads = new FileUploads();
            fileUploads.setOriginalFilename(name);
            fileUploads.setStoragePath(filePath);
            fileUploads.setFileSize(file.getSize());
            fileUploads.setFileType(checkFileType(name));
            fileUploads.setSha256Hash(sha256);
            fileUploads.setStatus(1);

            fileUploadsMapper.insert(fileUploads);
            return fileUploads.getId().toString();
        }
        return "0";
    }

    @Override
    public String upload(String name, String sha256,Long size, MultipartFile file) throws IOException {


        String fileName = FileUtils.generateFileName();
        String filePath = UploadConfig.PATH+fileName;

        InputStream inputStream = file.getInputStream();
        FileUtils.write(filePath, inputStream);
        inputStream.close();

        FileUploads fileUploads = new FileUploads();
        fileUploads.setOriginalFilename(name);
        fileUploads.setStoragePath(filePath);
        fileUploads.setFileSize(size);
        fileUploads.setFileType(checkFileType(name));
        fileUploads.setSha256Hash(sha256);
        fileUploads.setStatus(1);

        fileUploadsMapper.insert(fileUploads);

        return fileUploads.getId().toString();
    }

    // 检查文件类型
    public String checkFileType(String name){
        String fileName = name.substring(name.lastIndexOf("."));
        String type = null;
        switch (fileName){
            case ".jpg":
                type = "image/jpeg";
                break;
            case ".png":
                type = "image/png";
                break;
            case ".gif":
                type = "image/gif";
                break;
            case ".mp4":
                type = "video/mp4";
                break;
            case ".mp3":
                type = "audio/mp3";
                break;
            case ".wav":
                type = "audio/wav";
                break;
            case ".avi":
                type = "video/avi";
                break;
            case ".flv":
                type = "video/flv";
                break;
            case ".mkv":
                type = "video/mkv";
                break;
            case ".mov":
                type = "video/mov";
                break;
            case ".wmv":
                type = "video/wmv";
                break;
            case ".pdf":
                type = "application/pdf";
                break;
            case ".doc":
                type = "application/msword";
                break;
            case ".docx":
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case ".xls":
                type = "application/vnd.ms-excel";
                break;
            case ".xlsx":
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case ".ppt":
                type = "application/vnd.ms-powerpoint";
                break;
            case ".pptx":
                type = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                break;
            case ".txt":
                type = "text/plain";
                break;
            case ".zip":
                type = "application/zip";
                break;
            case ".rar":
                type = "application/x-rar-compressed";
                break;
            default:
                type = "application/octet-stream";
                break;
        }
        return type;
    }

    // 检查文件是否存在
    @Override
    public Long checkSha256(String sha256) {
        QueryWrapper<FileUploads> qw = new QueryWrapper<>();
        qw.eq("sha256_hash",sha256);
        FileUploads fileUploads = fileUploadsMapper.selectOne(qw);
        if (fileUploads!=null){
            return fileUploads.getId();
        }else {
            return 0L;
        }
    }

}
