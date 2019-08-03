package com.task.searcherservice.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alexander Shakhov
 */
@Configuration
@NoArgsConstructor
public class Config {

    /**
     * Bean for reactive calls.
     * @return WebClient.Builder.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    /**
     * Object mapper Jackson binding instance.
     * @return ObjectMapper.
     */
    @Bean
    public ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }
}
