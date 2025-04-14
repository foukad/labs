package fr.ttis.npp.ipera.myoauth2cachemanager.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuth2TokenService {

    // Stocke les informations d'expiration des tokens
    private final Map<String, Long> tokenExpiryMap = new HashMap<>();

    /**
     * Récupère un token pour le provider donné. Si le token est expiré, il est régénéré.
     */
    @Cacheable(value = "tokenCache", key = "#provider")
    public String getToken(String provider) {
        String token = checkCacheForAccessToken(provider);

        if (isTokenExpired(provider)) {
            token = regenerateAccessToken(provider);
            updateCacheWithNewToken(provider, token);
        }

        return token;
    }

    /**
     * Vérifie si le token d'un provider est expiré.
     */
    public boolean isTokenExpired(String provider) {
        Long expiryTime = tokenExpiryMap.get(provider);
        return expiryTime == null || Instant.now().isAfter(Instant.ofEpochSecond(expiryTime));
    }

    /**
     * Régénère un nouveau token via le provider OAuth2.
     */
    public String regenerateAccessToken(String provider) {
        System.out.println("Régénération du token pour le provider : " + provider);
        String newToken = callOAuthProviderForNewToken(provider);

        // Stocker l'heure d'expiration (1 heure dans cet exemple)
        tokenExpiryMap.put(provider, Instant.now().plusSeconds(3600).getEpochSecond());

        return newToken;
    }

    /**
     * Met à jour le cache avec un nouveau token pour le provider donné.
     */
    @CachePut(value = "tokenCache", key = "#provider")
    public String updateCacheWithNewToken(String provider, String token) {
        System.out.println("Mise à jour du cache pour le provider: " + provider);
        return token;
    }

    /**
     * Simule la vérification dans le cache si un token existe déjà.
     */
    private String checkCacheForAccessToken(String provider) {
        // Logique pour vérifier si un token existe dans le cache
        return "existing_access_token";  // Simulation d'un token
    }

    /**
     * Simule l'appel au provider OAuth2 pour obtenir un nouveau token.
     */
    private String callOAuthProviderForNewToken(String provider) {
        // Logique pour obtenir un nouveau token du provider OAuth2
        return "new_access_token_example";
    }
}
