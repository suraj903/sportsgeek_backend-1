package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.ContestWithResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContestWithUsersResultRowMapper implements RowMapper<ContestWithResult> {
    @Override
    public ContestWithResult mapRow(ResultSet rs, int rowNum) throws SQLException {

        ContestWithResult contestWithResult = new ContestWithResult();
        contestWithResult.setContestId(rs.getInt("BetTeamId"));
        contestWithResult.setContestPoints(rs.getInt("BetPoints"));
        contestWithResult.setFirstName(rs.getString("FirstName"));
        contestWithResult.setLastName(rs.getString("LastName"));
        contestWithResult.setTeamShortName(rs.getString("TeamShortName"));
        contestWithResult.setWinningPoints(rs.getInt("WinningPoints"));
        contestWithResult.setProfilePicture(rs.getString("ProfilePicture"));
        contestWithResult.setUsername(rs.getString("Username"));
        return  contestWithResult;
    }
}
