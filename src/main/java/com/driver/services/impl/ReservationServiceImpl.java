package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if (!userRepository3.findById(userId).isPresent()) {
            throw new Exception("Cannot make reservation");
        }
        if (!parkingLotRepository3.findById(parkingLotId).isPresent()) {
            throw new Exception("Cannot make reservation");
        }


        User user = userRepository3.findById(userId).get();

        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        List<Spot> spotList = parkingLot.getSpotList();

        boolean spotAvailable = false;
        for (Spot spot : spotList) {
            if (!spot.getOccupied()) {
                spotAvailable = true;
                break;
            }
        }
        if (!spotAvailable) throw new Exception("Cannot make reservation");


        Spot appropriateSpot = findAppropriateSpot(spotList, numberOfWheels, timeInHours);

        if (appropriateSpot == null)
        {
            throw new Exception("Cannot make reservation");
        }
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

    static  Integer minimumPrice = Integer.MAX_VALUE;
    private Spot findAppropriateSpot(List<Spot> spotList, Integer numberOfWheels, Integer timeInHours) {

        boolean checkForSpots = false;


        Spot newSpot = new Spot();

        Spot spotChosen = null;
        if (numberOfWheels > 4) newSpot.setSpotType(SpotType.FOUR_WHEELER);
        else if (numberOfWheels > 2) newSpot.setSpotType(SpotType.TWO_WHEELER);
        else newSpot.setSpotType(SpotType.OTHERS);

        for (Spot spot : spotList) {
            if (newSpot.getSpotType().equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS))
            {
                if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied())
                {
                    minimumPrice = spot.getPricePerHour() * timeInHours;
                    checkForSpots = true;
                    spotChosen = spot;
                }
            }
            else if (newSpot.getSpotType().equals(SpotType.FOUR_WHEELER)
                    && spot.getSpotType().equals(SpotType.OTHERS) ||
                    spot.getSpotType().equals(SpotType.FOUR_WHEELER))
            {
                if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied())
                {
                    minimumPrice = spot.getPricePerHour() * timeInHours;
                    checkForSpots = true;
                    spotChosen = spot;
                }
            }
            else if (newSpot.getSpotType().equals(SpotType.TWO_WHEELER)
                    && spot.getSpotType().equals(SpotType.OTHERS) ||
                    spot.getSpotType().equals(SpotType.FOUR_WHEELER) ||
                    spot.getSpotType().equals(SpotType.TWO_WHEELER))
            {
                if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                    minimumPrice = spot.getPricePerHour() * timeInHours;
                    checkForSpots = true;
                    spotChosen = spot;
                }
            }

        }
        return checkForSpots ? spotChosen :null;
    }
}
