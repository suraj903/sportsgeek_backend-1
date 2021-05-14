package com.project.sportsgeek.mapper;

import com.project.sportsgeek.model.PrivateChat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivateChatRowMapper implements RowMapper<PrivateChat> {

    @Override
    public PrivateChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrivateChat privateChat = new PrivateChat();
        privateChat.setPrivateChatId(rs.getInt("PrivateChatId"));
        privateChat.setFromUserId(rs.getInt("FromUserId"));
        privateChat.setToUserId(rs.getInt("ToUserId"));
        privateChat.setMessage(rs.getString("Message"));
        privateChat.setChatTimestamp(rs.getTimestamp("ChatTimestamp"));
        return privateChat;
    }
}
