package br.com.yurizp.chatbotfilemanagerservice.request;

import br.com.yurizp.chatbotfilemanagerservice.MediaTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUploadFile {

    @JsonDeserialize(using = MediaTypeDeserializer.class)
    private MediaType mediaType;
    private String fileBase64;

}
