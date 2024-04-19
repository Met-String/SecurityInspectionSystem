package com.STR.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
public class ImageUploadUtil {
    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    public String imgUpload(MultipartFile img){
        String bucketName = "yosafire233";
        String filename = UUID.randomUUID().toString().replace("-", "") + "-" + img.getOriginalFilename();
        try {
            InputStream fileInputStream = img.getInputStream();
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName,filename,fileInputStream);
            return "https://" + bucketName + "." + endpoint + "/" + filename;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
