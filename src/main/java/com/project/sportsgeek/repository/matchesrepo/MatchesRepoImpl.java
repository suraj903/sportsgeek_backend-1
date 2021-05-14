package com.project.sportsgeek.repository.matchesrepo;

import com.project.sportsgeek.mapper.MatchesRowMapper;
import com.project.sportsgeek.mapper.TournamentRowMapper;
import com.project.sportsgeek.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository(value = "matchesRepo")
public class MatchesRepoImpl implements MatchesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;
    @Override
    public List<MatchesWithVenue> findAllMatches(int tournamentId) throws Exception {
       String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
               "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
               "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
               "where TournamentId=:tournamentId and StartDatetime > CURRENT_TIMESTAMP order by StartDatetime";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(tournamentId);
       return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(tournament), new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByTournament(int id) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where TournamentId=:tournamentId";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(id);
        return namedParameterJdbcTemplate.query(sql,new BeanPropertySqlParameterSource(tournament),new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByVenue(int id) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.VenueId=:venueId";
        Venue venue = new Venue();
        venue.setVenueId(id);
        return namedParameterJdbcTemplate.query(sql,new BeanPropertySqlParameterSource(venue),new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByTeam(int id) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.Team1=:teamId or m.Team2=:teamId";
        Team team = new Team();
        team.setTeamId(id);
        return namedParameterJdbcTemplate.query(sql,new BeanPropertySqlParameterSource(team),new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByPreviousDateAndResultStatus(int tournamentId) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where TournamentId=:tournamentId and StartDatetime < CURRENT_TIMESTAMP and m.ResultStatus IS NULL order by StartDatetime";
        Tournament tournament = new Tournament();
        tournament.setTournamentId(tournamentId);
        return namedParameterJdbcTemplate.query(sql,new BeanPropertySqlParameterSource(tournament),new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByMinimumBet(int minBet) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.MinimumBet=:minimumBet";
        Matches matches = new Matches();
        matches.setMinimumBet(minBet);
        return namedParameterJdbcTemplate.query(sql,new BeanPropertySqlParameterSource(matches),new MatchesRowMapper());
    }
//Pending
    @Override
    public int addMatch(Matches matches) throws Exception {
        String sql = "INSERT INTO Matches (MatchId,TournamentId,Name,StartDateTime,VenueId,Team1,Team2,MinimumBet) VALUES(:matchId,:tournamentId,:name,:startDateTime,:venueId,:team1,:team2,:minimumBet)";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    //Pending
    @Override
    public boolean updateMatch(int id, Matches matches) throws Exception {
            String sql = "UPDATE Matches SET TournamentId = :tournamentId, Name = :name, StartDatetime = :startDateTime, VenueId=:venueId, Team1=:team1, Team2=:team2, WinnerTeamId=:winnerTeamId, ResultStatus=:resultStatus,MinimumBet=:minimumBet WHERE MatchId=:matchId";
            matches.setMatchId(id);
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches)) > 0;
    }

    @Override
    public boolean updateMatchWinningTeam(int matchId, int ResultStatus, int winningTeamId) throws Exception {
        try
        {
            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("vMatchId", matchId)
                    .addValue("vResultStatus",ResultStatus)
                    .addValue("vWinnerTeamId",winningTeamId);

            simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withProcedureName("setMatchResult");
            simpleJdbcCall.execute(in);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int updateMatchVenue(int id, int venueId) throws Exception {
        String sql = "UPDATE Matches SET VenueId=:venueId WHERE MatchId=:matchId";
        Matches matches = new Matches();
        matches.setVenueId(venueId);
        matches.setMatchId(id);
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    @Override
    public int updateResultStatus(int id, boolean status) throws Exception {
        String sql = "UPDATE Matches SET ResultStatus=:resultStatus WHERE MatchId=:matchId";
        Matches matches = new Matches();
        matches.setResultStatus(status);
        matches.setMatchId(id);
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    @Override
    public int updateMinimumBet(int matchId, int minBet) throws Exception {
       String sql = "UPDATE Matches SET MinimumBet=:minimumBet WHERE MatchId=:matchId";
       Matches matches = new Matches();
       matches.setMatchId(matchId);
       matches.setMinimumBet(minBet);
       return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    @Override
    public int updateMatchScheduleDate(int id, Timestamp date) throws Exception {
        String sql = "UPDATE Matches SET StartDatetime=:startDateTime WHERE MatchId=:matchId";
        Matches matches = new Matches();
        matches.setMatchId(id);
        matches.setStartDateTime(date);
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    @Override
    public int deleteMatches(int id) throws Exception {
       String sql = "DELETE FROM Matches WHERE MatchId=:matchId";
       Matches matches = new Matches();
       matches.setMatchId(id);
       return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

	@Override
	public List<MatchesWithVenue> findMatchesById(int id,int tournamentId) throws Exception {
//		String tournament_sql = "SELECT * from Tournament WHERE active = true";
//        int tournamentid = jdbcTemplate.query(tournament_sql,new TournamentRowMapper()).get(0).getTournamentId();
       String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
               "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumBet, WinnerTeamId, ResultStatus, TournamentId  " +
               "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
               "where TournamentId=:tournamentId AND MatchId=:matchId";
       Matches matches = new Matches();
       matches.setMatchId(id);
       matches.setTournamentId(tournamentId);
       return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(matches) , new MatchesRowMapper());
	}
}
