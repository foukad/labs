package fr.ttis.npp.controller;

import fr.ttis.npp.exception.HmacGenerationException;
import fr.ttis.npp.exception.HmacVerificationException;
import fr.ttis.npp.handler.GlobalExceptionHandler;
import fr.ttis.npp.utils.HmacUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HmacControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private HmacController hmacController;

    @Mock
    private HmacUtils hmacUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hmacController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGenerateHmac() throws Exception {
        String data = "testdata";
        String hmac = "generatedhmac";

        mockMvc.perform(post("/hmac/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isOk())
                .andExpect(content().string(hmac));
    }

    @Test
    public void testGenerateHmac_Exception() throws Exception {
        String data = "testdata";

        doThrow(new HmacGenerationException("Error generating HMAC", new Exception())).when(hmacUtils).generateHMAC(data, "your-secret-key");

        mockMvc.perform(post("/hmac/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorCode\":\"HMAC_GENERATION_ERROR\",\"errorMessage\":\"Error generating HMAC\"}"));
    }

    @Test
    public void testVerifyHmac() throws Exception {
        HmacController.HmacRequest request = new HmacController.HmacRequest();
        request.setData("testdata");
        request.setHmac("testhmac");

        mockMvc.perform(post("/hmac/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\":\"testdata\",\"hmac\":\"testhmac\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testVerifyHmac_Exception() throws Exception {
        HmacController.HmacRequest request = new HmacController.HmacRequest();
        request.setData("testdata");
        request.setHmac("testhmac");

        doThrow(new HmacVerificationException("Error verifying HMAC", new Exception())).when(hmacUtils).verifyHMAC(request.getData(), "your-secret-key", request.getHmac());

        mockMvc.perform(post("/hmac/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\":\"testdata\",\"hmac\":\"testhmac\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"errorCode\":\"HMAC_VERIFICATION_ERROR\",\"errorMessage\":\"Error verifying HMAC\"}"));
    }
}
