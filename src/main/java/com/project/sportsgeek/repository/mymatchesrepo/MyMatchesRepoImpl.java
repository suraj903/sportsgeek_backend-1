package com.project.sportsgeek.repository.mymatchesrepo;

import com.project.sportsgeek.mapper.MyMatchesResultRowMapper;
import com.project.sportsgeek.mapper.MyMatchesRowMapper;
import com.project.sportsgeek.model.Matches;
import com.project.sportsgeek.model.MyMatches;
import com.project.sportsgeek.model.profile.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "myMatchesRepo")
public class MyMatchesRepoImpl implements MyMatchesRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<MyMatches> findUpcomingContestByUserId(int userId) throws Exception {
       String sql = " SELECT StartDatetime, t3.ShortName as TeamName, BetPoints,bot.MatchId as MatchId, t1.ShortName as Team1Short, t1.TeamLogo as Team1Logo, t2.ShortName as Team2Short, t2.TeamLogo as Team2Logo, v.Name as Venue\n" +
               "        FROM Matches as m inner join Team as t1 on m.Team1=t1.TeamId\n" +
               "        inner join Team as t2 on m.Team2=t2.TeamId\n" +
               "        inner join Contest as bot on bot.MatchId = m.MatchId\n" +
               "        inner join Team as t3 on bot.TeamId = t3.TeamId\n" +
               "        inner join Venue as v on m.VenueId = v.VenueId WHERE m.StartDateTime > CURRENT_TIMESTAMP and bot.UserId=:userId order by StartDatetime";

       User user = new User();
       user.setUserId(userId);
       return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(user),new MyMatchesRowMapper());
    }

    @Override
    public List<MyMatches> findLiveContestByUserId(int userId) throws Exception {
        String sql = " SELECT StartDatetime, t3.ShortName as TeamName, bot.MatchId as MatchId, BetPoints, t1.ShortName as Team1Short, t1.TeamLogo as Team1Logo, t2.ShortName as Team2Short, t2.TeamLogo as Team2Logo, v.Name as Venue\n" +
                "        FROM Matches as m inner join Team as t1 on m.Team1=t1.TeamId\n" +
                "        inner join Team as t2 on m.Team2=t2.TeamId\n" +
                "        inner join Contest as bot on bot.MatchId = m.MatchId\n" +
                "        inner join Team as t3 on bot.TeamId = t3.TeamId\n" +
                "        inner join Venue as v on m.VenueId = v.VenueId WHERE  date(m.StartDatetime) = current_date and StartDatetime <= current_timestamp  and ResultStatus IS NULL  and bot.UserId=:userId order by StartDatetime DESC";
        User user = new User();
        user.setUserId(userId);
        return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(user),new MyMatchesRowMapper());
    }

    @Override
    public List<MyMatches> findResultContestByUserId(int userId) throws Exception {
        String sql = " SELECT StartDatetime, t3.ShortName as TeamName, BetPoints, t1.ShortName as Team1Short, t1.TeamLogo as Team1Logo, t2.ShortName as Team2Short, t2.TeamLogo as Team2Logo, v.Name as Venue, WinningPoints, t4.ShortName as WinnerTeam,bot.MatchId as MatchId \n" +
                "        FROM Matches as m inner join Team as t1 on m.Team1=t1.TeamId\n" +
                "        inner join Team as t2 on m.Team2=t2.TeamId\n" +
                "        inner join Contest as bot on bot.MatchId = m.MatchId\n" +
                "        inner join Team as t3 on bot.TeamId = t3.TeamId\n" +
                "        left join Team as t4 on m.WinnerTeamId = t4.TeamId" +
                "        inner join Venue as v on m.VenueId = v.VenueId WHERE m.StartDateTime < CURRENT_TIMESTAMP and ResultStatus IS NOT NULL and bot.UserId="+userId+" order by StartDatetime DESC";

        return jdbcTemplate.query(sql,new MyMatchesResultRowMapper());
    }
}
