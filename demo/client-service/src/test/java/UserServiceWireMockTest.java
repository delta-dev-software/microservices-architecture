
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceWireMockTest {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            //.options(WireMock.options("")
             //       .port(8080)) // Use port 8080 for WireMock
            .build();

    @Autowired
    private RestTemplate restTemplate;

    private final String externalServiceUrl = "http://localhost:8080/external-service";

    @BeforeEach
    public void setupWireMock() {
        // Simulate the external service's response
        wireMockServer.stubFor(get(WireMock.urlEqualTo("/external-service"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\":\"Hello from the external service\"}")
                        .withStatus(200)));
    }

    @Test
    public void testGetExternalServiceData() {
        // Use RestTemplate to call the mocked external service
        ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl, String.class);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"message\":\"Hello from the external service\"}", response.getBody());
    }
}
