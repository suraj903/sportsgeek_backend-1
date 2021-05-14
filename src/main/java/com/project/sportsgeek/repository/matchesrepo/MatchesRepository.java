package com.project.sportsgeek.repository.matchesrepo;


import com.project.sportsgeek.model.Matches;
import com.project.sportsgeek.model.MatchesWithVenue;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository(value = "matchesRepo")
public interface MatchesRepository {

    List<MatchesWithVenue> findAllMatches(int tournamentId) throws Exception;

    List<MatchesWithVenue> findMatchesById(int id, int tournamentId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByTournament(int id) throws Exception;

    List<MatchesWithVenue> findAllMatchesByVenue(int id) throws Exception;

    List<MatchesWithVenue> findAllMatchesByTeam(int id) throws Exception;

    List<MatchesWithVenue> findAllMatchesByPreviousDateAndResultStatus(int tournamentId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByMinimumBet(int minBet) throws Exception;

    int addMatch(Matches matches) throws Exception;

    boolean updateMatch(int id, Matches matches) throws Exception;

    boolean updateMatchWinningTeam(int matchId, int ResultStatus, int winningTeamId) throws Exception;

    int updateMatchVenue(int id,int venueId) throws Exception;

    int updateResultStatus(int id, boolean status) throws Exception;

    int updateMinimumBet(int matchId, int minBet) throws Exception;

    int updateMatchScheduleDate(int id, Timestamp date) throws Exception;

    int deleteMatches(int id) throws Exception;

}
