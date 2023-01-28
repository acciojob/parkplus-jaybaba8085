package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        //Attempt a payment of amountSent for reservationId using the given mode ("cASh", "card", or "upi")
        //If the amountSent is less than bill, throw "Insufficient Amount" exception, otherwise update payment attributes
        //If the mode contains a string other than "cash", "card", or "upi" (any character in uppercase or lowercase), throw "Payment mode not detected" exception.
        //Note that the reservationId always exists


        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Spot spot=reservation.getSpot();
        mode =mode.toUpperCase();


        int bill = reservation.getNumberOfHours()*spot.getPricePerHour();

        if(amountSent<bill)
        {
            throw new Exception("Insufficient Amount");
        }
        if(  ! mode.matches(String.valueOf(PaymentMode.CARD))
                && ! mode.matches(String.valueOf(PaymentMode.CASH))
                && ! mode.matches(String.valueOf(PaymentMode.UPI)))
        {
            throw new Exception("Payment mode not detected");
        }

            Payment payment=new Payment();

            PaymentMode pm =getPaymentMode(mode);
            payment.setPaymentMode(pm);

            payment.setPaymentCompleted(true);
            payment.setReservation(reservation);

            reservation.setPayment(payment);

            reservationRepository2.save(reservation);

            return payment;
        }
    private PaymentMode getPaymentMode(String mode) {

        if (mode.equals("CASH")){
            return PaymentMode.CASH;
        }
        if (mode.equals("CARD")){
            return PaymentMode.CARD;
        }
        return PaymentMode.UPI;
    }

}
