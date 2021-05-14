package com.project.sportsgeek.repository.contestrepo;

import java.util.List;

import com.project.sportsgeek.mapper.ContestRowMapper;
import com.project.sportsgeek.model.Contest;
import com.project.sportsgeek.model.ContestWithResult;
import com.project.sportsgeek.model.ContestWithUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.sportsgeek.mapper.ContestWithMatchRowMapper;
import com.project.sportsgeek.mapper.ContestWithUsersResultRowMapper;

@Repository(value = "betOnTeamsRepo")
public class ContestRepoImpl implements ContestRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Contest> findContestByUserAndMatch(int userId, int matchId) throws Exception {
        String sql = "SELECT ContestId, UserId, MatchId, TeamId, ContestPoints, WinningPoints from Contest WHERE UserId = :userId and MatchId = :matchId";
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
        params.addValue("matchId", matchId);
        return jdbcTemplate.query(sql, params, new ContestWithMatchRowMapper());
    }

    @Override
    public List<ContestWithUser> findAllContestByMatchId(int matchId) throws Exception {
        String sql = "SELECT BetTeamId, bot.UserId as UserId, MatchId, bot.TeamId as TeamId, t.ShortName as TeamShortName, FirstName, LastName, Username, ProfilePicture, BetPoints, WinningPoints " +
                "FROM Contest as bot, User as u, Team as t " +
                "WHERE bot.TeamId=t.TeamId AND bot.UserId=u.UserId AND MatchId="+matchId;

        List<ContestWithUser> betOnTeamWithUsers = jdbcTemplate.query(sql, new ContestRowMapper());
        System.out.println(betOnTeamWithUsers);
        return betOnTeamWithUsers;
    }

    @Override
    public List<ContestWithResult> findContestResultByMatchId(int matchId) throws Exception {
        String sql = "SELECT BetTeamId, t.ShortName as TeamShortName, FirstName, LastName, Username, ProfilePicture, BetPoints, WinningPoints FROM Contest as bot inner join User as u on bot.UserId=u.UserId inner join Team as t on bot.TeamId=t.TeamId " +
                "WHERE MatchId="+matchId+" ORDER BY WinningPoints desc, BetPoints desc;";
        return jdbcTemplate.query(sql, new ContestWithUsersResultRowMapper());
    }

    @Override
    public int addContest(Contest contest) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO Contest (UserId, MatchId, TeamId, ContestPoints, WinningPoints) VALUES (:userId, :matchId, :teamId, :contestPoints, :winningPoints)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(contest), holder);
        return holder.getKey().intValue();
    }

    @Override
    public boolean updateContest(int contestId, Contest contest) throws Exception {
//        contest.setBetTeamId(id);
        // Update Contest
        String sql = "Update Contest SET TeamId = :teamId, ContestPoints = :contestPoints WHERE ContestId = :contestId";
        return jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(contest)) > 0;
    }

    @Override
    public int getContestPoints(int contestId) throws Exception {
        String sql = "SELECT ContestId, UserId, MatchId, TeamId, BetPoints, WinningPoints FROM Contest WHERE ContestId = :contestId";
        MapSqlParameterSource params = new MapSqlParameterSource("contestId", contestId);
        return jdbcTemplate.query(sql, new ContestWithMatchRowMapper()).get(0).getContestPoints();
    }

}