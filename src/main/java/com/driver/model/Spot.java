package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "Spots")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(value = EnumType.STRING)
    private SpotType spotType;

    private int pricePerHour;

    private boolean isOccupied;

    @ManyToOne
    @JoinColumn
    private ParkingLot parkingLot;


    public Spot() {
    }

    public Spot(int id, SpotType spotType, int pricePerHour, boolean isOccupied) {
        this.id = id;
        this.spotType = spotType;
        this.pricePerHour = pricePerHour;
        this.isOccupied = isOccupied;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(SpotType spotType) {
        this.spotType = spotType;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
