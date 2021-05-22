package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.profile.User;
import com.project.sportsgeek.model.profile.UserResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResponseRowMapper implements RowMapper<UserResponse> {
    @Override
    public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserResponse user = new UserResponse();
        user.setUserId(rs.getInt("UserId"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setGenderId(rs.getInt("GenderId"));
        user.setGenderName(rs.getString("GenderName"));
        user.setUsername(rs.getString("UserName"));
        user.setEmail(rs.getString("Email"));
        user.setMobileNumber(rs.getString("MobileNumber"));
        user.setProfilePicture(rs.getString("ProfilePicture"));
        user.setRoleId(rs.getInt("RoleId"));
        user.setRoleName(rs.getString("RoleName"));
        user.setAvailablePoints(rs.getInt("AvailablePoints"));
        user.setStatus(rs.getBoolean("Status"));
        return user;
    }
}
