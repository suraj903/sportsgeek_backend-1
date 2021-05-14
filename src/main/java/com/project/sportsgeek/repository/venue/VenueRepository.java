package com.project.sportsgeek.repository.venue;

import com.project.sportsgeek.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "venueRepo")
public interface VenueRepository {

    List<Venue> findAllVenue();

    List<Venue> findVenueById(int id) throws Exception;

    int addVenue(Venue venue) throws Exception;

    boolean updateVenue(int id, Venue venue) throws Exception;

    int deleteVenue(int id) throws Exception;

}
