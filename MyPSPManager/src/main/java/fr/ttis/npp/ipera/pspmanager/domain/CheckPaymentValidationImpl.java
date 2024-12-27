package ff.transactis.sip.npp.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class CheckPaymentValidationImpl implements ICheckPaymentValidation {

    private final IPaymentMeanRepository paymentMeanRepository;
    private final IPaymentSourceRepository paymentSourceRepository;
    private final IConsumerRepository consumerRepository;
    private final EventService eventService;

    public CheckPaymentValidationImpl(IPaymentMeanRepository paymentMeanRepository,
                                       IPaymentSourceRepository paymentSourceRepository,
                                       IConsumerRepository consumerRepository,
                                       EventService eventService) {
        this.paymentMeanRepository = paymentMeanRepository;
        this.paymentSourceRepository = paymentSourceRepository;
        this.consumerRepository = consumerRepository;
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<PaymentValidationResponse> execute(PaymentValidationRequest request, String requestId) {
        try {
            PaymentValidationData validationData = validateAndRetrieveData(request);

            // Save event asynchronously
            saveEventAsync(requestId, validationData);

            // Return successful response
            return ResponseEntity.ok(
                PaymentValidationResponse.builder()
                    .allowed(true)
                    .beneficiaryName(validationData.getBeneficiaryName())
                    .build()
            );

        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                PaymentValidationResponse.builder()
                    .allowed(false)
                    .errorMessage(e.getMessage())
                    .build()
            );
        }
    }

    private PaymentValidationData validateAndRetrieveData(PaymentValidationRequest request) {
        // Validate Payment Mean
        PaymentMean paymentMean = paymentMeanRepository.findById(request.getOriginatorPaymentMeansId())
            .orElseThrow(() -> new ValidationException("Payment Means not found"));

        // Validate Payment Source
        PaymentSource paymentSource = paymentSourceRepository
            .findByPaySrcEpiAndWalletEpi(paymentMean.getPaymentSourceEpi(), paymentMean.getWalletEpi())
            .orElseThrow(() -> new ValidationException("Payment Source not found"));

        // Validate Consumer
        Consumer consumer = consumerRepository.findByIdConsPsp(paymentSource.getIdConsPsp())
            .orElseThrow(() -> new ValidationException("Consumer not found"));

        return new PaymentValidationData(
            consumer.getFirstName() + " " + consumer.getLastName(),
            paymentMean.getWalletEpi(),
            paymentMean.getPaymentSourceEpi()
        );
    }

    private Mono<Void> saveEventAsync(String requestId, PaymentValidationData data) {
        return Mono.fromRunnable(() -> {
            EventHistory event = EventHistory.builder()
                .idEvt("eventId")
                .requestId(requestId)
                .consumerId(data.getConsumerId())
                .status("SUCCESS")
                .build();

            eventService.saveEvent(event);
        }).then();
    }

    // DTO to hold validation data
    private static class PaymentValidationData {
        private final String beneficiaryName;
        private final String walletId;
        private final String paymentSource;

        public PaymentValidationData(String beneficiaryName, String walletId, String paymentSource) {
            this.beneficiaryName = beneficiaryName;
            this.walletId = walletId;
            this.paymentSource = paymentSource;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public String getWalletId() {
            return walletId;
        }

        public String getPaymentSource() {
            return paymentSource;
        }
    }
}
