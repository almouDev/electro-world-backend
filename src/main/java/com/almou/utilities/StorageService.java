package com.almou.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String  uploadDir();
    void saveFile(String uploadDir, String fileName,
                  MultipartFile multipartFile) throws IOException;

    void deleteFile(String uploadDir, String fileName) throws IOException;

    byte[] downloadFile(String uploadDir, String fileName) throws IOException;
}
