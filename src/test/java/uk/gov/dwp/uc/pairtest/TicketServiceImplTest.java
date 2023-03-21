package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import static org.junit.Assert.*;

public class TicketServiceImplTest {

    @Mock
    private TicketPaymentService ticketPaymentService;
    @Mock
    private SeatReservationService seatReservationService;
    @InjectMocks
    private TicketServiceImpl ticketService;
    @Captor
    ArgumentCaptor<Integer> integerCaptor;
    @Rule
    private final ExpectedException thrown = ExpectedException.none();

    private final Long VALID_ACCOUNT_ID = 1L;
    private final Long INVALID_ACCOUNT_ID = -1L;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void given_ValidAccountIDAndValidTicketRequests_When_callingPurchaseTickets_Then_ReserveSeatsAndMakePaymentAreCalled() {

    }

    @Test
    public void given_InValidAccountID_When_callingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_TicketRequestWithNoTickets_When_CallingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_21TotalTickets_When_CallingPurchaseTickets_Then_ThrowException() {}

    @Test
    public void given_TicketRequestsWithNoAdult_When_CallingPurchaseTickets_Then_ThrowException(){}

    @Test
    public void given_InfantAndChildAndAnAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreasePrice(){}

    @Test
    public void given_InfantAndChildAndAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreaseTotalSeatCount() {}





}