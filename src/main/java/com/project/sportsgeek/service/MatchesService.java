package com.project.sportsgeek.service;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.Matches;
import com.project.sportsgeek.model.MatchesWithVenue;
import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.model.Tournament;
import com.project.sportsgeek.repository.matchesrepo.MatchesRepository;
import com.project.sportsgeek.repository.tournamentrepo.TournamentRepository;
import com.project.sportsgeek.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MatchesService {
    @Autowired
    @Qualifier("matchesRepo")
    MatchesRepository matchesRepository;

    @Autowired
    @Qualifier("tournamentRepo")
    TournamentRepository tournamentRepository;

    public Result<List<MatchesWithVenue>> findAllMatches() throws Exception {
        List<Tournament> tournaments = tournamentRepository.findTournamentByActive();
        if(tournaments.size() > 0)
        {
            List<MatchesWithVenue> matchesList = matchesRepository.findAllMatches(tournaments.get(0).getTournamentId());
            return new Result<>(200,"Matches Detail Retrieved Successfully",matchesList);
        }
        else
        {
            return new Result<>(404,"Unable to find the Active Tournaments");
        }
    }
    
    public Result<MatchesWithVenue> findMatchesById(int id) throws Exception {
        List<Tournament> tournaments = tournamentRepository.findTournamentByActive();
        if (tournaments.size() >0)
        {
            List<MatchesWithVenue> matchesList = matchesRepository.findMatchesById(id,tournaments.get(0).getTournamentId());
            if (matchesList.size() > 0) {
                return new Result<>(200,"Matches Detail Retrieved Successfully", matchesList.get(0));
            }
            else {
                throw new ResultException((new Result<>(404,"No Match's found,please try again","Match with id=('"+ id +"') not found")));
            }
        }
        else
        {
            throw new ResultException((new Result<>(404,"No Active Tournaments found,please try again")));
        }

    }
    public Result<List<MatchesWithVenue>> findMatchesByTournament(int id) throws Exception {
        List<MatchesWithVenue> matchesList = matchesRepository.findAllMatchesByTournament(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Matches Detail Retrieved Successfully", matchesList);
        }
        else {
            throw new ResultException((new Result<>(404,"No Match's found,please try again","Tournament with id=('"+ id +"') not found")));
        }
    }
    public Result<List<MatchesWithVenue>> findMatchesByVenue(int id) throws Exception {
        List<MatchesWithVenue> matchesList = matchesRepository.findAllMatchesByVenue(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Matches Detail Retrieved Successfully", matchesList);
        }
        else {
            throw new ResultException((new Result<>(404,"No Match's found,please try again","Venue with id=('"+ id +"') not found")));
        }
    }
    public Result<List<MatchesWithVenue>> findMatchesByTeam(int id) throws Exception {
        List<MatchesWithVenue> matchesList = matchesRepository.findAllMatchesByTeam(id);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Matches Detail Retrieved Successfully", matchesList);
        }
        else {
            throw new ResultException((new Result<>(404,"No Match's found,please try again","Team with id=('"+ id +"') not found")));
        }
    }
    public Result<List<MatchesWithVenue>> findMatchesByMinBet(int minBet) throws Exception {
        List<MatchesWithVenue> matchesList = matchesRepository.findAllMatchesByMinimumBet(minBet);
        if (matchesList.size() > 0) {
            return new Result<>(200,"Matches Detail Retrieved Successfully", matchesList);
        }
        else {
            throw new ResultException((new Result<>(404,"No Match's found,please try again","MinBet=('"+ minBet +"') not found")));
        }
    }
    public Result<Matches> addMatches(Matches matches) throws Exception {
        int id = matchesRepository.addMatch(matches);
        if (id > 0) {
            return new Result<>(201,"Matches Added Successfully",matches);
        }
        throw new ResultException(new Result<>(400, "Error!, please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(matches.hashCode(), "unable to add the given gender")))));
    }
    public Result<Matches> updateMatch(int id, Matches matches) throws Exception {
        if (matchesRepository.updateMatch(id, matches)) {
            return new Result<>(201,"Player Details Updated Successfully",matches);
        }
        throw new ResultException(new Result<>(400, "Unable to update the given Player details! Please try again!", new ArrayList<>(Arrays
                .asList(new Result.SportsGeekSystemError(matches.hashCode(), "given PlayerId('"+id+"') does not exists")))));
    }
    public Result<List<MatchesWithVenue>> findAllMatchesByPreviousDateAndResultStatus() throws Exception {
        List<Tournament> tournaments = tournamentRepository.findTournamentByActive();
        if(tournaments.size() >0)
        {
            List<MatchesWithVenue> matchesList = matchesRepository.findAllMatchesByPreviousDateAndResultStatus(tournaments.get(0).getTournamentId());
            return new Result<>(200,matchesList);
        }
        else
        {
            return new Result<>(404,"Unable to find the Active Tournaments");
        }
    }
    public Result<String> updateMatchWinningTeam(int matchId, int ResultStatus, int winningTeamId) throws Exception {
            if(matchesRepository.updateMatchWinningTeam(matchId,ResultStatus,winningTeamId)== true) {
                return new Result<>(201,"Matches Updated Successfully!!");
            }else {
                return new Result<>(400,"Failed to Update Match Result!!");
            }
    }
    public Result<String> updateMinimumBet(int matchId,int minBet) throws Exception {
        int result = matchesRepository.updateMinimumBet(matchId,minBet);
        if(result > 0) {
            return new Result<>(201, "Successfully Updated Minimum Bet for Match");
        }
        else {
            return new Result<>(500, "Internal Server error!, Unable to update the Minimum Bet");
        }
    }
    public Result<String> updateMatchVenue(int matchId,int venueId) throws Exception {
        int result = matchesRepository.updateMatchVenue(matchId, venueId);
        if(result > 0) {
            return new Result<>(201, "Successfully Updated Venue for Match");
        }
        else {
            return new Result<>(500, "Internal Server error!, Unable to update the Venue");
        }
    }
    public Result<String> updateMatchResultStatus(int matchId,boolean status) throws Exception {
        int result = matchesRepository.updateResultStatus(matchId, status);
        if(result > 0) {
            return new Result<>(201, "Successfully Updated Result Status for Match");
        }
        else {
            return new Result<>(500, "Internal Server error!, Unable to update the Result Status");
        }
    }
    public Result<String> updateMatchStartDateTime(int matchId, Timestamp date) throws Exception {
        int result = matchesRepository.updateMatchScheduleDate(matchId, date);
        if(result > 0) {
            return new Result<>(201, "Successfully Updated Match StartDateTime");
        }
        else {
            return new Result<>(500, "Internal Server error!, Unable to update the Match StartDateTime");
        }
    }
    public Result<Integer> deleteMatch(int id) throws Exception{
        int data = matchesRepository.deleteMatches(id);
        if (data > 0) {
            return new Result<>(200,"Match Deleted Successfully",data);
        }
        else {
            throw new ResultException((new Result<>(404,"No Match found to delete,please try again","Match with id=('"+ id +"') not found")));
        }
    }
}
