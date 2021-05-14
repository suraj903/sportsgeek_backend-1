package com.project.sportsgeek.repository.privatechatrepo;

import com.project.sportsgeek.mapper.PrivateChatRowMapper;
import com.project.sportsgeek.model.PrivateChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "privateChatRepo")
public class PrivateChatRepoImpl implements PrivateChatRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<PrivateChat> findPrivateChatByUserId(int userid1, int userid2) throws Exception {
        String sql = "SELECT PrivateChatId, FromUserId, ToUserId, Message, ChatTimestamp FROM PrivateChat WHERE (FromUserId=" + userid1 + " AND ToUserId=" + userid2 + ") OR (FromUserId=" + userid2 + " AND ToUserId=" + userid1 + ") ORDER BY PrivateChatId";
        System.out.println(sql);
        return jdbcTemplate.query(sql,new PrivateChatRowMapper());
    }

    @Override
    public int addPrivateChat(PrivateChat privateChat) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PrivateChat (FromUserId, ToUserId, Message) VALUES (:fromUserId, :toUserId, :message)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(privateChat), holder);
        return holder.getKey().intValue();
    }

    @Override
    public boolean updatePrivateChat(int id, PrivateChat privateChat) throws Exception {
        String sql = "UPDATE PrivateChat SET FromUserId=:fromUserId, ToUserId=:toUserId, Message=:message WHERE PrivateChatId=:privateChatId";
        privateChat.setPrivateChatId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(privateChat)) > 0;
    }

    @Override
    public int deletePrivateChat(int id) throws Exception {
        String sql = "DELETE FROM PrivateChat WHERE PrivateChatId=:privateChatId";
        PrivateChat privateChat = new PrivateChat();
        privateChat.setPrivateChatId(id);
        return  jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(privateChat));
    }
}