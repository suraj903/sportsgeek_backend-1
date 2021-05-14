package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.MyMatches;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyMatchesResultRowMapper implements RowMapper<MyMatches> {
    @Override
    public MyMatches mapRow(ResultSet rs, int rowNum) throws SQLException {
        MyMatches myMatchesResult = new MyMatches();
        myMatchesResult.setTeam1Logo(rs.getString("Team1Logo"));
        myMatchesResult.setBetPoints(rs.getInt("BetPoints"));
        myMatchesResult.setTeam1Short(rs.getString("Team1Short"));
        myMatchesResult.setTeam2Logo(rs.getString("Team2Logo"));
        myMatchesResult.setVenue(rs.getString("Venue"));
        myMatchesResult.setTeam2Short(rs.getString("Team2Short"));
        myMatchesResult.setTeamName(rs.getString("TeamName"));
        myMatchesResult.setStartDatetime(rs.getTimestamp("StartDateTime"));
        myMatchesResult.setWinningPoints(rs.getInt("WinningPoints"));
        myMatchesResult.setWinnerTeamName(rs.getString("WinnerTeam"));
        myMatchesResult.setMatchId(rs.getInt("MatchId"));
        return myMatchesResult;
    }
}
