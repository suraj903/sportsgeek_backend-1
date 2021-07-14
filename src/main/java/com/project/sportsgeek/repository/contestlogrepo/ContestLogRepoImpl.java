package com.project.sportsgeek.repository.contestlogrepo;

import com.project.sportsgeek.mapper.ContestLogFormattedRowMapper;
import com.project.sportsgeek.mapper.ContestLogWithUserRowMapper;
import com.project.sportsgeek.mapper.PublicChatFormattedRowMapper;
import com.project.sportsgeek.model.ContestLog;
import com.project.sportsgeek.model.ContestLogFormatted;
import com.project.sportsgeek.model.ContestLogWithUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "contestLogRepo")
public class ContestLogRepoImpl implements ContestLogRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int addContestLog(ContestLog contestLog) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO ContestLog (UserId, MatchId, OldTeamId, OldContestPoints, NewTeamId, NewContestPoints, Action)" +
                " VALUES(:userId, :matchId, :oldTeamId, :oldContestPoints, :newTeamId, :newContestPoints, :action)";
        int n = jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(contestLog), holder);
        System.out.println("n : " + n);
        if(n > 0) {
            return holder.getKey().intValue();
        }else {
            return 0;
        }
    }

    @Override
    public List<ContestLogWithUser> findAllContestLogForLastDays(int days) {
        String sql = "SELECT ContestLogId, StartDatetime, Team1, t1.ShortName as Team1Name, Team2, t2.ShortName as Team2Name, FirstName, LastName, OldTeamId, OldContestPoints, NewTeamId, NewContestPoints, Action, LogTimestamp " +
                "FROM ContestLog as c INNER JOIN User as u ON c.UserId=u.UserId " +
                "INNER JOIN Matches as m ON m.MatchId=c.MatchId " +
                "INNER JOIN Team as t1 ON m.Team1=t1.TeamId INNER JOIN Team as t2 ON m.Team2=t2.TeamId " +
                "WHERE LogTimestamp > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL :days DAY) " +
                "ORDER BY LogTimestamp";
        MapSqlParameterSource params = new MapSqlParameterSource("days", days);
        return jdbcTemplate.query(sql, params, new ContestLogWithUserRowMapper());
    }

    @Override
    public List<ContestLogWithUser> findAllContestLogAfterId(int contestLogId) {
        String sql = "SELECT ContestLogId, StartDatetime, Team1, t1.ShortName as Team1Name, Team2, t2.ShortName as Team2Name, FirstName, LastName, OldTeamId, OldContestPoints, NewTeamId, NewContestPoints, Action, LogTimestamp " +
                "FROM ContestLog as c INNER JOIN User as u ON c.UserId=u.UserId " +
                "INNER JOIN Matches as m ON m.MatchId=c.MatchId " +
                "INNER JOIN Team as t1 ON m.Team1=t1.TeamId INNER JOIN Team as t2 ON m.Team2=t2.TeamId " +
                "WHERE ContestLogId > :contestLogId " +
                "ORDER BY LogTimestamp";
        MapSqlParameterSource params = new MapSqlParameterSource("contestLogId", contestLogId);
        return jdbcTemplate.query(sql, params, new ContestLogWithUserRowMapper());
    }

    @Override
    public List<ContestLogFormatted> findAllContestLogFormattedForLastDays(int days) {
        String sql = "SELECT ContestLogId, StartDatetime, Team1, t1.ShortName as Team1Name, Team2, t2.ShortName as Team2Name, FirstName, LastName, OldTeamId, OldContestPoints, NewTeamId, NewContestPoints, Action, LogTimestamp " +
                "FROM ContestLog as c INNER JOIN User as u ON c.UserId=u.UserId " +
                "INNER JOIN Matches as m ON m.MatchId=c.MatchId " +
                "INNER JOIN Team as t1 ON m.Team1=t1.TeamId INNER JOIN Team as t2 ON m.Team2=t2.TeamId " +
                "WHERE LogTimestamp > DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL :days DAY) " +
                "ORDER BY LogTimestamp DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("days", days);
        return jdbcTemplate.query(sql, params, new ContestLogFormattedRowMapper());
    }

    @Override
    public List<ContestLogFormatted> findAllContestLogFormattedAfterId(int contestLogId) {
        String sql = "SELECT ContestLogId, StartDatetime, Team1, t1.ShortName as Team1Name, Team2, t2.ShortName as Team2Name, FirstName, LastName, OldTeamId, OldContestPoints, NewTeamId, NewContestPoints, Action, LogTimestamp " +
                "FROM ContestLog as c INNER JOIN User as u ON c.UserId=u.UserId " +
                "INNER JOIN Matches as m ON m.MatchId=c.MatchId " +
                "INNER JOIN Team as t1 ON m.Team1=t1.TeamId INNER JOIN Team as t2 ON m.Team2=t2.TeamId " +
                "WHERE ContestLogId > :contestLogId " +
                "ORDER BY LogTimestamp DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("contestLogId", contestLogId);
        return jdbcTemplate.query(sql, params, new ContestLogFormattedRowMapper());
    }
}
