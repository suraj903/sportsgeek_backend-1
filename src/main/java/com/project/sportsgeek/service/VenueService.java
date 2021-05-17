package com.project.sportsgeek.service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Venue;
import com.project.sportsgeek.repository.venuerepo.VenueRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VenueService {
    @Autowired
    @Qualifier(value = "venueRepo")
    VenueRepository venueRepository;

    public Result<List<Venue>> findAllVenue() {
        List<Venue> VenueList = venueRepository.findAllVenue();
        return new Result<>(200,"Venue Details Retrieved Successfully",VenueList);
    }

    public Result<Venue> findVenueById(int venueId) throws Exception {
        Venue venue = venueRepository.findVenueById(venueId);
        if (venue != null) {
            return new Result<>(200,"Venue Details Retrieved Successfully", venue);
        }
        else {
            throw new ResultException((new Result<>(404,"No Venue's found,please try again","Venue with id=('"+ venueId +"') not found")));
        }
    }

    public Result<Venue> addVenue(Venue Venue) throws Exception {
        int venueId = venueRepository.addVenue(Venue);
        Venue.setVenueId(venueId);
        if (venueId > 0) {
            return new Result<>(201,"Venue Details Added Successfully",Venue);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(Venue.hashCode(), "unable to add the given Venue")))));
    }

    public Result<Venue> updateVenue(int venueId, Venue Venue) throws Exception {
        if (venueRepository.updateVenue(venueId,Venue)) {
            return new Result<>(200,"Venue Details Updated Successfully",Venue);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Venue details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(Venue.hashCode(), "given VenueId('"+venueId+"') does not exists")))));
    }

    public Result<Integer> deleteVenue(int venueId) throws Exception{
        if (venueRepository.deleteVenue(venueId)) {
            return new Result<>(200,"Venue Deleted Successfully");
        }
        else {
            throw new ResultException((new Result<>(404,"No Venue's found to delete,please try again","Venue with id=('"+ venueId +"') not found")));
        }
    }
}
