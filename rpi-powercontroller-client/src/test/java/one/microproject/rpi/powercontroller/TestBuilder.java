package one.microproject.rpi.powercontroller;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestBuilder {

    @Test
    void testClientBuilder() throws MalformedURLException {
        PowerControllerClient powerControllerClient = PowerControllerClientBuilder.builder()
                .baseUrl("http://localhost:8090")
                .withCredentials("client-001", "secret")
                .build();
        assertNotNull(powerControllerClient);
    }

    @Test
    void testReadClientBuilder() throws MalformedURLException {
        PowerControllerReadClient powerControllerReadClient = PowerControllerClientBuilder.builder()
                .baseUrl("http://localhost:8090")
                .withCredentials("client-001", "secret")
                .buildReadClient();
        assertNotNull(powerControllerReadClient);
    }

    @Test
    void testInvalidUrlClientBuilder() {
        assertThrows(MalformedURLException.class, () -> {
            PowerControllerClientBuilder.builder()
                    .baseUrl("invalid-url")
                    .withCredentials("client-001", "secret")
                    .buildReadClient();
        });
    }

    @Test
    void testInvalidCredentialsClientBuilder() {
        assertThrows(ClientException.class, () -> {
            PowerControllerClientBuilder.builder()
                    .baseUrl("http://localhost:8090")
                    .buildReadClient();
        });
    }

}