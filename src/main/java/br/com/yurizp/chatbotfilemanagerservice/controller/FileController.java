package br.com.yurizp.chatbotfilemanagerservice.controller;

import br.com.yurizp.chatbotfilemanagerservice.mapper.S3ObjectsMapper;
import br.com.yurizp.chatbotfilemanagerservice.request.RequestUploadFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.Base64;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class FileController {

    private S3AsyncClient s3Client;
    private S3Presigner presigner;

    private static String BUCKET_NAME = "BUCKET_NAME";


    @PostMapping
    public Mono<ResponseEntity> uploadHandler(@RequestHeader HttpHeaders headers,
                                              @RequestHeader String format,
                                              @RequestBody RequestUploadFile requestUploadFile) {

        return Mono.just(S3ObjectsMapper.createPutRequest(BUCKET_NAME, requestUploadFile))
                .flatMap(request -> Mono.fromFuture(s3Client.putObject(request, AsyncRequestBody.fromBytes(Base64.getDecoder().decode(requestUploadFile.getFileBase64()))))
                        .map(x-> S3ObjectsMapper.createGetPresigner(BUCKET_NAME, request))
                        .map(getPresignerRequest -> presigner.presignGetObject(getPresignerRequest))
                        .map(resposnse -> resposnse.url().toString())
                ).map(url -> ResponseEntity.status(HttpStatus.CREATED).body(new String[]{url}));
    }
}
