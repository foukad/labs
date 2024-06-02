package fr.ttis.npp.handler;

import fr.ttis.npp.exception.HmacGenerationException;
import fr.ttis.npp.exception.HmacVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testHandleHmacGenerationException() throws Exception {
        mockMvc.perform(get("/test/hmacGenerationException"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorCode\":\"HMAC_GENERATION_ERROR\",\"errorMessage\":\"Error generating HMAC\"}"));
    }

    @Test
    public void testHandleHmacVerificationException() throws Exception {
        mockMvc.perform(get("/test/hmacVerificationException"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorCode\":\"HMAC_VERIFICATION_ERROR\",\"errorMessage\":\"Error verifying HMAC\"}"));
    }

    @RestController
    public static class TestController {

        @GetMapping("/test/hmacGenerationException")
        public void hmacGenerationException() {
            throw new HmacGenerationException("Error generating HMAC", new Exception());
        }

        @GetMapping("/test/hmacVerificationException")
        public void hmacVerificationException() {
            throw new HmacVerificationException("Error verifying HMAC", new Exception());
        }
    }
}
