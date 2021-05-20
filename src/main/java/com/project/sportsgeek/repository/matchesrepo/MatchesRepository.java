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

    List<MatchesWithVenue> findMatchesById(int matchId, int tournamentId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByTournament(int tournamentId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByVenue(int venueId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByTeam(int teamId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByPreviousDateAndResultStatus(int tournamentId) throws Exception;

    List<MatchesWithVenue> findAllMatchesByMinimumPoints(int minPoints) throws Exception;

    int addMatch(Matches matches) throws Exception;

    boolean updateMatch(int matchId, Matches matches) throws Exception;

    boolean updateMatchWinningTeam(int matchId, int ResultStatus, int winningTeamId) throws Exception;

    int updateMatchVenue(int matchId, int venueId) throws Exception;

    int updateResultStatus(int matchId, boolean status) throws Exception;

    int updateMinimumPoints(int matchId, int minPoints) throws Exception;

    int updateMatchScheduleDate(int matchId, Timestamp date) throws Exception;

    int deleteMatches(int matchId) throws Exception;

}
