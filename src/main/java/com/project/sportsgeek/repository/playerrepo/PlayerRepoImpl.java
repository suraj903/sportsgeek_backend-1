package com.project.sportsgeek.repository.playerrepo;

import com.project.sportsgeek.mapper.PlayerRowMapper;
import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "playerRepo")
public class PlayerRepoImpl implements PlayerRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<PlayerResponse> findAllPlayers() {
//       String sql = "SELECT PlayerId,TeamId,Name,TypeId,ProfilePicture FROM Player";
        String sql = "SELECT p.Credits as Credits,p.PlayerId as PlayerId, t.Name as TeamName, p.Name as Name,pt.TypeName as PlayerType, p.ProfilePicture as ProfilePicture " +
                "FROM Player as p INNER JOIN PlayerType as pt on p.TypeId = pt.PlayerTypeId INNER JOIN Team as t on p.TeamId=t.TeamId";
       return jdbcTemplate.query(sql, new PlayerRowMapper());
    }

    @Override
    public List<PlayerResponse> findPlayerByPlayerId(int id) throws Exception {
//        String sql = "SELECT PlayerId,TeamId,Name,TypeId,ProfilePicture FROM Player WHERE PlayerId="+id;
        String sql = "SELECT p.Credits as Credits,p.PlayerId as PlayerId, t.Name as TeamName, p.Name as Name,pt.TypeName as PlayerType, p.ProfilePicture as ProfilePicture " +
                "FROM Player as p INNER JOIN PlayerType as pt on p.TypeId = pt.PlayerTypeId INNER JOIN Team as t on p.TeamId=t.TeamId WHERE p.PlayerId=:playerId";
        Player player = new Player();
        player.setPlayerId(id);
        return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(player), new PlayerRowMapper());
    }

    @Override
    public List<PlayerResponse> findPlayerByPlayerType(int id) throws Exception {
//        String sql = "SELECT PlayerId,TeamId,Name,TypeId,ProfilePicture FROM Player WHERE TypeId="+id;
        String sql = "SELECT p.Credits as Credits,p.PlayerId as PlayerId, t.Name as TeamName, p.Name as Name,pt.TypeName as PlayerType, p.ProfilePicture as ProfilePicture " +
                "FROM Player as p INNER JOIN PlayerType as pt on p.TypeId = pt.PlayerTypeId INNER JOIN Team as t on p.TeamId=t.TeamId WHERE p.TypeId=:typeId";
      Player player = new Player();
      player.setTypeId(id);
        return jdbcTemplate.query(sql, new BeanPropertySqlParameterSource(player),new PlayerRowMapper());
    }

    @Override
    public List<PlayerResponse> findPlayerByTeamId(int id) throws Exception {
//        String sql = "SELECT PlayerId,TeamId,Name,TypeId,ProfilePicture FROM Player WHERE TeamId="+id;
        String sql = "SELECT p.Credits as Credits,p.PlayerId as PlayerId, t.Name as TeamName, p.Name as Name,pt.TypeName as PlayerType, p.ProfilePicture as ProfilePicture " +
                "FROM Player as p INNER JOIN PlayerType as pt on p.TypeId = pt.PlayerTypeId INNER JOIN Team as t on p.TeamId=t.TeamId WHERE p.TeamId=:teamId";
      Player player = new Player();
      player.setTeamId(id);
        return jdbcTemplate.query(sql,new BeanPropertySqlParameterSource(player), new PlayerRowMapper());
    }

    @Override
    public int addPlayer(Player player) throws Exception {
        String sql = "INSERT INTO Player (TeamId,Name,TypeId,ProfilePicture,Credits) VALUES(:teamId,:name,:typeId,:profilePicture,:credits)";
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(player));
    }

    @Override
    public boolean updatePlayer(int id, Player player) throws Exception {
        String sql = "UPDATE `" + "player" + "` set "
                + "`TeamId` = :teamId, `Name` = :name, `TypeId` = :typeId,`ProfilePicture` = :profilePicture, `Credits` = :credits where `PlayerId`=:playerId";
        player.setPlayerId(id);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(player)) > 0;
    }

    @Override
    public boolean updatePlayerType(int id, int playerTypeId) throws Exception {
       String sql = "UPDATE Player SET TypeId=:typeId WHERE PlayerId=:playerId";
       Player player = new Player();
       player.setPlayerId(id);
       player.setTypeId(playerTypeId);
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(player))> 0;
    }

    @Override
    public int deletePlayer(int id) throws Exception {
        String sql = "DELETE FROM Player WHERE PlayerId=:playerId";
        Player player = new Player();
        player.setPlayerId(id);
        return  jdbcTemplate.update(sql,new BeanPropertySqlParameterSource(player));
    }
}
