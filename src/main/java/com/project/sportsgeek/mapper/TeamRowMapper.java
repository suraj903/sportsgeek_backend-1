package com.project.sportsgeek.mapper;


import com.project.sportsgeek.model.Team;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamRowMapper implements RowMapper<Team> {
    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team =new Team();
        team.setTeamId(rs.getInt("TeamId"));
        team.setName(rs.getString("Name"));
        team.setShortName(rs.getString("ShortName"));
        team.setTeamLogo(rs.getString("TeamLogo"));
        return team;
    }
}
