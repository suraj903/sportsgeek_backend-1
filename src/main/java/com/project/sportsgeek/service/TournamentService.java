package com.project.sportsgeek.service;


import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Tournament;
import com.project.sportsgeek.repository.tournamentrepo.TournamentRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    @Qualifier("tournamentRepo")
    TournamentRepository tournamentRepository;

    public Result<List<Tournament>> findAllTournament() {
        List<Tournament> tournamentList = tournamentRepository.findAllTournament();
        return new Result<>(200,"Tournament Details Retrieved Successfully",tournamentList);
    }

    public Result<Tournament> findTournamentById(int id) throws Exception{
        List<Tournament> tournamentList = tournamentRepository.findTournamentById(id);
        if (tournamentList.size() > 0) {
            return new Result<>(200,"Tournament Details Retrieved Successfully", tournamentList.get(0));
        }
        else {
            throw new ResultException((new Result<>(404,"No Tournament's found,please try again","Tournament with id=('"+ id +"') not found")));
        }
    }

    public Result<Tournament> addTournament(Tournament tournament) throws Exception {
        int id = tournamentRepository.addTournament(tournament);
        tournament.setTournamentId(id);
        if (id > 0) {
            return new Result<>(201,"Tournament Added Successfully",tournament);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(tournament.hashCode(), "unable to add the given Tournament")))));
    }
    public Result<Tournament> updateTournament(int id, Tournament tournament) throws Exception {
        if (tournamentRepository.updateTournament(id,tournament)) {
            return new Result<>(201,"Tournament Updated Successfully",tournament);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given tournament details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(tournament.hashCode(), "given tournamentId('"+id+"') does not exists")))));
    }
    public Result<String> updateActiveTournament(int id) throws Exception {

        if(tournamentRepository.deactivateTournament())
        {
            if (tournamentRepository.updateActiveTournament(id)) {
                return new Result<>(201,"Tournament Activated Successfully");
            }
            else
            {
                return new Result<>(404,"No Tournament's found to Activate ,please try again");
            }
        }
        else
        {
            return new Result<>(404,"Deactivation Failed of tournament ,please try again");
        }
    }
    public Result<Integer> deleteTournament(int id) throws Exception{
        int result = tournamentRepository.deleteTournament(id);
        if (result > 0) {
            return new Result<>(200,"Tournament Deleted Successfully",result);
        }
        throw new ResultException((new Result<>(404,"No Tournament's found to delete ,please try again","Tournament with id=('"+ id +"') not found")));
    }
}
