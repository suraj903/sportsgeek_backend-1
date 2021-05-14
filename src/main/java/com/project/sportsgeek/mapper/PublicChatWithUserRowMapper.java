package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.PublicChatWithUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PublicChatWithUserRowMapper implements RowMapper<PublicChatWithUser> {
    private int publicChatId;
    private int userId;
    private int firstName;
    private int lastName;
    private String message;
    private boolean status;
    private Timestamp chatTimestamp;

    @Override
    public PublicChatWithUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        PublicChatWithUser publicChatWithUser = new PublicChatWithUser();
        publicChatWithUser.setPublicChatId(rs.getInt("PublicChatId"));
        publicChatWithUser.setUserId(rs.getInt("UserId"));
        publicChatWithUser.setFirstName(rs.getString("FirstName"));
        publicChatWithUser.setLastName(rs.getString("LastName"));
        publicChatWithUser.setMessage(rs.getString("Message"));
        publicChatWithUser.setStatus(rs.getBoolean("Status"));
        publicChatWithUser.setChatTimestamp(rs.getTimestamp("ChatTimeStamp"));
        return publicChatWithUser;
    }
}
