package com.artigo.dota.service.impl.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Slf4j
@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public void saveImageFile(String bucketName, String key, byte[] image){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType("image/jpeg")
                .key(key)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(image));
        log.info("Image saved inside s3 bucket");
    }

    public byte[] getImage(String bucketName, String key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);
        try {
            return responseInputStream.readAllBytes();
        } catch (NoSuchKeyException e) {
            log.error("The specified key does not exist in the bucket: {}", key);
        }
        catch (IOException e) {
            log.error("Image cannot be read");
        }
        return new byte[0];
    }

    public boolean deleteImage(String bucketName, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Image deleted from s3 bucket");
            return true;
        } catch (NoSuchKeyException e) {
            log.error("No image with provided key inside s3 bucket " + key);
            return false;
        }
    }


}
