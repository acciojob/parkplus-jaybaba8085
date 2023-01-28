package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
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


        Payment payment=new Payment();
        mode = mode.toUpperCase();

        Reservation reservation =reservationRepository2.findById(reservationId).get();

        if(amountSent<reservation.getNumberOfHours())
        {
            throw new Exception("Insufficient Amount");
        }
       else if ( mode.matches(String.valueOf(PaymentMode.CARD))
                || mode.matches(String.valueOf(PaymentMode.CASH))
                || mode.matches(String.valueOf(PaymentMode.UPI)))
        {
            throw new Exception("Payment mode not detected" );
        }
       else
       {
           payment.setPaymentCompleted(true);
           PaymentMode pm=  getPaymentMode(mode) ;
           payment.setPaymentMode(pm);

           paymentRepository2.save(payment);
           return payment;
       }
    }
    private PaymentMode getPaymentMode(String mode) {
        mode = mode.toUpperCase();

        if (mode.equals("CASH")){
            return PaymentMode.CASH;
        }
        if (mode.equals("CARD")){
            return PaymentMode.CARD;
        }
        return PaymentMode.UPI;
    }

}
