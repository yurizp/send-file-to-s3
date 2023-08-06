package br.com.yurizp.chatbotfilemanagerservice.bucket;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.auth.signer.params.AwsS3V4SignerParams;
import software.amazon.awssdk.awscore.presigner.PresignedRequest;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;

@Configuration
class BucketConfig {


    @Value("${aws.region}")
    public String region;
    @Value("${aws.access-key}")
    public String accessKey;
    @Value("${aws.secret-key}")
    public String secretKey;

    @Bean
    public S3AsyncClient s3AsyncClient(AwsCredentialsProvider awsCredentialsProvider) {

        return S3AsyncClient.builder()
                .httpClient(sdkAsyncHttpClient())
                .region(Region.of(this.region))
                .credentialsProvider(awsCredentialsProvider)
                .forcePathStyle(true)
                .serviceConfiguration(s3Configuration()).build();
    }

    @Bean
    public S3Presigner S3Presigner(AwsCredentialsProvider awsCredentialsProvider) {

        return S3Presigner.builder()
                .region(Region.of(this.region))
                .credentialsProvider(awsCredentialsProvider)
                .serviceConfiguration(s3Configuration())
                .build();
    }


    private SdkAsyncHttpClient sdkAsyncHttpClient() {
        return NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ofSeconds(30))
                .maxConcurrency(64)
                .build();
    }

    private S3Configuration s3Configuration() {
        return S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build();
    }

    @Bean
    AwsCredentialsProvider awsCredentialsProvider() {
        return () -> AwsBasicCredentials.create(accessKey, secretKey);
    }
}
