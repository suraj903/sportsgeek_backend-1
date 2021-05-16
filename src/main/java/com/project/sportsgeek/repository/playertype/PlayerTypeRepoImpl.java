package com.project.sportsgeek.repository.playertype;

import com.project.sportsgeek.mapper.PlayerTypeRowMapper;
import com.project.sportsgeek.model.PlayerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "playerTypeRepo")
public class PlayerTypeRepoImpl implements PlayerTypeRepository{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<PlayerType> findAllPlayerType() {
        String sql = "SELECT * FROM PlayerType";
        return jdbcTemplate.query(sql,new PlayerTypeRowMapper());
    }

    @Override
    public PlayerType findPlayerTypeById(int i) throws Exception {
        String sql = "SELECT * FROM PlayerType WHERE PlayerTypeId=:playerTypeId";
        PlayerType playerType = new PlayerType();
        playerType.setPlayerTypeId(i);
        List<PlayerType> playerTypeList = jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(playerType),new PlayerTypeRowMapper());
        if(playerTypeList.size() > 0){
            return playerTypeList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int addPlayerType(PlayerType playerType) throws Exception {
        String sql = "INSERT INTO PlayerType (TypeName) VALUES(:typeName)";
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(playerType));
    }

    @Override
    public boolean updatePlayerType(int id, PlayerType playerType) throws Exception {
        String sql = "UPDATE `" + "PlayerType" + "` set "
                + "`TypeName` = :typeName where `PlayerTypeId`=:playerTypeId";
        playerType.setPlayerTypeId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(playerType)) > 0;
    }

    @Override
    public int deletePlayerType(int id) throws Exception {
        String sql = "DELETE FROM PlayerType WHERE PlayerTypeId =:playerTypeId";
        PlayerType playerType = new PlayerType();
        playerType.setPlayerTypeId(id);
        return  jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(playerType));
    }
}
