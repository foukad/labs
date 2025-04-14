package fr.ttis.npp.ipera.myoauth2cachemanager.service;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.ehcache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OAuth2TokenServiceTest {

    @Mock
    private Cache<String, String> tokenCache;

    @InjectMocks
    private OAuth2TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetToken_WhenTokenNotExpired_ShouldReturnCachedToken() throws Exception {
        // Arrange
        String provider = "provider1";
        String cachedToken = "cached_token";
        when(tokenCache.get(provider)).thenReturn(cachedToken);
        mockJwtWithExpirationDate(cachedToken, futureDate());

        // Act
        String result = tokenService.getToken(provider);

        // Assert
        assertEquals(cachedToken, result);
        verify(tokenCache, never()).put(anyString(), anyString()); // No new token should be cached
    }

    @Test
    void testGetToken_WhenTokenExpired_ShouldRegenerateToken() throws Exception {
        // Arrange
        String provider = "provider1";
        String expiredToken = "expired_token";
        String newToken = "new_token";
        when(tokenCache.get(provider)).thenReturn(expiredToken);
        mockJwtWithExpirationDate(expiredToken, pastDate());
        when(tokenService.regenerateAccessToken(provider)).thenReturn(newToken);

        // Act
        String result = tokenService.getToken(provider);

        // Assert
        assertEquals(newToken, result);
        verify(tokenCache).put(provider, newToken); // Verify the new token was cached
    }

    @Test
    void testIsTokenExpired_WithExpiredToken_ShouldReturnTrue() throws Exception {
        // Arrange
        String token = "expired_token";
        mockJwtWithExpirationDate(token, pastDate());

        // Act
        boolean result = tokenService.isTokenExpired(token);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsTokenExpired_WithValidToken_ShouldReturnFalse() throws Exception {
        // Arrange
        String token = "valid_token";
        mockJwtWithExpirationDate(token, futureDate());

        // Act
        boolean result = tokenService.isTokenExpired(token);

        // Assert
        assertFalse(result);
    }

    // Helper method to mock a SignedJWT with a specific expiration date
    private void mockJwtWithExpirationDate(String token, Date expirationDate) throws ParseException {
        SignedJWT signedJWT = mock(SignedJWT.class);
        JWTClaimsSet claimsSet = mock(JWTClaimsSet.class);
        when(claimsSet.getExpirationTime()).thenReturn(expirationDate);
        when(signedJWT.getJWTClaimsSet()).thenReturn(claimsSet);
        when(SignedJWT.parse(token)).thenReturn(signedJWT);
    }

    // Helper method to return a future expiration date
    private Date futureDate() {
        return new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
    }

    // Helper method to return a past expiration date
    private Date pastDate() {
        return new Date(System.currentTimeMillis() - 3600000); // 1 hour ago
    }
}
