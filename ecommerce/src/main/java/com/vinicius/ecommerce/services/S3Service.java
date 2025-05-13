package com.vinicius.ecommerce.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.vinicius.ecommerce.exceptions.ImageUploadException;
import com.vinicius.ecommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public String uploadImage(MultipartFile image) {
        String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();

        try {
            File file = this.convertMultPartForFile(image);
            amazonS3.putObject(bucketName, fileName, file);
            file.delete();
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException ex) {
            throw new ImageUploadException(ex.getMessage());
        }
    }

    private File convertMultPartForFile(MultipartFile image) throws IOException {
        File fileConvert = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream out = new FileOutputStream(fileConvert);
        out.write(image.getBytes());
        out.close();
        return fileConvert;
    }

    public String updateImage(Product product, MultipartFile image) {
        String fileName = this.formatImageName(product.getImageUrl());
        this.deleteImage(fileName);
        return this.uploadImage(image);
    }

    public void deleteImage(String image) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, image);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public String formatImageName(String image) {
        int lastSlashIndex = image.lastIndexOf("/");
        return image.substring(lastSlashIndex + 1);
    }
}
