package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Payments")
public class Payment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isPaymentCompleted;


    @Enumerated(value = EnumType.STRING)
    private PaymentMode paymentMode;


    @OneToMany(mappedBy = "payment",cascade = CascadeType.ALL)
    List<Reservation>reservationList;

    public Payment() {
    }

    public Payment(int id, boolean isPaymentCompleted, PaymentMode paymentMode) {
        this.id = id;
        this.isPaymentCompleted = isPaymentCompleted;
        this.paymentMode = paymentMode;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaymentCompleted() {
        return isPaymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        isPaymentCompleted = paymentCompleted;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
