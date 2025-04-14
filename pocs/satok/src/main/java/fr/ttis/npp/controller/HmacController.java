package fr.ttis.npp.controller;

import fr.ttis.npp.exception.HmacGenerationException;
import fr.ttis.npp.exception.HmacVerificationException;
import fr.ttis.npp.utils.HmacUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hmac")
@Tag(name = "HMAC API", description = "API pour générer et vérifier des HMAC")
@Slf4j
public class HmacController {

    private static final String SECRET_KEY = "your-secret-key";

    @PostMapping("/generate")
    @Operation(summary = "Générer un HMAC", description = "ignore un HMAC pour les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HMAC généré avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la génération du HMAC", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public String generateHmac(@RequestBody String data) {
        try {
            String hmac = HmacUtils.generateHMAC(data, SECRET_KEY);
            log.info("Generated HMAC for data: {}", data);
            return hmac;
        } catch (Exception e) {
            log.error("Error generating HMAC for data: {}", data, e);
            throw new HmacGenerationException("Error generating HMAC", e);
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Générer un HMAC", description = "Génère un HMAC pour les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HMAC généré avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la génération du HMAC", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public boolean verifyHmac(@RequestBody HmacRequest request) {
        try {
            boolean isValid = HmacUtils.verifyHMAC(request.getData(), SECRET_KEY, request.getHmac());
            log.info("Verified HMAC for data: {}", request.getData());
            return isValid;
        } catch (Exception e) {
            log.error("Error verifying HMAC for data: {}", request.getData(), e);
            throw new HmacVerificationException("Error verifying HMAC", e);
        }
    }

    public static class HmacRequest {
        private String data;
        private String hmac;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getHmac() {
            return hmac;
        }

        public void setHmac(String hmac) {
            this.hmac = hmac;
        }
    }
}
