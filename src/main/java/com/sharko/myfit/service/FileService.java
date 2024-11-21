package com.sharko.myfit.service;

import com.sharko.myfit.exeption.UploadExeption;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void initializeBucket() {
        try {
            log.info("Проверяем существование bucket: " + bucketName);
            boolean isBucketExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            log.info("Bucket существует? " + isBucketExist);

            if (!isBucketExist) {
                log.info("Создаем bucket: " + bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
        } catch (Exception e) {
            log.error("Ошибка при инициализации бакета: {}", e.getMessage());
            throw new RuntimeException("Ошибка при создании бакета", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String objectName = file.getOriginalFilename();
            log.info("Загружаем файл: " + objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Файл загружен, создаем URL.");
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );

        } catch (Exception e) {
            throw new UploadExeption("Ошибка загрузки файла в MinIO: " + e.getMessage() + e);
        }
    }
}