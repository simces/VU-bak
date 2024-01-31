package com.photo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    static {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("aws.accessKeyId", dotenv.get("AWS_ACCESS_KEY_ID"));
        System.setProperty("aws.secretKey", dotenv.get("AWS_SECRET_ACCESS_KEY"));
        System.setProperty("aws.s3.region", dotenv.get("AWS_S3_REGION"));
    }

    @Bean
    public AmazonS3 s3client() {
        String awsAccessKeyId = System.getProperty("aws.accessKeyId");
        String awsSecretKey = System.getProperty("aws.secretKey");
        String region = System.getProperty("aws.s3.region");

        if (awsAccessKeyId == null || awsSecretKey == null || region == null) {
            throw new IllegalStateException("AWS configuration not found in environment variables");
        }

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
