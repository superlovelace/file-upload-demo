package com.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author peter
 */
public interface FileUploadsService {

    String uploadChunks(String name,
                      String sha236,
                      Long size,
                      Integer chunks,
                      Integer chunk,
                      MultipartFile file) throws IOException, RuntimeException;

    String upload(String name,
               String sha256,
               Long size,
               MultipartFile file) throws IOException;

    Long checkSha256(String sha256);
}
