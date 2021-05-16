package com.project.sportsgeek.repository.privatechatrepo;

import com.project.sportsgeek.mapper.PrivateChatRowMapper;
import com.project.sportsgeek.model.PrivateChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
    public List<PrivateChat> findPrivateChatByUserId(int userId1, int userId2) throws Exception {
        String sql = "SELECT PrivateChatId, FromUserId, ToUserId, Message, ChatTimestamp FROM PrivateChat WHERE (FromUserId = :userId1 AND ToUserId = :userId2) OR (FromUserId = :userId2 AND ToUserId = :userId1) ORDER BY PrivateChatId";
        MapSqlParameterSource params = new MapSqlParameterSource("userId1", userId1);
        params.addValue("userId2", userId2);
        return jdbcTemplate.query(sql, params, new PrivateChatRowMapper());
    }

    @Override
    public int addPrivateChat(PrivateChat privateChat) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PrivateChat (FromUserId, ToUserId, Message) VALUES (:fromUserId, :toUserId, :message)";
        int n = jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(privateChat), holder);
        if(n > 0){
            return holder.getKey().intValue();
        }else{
            return 0;
        }
    }

    @Override
    public boolean updatePrivateChat(int privateChatId, PrivateChat privateChat) throws Exception {
        String sql = "UPDATE PrivateChat SET FromUserId = :fromUserId, ToUserId = :toUserId, Message = :message WHERE PrivateChatId = :privateChatId";
        privateChat.setPrivateChatId(privateChatId);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(privateChat)) > 0;
    }

    @Override
    public boolean deletePrivateChat(int privateChatId) throws Exception {
        String sql = "DELETE FROM PrivateChat WHERE PrivateChatId = :privateChatId";
        MapSqlParameterSource params = new MapSqlParameterSource("privateChatId", privateChatId);
        return jdbcTemplate.update(sql, params) > 0;
    }
}