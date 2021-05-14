package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.MyMatches;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyMatchesRowMapper implements RowMapper<MyMatches> {
    @Override
    public MyMatches mapRow(ResultSet rs, int rowNum) throws SQLException {
        MyMatches myMatches = new MyMatches();
        myMatches.setTeam1Logo(rs.getString("Team1Logo"));
        myMatches.setBetPoints(rs.getInt("BetPoints"));
        myMatches.setTeam1Short(rs.getString("Team1Short"));
        myMatches.setTeam2Logo(rs.getString("Team2Logo"));
        myMatches.setVenue(rs.getString("Venue"));
        myMatches.setTeam2Short(rs.getString("Team2Short"));
        myMatches.setTeamName(rs.getString("TeamName"));
        myMatches.setStartDatetime(rs.getTimestamp("StartDateTime"));
        myMatches.setMatchId(rs.getInt("MatchId"));
        return myMatches;
    }
}
