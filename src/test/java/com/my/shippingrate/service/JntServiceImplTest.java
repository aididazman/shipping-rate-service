package com.my.shippingrate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JntServiceImplTest {

    @Autowired
    @Qualifier("jntWebClient")
    private WebClient webClient;

    @Test
    void testWithHtmlSample() throws Exception {

        // Load your sample HTML from the resources folder
        HtmlLoader loader = new HtmlLoader();
        String html = loader.loadHtmlAsString("JNT_SAMPLE_HTML_RESPONSE.html");
        assertNotNull(html);

        JntServiceImpl service = new JntServiceImpl(webClient);
        List<Map<String, String>> result = service.extractDataFromResponse(html);

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
