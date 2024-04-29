package com.almou.utilities;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class StorageServiceImpl implements StorageService {
    @Override
    public String uploadDir() {
        return System.getProperty("user.dir")+"/src/main/resources/static/";
    }

    @Override
    public void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws  IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
    @Override
    public  void deleteFile(String uploadDir,String fileName) throws IOException {
        Path path=Paths.get(uploadDir);
        Path filePath=path.resolve(fileName);
        Files.deleteIfExists(filePath);
    }
    @Override
    public byte[] downloadFile(String uploadDir,String fileName) throws IOException {
        Path downloadPath=Paths.get(uploadDir+"/"+fileName);
        return Files.readAllBytes(downloadPath);
    }
}
