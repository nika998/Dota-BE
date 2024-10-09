package com.artigo.dota.service.impl.s3;

import com.artigo.dota.configuration.S3BucketProperties;
import com.artigo.dota.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Service
@Slf4j
public class S3ImageServiceImpl implements ImageService {

    private final S3BucketProperties s3Bucket;

    private final S3Client s3Client;

    public S3ImageServiceImpl(S3BucketProperties s3Bucket, S3Client s3Client) {
        this.s3Bucket = s3Bucket;
        this.s3Client = s3Client;
    }

    @Override
    public String getRootFolder() {
        return s3Bucket.getRootFolder();
    }

    @Override
    public String getImageUrlPrefix() {
        return s3Bucket.getImageUrlPrefix();
    }

    @Override
    public String saveImage(String key, byte[] file) {
        try {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(s3Bucket.getBucket())
                    .contentType("image/jpeg")
                    .key(key)
                    .build();

            s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
            log.info("Image saved inside s3 bucket");

            return key;
        } catch (SdkClientException e) {
            log.error("Cannot save image inside s3 bucket");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteImage(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3Bucket.getBucket())
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

    @Override
    public byte[] getImage(String imageUrl) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Bucket.getBucket())
                .key(imageUrl)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);
        try {
            return responseInputStream.readAllBytes();
        } catch (NoSuchKeyException e) {
            log.error("The specified key does not exist in the bucket: {}", imageUrl);
        }
        catch (IOException e) {
            log.error("Image cannot be read");
        }
        return new byte[0];
    }
}
