@ExtendWith(MockitoExtension.class)
class CheckPaymentValidationImplTest {

    @Mock
    private IPaymentMeanRepository iPaymentMeanRepository;

    @Mock
    private IPaymentSourceRepository iPaymentSourceRepository;

    @Mock
    private EventService eventService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CheckPaymentValidationImpl checkPaymentValidation;

    @Test
    void execute_ShouldReturnResponse_WhenAllDataIsValid() {
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId("123");

        PaymentMean paymentMean = new PaymentMean();
        paymentMean.setPaymentSourceEpi("sourceEpi");
        paymentMean.setWalletId("wallet123");

        PaymentSource paymentSource = new PaymentSource();
        paymentSource.setIban("FR7612345678901234567890189");

        when(iPaymentMeanRepository.findById("123")).thenReturn(Optional.of(paymentMean));
        when(iPaymentSourceRepository.findByPaySrcEpiAndWalletEpi("sourceEpi", "wallet123"))
                .thenReturn(Optional.of(paymentSource));
        when(notificationService.callNotifyPpPreliminaryFeasibilityCheck(any(), eq("REQ123"))).thenReturn(true);

        ResponseEntity<PaymentValidationResponse> response = checkPaymentValidation.execute(request, "REQ123");

        assertNotNull(response);
        assertTrue(response.getBody().isAllowed());
        assertEquals("FR7612345678901234567890189", response.getBody().getIbanAccountIdentifier());
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenPaymentMeanNotFound() {
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId("123");

        when(iPaymentMeanRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> checkPaymentValidation.execute(request, "REQ123"));
    }

    @Test
    void execute_ShouldThrowValidationException_WhenRequestIsInvalid() {
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setOriginatorPaymentMeansId(null);

        assertThrows(ValidationException.class, () -> checkPaymentValidation.execute(request, "REQ123"));
    }
}
