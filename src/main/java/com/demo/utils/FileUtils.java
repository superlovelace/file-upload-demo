package com.demo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件操作工具类
 * @author peter
 */
public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("工具类不能被实例化");
    }

    /**
     * 不分块写入文件
     * @param target 待上传文件
     * @param src 文件输入流
     * @throws IOException IO异常
     */
    public static void write(String target, InputStream src) throws IOException {
        try (OutputStream os = Files.newOutputStream(Paths.get(target))) {
            byte[] buf = new byte[1024];
            int len;
            while (-1 != (len = src.read(buf))) {
                os.write(buf, 0, len);
            }
            os.flush();
        }
    }

    /**
     * 分块写入文件
     * @param target 待上传文件
     * @param targetSize 待上传文件大小
     * @param src 文件输入流
     * @param srcSize 待上传文件块大小
     * @param chunks 文件总块数
     * @param chunk 当前块
     * @throws IOException IO异常
     */
    public static void writeWithBlok(String target, Long targetSize, MultipartFile src, Long srcSize, Integer chunks, Integer chunk) throws IOException {

        // 创建文件
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(target, "rw")) {
            // 设置文件长度
            randomAccessFile.setLength(targetSize);
            // 移动文件指针
            if (chunk == chunks - 1) {
                // 最后一块，文件大小-最后一块的长度 = 最后一块的偏移量
                randomAccessFile.seek(targetSize - srcSize);
            } else {
                // 根据分块大小，设置偏移量
                randomAccessFile.seek(chunk * srcSize);
            }
            // 设置缓冲区大小
            byte[] buf = new byte[1024];
            int len;
            try (InputStream inputStream = src.getInputStream()) {
                // 读取文件
                while (-1 != (len = inputStream.read(buf))) {
                    randomAccessFile.write(buf, 0, len);
                }
            }

        }
    }

    /**
     * 生成随机文件名
     * @return UUID
     */
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }


}
