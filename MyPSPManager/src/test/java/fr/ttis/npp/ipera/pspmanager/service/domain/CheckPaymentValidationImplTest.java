import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckPaymentValidationImplTest {

    @Mock
    private IPaymentMeanRepository paymentMeanRepository;

    @Mock
    private IPaymentSourceRepository paymentSourceRepository;

    @Mock
    private IConsumerRepository consumerRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private CheckPaymentValidationImpl checkPaymentValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_ShouldReturnSuccessResponse_WhenDataIsValid() {
        // Arrange
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId("validPaymentMeanId");

        PaymentMean paymentMean = new PaymentMean();
        paymentMean.setWalletEpi("wallet123");
        paymentMean.setPaymentSourceEpi("source123");

        PaymentSource paymentSource = new PaymentSource();
        paymentSource.setIdConsPsp("consumer123");

        Consumer consumer = new Consumer();
        consumer.setFirstName("John");
        consumer.setLastName("Doe");

        when(paymentMeanRepository.findById("validPaymentMeanId")).thenReturn(Optional.of(paymentMean));
        when(paymentSourceRepository.findByPaySrcEpiAndWalletEpi("source123", "wallet123")).thenReturn(Optional.of(paymentSource));
        when(consumerRepository.findByIdConsPsp("consumer123")).thenReturn(Optional.of(consumer));

        // Act
        ResponseEntity<PaymentValidationResponse> response = checkPaymentValidation.execute(request, "request123");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isAllowed());
        assertEquals("John Doe", response.getBody().getBeneficiaryName());

        verify(paymentMeanRepository, times(1)).findById("validPaymentMeanId");
        verify(paymentSourceRepository, times(1)).findByPaySrcEpiAndWalletEpi("source123", "wallet123");
        verify(consumerRepository, times(1)).findByIdConsPsp("consumer123");
    }

    @Test
    void execute_ShouldReturnErrorResponse_WhenPaymentMeanNotFound() {
        // Arrange
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId("invalidPaymentMeanId");

        when(paymentMeanRepository.findById("invalidPaymentMeanId")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PaymentValidationResponse> response = checkPaymentValidation.execute(request, "request123");

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isAllowed());
        assertEquals("Payment Means not found", response.getBody().getErrorMessage());

        verify(paymentMeanRepository, times(1)).findById("invalidPaymentMeanId");
        verifyNoInteractions(paymentSourceRepository);
        verifyNoInteractions(consumerRepository);
    }

    @Test
    void execute_ShouldReturnErrorResponse_WhenPaymentSourceNotFound() {
        // Arrange
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId("validPaymentMeanId");

        PaymentMean paymentMean = new PaymentMean();
        paymentMean.setWalletEpi("wallet123");
        paymentMean.setPaymentSourceEpi("source123");

        when(paymentMeanRepository.findById("validPaymentMeanId")).thenReturn(Optional.of(paymentMean));
        when(paymentSourceRepository.findByPaySrcEpiAndWalletEpi("source123", "wallet123")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PaymentValidationResponse> response = checkPaymentValidation.execute(request, "request123");

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isAllowed());
        assertEquals("Payment Source not found", response.getBody().getErrorMessage());

        verify(paymentMeanRepository, times(1)).findById("validPaymentMeanId");
        verify(paymentSourceRepository, times(1)).findByPaySrcEpiAndWalletEpi("source123", "wallet123");
        verifyNoInteractions(consumerRepository);
    }

    @Test
    void saveEventAsync_ShouldCompleteSuccessfully() {
        // Arrange
        doNothing().when(eventService).saveEvent(any(EventHistory.class));

        PaymentValidationData validationData = new PaymentValidationData("John Doe", "wallet123", "source123");

        // Act
        Mono<Void> result = checkPaymentValidation.saveEventAsync("request123", validationData);

        // Assert
        StepVerifier.create(result)
            .verifyComplete();

        verify(eventService, times(1)).saveEvent(any(EventHistory.class));
    }
}
