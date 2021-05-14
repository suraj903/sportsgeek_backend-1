package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.Contest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FutureBetsRowMapper implements RowMapper<Contest> {
    @Override
    public Contest mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contest contest = new Contest();
        contest.setContestPoints(rs.getInt("TotalBetPoints"));
        contest.setUserId(rs.getInt("UserId"));
        return contest;
    }
}
