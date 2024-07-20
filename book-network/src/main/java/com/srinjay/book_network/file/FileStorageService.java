package com.srinjay.book_network.file;

import com.srinjay.book_network.book.Book;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile cover,
            @Nonnull Long userId
    ) {
         final String fileUploadSubPath = "users"+ separator + userId;

        return uploadFile(cover, fileUploadSubPath);
    }

    private String uploadFile(
            @Nonnull MultipartFile cover,
            @Nonnull String fileUploadSubPath
    ) {
        final String fileUploadPath = this.fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(fileUploadPath);
        if(!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated) {
                log.warn ("failed to create target folder: {}", fileUploadPath);
                return null;
            }
        }

        final String fileExtension = getFileExtension(cover.getOriginalFilename());
        String targetFilePath = fileUploadPath + separator + currentTimeMillis () + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write (targetPath, cover.getBytes());
            log.info ("file saved to: {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error ("failed to save file: {}", targetFilePath, e);
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex+1).toLowerCase ();
    }
}
