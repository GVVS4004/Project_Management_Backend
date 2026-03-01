package com.fullstack.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

@Service
public class FileUploadServiceImpl implements FileUploadService{
    @Value("${cloud.storage.bucket-name}")
    private String bucketName;

    @Value("${cloud.storage.region}")
    private String region;

    @Value("${cloud.storage.endpoint}")
    private String endpoint;

    @Value("${cloud.storage.access-key}")
    private String accessKey;

    @Value("${cloud.storage.secret-key}")
    private String secretKey;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @PostConstruct
    public void init(){

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .forcePathStyle(true)
                .build();

        s3Presigner = S3Presigner.builder()
                    .region(Region.of(region))
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
    }

    @Override
    public void upload(String storageKey, MultipartFile file) throws IOException {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    @Override
    public String generatePresignedUrl(String storageKey, Duration expiration) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(expiration)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void delete(String storageKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(storageKey)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

}
