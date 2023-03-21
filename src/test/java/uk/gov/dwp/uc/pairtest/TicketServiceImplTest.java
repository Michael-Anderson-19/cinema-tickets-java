package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;

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
    public void given_ValidAccountIDAndValidTicketRequests_When_callingPurchaseTickets_Then_ReserveSeatsAndMakePaymentAreCalled(){
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);

        ticketService.purchaseTickets(VALID_ACCOUNT_ID, adultTicket, childTicket, infantTicket);

        Mockito.verify(seatReservationService, Mockito.times(1)).reserveSeat(any(Long.class), anyInt());
        Mockito.verify(ticketPaymentService, Mockito.times(1)).makePayment(any(Long.class), anyInt());
    }

    @Test
    public void given_InValidAccountID_When_callingPurchaseTickets_Then_ThrowException(){
        TicketTypeRequest ticket1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);

        thrown.expect(InvalidPurchaseException.class);
        thrown.expectMessage("Invalid account"); //TODO change
        ticketService.purchaseTickets(INVALID_ACCOUNT_ID, ticket1);
    }

    @Test
    public void given_TicketRequestWithNoTickets_When_CallingPurchaseTickets_Then_ThrowException(){
        TicketTypeRequest ticket1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);

        thrown.expect(InvalidPurchaseException.class);
        thrown.expectMessage("Invalid ticket request"); //TODO change
        ticketService.purchaseTickets(VALID_ACCOUNT_ID, ticket1);
    }

    @Test
    public void given_21TotalTickets_When_CallingPurchaseTickets_Then_ThrowException() {
        TicketTypeRequest ticket1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);

        thrown.expect(InvalidPurchaseException.class);
        thrown.expectMessage("Invalid ticket request");

        ticketService.purchaseTickets(VALID_ACCOUNT_ID, ticket1);
    }

    @Test
    public void given_TicketRequestsWithNoAdultTicket_When_CallingPurchaseTickets_Then_ThrowException(){
        TicketTypeRequest childTickets = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest infantTickets = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        thrown.expect(InvalidPurchaseException.class);
        thrown.expectMessage("Invalid ticket request - No adult is present"); //TODO implement

        ticketService.purchaseTickets(VALID_ACCOUNT_ID, childTickets,infantTickets);
    }

    @Test
    public void given_InfantAndChildAndAnAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreasePrice(){
        TicketTypeRequest adultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5);
        TicketTypeRequest childTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest infantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);

        final int predictedCost = TicketServiceImpl.ADULT_TICKET_COST * adultTicketRequest.getNoOfTickets()
                + TicketServiceImpl.CHILD_TICKET_COST * childTicketRequest.getNoOfTickets();

        ticketService.purchaseTickets(VALID_ACCOUNT_ID, adultTicketRequest, childTicketRequest, infantTicketRequest);

        Mockito.verify(ticketPaymentService, Mockito.times(1)).makePayment(eq(VALID_ACCOUNT_ID), integerCaptor.capture());

        int actualCost = integerCaptor.getValue();
        assertEquals(predictedCost,actualCost);
    }

    @Test
    public void given_InfantAndChildAndAdultTicketRequests_When_CallingPurchaseTickets_Then_InfantTicketsDoNotIncreaseTotalSeatCount() {
        TicketTypeRequest adultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3);
        TicketTypeRequest childTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 6);
        TicketTypeRequest infantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);

        final int predictedSeats = adultTicketRequest.getNoOfTickets() + childTicketRequest.getNoOfTickets();

        ticketService.purchaseTickets(VALID_ACCOUNT_ID, adultTicketRequest, childTicketRequest, infantTicketRequest);

        Mockito.verify(ticketPaymentService, Mockito.times(1)).makePayment(eq(VALID_ACCOUNT_ID), anyInt());
        Mockito.verify(seatReservationService, Mockito.times(1)).reserveSeat(eq(VALID_ACCOUNT_ID), integerCaptor.capture());

        final int actualSeatCount = integerCaptor.getValue();
        assertEquals(predictedSeats,actualSeatCount);
    }
}