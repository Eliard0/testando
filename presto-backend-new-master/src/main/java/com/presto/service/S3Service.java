package com.presto.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucketName;

    public URI uploadFile(MultipartFile file) throws IOException, URISyntaxException {
        String nameFile = file.getOriginalFilename();
        InputStream inputStream =  file.getInputStream();
        String contentType = file.getContentType();
        return this.uploadFile(nameFile, inputStream, contentType);
    }

    private URI uploadFile(String nameFile, InputStream inputStream, String contentType) throws URISyntaxException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        this.amazonS3.putObject(this.bucketName, nameFile, inputStream, objectMetadata);
        return this.amazonS3.getUrl(this.bucketName, nameFile).toURI();
    }


}
