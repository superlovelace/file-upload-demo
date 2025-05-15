package com.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件上传类
 *
 * @author peter
 * @date 2025/5/6
 */
@TableName("file_uploads")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploads implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 文件存储路径
     */
    private String storagePath;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件MIME类型（如 image/jpeg）
     */
    private String fileType;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 上传者id
     */
    private Long uploaderId;

    /**
     * 文件指纹（文件sha256校验值: 64位）
     */
    private String sha256Hash;

    /**
     * 文件状态（1-正常, 2-审核中, 3-已删除）
     */
    private Integer status;




    private static final long serialVersionUID = 1L;

}
