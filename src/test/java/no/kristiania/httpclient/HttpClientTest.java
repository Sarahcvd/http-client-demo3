package no.kristiania.httpclient;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    void shouldReadSuccessStatusCode() throws IOException {
        HttpClient httpClient = createEchoRequest("/echo");
        assertEquals(200, httpClient.getStatusCode());
    }

    @Test
    void shouldReadFailureStatusCode() throws IOException {
        HttpClient httpClient = createEchoRequest("/echo?status=404");
        assertEquals(404, httpClient.getStatusCode());
    }

    @Test
    void shouldReturnResponseHeaders() throws IOException {
        HttpClient httpClient = createEchoRequest("/echo?body=Kristiania");
        assertEquals("10", httpClient.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnResponseBody() throws IOException {
        HttpClient httpClient = createEchoRequest("/echo?body=Kristiania");
        assertEquals("Kristiania", httpClient.getResponseBody());
    }

    private HttpClient createEchoRequest(String requestTarget) throws IOException {
        return new HttpClient("urlecho.appspot.com", 80, requestTarget);
    }

}