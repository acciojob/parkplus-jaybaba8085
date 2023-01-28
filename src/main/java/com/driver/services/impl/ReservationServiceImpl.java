package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;


    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        if(! userRepository3.findById(userId).isPresent())
        {
            throw new Exception("Cannot make reservation");
        }
        if(! parkingLotRepository3.findById(parkingLotId).isPresent())
        {
            throw new Exception("Cannot make reservation");
        }


        User user=userRepository3.findById(userId).get();

        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();

        List<Spot>spotList=parkingLot.getSpotList();

        boolean spotAvailable = false;
        for (Spot spot : spotList)
        {
            if (!spot.getOccupied())
            {
                spotAvailable = true;
                break;
            }
        }
        if (!spotAvailable)  throw new Exception("Cannot make reservation");


        Spot appropriateSpot =findAppropriateSpot(spotList,numberOfWheels);

        List<Reservation> reservationList;

        if( appropriateSpot != null)    reservationList = appropriateSpot.getReservationList();

        else     throw new Exception("Cannot make reservation");

        appropriateSpot.setOccupied(true);

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(appropriateSpot);
        reservation.setUser(user);

         appropriateSpot.getReservationList().add(reservation);
         user.getReservationList().add(reservation);


         userRepository3.save(user);
         spotRepository3.save(appropriateSpot);

         return reservation;
    }

    private Spot findAppropriateSpot(List<Spot> spotList, Integer numberOfWheels) {

        boolean   spotFound   = false;
        Integer maxPrice = Integer.MAX_VALUE;

        Spot newSpot = new Spot();
        if(numberOfWheels>4) newSpot.setSpotType(SpotType.FOUR_WHEELER);
        else if(numberOfWheels>2) newSpot.setSpotType(SpotType.TWO_WHEELER);
        else  newSpot.setSpotType(SpotType.OTHERS);

        for (Spot spot : spotList){
            if(!spot.isOccupied() && spot.getSpotType().equals(newSpot.getSpotType()))
            {
                spotFound=true;
                newSpot=spot;
                return  spot;
            }
        }
        for (Spot spot : spotList){
            if(!spot.isOccupied() &&  numberOfWheels>4 && spot.getSpotType().equals(SpotType.OTHERS))
            {
                spotFound=true;
                newSpot=spot;
                return  spot;
            }
        }
        for (Spot spot : spotList){
            if(!spot.isOccupied() &&  numberOfWheels>2 && spot.getSpotType().equals(SpotType.FOUR_WHEELER))
            {
                spotFound=true;
                newSpot=spot;
                return  spot;
            }
        }
        for (Spot spot : spotList){
            if(!spot.isOccupied() &&  numberOfWheels<=2 && spot.getSpotType().equals(SpotType.TWO_WHEELER))
            {
                spotFound=true;
                newSpot=spot;
                return  spot;
            }
        }
        return spotFound ? newSpot : null;
    }
}
