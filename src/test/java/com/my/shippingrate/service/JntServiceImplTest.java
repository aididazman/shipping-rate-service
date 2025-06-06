package com.my.shippingrate.service;

import com.my.shippingrate.service.jnt.JntServiceImpl;
import com.my.shippingrate.service.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JntServiceImplTest {

    @Autowired
    @Qualifier("jntWebClient")
    private WebClient webClient;

    @Autowired
    private RedisService redisService;

    @Test
    void testWithHtmlSample() throws Exception {

        // Load your sample HTML from the resources folder
        HtmlLoader loader = new HtmlLoader();
        String html = loader.loadHtmlAsString("JNT_SAMPLE_HTML_RESPONSE.html");

        JntServiceImpl service = new JntServiceImpl(webClient, redisService);
        String result = service.extractDataFromResponse(html);

    }

    private class HtmlLoader {
        public String loadHtmlAsString(String fileName) throws Exception {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("File not found: " + fileName);
                }
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

}
