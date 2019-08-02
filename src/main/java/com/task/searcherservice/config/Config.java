package com.task.searcherservice.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import lombok.NoArgsConstructor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
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
    public WebClient.Builder webClient() {
        return WebClient.builder()
                .defaultHeaders((h) -> httpHeaders());
    }

    /**
     * Bean for multiple rest calls.
     * @return RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(customHttpClient());
        final RestTemplate template = new RestTemplate(requestFactory);

        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        template.getMessageConverters().add(1, mappingJacksonHttpMessageConverter());
        template.getMessageConverters().add(2, new ByteArrayHttpMessageConverter());

        return template;
    }

    /**
     * Jackson Http converter.
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        converter.setObjectMapper(mapper());

        return converter;
    }

    /**
     * Object mapper Jackson binding instance.
     * @return ObjectMapper.
     */
    public static ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    /**
     * HttpHeaders instance.
     * @return HttpHeaders.
     */
    @Bean
    public HttpHeaders httpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    /**
     * Http client.
     * @return HttpClient http client.
     */
    @Bean
    public CloseableHttpClient customHttpClient() {
        try {
            final SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .build();
            final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);

            final Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();

            final PoolingHttpClientConnectionManager httpClientConnectionManager =
                    new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);

            return HttpClients.custom()
                    .setConnectionManager(httpClientConnectionManager)
                    .setUserAgent("Searcher Client")
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
