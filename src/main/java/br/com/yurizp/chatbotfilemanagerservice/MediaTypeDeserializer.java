package br.com.yurizp.chatbotfilemanagerservice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.http.MediaType;

import java.io.IOException;

public class MediaTypeDeserializer extends StdDeserializer<MediaType> {
    public MediaTypeDeserializer() {
        super(MediaType.class);
    }
    @Override
    public MediaType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode tree = p.getCodec().readTree(p);
        return MediaType.valueOf(tree.textValue());
    }
}