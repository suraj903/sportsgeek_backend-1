package com.project.sportsgeek.repository.contestlogrepo;

import com.project.sportsgeek.model.ContestLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository(value = "contestLogRepo")
public class ContestLogRepoImpl implements ContestLogRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int addContestLog(ContestLog contestLog) throws Exception {
        System.out.println("addContestLog");
        System.out.println(contestLog);
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
}
