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
               "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
               "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
               "where TournamentId=:tournamentId and StartDatetime > CURRENT_TIMESTAMP order by StartDatetime";
       MapSqlParameterSource params = new MapSqlParameterSource("tournamentId", tournamentId);
       return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByTournament(int tournamentId) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where TournamentId=:tournamentId ORDER BY MatchId";
        MapSqlParameterSource params = new MapSqlParameterSource("tournamentId", tournamentId);
        return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByVenue(int venueId) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.VenueId=:venueId ORDER BY MatchId";
        MapSqlParameterSource params = new MapSqlParameterSource("venueId", venueId);
        return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByTeam(int teamId) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.Team1=:teamId or m.Team2=:teamId ORDER BY MatchId";
        MapSqlParameterSource params = new MapSqlParameterSource("teamId", teamId);
        return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByPreviousDateAndResultStatus(int tournamentId) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where TournamentId=:tournamentId and StartDatetime < CURRENT_TIMESTAMP and m.ResultStatus IS NULL order by StartDatetime";
        MapSqlParameterSource params = new MapSqlParameterSource("tournamentId", tournamentId);
        return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }

    @Override
    public List<MatchesWithVenue> findAllMatchesByMinimumPoints(int minPoints) throws Exception {
        String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
                "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
                "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
                "where m.MinimumPoints = :minPoints";
        MapSqlParameterSource params = new MapSqlParameterSource("minPoints", minPoints);
        return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
    }
//Pending
    @Override
    public int addMatch(Matches matches) throws Exception {
        String sql = "INSERT INTO Matches (MatchId,TournamentId,Name,StartDatetime,VenueId,Team1,Team2,MinimumPoints) VALUES(:matchId,:tournamentId,:name,:startDatetime,:venueId,:team1,:team2,:MinimumPoints)";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(matches));
    }

    //Pending
    @Override
    public boolean updateMatch(int matchId, Matches matches) throws Exception {
//        System.out.println("WinnerTeamId : " + matches.getWinnerTeamId());
//        String sql = "UPDATE Matches SET TournamentId = :tournamentId, Name = :name, StartDatetime = :startDatetime, VenueId=:venueId, Team1=:team1, Team2=:team2, WinnerTeamId=:winnerTeamId, ResultStatus=:resultStatus,MinimumPoints=:MinimumPoints WHERE MatchId=:matchId";
        String sql = "UPDATE Matches SET TournamentId = :tournamentId, Name = :name, StartDatetime = :startDatetime, VenueId=:venueId, Team1=:team1, Team2=:team2, MinimumPoints=:MinimumPoints WHERE MatchId=:matchId";
        matches.setMatchId(matchId);
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
    public int updateMatchVenue(int matchId, int venueId) throws Exception {
        String sql = "UPDATE Matches SET VenueId=:venueId WHERE MatchId=:matchId";
        MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
        params.addValue("venueId", venueId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int updateResultStatus(int matchId, boolean status) throws Exception {
        String sql = "UPDATE Matches SET ResultStatus=:resultStatus WHERE MatchId=:matchId";
        MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
        params.addValue("status", status);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int updateMinimumPoints(int matchId, int minPoints) throws Exception {
       String sql = "UPDATE Matches SET MinimumPoints=:minPoints WHERE MatchId=:matchId";
       MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
       params.addValue("minPoints", minPoints);
       return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int updateMatchScheduleDate(int matchId, Timestamp date) throws Exception {
        String sql = "UPDATE Matches SET StartDatetime=:startDatetime WHERE MatchId=:matchId";
        MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
        params.addValue("startDatetime", date);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteMatches(int matchId) throws Exception {
       String sql = "DELETE FROM Matches WHERE MatchId=:matchId";
       MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
       return namedParameterJdbcTemplate.update(sql, params);
    }

	@Override
	public List<MatchesWithVenue> findMatchesById(int matchId, int tournamentId) throws Exception {
//		String tournament_sql = "SELECT * from Tournament WHERE active = true";
//        int tournamentid = jdbcTemplate.query(tournament_sql,new TournamentRowMapper()).get(0).getTournamentId();
       String sql = "SELECT MatchId , StartDatetime, Team1,Team2, t1.Name as team1long, t1.ShortName as team1short, " +
               "t1.TeamLogo as team1logo, t2.Name as team2long, t2.ShortName as team2short, t2.TeamLogo as team2logo, v.Name as venue, MinimumPoints, WinnerTeamId, ResultStatus, TournamentId  " +
               "FROM Matches as m INNER JOIN Venue as v on m.VenueId=v.VenueId left JOIN Team as t1 on m.Team1=t1.TeamId left JOIN Team as t2 on m.Team2=t2.TeamId " +
               "where TournamentId=:tournamentId AND MatchId=:matchId";
       MapSqlParameterSource params = new MapSqlParameterSource("matchId", matchId);
       params.addValue("tournamentId", tournamentId);
       return namedParameterJdbcTemplate.query(sql, params, new MatchesRowMapper());
	}
}
