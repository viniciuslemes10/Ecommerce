package com.vinicius.ecommerce.services;

import com.amazonaws.services.s3.AmazonS3;
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
            System.out.println("Error uploading image to s3 bucket: " + ex.getMessage());
            return null;
        }
    }

    private File convertMultPartForFile(MultipartFile image) throws IOException {
        File fileConvert = new File(Objects.requireNonNull(image.getOriginalFilename()));
        FileOutputStream out = new FileOutputStream(fileConvert);
        out.write(image.getBytes());
        out.close();
        return fileConvert;
    }
}
