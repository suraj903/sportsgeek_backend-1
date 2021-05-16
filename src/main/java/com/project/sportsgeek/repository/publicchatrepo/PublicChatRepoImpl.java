package com.project.sportsgeek.repository.publicchatrepo;

import com.project.sportsgeek.mapper.PublicChatWithUserRowMapper;
import com.project.sportsgeek.mapper.VenueRowMapper;
import com.project.sportsgeek.model.PublicChat;
import com.project.sportsgeek.model.PublicChatWithUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "publicChatRepo")
public class PublicChatRepoImpl implements PublicChatRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<PublicChatWithUser> findAllPublicChat() {
        String sql = "SELECT PublicChatId, pc.UserId as UserId, u.FirstName as FirstName, u.LastName as LastName, Message, pc.Status as Status, ChatTimestamp FROM PublicChat as pc INNER JOIN User as u on pc.UserId=u.UserId";
        return jdbcTemplate.query(sql,new PublicChatWithUserRowMapper());
    }

    @Override
    public PublicChatWithUser findPublicChatById(int publicChatId) throws Exception {
        String sql = "SELECT PublicChatId, pc.UserId as UserId, u.FirstName as FirstName, u.LastName as LastName, Message, pc.Status as Status, ChatTimestamp FROM PublicChat as pc INNER JOIN User as u on pc.UserId=u.UserId WHERE PublicChatId = :publicChatId";
        MapSqlParameterSource params = new MapSqlParameterSource("publicChatId", publicChatId);
        List<PublicChatWithUser> publicChatList = jdbcTemplate.query(sql, params, new PublicChatWithUserRowMapper());
        if(publicChatList.size() > 0){
            return publicChatList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int addPublicChat(PublicChat publicChat) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PublicChat (userId, message) VALUES (:userId, :message)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(publicChat), holder);
        return holder.getKey().intValue();
    }

    @Override
    public boolean updatePublicChat(int publicChatId, PublicChat publicChat) throws Exception {
        String sql = "UPDATE PublicChat SET UserId = :userId, Message=:message WHERE PublicChatId = :publicChatId";
        publicChat.setPublicChatId(publicChatId);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(publicChat)) > 0;
    }

    @Override
    public boolean deletePublicChat(int publicChatId) throws Exception {
        String sql = "DELETE FROM PublicChat WHERE PublicChatId = :publicChatId";
        MapSqlParameterSource params = new MapSqlParameterSource("publicChatId", publicChatId);
        return jdbcTemplate.update(sql, params) > 0;
    }

}