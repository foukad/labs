@Service
public class CheckPaymentValidationImpl implements ICheckPaymentValidation {

    private static final String NOT_FOUND = "not found";

    private final IPaymentMeanRepository iPaymentMeanRepository;
    private final IPaymentSourceRepository iPaymentSourceRepository;
    private final IConsumerRepository iConsumerRepository;
    private final EventService eventService;
    private final NotificationService notificationService;

    public CheckPaymentValidationImpl(IPaymentMeanRepository iPaymentMeanRepository,
                                      IPaymentSourceRepository iPaymentSourceRepository,
                                      IConsumerRepository iConsumerRepository,
                                      EventService eventService,
                                      NotificationService notificationService) {
        this.iPaymentMeanRepository = iPaymentMeanRepository;
        this.iPaymentSourceRepository = iPaymentSourceRepository;
        this.iConsumerRepository = iConsumerRepository;
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<PaymentValidationResponse> execute(PaymentValidationRequest request, String requestId) {
        validateRequest(request);

        PaymentData paymentData = fetchPaymentData(request);

        PaymentValidationResponse response = processPayment(paymentData, requestId);

        saveEvent(response, requestId);

        return ResponseEntity.ok(response);
    }

    private void validateRequest(PaymentValidationRequest request) {
        if (request == null || request.getOriginatorPaymentMeansId() == null) {
            throw new ValidationException("Payment means ID is mandatory");
        }
    }

    private PaymentData fetchPaymentData(PaymentValidationRequest request) {
        PaymentMean paymentMean = iPaymentMeanRepository.findById(request.getOriginatorPaymentMeansId())
                .orElseThrow(() -> new NotFoundException("Payment means not found"));

        PaymentSource paymentSource = iPaymentSourceRepository
                .findByPaySrcEpiAndWalletEpi(paymentMean.getPaymentSourceEpi(), paymentMean.getWalletId())
                .orElseThrow(() -> new NotFoundException("Payment source not found"));

        return new PaymentData(paymentMean, paymentSource);
    }

    private PaymentValidationResponse processPayment(PaymentData paymentData, String requestId) {
        boolean isAllowed = notificationService.callNotifyPpPreliminaryFeasibilityCheck(paymentData, requestId);

        return PaymentValidationResponse.builder()
                .allowed(isAllowed)
                .idConsPsp(paymentData.getPaymentMean().getIdConsPsp())
                .ibanAccountIdentifier(paymentData.getPaymentSource().getIban())
                .build();
    }

    private void saveEvent(PaymentValidationResponse response, String requestId) {
        EventHistory eventHistory = EventHistory.builder()
                .idEvt(response.getIdConsPsp())
                .statusLib(response.isAllowed() ? "ALLOWED" : "NOT_ALLOWED")
                .build();

        Mono.fromFuture(() -> eventService.saveEvent(eventHistory, requestId)).subscribe();
    }
}
