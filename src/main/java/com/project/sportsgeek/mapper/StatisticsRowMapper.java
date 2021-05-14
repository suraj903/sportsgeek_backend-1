package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.Statistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsRowMapper implements RowMapper<Statistics> {
    @Override
    public Statistics mapRow(ResultSet rs, int rowNum) throws SQLException {
        Statistics statistics = new Statistics();
        statistics.setLastName(rs.getString("LastName"));
        statistics.setFirstName(rs.getString("FirstName"));
        statistics.setUserName(rs.getString("UserName"));
        statistics.setUserId(rs.getInt("UserId"));
        statistics.setTotalWinningPoints(rs.getInt("TotalWinningPoints"));
        return statistics;
    }
}
