package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.Contest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContestWithMatchRowMapper implements RowMapper<Contest> {
    @Override
    public Contest mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contest contest = new Contest();
        contest.setContestId(rs.getInt("BetTeamId"));
        contest.setTeamId(rs.getInt("TeamId"));
        contest.setContestPoints(rs.getInt("BetPoints"));
        contest.setMatchId(rs.getInt("MatchId"));
        contest.setUserId(rs.getInt("UserId"));
        contest.setWinningPoints(rs.getInt("WinningPoints"));
        return contest;
    }
}
