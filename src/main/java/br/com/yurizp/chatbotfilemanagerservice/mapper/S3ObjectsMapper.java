package br.com.yurizp.chatbotfilemanagerservice.mapper;

import br.com.yurizp.chatbotfilemanagerservice.request.RequestUploadFile;
import org.springframework.http.MediaType;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

public class S3ObjectsMapper {
    public static PutObjectRequest createPutRequest(final String bucketName, final RequestUploadFile requestUploadFile) {
        byte[] file = Base64.getDecoder().decode(requestUploadFile.getFileBase64());
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .contentLength((long) file.length)
                .key(UUID.randomUUID().toString())
                .contentType(requestUploadFile.getMediaType().getType())
                .expires(Instant.now().plus(10, ChronoUnit.MINUTES))
                .build();
    }

    public static GetObjectPresignRequest createGetPresigner(final String bucketName, final PutObjectRequest request) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(request.bucket())
                .key(request.key())
                .build();

        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(30))
                .getObjectRequest(getObjectRequest)
                .build();

    }
}
