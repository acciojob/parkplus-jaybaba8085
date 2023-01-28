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
        List<Reservation>userReservationList= new ArrayList<>();
        userReservationList =user.getReservationList();

        ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        List<Spot>spotList=parkingLot.getSpotList();

        Spot appropriateSpot =findAppropriateSpot(spotList,numberOfWheels);
        List<Reservation> reservationList = appropriateSpot.getReservationList();

        int amount=0;
       if(appropriateSpot!=null)
       {
           appropriateSpot.setOccupied(true);
            amount =timeInHours*appropriateSpot.getPricePerHour();
           appropriateSpot.setPricePerHour(amount);
           spotList.add(appropriateSpot);
           parkingLot.setSpotList(spotList);
           parkingLotRepository3.save(parkingLot);
       }
       else
       {
           throw new Exception("Cannot make reservation");
       }

         Reservation reservation=new Reservation();

         reservation.setNumberOfHours(amount);


         appropriateSpot.getReservationList().add(reservation);


         userReservationList.add(reservation);


         userRepository3.save(user);
         reservationRepository3.save(reservation);
         return reservation;
    }

    private Spot findAppropriateSpot(List<Spot> spotList, Integer numberOfWheels) {

        boolean   spotFound   = false;
        Integer maxPrice = Integer.MAX_VALUE;

        Spot newSpot = new Spot();
        if(numberOfWheels==2) newSpot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels==4) newSpot.setSpotType(SpotType.FOUR_WHEELER);
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
            if(!spot.isOccupied() &&  numberOfWheels==2 && spot.getSpotType().equals(SpotType.FOUR_WHEELER))
            {
                spotFound=true;
                newSpot=spot;
                return  spot;
            }
        }
        return spotFound ? newSpot : null;
    }
}
